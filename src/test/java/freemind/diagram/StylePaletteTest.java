package freemind.diagram;

import freemind.diagram.style.ColorEntry;
import freemind.diagram.style.FontEntry;
import freemind.diagram.style.FontWeight;
import freemind.diagram.style.StrokeEntry;
import freemind.diagram.style.StrokeStyle;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class StylePaletteTest {

    @Test
    void emptyPaletteResolvesToEmptyOptionals() {
        var p = StylePalette.empty();
        assertEquals(Optional.empty(), p.findColor(new StyleId("any")));
        assertEquals(Optional.empty(), p.findFont(new StyleId("any")));
        assertEquals(Optional.empty(), p.findStroke(new StyleId("any")));
    }

    @Test
    void resolvesRegisteredColor() {
        var p = StylePalette.empty().withColor(
            new StyleId("primary"), new ColorEntry("#2D5BAA"));
        assertEquals(Optional.of(new ColorEntry("#2D5BAA")),
            p.findColor(new StyleId("primary")));
    }

    @Test
    void resolvesRegisteredFontAndStroke() {
        var p = StylePalette.empty()
            .withFont(new StyleId("title"), new FontEntry("Inter", 16, FontWeight.BOLD))
            .withStroke(new StyleId("thick"), new StrokeEntry(2.0, StrokeStyle.SOLID));
        assertTrue(p.findFont(new StyleId("title")).isPresent());
        assertTrue(p.findStroke(new StyleId("thick")).isPresent());
    }

    @Test
    void withColorIsImmutableUpdate() {
        var p1 = StylePalette.empty();
        var p2 = p1.withColor(new StyleId("c1"), new ColorEntry("#000000"));
        assertEquals(Optional.empty(), p1.findColor(new StyleId("c1")));
        assertTrue(p2.findColor(new StyleId("c1")).isPresent());
    }

    @Test
    void rejectsNullEntries() {
        assertThrows(NullPointerException.class,
            () -> new StylePalette(null, Map.of(), Map.of()));
    }
}
