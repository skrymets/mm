package freemind.diagram.plugin;

import freemind.diagram.Diagram;

/** Creates new, empty instances of a diagram type. */
public interface DiagramModelFactory<D extends Diagram> {

    /** A fresh, empty diagram with a newly generated {@link freemind.diagram.DocumentId}. */
    D createNew();
}
