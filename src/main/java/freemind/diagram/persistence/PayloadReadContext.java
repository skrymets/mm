package freemind.diagram.persistence;

import freemind.diagram.DiagramMetadata;
import freemind.diagram.DocumentId;
import freemind.diagram.ResourceTable;
import freemind.diagram.StylePalette;
import org.w3c.dom.Element;
import java.util.Objects;

/**
 * Context passed to a codec when reading a payload. Gives the codec access
 * to envelope-shared state (metadata, style palette, resources) and document identity
 * so the resulting diagram can reference them.
 */
public record PayloadReadContext(
    DocumentId documentId,
    DiagramMetadata metadata,
    StylePalette stylePalette,
    ResourceTable resources,
    Element payloadElement
) {

    public PayloadReadContext {
        Objects.requireNonNull(documentId, "documentId");
        Objects.requireNonNull(metadata, "metadata");
        Objects.requireNonNull(stylePalette, "stylePalette");
        Objects.requireNonNull(resources, "resources");
        Objects.requireNonNull(payloadElement, "payloadElement");
    }
}
