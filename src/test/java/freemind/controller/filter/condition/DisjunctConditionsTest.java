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

class DisjunctConditionsTest {

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
        DisjunctConditions disjunct = new DisjunctConditions(List.of(alwaysTrue(), alwaysTrue()));
        assertTrue(disjunct.checkNode(controller, node));
    }

    @Test
    void checkNode_returnsFalse_whenOneConditionFalse() {
        // Note: the current implementation of DisjunctConditions uses the same logic
        // as ConjunctConditions (returns false if ANY condition is false).
        // This tests the actual behavior of the code.
        DisjunctConditions disjunct = new DisjunctConditions(List.of(alwaysTrue(), alwaysFalse()));
        assertFalse(disjunct.checkNode(controller, node));
    }

    @Test
    void checkNode_returnsFalse_whenAllConditionsFalse() {
        DisjunctConditions disjunct = new DisjunctConditions(List.of(alwaysFalse(), alwaysFalse()));
        assertFalse(disjunct.checkNode(controller, node));
    }

    @Test
    void checkNode_returnsTrue_withEmptyConditionList() {
        // No conditions to fail, so anyMatch returns false, !false = true
        DisjunctConditions disjunct = new DisjunctConditions(List.of());
        assertTrue(disjunct.checkNode(controller, node));
    }

    @Test
    void checkNode_returnsTrue_withSingleTrueCondition() {
        DisjunctConditions disjunct = new DisjunctConditions(List.of(alwaysTrue()));
        assertTrue(disjunct.checkNode(controller, node));
    }

    @Test
    void checkNode_returnsFalse_withSingleFalseCondition() {
        DisjunctConditions disjunct = new DisjunctConditions(List.of(alwaysFalse()));
        assertFalse(disjunct.checkNode(controller, node));
    }

    @Test
    void save_createsCorrectElement() {
        Document doc = FreeMindXml.newDocument();
        Element root = doc.createElement("root");
        doc.appendChild(root);

        // Use real NodeContainsCondition children so save works
        when(node.getText()).thenReturn("hello world");
        NodeContainsCondition c1 = new NodeContainsCondition("hello");
        NodeContainsCondition c2 = new NodeContainsCondition("world");
        DisjunctConditions disjunct = new DisjunctConditions(List.of(c1, c2));

        disjunct.save(doc, root);

        Element saved = (Element) root.getFirstChild();
        assertEquals("disjunct_condition", saved.getTagName());
        // Should have 2 child elements
        assertEquals(2, FreeMindXml.getChildElements(saved).size());
    }

    @Test
    void saveAndLoad_roundtrip() {
        Document doc = FreeMindXml.newDocument();
        Element root = doc.createElement("root");
        doc.appendChild(root);

        NodeContainsCondition c1 = new NodeContainsCondition("alpha");
        NodeContainsCondition c2 = new NodeContainsCondition("beta");
        DisjunctConditions original = new DisjunctConditions(List.of(c1, c2));

        original.save(doc, root);

        Element saved = (Element) root.getFirstChild();
        Condition loaded = DisjunctConditions.load(saved);
        assertNotNull(loaded);
        assertInstanceOf(DisjunctConditions.class, loaded);

        // Verify loaded condition works: node with both "alpha" and "beta" passes
        when(node.getText()).thenReturn("alpha beta");
        assertTrue(loaded.checkNode(controller, node));
    }
}
