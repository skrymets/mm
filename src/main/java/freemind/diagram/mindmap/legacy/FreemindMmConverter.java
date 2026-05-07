package freemind.diagram.mindmap.legacy;

import freemind.controller.actions.xml.document.Arrowlink;
import freemind.controller.actions.xml.document.Attribute;
import freemind.controller.actions.xml.document.Node;
import freemind.controller.actions.xml.document.Richcontent;
import freemind.diagram.AttributeBag;
import freemind.diagram.NodeContent;
import freemind.diagram.NodeId;
import freemind.diagram.StyleReferences;
import freemind.diagram.capabilities.AuxiliaryLink;
import freemind.diagram.mindmap.MindMapDiagram;
import freemind.diagram.mindmap.MindMapDiagramImpl;
import freemind.diagram.mindmap.MindMapNode;
import freemind.diagram.mindmap.MindMapNodeImpl;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Converts a legacy {@code .mm}-format JAXB tree to a {@link MindMapDiagram}.
 * Read-only / one-way: imported diagrams round-trip through the new native
 * envelope, NOT back to {@code .mm}.
 *
 * <p>Plan 1 converter coverage (rule of "preserve, don't perfect"):
 * <ul>
 *   <li>tree structure (root + children)</li>
 *   <li>node text (plain) and richcontent (HTML)</li>
 *   <li>node attributes (NAME/VALUE)</li>
 *   <li>arrowlinks &rarr; auxiliary links</li>
 * </ul>
 *
 * <p>Deliberately skipped in Plan 1, recovered in later plans:
 * node colors/fonts/edge styles, clouds, icons, encrypted nodes, hooks,
 * link targets, reverse-geocode, search results.
 */
@Slf4j
public final class FreemindMmConverter {

    /**
     * Converts a fully-unmarshalled legacy {@code Map} to a {@link MindMapDiagram}.
     *
     * @throws IllegalStateException if essential structure is missing (e.g., no root node)
     */
    public MindMapDiagram convert(freemind.controller.actions.xml.document.Map legacyMap) {
        var legacyRoot = legacyMap.getNode();
        if (legacyRoot == null) {
            throw new IllegalStateException("Legacy map has no root node");
        }

        var idCounter = new AtomicInteger(1);
        // Map from legacy node ID -> MindMapNodeImpl (for arrowlink resolution)
        var legacyIdToNode = new HashMap<String, MindMapNodeImpl>();
        // Collect deferred arrowlinks: (source, destinationLegacyId)
        var deferredArrowlinks = new ArrayList<DeferredLink>();

        var rootImpl = buildNode(legacyRoot, idCounter, legacyIdToNode, deferredArrowlinks);
        var diagram = MindMapDiagramImpl.createEmpty(rootImpl);

        attachChildren(legacyRoot, rootImpl, diagram, idCounter, legacyIdToNode, deferredArrowlinks);
        resolveArrowlinks(deferredArrowlinks, legacyIdToNode, diagram);

        return diagram;
    }

    private MindMapNodeImpl buildNode(
            Node legacyNode,
            AtomicInteger idCounter,
            Map<String, MindMapNodeImpl> legacyIdToNode,
            List<DeferredLink> deferredArrowlinks) {

        var nodeId = new NodeId(assignId(legacyNode, idCounter));
        var content = contentFromLegacy(legacyNode);
        var impl = new MindMapNodeImpl(nodeId, content);
        impl.setAttributes(attributesFromLegacy(legacyNode));

        // Register in the legacy-ID map for arrowlink resolution
        var legacyId = legacyNode.getID();
        if (legacyId != null && !legacyId.isBlank()) {
            legacyIdToNode.put(legacyId, impl);
        }

        // Collect arrowlinks from this node
        collectArrowlinks(legacyNode, impl, deferredArrowlinks);

        return impl;
    }

