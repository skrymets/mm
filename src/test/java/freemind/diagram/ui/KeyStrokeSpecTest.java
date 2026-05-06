package freemind.diagram.ui;

import org.junit.jupiter.api.Test;
import java.util.EnumSet;
import static freemind.diagram.ui.KeyStrokeSpec.Modifier.*;
import static org.junit.jupiter.api.Assertions.*;

class KeyStrokeSpecTest {

    @Test
    void factoryWithNoModifiersIsAllowed() {
        var k = KeyStrokeSpec.of("ENTER");
        assertTrue(k.modifiers().isEmpty());
        assertEquals("ENTER", k.key());
    }

    @Test
    void factoryWithModifiers() {
        var k = KeyStrokeSpec.of("S", CTRL, SHIFT);
        assertEquals(EnumSet.of(CTRL, SHIFT), k.modifiers());
    }

    @Test
    void rejectsBlankKey() {
        assertThrows(IllegalArgumentException.class, () -> KeyStrokeSpec.of(""));
    }
}
