package freemind.main;

import org.junit.jupiter.api.Test;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.*;

class ColorUtilsTest {

    @Test
    void colorToXml_red() {
        assertEquals("#ff0000", ColorUtils.colorToXml(Color.RED));
    }

    @Test
    void colorToXml_black() {
        assertEquals("#000000", ColorUtils.colorToXml(Color.BLACK));
    }

    @Test
    void colorToXml_white() {
        assertEquals("#ffffff", ColorUtils.colorToXml(Color.WHITE));
    }

    @Test
    void colorToXml_null_returnsNull() {
        assertNull(ColorUtils.colorToXml(null));
    }

    @Test
    void xmlToColor_parsesRed() {
        assertEquals(Color.RED, ColorUtils.xmlToColor("#ff0000"));
    }

    @Test
    void xmlToColor_parsesBlack() {
        assertEquals(Color.BLACK, ColorUtils.xmlToColor("#000000"));
    }

    @Test
    void xmlToColor_toleratesWhitespace() {
        assertEquals(Color.RED, ColorUtils.xmlToColor(" #ff0000 "));
    }

    @Test
    void xmlToColor_invalidLength_throws() {
        assertThrows(IllegalArgumentException.class, () -> ColorUtils.xmlToColor("#fff"));
    }

    @Test
    void xmlToColor_emptyString_throws() {
        assertThrows(IllegalArgumentException.class, () -> ColorUtils.xmlToColor(""));
    }

    @Test
    void roundtrip() {
        Color original = new Color(18, 52, 86);
        String xml = ColorUtils.colorToXml(original);
        Color parsed = ColorUtils.xmlToColor(xml);
        assertEquals(original, parsed);
    }
}
