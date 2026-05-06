package freemind.diagram.capabilities;

import freemind.diagram.AttributeBag;
import freemind.diagram.DiagramNode;
import freemind.diagram.DiagramNodeListener;
import freemind.diagram.NodeContent;
import freemind.diagram.NodeId;
import freemind.diagram.StyleReferences;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class AuxiliaryLinkTest {

    private static DiagramNode node(String id) {
        return new TestNode(new NodeId(id));
    }

    @Test
    void factoryProducesUnlabeledLinkWithEmptyStyle() {
        var link = AuxiliaryLink.of(node("a"), node("b"));
        assertTrue(link.label().isEmpty());
        assertEquals(StyleReferences.none(), link.style());
    }

    @Test
    void rejectsNullSource() {
        assertThrows(NullPointerException.class,
            () -> new AuxiliaryLink<>(null, node("b"), Optional.empty(), StyleReferences.none()));
    }

    @Test
    void rejectsNullStyle() {
        assertThrows(NullPointerException.class,
            () -> new AuxiliaryLink<>(node("a"), node("b"), Optional.empty(), null));
    }

    private record TestNode(NodeId nodeId) implements DiagramNode {
        @Override public NodeContent content()                  { return NodeContent.plain(""); }
        @Override public StyleReferences styleReferences()      { return StyleReferences.none(); }
        @Override public AttributeBag attributes()              { return AttributeBag.empty(); }
        @Override public void addListener(DiagramNodeListener l) { /* no-op for test */ }
        @Override public void removeListener(DiagramNodeListener l) { /* no-op for test */ }
    }
}
