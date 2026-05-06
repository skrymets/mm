package freemind.diagram;

import org.junit.jupiter.api.Test;
import java.net.URI;
import static org.junit.jupiter.api.Assertions.*;

class ResourceTableTest {

    @Test
    void emptyTableHasNoEntries() {
        assertTrue(ResourceTable.empty().findEntry(new ResourceId("x")).isEmpty());
    }

    @Test
    void resolvesAddedEntry() {
        var t = ResourceTable.empty().withEntry(
            new ResourceId("img-1"),
            ResourceEntry.external("image/png", URI.create("https://example.com/a.png")));
        assertTrue(t.findEntry(new ResourceId("img-1")).isPresent());
    }

    @Test
    void withEntryIsImmutable() {
        var t1 = ResourceTable.empty();
        var t2 = t1.withEntry(new ResourceId("a"),
            ResourceEntry.embedded("text/plain", new byte[]{1, 2, 3}));
        assertTrue(t1.findEntry(new ResourceId("a")).isEmpty());
        assertTrue(t2.findEntry(new ResourceId("a")).isPresent());
    }
}
