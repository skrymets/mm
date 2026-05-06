package freemind.diagram;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class DocumentIdTest {

    @Test
    void wrapsAUuid() {
        var uuid = UUID.randomUUID();
        var id = new DocumentId(uuid);
        assertEquals(uuid, id.value());
    }

    @Test
    void rejectsNullUuid() {
        assertThrows(NullPointerException.class, () -> new DocumentId(null));
    }

    @Test
    void newRandomProducesDistinctIds() {
        assertNotEquals(DocumentId.newRandom(), DocumentId.newRandom());
    }

    @Test
    void equalityIsValueBased() {
        var uuid = UUID.randomUUID();
        assertEquals(new DocumentId(uuid), new DocumentId(uuid));
    }
}
