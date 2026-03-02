package freemind.extensions;

import freemind.model.MindMap;
import freemind.modes.mindmapmode.MindMapController;

/**
 * A plugin (or a set of plugins) may have a plugin base class.
 * <p>
 * It is created at the start of each mindmap and registered.
 * <p>
 * It is deregistered at mindmap shutdown (eg. the user closes the mindmap or the application).
 * <p>
 * The use case for these registration classes to provide some static data, caches.
 * Some plugins use it to register additional preference pages. Others register a mouse wheel listener.
 */
public interface HookRegistration {

    /**
     * Is called at mindmap startup. The constructor passes the {@link MindMapController}
     * and the {@link MindMap}.
     */
    void register();

    /**
     * Is called at mindmap shutdown.
     */
    void deRegister();

}
