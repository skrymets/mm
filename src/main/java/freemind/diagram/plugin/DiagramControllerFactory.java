package freemind.diagram.plugin;

import freemind.diagram.Diagram;

/** Builds a {@link DiagramController} for an open {@link Diagram}. */
public interface DiagramControllerFactory<D extends Diagram> {

    DiagramController<D> createFor(D diagram);
}
