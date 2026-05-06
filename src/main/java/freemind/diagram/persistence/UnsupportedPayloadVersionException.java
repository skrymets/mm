package freemind.diagram.persistence;

import freemind.diagram.DiagramTypeId;

/** Thrown when a codec is asked to read a payload version it doesn't support. */
public class UnsupportedPayloadVersionException extends RuntimeException {

    private final DiagramTypeId typeId;
    private final int requestedVersion;

    public UnsupportedPayloadVersionException(DiagramTypeId typeId, int requestedVersion) {
        super("Codec for diagram type '" + typeId.value()
            + "' does not support payload version " + requestedVersion);
        this.typeId = typeId;
        this.requestedVersion = requestedVersion;
    }

    public DiagramTypeId typeId()         { return typeId; }
    public int requestedVersion()         { return requestedVersion; }
}
