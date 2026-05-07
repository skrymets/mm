package freemind.diagram.mindmap;

import freemind.diagram.AttributeBag;
import freemind.diagram.ContentFormat;
import freemind.diagram.NodeContent;
import freemind.diagram.NodeId;
import freemind.diagram.StyleId;
import freemind.diagram.StyleRef;
import freemind.diagram.StyleReferences;
import freemind.diagram.capabilities.AuxiliaryLink;
import freemind.diagram.persistence.DiagramPayloadCodec;
import freemind.diagram.persistence.PayloadReadContext;
import freemind.diagram.persistence.PayloadWriteContext;
import freemind.diagram.persistence.UnsupportedPayloadVersionException;
import freemind.diagram.mindmap.payload.MindMapAuxLinkXml;
import freemind.diagram.mindmap.payload.MindMapNodeXml;
import freemind.diagram.mindmap.payload.MindMapPayloadXml;
import freemind.diagram.mindmap.payload.StyleRefXml;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/** JAXB-backed codec for the {@link MindMapDiagramImpl} payload. */
public final class MindMapPayloadCodec implements DiagramPayloadCodec<MindMapDiagram> {

    private static final int CURRENT_VERSION = 1;
    private final JAXBContext jaxb;

    public MindMapPayloadCodec() {
        try {
            this.jaxb = JAXBContext.newInstance(MindMapPayloadXml.class);
        } catch (JAXBException e) {
            throw new IllegalStateException("Failed to build mindmap payload JAXB context", e);
        }
    }

    @Override public int currentPayloadVersion()              { return CURRENT_VERSION; }
    @Override public Set<Integer> supportedPayloadVersions()  { return Set.of(CURRENT_VERSION); }

    @Override
    public MindMapDiagram readPayload(int payloadVersion, PayloadReadContext ctx) {
        if (!supportedPayloadVersions().contains(payloadVersion)) {
            throw new UnsupportedPayloadVersionException(MindMapDiagramImpl.TYPE_ID, payloadVersion);
        }
        var xml = unmarshalFromElement(ctx.payloadElement());
        return buildDiagramFromXml(xml, ctx);
    }

    @Override
    public void writePayload(MindMapDiagram diagram, PayloadWriteContext ctx) {
        var xml = buildXmlFromDiagram(diagram);
        var dom = marshalToDocument(xml);
        ctx.setPayloadRoot(dom.getDocumentElement());
    }

    // --- read path: XML -> MindMapDiagramImpl ---

    private MindMapDiagram buildDiagramFromXml(MindMapPayloadXml xml, PayloadReadContext ctx) {
        var rootImpl = nodeFromXml(xml.root);
        var diagram = new MindMapDiagramImpl(ctx.documentId(), rootImpl, ctx.metadata());
        diagram.setStylePalette(ctx.stylePalette());
        diagram.setResources(ctx.resources());
        // Build node id -> node map for aux-link resolution and recursive child attachment.
        var byId = new LinkedHashMap<String, MindMapNodeImpl>();
        byId.put(rootImpl.nodeId().value(), rootImpl);
        attachChildren(diagram, rootImpl, xml.root, byId);
        for (var linkXml : xml.auxiliaryLinks) {
            var src = byId.get(linkXml.source);
            var tgt = byId.get(linkXml.target);
            if (src == null || tgt == null) {
                throw new IllegalStateException("Auxiliary link references unknown node id: "
                    + linkXml.source + " -> " + linkXml.target);
            }
            var label = linkXml.label == null ? Optional.<String>empty() : Optional.of(linkXml.label);
            diagram.addAuxiliaryLink(new AuxiliaryLink<>(src, tgt, label, StyleReferences.none()));
        }
        return diagram;
    }

    private void attachChildren(MindMapDiagramImpl diagram, MindMapNodeImpl parent,
                                 MindMapNodeXml parentXml, Map<String, MindMapNodeImpl> byId) {
        for (var childXml : parentXml.children) {
            var child = nodeFromXml(childXml);
            byId.put(child.nodeId().value(), child);
            diagram.addChild(parent, child);
            attachChildren(diagram, child, childXml, byId);
        }
    }

