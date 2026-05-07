package freemind.diagram.mindmap.legacy;

import freemind.controller.actions.xml.document.Arrowlink;
import freemind.controller.actions.xml.document.Attribute;
import freemind.controller.actions.xml.document.Html;
import freemind.controller.actions.xml.document.Node;
import freemind.controller.actions.xml.document.Richcontent;
import freemind.diagram.ContentFormat;
import freemind.diagram.mindmap.MindMapDiagram;
import freemind.diagram.mindmap.MindMapNode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FreemindMmConverterTest {

    private FreemindMmConverter converter;

    @BeforeEach
    void setUp() {
        converter = new FreemindMmConverter();
    }

    // --- helpers ---

    private static freemind.controller.actions.xml.document.Map mapWith(Node root) {
        var map = new freemind.controller.actions.xml.document.Map();
        map.setVersion("1.0.1");
        map.setNode(root);
        return map;
    }

    private static Node plainNode(String id, String text) {
        var node = new Node();
        node.setID(id);
        node.setTEXT(text);
        return node;
    }

    private static Node addChild(Node parent, Node child) {
        var choice = new Node.Choice();
        choice.setNode(child);
        parent.addChoice(choice);
        return parent;
    }

    private static Node addAttribute(Node node, String name, String value) {
        var attr = new Attribute();
        attr.setNAME(name);
        attr.setVALUE(value);
        var choice = new Node.Choice();
        choice.setAttribute(attr);
        node.addChoice(choice);
        return node;
    }

    private static Node addArrowlink(Node source, String destinationId) {
        var arrow = new Arrowlink();
        arrow.setDESTINATION(destinationId);
        var choice = new Node.Choice();
        choice.setArrowlink(arrow);
        source.addChoice(choice);
        return source;
    }

    private static Node addRichcontentNode(Node node, String htmlFragment) throws Exception {
        var dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        var builder = dbf.newDocumentBuilder();
        var doc = builder.newDocument();
        // Create a body element wrapping the text
        Element body = doc.createElement("body");
        body.setTextContent(htmlFragment);

        var html = new Html();
        html.addAny(body);

        var rc = new Richcontent();
        rc.setTYPE1(Richcontent.TYPE.NODE);
        rc.setHtml(html);

        var choice = new Node.Choice();
        choice.setRichcontent(rc);
        node.addChoice(choice);
        return node;
    }

    private static List<MindMapNode> allNodes(MindMapDiagram diagram) {
        var list = new java.util.ArrayList<MindMapNode>();
        diagram.allNodes().forEach(list::add);
        return list;
    }

    // --- tests ---

    @Test
    void convertsRootAndChildren() {
        var childA = plainNode("a1", "a");
        var root = plainNode("root1", "root");
        addChild(root, childA);

        var diagram = converter.convert(mapWith(root));

        assertEquals("root1", diagram.rootNode().nodeId().value());
        assertEquals("root", diagram.rootNode().content().text());

        var children = diagram.getChildren(diagram.rootNode());
        assertEquals(1, children.size());
        assertEquals("a1", children.get(0).nodeId().value());
        assertEquals("a", children.get(0).content().text());
    }

    @Test
    void convertsRichcontentAsHtml() throws Exception {
        var node = new Node();
        node.setID("rc1");
        // No TEXT attribute; richcontent provides content
        addRichcontentNode(node, "Hello <b>world</b>");

        var diagram = converter.convert(mapWith(node));

        var content = diagram.rootNode().content();
        assertEquals(ContentFormat.HTML, content.format());
        assertTrue(content.text().contains("Hello"), "HTML content should contain 'Hello'");
    }

    @Test
    void convertsAttributes() {
        var node = plainNode("attr1", "node with attrs");
        addAttribute(node, "priority", "high");
        addAttribute(node, "status", "done");

        var diagram = converter.convert(mapWith(node));

        var attrs = diagram.rootNode().attributes();
        assertEquals("high", attrs.get("priority").orElse(null));
        assertEquals("done", attrs.get("status").orElse(null));
    }

    @Test
    void convertsArrowlink() {
        var nodeB = plainNode("bId", "B");
        var nodeA = plainNode("aId", "A");
        addArrowlink(nodeA, "bId");
        addChild(nodeA, nodeB);

        var diagram = converter.convert(mapWith(nodeA));

        var links = diagram.outgoingAuxiliaryLinks(diagram.rootNode());
        assertEquals(1, links.size(), "Expected exactly one auxiliary link from A");

        var link = links.get(0);
        assertEquals("aId", link.source().nodeId().value());
        assertEquals("bId", link.target().nodeId().value());
    }

    @Test
    void assignsGeneratedIdWhenNodeHasNoId() {
        var root = new Node();
        root.setTEXT("no-id root");
        // No ID set

        var diagram = converter.convert(mapWith(root));

        var id = diagram.rootNode().nodeId().value();
        assertFalse(id.isBlank(), "Generated ID must be non-blank");
        assertTrue(id.startsWith("gen-"), "Generated ID should start with 'gen-'");
    }

    @Test
    void skipsArrowlinkWithUnresolvableDestination() {
        var root = plainNode("r1", "root");
        // Arrow pointing to a non-existent node
        addArrowlink(root, "nonexistent-id");

        // Should not throw; just logs a warning
        var diagram = converter.convert(mapWith(root));

        var links = diagram.outgoingAuxiliaryLinks(diagram.rootNode());
        assertTrue(links.isEmpty(), "Unresolvable arrowlink should be skipped");
    }
}
