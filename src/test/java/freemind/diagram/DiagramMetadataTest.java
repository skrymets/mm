package freemind.diagram;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class DiagramMetadataTest {

    @Test
    void emptyMetadataHasNoOptionalFields() {
        var now = Instant.parse("2026-01-01T00:00:00Z");
        var m = DiagramMetadata.empty(now);
        assertEquals(Optional.empty(), m.title());
        assertEquals(Optional.empty(), m.author());
        assertEquals(now, m.createdAt());
        assertEquals(now, m.modifiedAt());
    }

    @Test
    void withTitleProducesNewMetadata() {
        var m = DiagramMetadata.empty(Instant.parse("2026-01-01T00:00:00Z"))
            .withTitle("My Diagram");
        assertEquals(Optional.of("My Diagram"), m.title());
    }

    @Test
    void withModifiedAtUpdatesTimestamp() {
        var t1 = Instant.parse("2026-01-01T00:00:00Z");
        var t2 = Instant.parse("2026-02-01T00:00:00Z");
        var m = DiagramMetadata.empty(t1).withModifiedAt(t2);
        assertEquals(t2, m.modifiedAt());
        assertEquals(t1, m.createdAt());
    }

    @Test
    void rejectsNullCreatedAt() {
        assertThrows(NullPointerException.class, () -> DiagramMetadata.empty(null));
    }
}
