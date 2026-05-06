package freemind.diagram.persistence;

import freemind.diagram.DiagramTypeId;

/** Thrown when a document references a diagram type with no registered plugin. */
public class UnsupportedDiagramTypeException extends RuntimeException {

    private final DiagramTypeId typeId;

    public UnsupportedDiagramTypeException(DiagramTypeId typeId) {
        super("No registered DiagramPlugin for diagram type '" + typeId.value() + "'");
        this.typeId = typeId;
    }

    public DiagramTypeId typeId() { return typeId; }
}
