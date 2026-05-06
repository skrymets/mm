package freemind.diagram.topology;

import freemind.diagram.AttributeBag;
import freemind.diagram.DiagramNode;
import freemind.diagram.DiagramNodeListener;
import freemind.diagram.NodeContent;
import freemind.diagram.NodeId;
import freemind.diagram.StyleReferences;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EdgeTest {

    private static DiagramNode node(String id) {
        return new TestNode(new NodeId(id));
    }

    @Test
    void unlabeledEdgeHasNoLabel() {
        var e = Edge.unlabeled(node("a"), node("b"));
        assertTrue(e.label().isEmpty());
    }

    @Test
    void labeledEdgeWrapsString() {
        var e = Edge.labeled(node("a"), node("b"), "is-a");
        assertEquals("is-a", e.label().orElseThrow().value());
    }

    @Test
    void rejectsNullSource() {
        assertThrows(NullPointerException.class,
            () -> Edge.unlabeled(null, node("b")));
    }

    private record TestNode(NodeId nodeId) implements DiagramNode {
        @Override public NodeContent content()                  { return NodeContent.plain(""); }
        @Override public StyleReferences styleReferences()      { return StyleReferences.none(); }
        @Override public AttributeBag attributes()              { return AttributeBag.empty(); }
        @Override public void addListener(DiagramNodeListener l) { /* no-op for test */ }
        @Override public void removeListener(DiagramNodeListener l) { /* no-op for test */ }
    }
}
