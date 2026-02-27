package freemind.controller.filter.condition;

import freemind.controller.Controller;
import freemind.main.FreeMindXml;
import freemind.model.MindMapNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NodeContainsConditionTest {

    private Controller controller;
    private MindMapNode node;

    @BeforeEach
    void setUp() {
        controller = mock(Controller.class);
        node = mock(MindMapNode.class);
    }

    @Test
    void checkNode_returnsTrue_whenNodeTextContainsValue() {
        when(node.getText()).thenReturn("Hello World");
        NodeContainsCondition condition = new NodeContainsCondition("World");
        assertTrue(condition.checkNode(controller, node));
    }

    @Test
    void checkNode_returnsTrue_whenNodeTextEqualsValue() {
        when(node.getText()).thenReturn("exact");
        NodeContainsCondition condition = new NodeContainsCondition("exact");
        assertTrue(condition.checkNode(controller, node));
    }

    @Test
    void checkNode_returnsFalse_whenNodeTextDoesNotContainValue() {
        when(node.getText()).thenReturn("Hello World");
        NodeContainsCondition condition = new NodeContainsCondition("xyz");
        assertFalse(condition.checkNode(controller, node));
    }

    @Test
    void checkNode_isCaseSensitive() {
        when(node.getText()).thenReturn("Hello World");
        NodeContainsCondition condition = new NodeContainsCondition("hello");
        assertFalse(condition.checkNode(controller, node));
    }

    @Test
    void checkNode_returnsTrue_whenValueIsEmpty() {
        when(node.getText()).thenReturn("anything");
        NodeContainsCondition condition = new NodeContainsCondition("");
        assertTrue(condition.checkNode(controller, node));
    }

    @Test
    void saveAndLoad_roundtrip() {
        Document doc = FreeMindXml.newDocument();
        Element root = doc.createElement("root");
        doc.appendChild(root);

        NodeContainsCondition original = new NodeContainsCondition("test value");
        original.save(doc, root);

        Element saved = (Element) root.getFirstChild();
        assertEquals("node_contains_condition", saved.getTagName());
        assertEquals("test value", saved.getAttribute("value"));

        Condition loaded = NodeContainsCondition.load(saved);
        assertNotNull(loaded);

        when(node.getText()).thenReturn("this is a test value here");
        assertTrue(loaded.checkNode(controller, node));
    }
}
