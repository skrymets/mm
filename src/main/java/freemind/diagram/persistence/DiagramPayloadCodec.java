package freemind.diagram.persistence;

import freemind.diagram.Diagram;
import java.util.Set;

/**
 * Plugin-owned codec for marshalling/unmarshalling the payload portion of a
 * {@link DiagramDocument}. The envelope is handled by
 * {@link NativeDiagramDocumentFormat}; the codec only sees its own payload.
 *
 * <p>Codecs MUST support reading every {@link Set#of} {@code supportedPayloadVersions()};
 * if asked to read another version, throw {@link UnsupportedPayloadVersionException}.
 * Migration of older payloads to the current shape happens inside
 * {@link #readPayload(int, PayloadReadContext)}.
 */
public interface DiagramPayloadCodec<D extends Diagram> {

    int currentPayloadVersion();

    Set<Integer> supportedPayloadVersions();

    D readPayload(int payloadVersion, PayloadReadContext context);

    void writePayload(D diagram, PayloadWriteContext context);
}
