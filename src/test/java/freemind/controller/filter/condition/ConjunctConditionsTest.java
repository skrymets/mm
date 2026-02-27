package freemind.controller.filter.condition;

import freemind.controller.Controller;
import freemind.main.FreeMindXml;
import freemind.model.MindMapNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConjunctConditionsTest {

    private Controller controller;
    private MindMapNode node;

    @BeforeEach
    void setUp() {
        controller = mock(Controller.class);
        node = mock(MindMapNode.class);
    }

    private Condition alwaysTrue() {
        Condition c = mock(Condition.class);
        when(c.checkNode(any(), any())).thenReturn(true);
        return c;
    }

    private Condition alwaysFalse() {
        Condition c = mock(Condition.class);
        when(c.checkNode(any(), any())).thenReturn(false);
        return c;
    }

    @Test
    void checkNode_returnsTrue_whenAllConditionsTrue() {
        ConjunctConditions conjunct = new ConjunctConditions(List.of(alwaysTrue(), alwaysTrue()));
        assertTrue(conjunct.checkNode(controller, node));
    }

    @Test
    void checkNode_returnsFalse_whenOneConditionFalse() {
        ConjunctConditions conjunct = new ConjunctConditions(List.of(alwaysTrue(), alwaysFalse()));
        assertFalse(conjunct.checkNode(controller, node));
    }

    @Test
    void checkNode_returnsFalse_whenAllConditionsFalse() {
        ConjunctConditions conjunct = new ConjunctConditions(List.of(alwaysFalse(), alwaysFalse()));
        assertFalse(conjunct.checkNode(controller, node));
    }

    @Test
    void checkNode_returnsTrue_withEmptyConditionList() {
        // No conditions to fail, so anyMatch returns false, !false = true
        ConjunctConditions conjunct = new ConjunctConditions(List.of());
        assertTrue(conjunct.checkNode(controller, node));
    }

    @Test
    void checkNode_returnsTrue_withSingleTrueCondition() {
        ConjunctConditions conjunct = new ConjunctConditions(List.of(alwaysTrue()));
        assertTrue(conjunct.checkNode(controller, node));
    }

    @Test
    void checkNode_returnsFalse_withSingleFalseCondition() {
        ConjunctConditions conjunct = new ConjunctConditions(List.of(alwaysFalse()));
        assertFalse(conjunct.checkNode(controller, node));
    }

    @Test
    void checkNode_returnsFalse_whenFirstConditionFails() {
        ConjunctConditions conjunct = new ConjunctConditions(List.of(alwaysFalse(), alwaysTrue()));
        assertFalse(conjunct.checkNode(controller, node));
    }

    @Test
    void save_createsCorrectElement() {
        Document doc = FreeMindXml.newDocument();
        Element root = doc.createElement("root");
        doc.appendChild(root);

        when(node.getText()).thenReturn("hello world");
        NodeContainsCondition c1 = new NodeContainsCondition("hello");
        NodeContainsCondition c2 = new NodeContainsCondition("world");
        ConjunctConditions conjunct = new ConjunctConditions(List.of(c1, c2));

        conjunct.save(doc, root);

        Element saved = (Element) root.getFirstChild();
        assertEquals("conjunct_condition", saved.getTagName());
        assertEquals(2, FreeMindXml.getChildElements(saved).size());
    }

    @Test
    void saveAndLoad_roundtrip() {
        Document doc = FreeMindXml.newDocument();
        Element root = doc.createElement("root");
        doc.appendChild(root);

        NodeContainsCondition c1 = new NodeContainsCondition("alpha");
        NodeContainsCondition c2 = new NodeContainsCondition("beta");
        ConjunctConditions original = new ConjunctConditions(List.of(c1, c2));

        original.save(doc, root);

        Element saved = (Element) root.getFirstChild();
        Condition loaded = ConjunctConditions.load(saved);
        assertNotNull(loaded);
        assertInstanceOf(ConjunctConditions.class, loaded);

        // Both conditions must match for AND
        when(node.getText()).thenReturn("alpha beta");
        assertTrue(loaded.checkNode(controller, node));

        // Only one matches -- should fail
        when(node.getText()).thenReturn("alpha only");
        assertFalse(loaded.checkNode(controller, node));
    }
}
