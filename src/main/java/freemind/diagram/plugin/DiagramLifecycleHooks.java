package freemind.diagram.plugin;

import freemind.diagram.Diagram;

/**
 * Optional plugin-defined hooks invoked by the host at well-defined lifecycle
 * points. Default no-op implementations let plugins pick which to override.
 */
public interface DiagramLifecycleHooks<D extends Diagram> {

    default void onCreated(D diagram)  { }

    default void onLoaded(D diagram)   { }

    default void onSaved(D diagram)    { }

    default void onClosed(D diagram)   { }

    static <D extends Diagram> DiagramLifecycleHooks<D> noop() {
        return new DiagramLifecycleHooks<>() { };
    }
}