    private void attachChildren(
            Node legacyParent,
            MindMapNodeImpl parentImpl,
            MindMapDiagramImpl diagram,
            AtomicInteger idCounter,
            Map<String, MindMapNodeImpl> legacyIdToNode,
            List<DeferredLink> deferredArrowlinks) {

        for (var choice : legacyParent.getChoiceList()) {
            if (choice.ifNode()) {
                var legacyChild = choice.getNode();
                var childImpl = buildNode(legacyChild, idCounter, legacyIdToNode, deferredArrowlinks);
                diagram.addChild(parentImpl, childImpl);
                attachChildren(legacyChild, childImpl, diagram, idCounter, legacyIdToNode, deferredArrowlinks);
            }
        }
    }

    private String assignId(Node legacyNode, AtomicInteger idCounter) {
        var legacyId = legacyNode.getID();
        if (legacyId != null && !legacyId.isBlank()) {
            return legacyId;
        }
        return "gen-" + idCounter.getAndIncrement();
    }

    private NodeContent contentFromLegacy(Node legacyNode) {
        // Prefer richcontent of type NODE over the TEXT attribute
        for (var choice : legacyNode.getChoiceList()) {
            if (choice.ifRichcontent()) {
                var rc = choice.getRichcontent();
                if (rc.getTYPE1() == Richcontent.TYPE.NODE) {
                    return NodeContent.html(htmlFromRichcontent(rc));
                }
            }
        }

        var text = legacyNode.getTEXT();
        return NodeContent.plain(text != null ? text : "");
    }

    private String htmlFromRichcontent(Richcontent richcontent) {
        var html = richcontent.getHtml();
        if (html == null) {
            return "";
        }
        var sb = new StringBuilder();
        for (Element element : html.getAnyList()) {
            sb.append(elementToString(element));
        }
        return sb.toString();
    }

    private String elementToString(Element element) {
        try {
            var factory = javax.xml.transform.TransformerFactory.newInstance();
            var transformer = factory.newTransformer();
            transformer.setOutputProperty(javax.xml.transform.OutputKeys.OMIT_XML_DECLARATION, "yes");
            var source = new javax.xml.transform.dom.DOMSource(element);
            var result = new java.io.StringWriter();
            transformer.transform(source, new javax.xml.transform.stream.StreamResult(result));
            return result.toString();
        } catch (javax.xml.transform.TransformerException e) {
            log.warn("Failed to serialize HTML element to string", e);
            return "";
        }
    }

    private AttributeBag attributesFromLegacy(Node legacyNode) {
        var bag = AttributeBag.empty();
        for (var choice : legacyNode.getChoiceList()) {
            if (choice.ifAttribute()) {
                var attr = choice.getAttribute();
                var name = attr.getNAME();
                var value = attr.getVALUE();
                if (name != null && !name.isBlank()) {
                    bag = bag.with(name, value != null ? value : "");
                }
            }
        }
        return bag;
    }

    private void collectArrowlinks(
            Node legacyNode,
            MindMapNodeImpl sourceImpl,
            List<DeferredLink> deferredArrowlinks) {

        for (var choice : legacyNode.getChoiceList()) {
            if (choice.ifArrowlink()) {
                var arrowlink = choice.getArrowlink();
                var destination = arrowlink.getDESTINATION();
                if (destination != null && !destination.isBlank()) {
                    deferredArrowlinks.add(new DeferredLink(sourceImpl, destination));
                } else {
                    log.warn("Arrowlink with null/blank DESTINATION; skipping");
                }
            }
        }
    }

    private void resolveArrowlinks(
            List<DeferredLink> deferredArrowlinks,
            Map<String, MindMapNodeImpl> legacyIdToNode,
            MindMapDiagramImpl diagram) {

        for (var deferred : deferredArrowlinks) {
            var target = legacyIdToNode.get(deferred.destinationLegacyId());
            if (target == null) {
                log.warn("Arrowlink destination '{}' not found in node map; skipping",
                        deferred.destinationLegacyId());
                continue;
            }
            diagram.addAuxiliaryLink(AuxiliaryLink.of(
                    (MindMapNode) deferred.source(),
                    (MindMapNode) target));
        }
    }

    private record DeferredLink(MindMapNodeImpl source, String destinationLegacyId) {}
}
