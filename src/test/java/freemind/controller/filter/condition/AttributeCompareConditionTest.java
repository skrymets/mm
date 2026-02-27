package freemind.controller.filter.condition;

import freemind.controller.Controller;
import freemind.main.FreeMindXml;
import freemind.model.MindMapNode;
import freemind.modes.attributes.Attribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AttributeCompareConditionTest {

    private Controller controller;
    private MindMapNode node;

    @BeforeEach
    void setUp() {
        controller = mock(Controller.class);
        node = mock(MindMapNode.class);
    }

    private void setUpNodeWithAttribute(String name, String value) {
        when(node.getAttributeTableLength()).thenReturn(1);
        when(node.getAttribute(0)).thenReturn(new Attribute(name, value));
    }

    @Test
    void checkNode_equalsString_returnsTrue_whenAttributeMatches() {
        setUpNodeWithAttribute("status", "done");
        // comparationResult=0, succeed=true means "is equal to"
        AttributeCompareCondition condition = new AttributeCompareCondition(
                "status", "done", false, 0, true);
        assertTrue(condition.checkNode(controller, node));
    }

    @Test
    void checkNode_equalsString_returnsFalse_whenAttributeDoesNotMatch() {
        setUpNodeWithAttribute("status", "pending");
        AttributeCompareCondition condition = new AttributeCompareCondition(
                "status", "done", false, 0, true);
        assertFalse(condition.checkNode(controller, node));
    }

    @Test
    void checkNode_notEquals_returnsTrue_whenValuesDiffer() {
        setUpNodeWithAttribute("status", "pending");
        // comparationResult=0, succeed=false means "is not equal to"
        AttributeCompareCondition condition = new AttributeCompareCondition(
                "status", "done", false, 0, false);
        assertTrue(condition.checkNode(controller, node));
    }

    @Test
    void checkNode_returnsFalse_whenNoAttributes() {
        when(node.getAttributeTableLength()).thenReturn(0);
        AttributeCompareCondition condition = new AttributeCompareCondition(
                "status", "done", false, 0, true);
        assertFalse(condition.checkNode(controller, node));
    }

    @Test
    void checkNode_returnsFalse_whenAttributeNameDoesNotMatch() {
        setUpNodeWithAttribute("priority", "done");
        AttributeCompareCondition condition = new AttributeCompareCondition(
                "status", "done", false, 0, true);
        assertFalse(condition.checkNode(controller, node));
    }

    @Test
    void checkNode_numericComparison_lessThan() {
        setUpNodeWithAttribute("priority", "3");
        // comparationResult=-1, succeed=true means "less than"
        AttributeCompareCondition condition = new AttributeCompareCondition(
                "priority", "5", false, -1, true);
        assertTrue(condition.checkNode(controller, node));
    }

    @Test
    void checkNode_numericComparison_greaterThan() {
        setUpNodeWithAttribute("priority", "10");
        // comparationResult=1, succeed=true means "greater than"
        AttributeCompareCondition condition = new AttributeCompareCondition(
                "priority", "5", false, 1, true);
        assertTrue(condition.checkNode(controller, node));
    }

    @Test
    void checkNode_numericComparison_greaterThan_returnsFalse() {
        setUpNodeWithAttribute("priority", "3");
        AttributeCompareCondition condition = new AttributeCompareCondition(
                "priority", "5", false, 1, true);
        assertFalse(condition.checkNode(controller, node));
    }

    @Test
    void checkNode_ignoreCase_equalsString() {
        setUpNodeWithAttribute("status", "DONE");
        AttributeCompareCondition condition = new AttributeCompareCondition(
                "status", "done", true, 0, true);
        assertTrue(condition.checkNode(controller, node));
    }

    @Test
    void checkNode_multipleAttributes_matchesSecond() {
        when(node.getAttributeTableLength()).thenReturn(2);
        when(node.getAttribute(0)).thenReturn(new Attribute("color", "red"));
        when(node.getAttribute(1)).thenReturn(new Attribute("status", "done"));

        AttributeCompareCondition condition = new AttributeCompareCondition(
                "status", "done", false, 0, true);
        assertTrue(condition.checkNode(controller, node));
    }

    @Test
    void saveAndLoad_roundtrip() {
        Document doc = FreeMindXml.newDocument();
        Element root = doc.createElement("root");
        doc.appendChild(root);

        AttributeCompareCondition original = new AttributeCompareCondition(
                "priority", "5", true, -1, true);
        original.save(doc, root);

        Element saved = (Element) root.getFirstChild();
        assertEquals("attribute_compare_condition", saved.getTagName());
        assertEquals("priority", saved.getAttribute("attribute"));
        assertEquals("5", saved.getAttribute("value"));
        assertEquals("true", saved.getAttribute("ignore_case"));
        assertEquals("-1", saved.getAttribute("comparation_result"));
        assertEquals("true", saved.getAttribute("succeed"));

        Condition loaded = AttributeCompareCondition.load(saved);
        assertNotNull(loaded);
        assertInstanceOf(AttributeCompareCondition.class, loaded);

        // Verify loaded condition works: priority=3 < 5 should be true
        setUpNodeWithAttribute("priority", "3");
        assertTrue(loaded.checkNode(controller, node));
    }
}
