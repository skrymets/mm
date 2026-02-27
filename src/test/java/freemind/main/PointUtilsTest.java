package freemind.main;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.awt.Point;

import static org.junit.jupiter.api.Assertions.*;

class PointUtilsTest {

    @Test
    void pointToXml_nullReturnsNull() {
        assertNull(PointUtils.PointToXml(null));
    }

    @Test
    void xmlToPoint_nullReturnsNull() {
        assertNull(PointUtils.xmlToPoint(null));
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0",
            "10, 20",
            "-5, -15",
            "100, -200",
            "2147483647, -2147483648"
    })
    void roundtrip(int x, int y) {
        Point original = new Point(x, y);
        String xml = PointUtils.PointToXml(original);
        Point restored = PointUtils.xmlToPoint(xml);
        assertEquals(original, restored);
    }

    @Test
    void pointToXml_format() {
        assertEquals("10;20", PointUtils.PointToXml(new Point(10, 20)));
    }

    @Test
    void xmlToPoint_simpleParse() {
        assertEquals(new Point(10, 20), PointUtils.xmlToPoint("10;20"));
    }

    @Test
    void xmlToPoint_legacyFormat() {
        Point p = PointUtils.xmlToPoint("java.awt.Point[x=42,y=-7]");
        assertEquals(new Point(42, -7), p);
    }

    @Test
    void xmlToPoint_invalidThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> PointUtils.xmlToPoint("1;2;3"));
    }

    @Test
    void xmlToPoint_singleNumberThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> PointUtils.xmlToPoint("42"));
    }
}
