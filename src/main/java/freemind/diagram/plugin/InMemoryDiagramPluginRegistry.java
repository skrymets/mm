package freemind.diagram.plugin;

import freemind.diagram.DiagramTypeId;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Default {@link DiagramPluginRegistry} implementation backed by an
 * insertion-ordered map. Not thread-safe; expected to be populated once at
 * application startup, then read-only.
 */
public final class InMemoryDiagramPluginRegistry implements DiagramPluginRegistry {

    private final Map<DiagramTypeId, DiagramPlugin<?>> plugins = new LinkedHashMap<>();

    @Override
    public void register(DiagramPlugin<?> plugin) {
        Objects.requireNonNull(plugin, "plugin");
        var id = plugin.typeId();
        if (plugins.containsKey(id)) {
            throw new IllegalStateException(
                "DiagramPlugin already registered for typeId=" + id.value());
        }
        plugins.put(id, plugin);
    }

    @Override
    public Optional<DiagramPlugin<?>> findByTypeId(DiagramTypeId id) {
        return Optional.ofNullable(plugins.get(id));
    }

    @Override
    public List<DiagramPlugin<?>> all() {
        return List.copyOf(plugins.values());
    }
}
