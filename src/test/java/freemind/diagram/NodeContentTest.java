package freemind.diagram;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NodeContentTest {

    @Test
    void plainTextHasFormatPlain() {
        var c = NodeContent.plain("hello");
        assertEquals("hello", c.text());
        assertEquals(ContentFormat.PLAIN, c.format());
    }

    @Test
    void htmlHasFormatHtml() {
        var c = NodeContent.html("<b>hi</b>");
        assertEquals(ContentFormat.HTML, c.format());
    }

    @Test
    void emptyTextIsAllowed() {
        assertEquals("", NodeContent.plain("").text());
    }

    @Test
    void rejectsNullText() {
        assertThrows(NullPointerException.class, () -> NodeContent.plain(null));
    }
}
