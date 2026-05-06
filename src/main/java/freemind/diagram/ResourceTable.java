package freemind.diagram;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/** Id-keyed table of resources (embedded blobs or external URIs). Immutable. */
public record ResourceTable(Map<ResourceId, ResourceEntry> entries) {

    public ResourceTable {
        Objects.requireNonNull(entries, "entries");
        entries = Map.copyOf(entries);
    }

    public static ResourceTable empty() {
        return new ResourceTable(Map.of());
    }

    public Optional<ResourceEntry> findEntry(ResourceId id) {
        return Optional.ofNullable(entries.get(id));
    }

    public ResourceTable withEntry(ResourceId id, ResourceEntry entry) {
        var next = new HashMap<>(entries);
        next.put(id, entry);
        return new ResourceTable(next);
    }
}
