package freemind.main;

import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FreeMindXmlTest {

    @Test
    void parseSimpleXmlAndVerifyElementNameAndAttributes() {
        String xml = "<node TEXT=\"Hello\" POSITION=\"right\"/>";
        Document doc = FreeMindXml.parse(xml);
        Element root = doc.getDocumentElement();

        assertEquals("node", root.getTagName());
        assertEquals("Hello", FreeMindXml.getStringAttribute(root, "TEXT"));
        assertEquals("right", FreeMindXml.getStringAttribute(root, "POSITION"));
    }

    @Test
    void getChildElementsFiltersTextNodes() {
        String xml = "<root>\n  text content\n  <child1/>\n  more text\n  <child2/>\n</root>";
        Document doc = FreeMindXml.parse(xml);
        Element root = doc.getDocumentElement();

        List<Element> children = FreeMindXml.getChildElements(root);
        assertEquals(2, children.size());
        assertEquals("child1", children.get(0).getTagName());
        assertEquals("child2", children.get(1).getTagName());
    }

    @Test
    void getStringAttributeReturnsNullForMissing() {
        String xml = "<node TEXT=\"Hello\"/>";
        Document doc = FreeMindXml.parse(xml);
        Element root = doc.getDocumentElement();

        assertNull(FreeMindXml.getStringAttribute(root, "NONEXISTENT"));
    }

    @Test
    void getStringAttributeReturnsEmptyStringWhenSetToEmpty() {
        String xml = "<node TEXT=\"\"/>";
        Document doc = FreeMindXml.parse(xml);
        Element root = doc.getDocumentElement();

        assertEquals("", FreeMindXml.getStringAttribute(root, "TEXT"));
    }

    @Test
    void getIntAttributeWithDefaultValue() {
        String xml = "<node WIDTH=\"42\" BAD=\"abc\"/>";
        Document doc = FreeMindXml.parse(xml);
        Element root = doc.getDocumentElement();

        assertEquals(42, FreeMindXml.getIntAttribute(root, "WIDTH", -1));
        assertEquals(-1, FreeMindXml.getIntAttribute(root, "MISSING", -1));
        assertEquals(-1, FreeMindXml.getIntAttribute(root, "BAD", -1));
    }

    @Test
    void getDoubleAttribute() {
        String xml = "<edge WIDTH=\"1.5\"/>";
        Document doc = FreeMindXml.parse(xml);
        Element root = doc.getDocumentElement();

        assertEquals(1.5, FreeMindXml.getDoubleAttribute(root, "WIDTH", 0.0), 0.001);
        assertEquals(0.0, FreeMindXml.getDoubleAttribute(root, "MISSING", 0.0), 0.001);
    }

    @Test
    void getBooleanAttribute() {
        String xml = "<node FOLDED=\"true\" VISIBLE=\"false\"/>";
        Document doc = FreeMindXml.parse(xml);
        Element root = doc.getDocumentElement();

        assertTrue(FreeMindXml.getBooleanAttribute(root, "FOLDED", false));
        assertFalse(FreeMindXml.getBooleanAttribute(root, "VISIBLE", true));
        assertTrue(FreeMindXml.getBooleanAttribute(root, "MISSING", true));
        assertFalse(FreeMindXml.getBooleanAttribute(root, "MISSING", false));
    }

    @Test
    void createElementAndWriteRoundtrip() {
        Document doc = FreeMindXml.newDocument();
        Element root = FreeMindXml.createElement(doc, "map");
        root.setAttribute("version", "1.0");
        doc.appendChild(root);

        Element child = FreeMindXml.createElement(doc, "node");
        child.setAttribute("TEXT", "Root");
        root.appendChild(child);

        StringWriter sw = new StringWriter();
        FreeMindXml.write(doc, sw);
        String output = sw.toString();

        // Parse back and verify
        Document parsed = FreeMindXml.parse(output);
        Element parsedRoot = parsed.getDocumentElement();
        assertEquals("map", parsedRoot.getTagName());
        assertEquals("1.0", FreeMindXml.getStringAttribute(parsedRoot, "version"));

        Element parsedChild = FreeMindXml.getChildElement(parsedRoot, "node");
        assertNotNull(parsedChild);
        assertEquals("Root", FreeMindXml.getStringAttribute(parsedChild, "TEXT"));
    }

    @Test
    void nestedElementsWriteAndParseRoundtrip() {
        Document doc = FreeMindXml.newDocument();
        Element map = FreeMindXml.createElement(doc, "map");
        doc.appendChild(map);

        Element node1 = FreeMindXml.createElement(doc, "node");
        node1.setAttribute("TEXT", "Parent");
        map.appendChild(node1);

        Element node2 = FreeMindXml.createElement(doc, "node");
        node2.setAttribute("TEXT", "Child1");
        node1.appendChild(node2);

        Element node3 = FreeMindXml.createElement(doc, "node");
        node3.setAttribute("TEXT", "Child2");
        node1.appendChild(node3);

        String xml = FreeMindXml.toString(map);

        // Parse back
        Document parsed = FreeMindXml.parse(xml);
        Element parsedMap = parsed.getDocumentElement();
        Element parsedNode1 = FreeMindXml.getChildElement(parsedMap, "node");
        assertNotNull(parsedNode1);
        assertEquals("Parent", FreeMindXml.getStringAttribute(parsedNode1, "TEXT"));

        List<Element> grandChildren = FreeMindXml.getChildElements(parsedNode1);
        assertEquals(2, grandChildren.size());
        assertEquals("Child1", FreeMindXml.getStringAttribute(grandChildren.get(0), "TEXT"));
        assertEquals("Child2", FreeMindXml.getStringAttribute(grandChildren.get(1), "TEXT"));
    }

    @Test
    void parseFromReader() {
        StringReader reader = new StringReader("<test attr=\"value\">content</test>");
        Document doc = FreeMindXml.parse(reader);
        Element root = doc.getDocumentElement();

        assertEquals("test", root.getTagName());
        assertEquals("value", FreeMindXml.getStringAttribute(root, "attr"));
        assertEquals("content", FreeMindXml.getTextContent(root));
    }

    @Test
    void getChildElementReturnsNullWhenNotFound() {
        String xml = "<root><child/></root>";
        Document doc = FreeMindXml.parse(xml);
        Element root = doc.getDocumentElement();

        assertNull(FreeMindXml.getChildElement(root, "nonexistent"));
    }

    @Test
    void toStringProducesXml() {
        Document doc = FreeMindXml.newDocument();
        Element el = FreeMindXml.createElement(doc, "node");
        el.setAttribute("TEXT", "hello");
        doc.appendChild(el);

        String result = FreeMindXml.toString(el);
        assertTrue(result.contains("node"));
        assertTrue(result.contains("TEXT"));
        assertTrue(result.contains("hello"));
        // Should not contain XML declaration
        assertFalse(result.contains("<?xml"));
    }
}
