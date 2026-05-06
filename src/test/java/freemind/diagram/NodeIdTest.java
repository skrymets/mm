package freemind.diagram;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NodeIdTest {

    @Test
    void acceptsArbitraryNonBlankString() {
        assertEquals("n_123", new NodeId("n_123").value());
        assertEquals("ID_42", new NodeId("ID_42").value());
    }

    @Test
    void rejectsNullValue() {
        assertThrows(NullPointerException.class, () -> new NodeId(null));
    }

    @Test
    void rejectsBlankValue() {
        assertThrows(IllegalArgumentException.class, () -> new NodeId(""));
        assertThrows(IllegalArgumentException.class, () -> new NodeId("   "));
    }

    @Test
    void equalityIsValueBased() {
        assertEquals(new NodeId("x"), new NodeId("x"));
        assertNotEquals(new NodeId("x"), new NodeId("y"));
    }
}
