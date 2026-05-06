package freemind.diagram;

import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class AttributeBagTest {

    @Test
    void emptyBagHasNoValues() {
        assertTrue(AttributeBag.empty().get("k").isEmpty());
    }

    @Test
    void getReturnsAddedValue() {
        var b = AttributeBag.empty().with("priority", "high");
        assertEquals(Optional.of("high"), b.get("priority"));
    }

    @Test
    void withIsImmutable() {
        var b1 = AttributeBag.empty();
        var b2 = b1.with("k", "v");
        assertTrue(b1.get("k").isEmpty());
        assertEquals(Optional.of("v"), b2.get("k"));
    }

    @Test
    void rejectsNullKey() {
        assertThrows(NullPointerException.class,
            () -> AttributeBag.empty().with(null, "v"));
    }

    @Test
    void rejectsNullValue() {
        assertThrows(NullPointerException.class,
            () -> AttributeBag.empty().with("k", null));
    }

    @Test
    void rejectsNullValuesMap() {
        assertThrows(NullPointerException.class, () -> new AttributeBag(null));
    }
}