    private MindMapNodeImpl nodeFromXml(MindMapNodeXml xml) {
        var content = contentFromXml(xml.content);
        var node = new MindMapNodeImpl(new NodeId(xml.nodeId), content);
        if (xml.style != null) {
            node.setStyleReferences(styleRefsFromXml(xml.style));
        }
        if (xml.attributes != null) {
            var bag = AttributeBag.empty();
            for (var entry : xml.attributes.entries) {
                bag = bag.with(entry.key, entry.value);
            }
            node.setAttributes(bag);
        }
        return node;
    }

    private NodeContent contentFromXml(MindMapNodeXml.ContentXml xml) {
        var format = ContentFormat.valueOf(xml.format);
        var text = xml.text == null ? "" : xml.text;
        return new NodeContent(text, format);
    }

    private StyleReferences styleRefsFromXml(StyleRefXml xml) {
        return new StyleReferences(
            xml.colorRef == null  ? Optional.empty() : Optional.of(new StyleRef(new StyleId(xml.colorRef))),
            xml.fontRef == null   ? Optional.empty() : Optional.of(new StyleRef(new StyleId(xml.fontRef))),
            xml.strokeRef == null ? Optional.empty() : Optional.of(new StyleRef(new StyleId(xml.strokeRef))));
    }

    // --- write path: MindMapDiagram -> XML ---

    private MindMapPayloadXml buildXmlFromDiagram(MindMapDiagram diagram) {
        var xml = new MindMapPayloadXml();
        xml.root = nodeToXml(diagram, diagram.rootNode());
        for (var link : diagram.auxiliaryLinks()) {
            var linkXml = new MindMapAuxLinkXml();
            linkXml.source = link.source().nodeId().value();
            linkXml.target = link.target().nodeId().value();
            linkXml.label = link.label().orElse(null);
            xml.auxiliaryLinks.add(linkXml);
        }
        return xml;
    }

    private MindMapNodeXml nodeToXml(MindMapDiagram diagram, MindMapNode node) {
        var xml = new MindMapNodeXml();
        xml.nodeId = node.nodeId().value();
        xml.content = contentToXml(node.content());
        var styleRefs = node.styleReferences();
        if (styleRefs.color().isPresent() || styleRefs.font().isPresent() || styleRefs.stroke().isPresent()) {
            xml.style = styleRefsToXml(styleRefs);
        }
        var attrs = node.attributes();
        if (!attrs.values().isEmpty()) {
            xml.attributes = attributesToXml(attrs);
        }
        for (var child : diagram.getChildren(node)) {
            xml.children.add(nodeToXml(diagram, child));
        }
        return xml;
    }

    private MindMapNodeXml.ContentXml contentToXml(NodeContent content) {
        var xml = new MindMapNodeXml.ContentXml();
        xml.format = content.format().name();
        xml.text = content.text();
        return xml;
    }

    private StyleRefXml styleRefsToXml(StyleReferences styleRefs) {
        var xml = new StyleRefXml();
        xml.colorRef  = styleRefs.color().map(r -> r.paletteId().value()).orElse(null);
        xml.fontRef   = styleRefs.font().map(r -> r.paletteId().value()).orElse(null);
        xml.strokeRef = styleRefs.stroke().map(r -> r.paletteId().value()).orElse(null);
        return xml;
    }

    private MindMapNodeXml.AttributesXml attributesToXml(AttributeBag attrs) {
        var xml = new MindMapNodeXml.AttributesXml();
        attrs.values().forEach((k, v) -> {
            var entry = new MindMapNodeXml.AttributeXml();
            entry.key = k;
            entry.value = v;
            xml.entries.add(entry);
        });
        return xml;
    }

    // --- JAXB I/O via DOM ---

    private MindMapPayloadXml unmarshalFromElement(Element element) {
        try {
            Unmarshaller u = jaxb.createUnmarshaller();
            return (MindMapPayloadXml) u.unmarshal(new DOMSource(element));
        } catch (JAXBException e) {
            throw new IllegalStateException("Failed to unmarshal mindmap payload", e);
        }
    }

    private Document marshalToDocument(MindMapPayloadXml xml) {
        try {
            var dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            var doc = dbf.newDocumentBuilder().newDocument();
            Marshaller m = jaxb.createMarshaller();
            m.marshal(xml, new DOMResult(doc));
            return doc;
        } catch (JAXBException | ParserConfigurationException e) {
            throw new IllegalStateException("Failed to marshal mindmap payload", e);
        }
    }
}
