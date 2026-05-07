package freemind.diagram.mindmap.legacy;

import freemind.controller.actions.xml.document.Arrowlink;
import freemind.controller.actions.xml.document.Attribute;
import freemind.controller.actions.xml.document.Html;
import freemind.controller.actions.xml.document.Node;
import freemind.controller.actions.xml.document.Richcontent;
import freemind.diagram.mindmap.MindMapDiagram;
import freemind.diagram.persistence.external.ExportContext;
import freemind.diagram.persistence.external.ExternalDiagramFormat;
import freemind.diagram.persistence.external.ExternalFormatId;
import freemind.diagram.persistence.external.ImportContext;
import freemind.diagram.persistence.external.ProbeContext;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

/**
 * Legacy {@code .mm} (FreeMind) import format. Read-only.
 *
 * <p>Parses the {@code .mm} file via DOM (not JAXB) because
 * {@link Node#getChoiceList()} is a hand-written union wrapper that carries no
 * JAXB {@code @XmlElements} mapping; JAXB cannot populate it during unmarshal.
 * The DOM bridge constructs the same {@code document.Map} / {@code document.Node}
 * tree that {@link FreemindMmConverter} expects.
 */
@Slf4j
public final class FreemindMmImportFormat implements ExternalDiagramFormat<MindMapDiagram> {

    public static final ExternalFormatId ID = new ExternalFormatId("freemind.mm");
    private static final Set<String> EXTENSIONS = Set.of("mm");

    private final FreemindMmConverter converter = new FreemindMmConverter();

    @Override public ExternalFormatId id()        { return ID; }
    @Override public Set<String> fileExtensions() { return EXTENSIONS; }

    @Override
    public boolean canRead(Path file, ProbeContext context) {
        var name = file.getFileName().toString().toLowerCase();
        return name.endsWith(".mm");
    }

    @Override
    public MindMapDiagram importDiagram(Path file, ImportContext context) {
        try (var in = Files.newInputStream(file)) {
            var dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(false);
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
            dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            dbf.setXIncludeAware(false);
            dbf.setExpandEntityReferences(false);
            var doc = dbf.newDocumentBuilder().parse(in);
            var docElement = doc.getDocumentElement();
            if (docElement == null) {
                throw new IllegalStateException(
                    "File is not a valid .mm document (no root element): " + file);
            }
            if (!"map".equals(docElement.getTagName())) {
                throw new IllegalStateException(
                    "File is not a .mm document (root element is <" + docElement.getTagName()
                    + ">, expected <map>): " + file);
            }
            var legacyMap = parseMap(docElement);
            return converter.convert(legacyMap);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read .mm file: " + file, e);
        } catch (SAXException | ParserConfigurationException e) {
            throw new IllegalStateException("Failed to parse .mm XML in file: " + file, e);
        }
    }

    @Override public boolean canExport(MindMapDiagram diagram) { return false; }

    @Override
    public void exportDiagram(MindMapDiagram diagram, Path file, ExportContext context) {
        throw new UnsupportedOperationException(".mm export is not supported (read-only legacy format)");
    }

    // -------------------------------------------------------------------------
    // DOM -> document model bridge
    // -------------------------------------------------------------------------

    private static freemind.controller.actions.xml.document.Map parseMap(Element mapEl) {
        var map = new freemind.controller.actions.xml.document.Map();
        map.setVersion(mapEl.getAttribute("version"));
        var nodeEl = firstChildElement(mapEl, "node");
        if (nodeEl != null) {
            map.setNode(parseNode(nodeEl));
        }
        return map;
    }

    private static Node parseNode(Element el) {
        var node = new Node();
        node.setID(el.getAttribute("ID"));
        var text = el.getAttribute("TEXT");
        if (!text.isEmpty()) {
            node.setTEXT(text);
        }

        var children = el.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (!(children.item(i) instanceof Element child)) {
                continue;
            }
            var choice = buildChoice(child);
            if (choice != null) {
                node.addChoice(choice);
            }
        }
        return node;
    }

    private static Node.Choice buildChoice(Element el) {
        var choice = new Node.Choice();
        return switch (el.getTagName()) {
            case "node" -> {
                choice.setNode(parseNode(el));
                yield choice;
            }
            case "arrowlink" -> {
                var al = new Arrowlink();
                al.setDESTINATION(el.getAttribute("DESTINATION"));
                al.setID(el.getAttribute("ID"));
                al.setCOLOR(el.getAttribute("COLOR"));
                choice.setArrowlink(al);
                yield choice;
            }
            case "attribute" -> {
                var attr = new Attribute();
                attr.setNAME(el.getAttribute("NAME"));
                attr.setVALUE(el.getAttribute("VALUE"));
                choice.setAttribute(attr);
                yield choice;
            }
            case "richcontent" -> {
                var rc = buildRichcontent(el);
                if (rc != null) {
                    choice.setRichcontent(rc);
                    yield choice;
                }
                yield null;
            }
            default -> null; // cloud, edge, font, hook, icon, linktarget — skip for Plan 1
        };
    }

    private static Richcontent buildRichcontent(Element el) {
        var typeAttr = el.getAttribute("TYPE");
        Richcontent.TYPE type;
        try {
            type = Richcontent.TYPE.valueOf(typeAttr);
        } catch (IllegalArgumentException e) {
            log.warn("Unknown richcontent TYPE '{}'; skipping", typeAttr);
            return null;
        }

        var htmlEl = firstChildElement(el, "html");
        if (htmlEl == null) {
            log.warn("richcontent element has no <html> child; skipping");
            return null;
        }

        var html = new Html();
        NodeList htmlChildren = htmlEl.getChildNodes();
        for (int i = 0; i < htmlChildren.getLength(); i++) {
            if (htmlChildren.item(i) instanceof Element child) {
                html.addAny(child);
            }
        }

        var rc = new Richcontent();
        rc.setTYPE1(type);
        rc.setHtml(html);
        return rc;
    }

    private static Element firstChildElement(Element parent, String tagName) {
        var children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i) instanceof Element el && tagName.equals(el.getTagName())) {
                return el;
            }
        }
        return null;
    }
}
