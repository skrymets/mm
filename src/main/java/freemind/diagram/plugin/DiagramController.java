package freemind.diagram.plugin;

import freemind.diagram.Diagram;

/**
 * Opaque handle the host receives from {@link DiagramControllerFactory#createFor}.
 * Plugins are free to extend this with concrete service interfaces; the host
 * holds the controller for the lifetime of the open document and disposes it
 * when the document closes.
 */
public interface DiagramController<D extends Diagram> {

    D diagram();

    void dispose();
}
