package freemind.diagram;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DiagramTypeIdTest {

    @Test
    void acceptsLowercaseHyphenatedIdentifier() {
        var id = new DiagramTypeId("mindmap");
        assertEquals("mindmap", id.value());
    }

    @Test
    void acceptsHyphenatedMultiWordIdentifier() {
        var id = new DiagramTypeId("concept-map");
        assertEquals("concept-map", id.value());
    }

    @Test
    void rejectsNullValue() {
        assertThrows(NullPointerException.class, () -> new DiagramTypeId(null));
    }

    @Test
    void rejectsBlankValue() {
        assertThrows(IllegalArgumentException.class, () -> new DiagramTypeId(""));
        assertThrows(IllegalArgumentException.class, () -> new DiagramTypeId("   "));
    }

    @Test
    void rejectsUppercase() {
        assertThrows(IllegalArgumentException.class, () -> new DiagramTypeId("MindMap"));
    }

    @Test
    void rejectsWhitespaceInValue() {
        assertThrows(IllegalArgumentException.class, () -> new DiagramTypeId("mind map"));
    }

    @Test
    void equalityIsValueBased() {
        assertEquals(new DiagramTypeId("mindmap"), new DiagramTypeId("mindmap"));
        assertNotEquals(new DiagramTypeId("mindmap"), new DiagramTypeId("concept-map"));
    }
}
