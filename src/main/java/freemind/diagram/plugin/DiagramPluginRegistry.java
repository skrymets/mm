package freemind.diagram.plugin;

import freemind.diagram.DiagramTypeId;
import java.util.List;
import java.util.Optional;

/**
 * Registry of installed {@link DiagramPlugin}s keyed by {@link DiagramTypeId}.
 * The host populates the registry at startup; lookups by type id are how
 * the persistence and UI layers dispatch to a specific plugin.
 */
public interface DiagramPluginRegistry {

    void register(DiagramPlugin<?> plugin);

    Optional<DiagramPlugin<?>> findByTypeId(DiagramTypeId id);

    List<DiagramPlugin<?>> all();
}
