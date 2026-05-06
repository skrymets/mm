package freemind.diagram.persistence;

import freemind.diagram.DiagramMetadata;
import freemind.diagram.DiagramTypeId;
import freemind.diagram.DocumentId;
import freemind.diagram.ResourceTable;
import freemind.diagram.StylePalette;
import org.w3c.dom.Element;
import java.util.Objects;

/**
 * In-memory representation of an envelope file before/after the plugin
 * payload is decoded/encoded.
 *
 * @param formatVersion       envelope schema version (currently always 1)
 * @param applicationVersion  application version that wrote the file
 * @param documentId          document identity
 * @param metadata            title, author, timestamps
 * @param stylePalette        named colors / fonts / strokes
 * @param resources           id-keyed resource table
 * @param diagramTypeId       discriminator the registry uses to pick a plugin
 * @param payloadVersion      plugin-defined payload schema version
 * @param payloadElement      opaque payload XML root, marshalled by the plugin codec
 */
public record DiagramDocument(
    int formatVersion,
    String applicationVersion,
    DocumentId documentId,
    DiagramMetadata metadata,
    StylePalette stylePalette,
    ResourceTable resources,
    DiagramTypeId diagramTypeId,
    int payloadVersion,
    Element payloadElement
) {

    public DiagramDocument {
        Objects.requireNonNull(applicationVersion, "applicationVersion");
        Objects.requireNonNull(documentId, "documentId");
        Objects.requireNonNull(metadata, "metadata");
        Objects.requireNonNull(stylePalette, "stylePalette");
        Objects.requireNonNull(resources, "resources");
        Objects.requireNonNull(diagramTypeId, "diagramTypeId");
        Objects.requireNonNull(payloadElement, "payloadElement");
        if (formatVersion < 1) {
            throw new IllegalArgumentException("formatVersion must be >= 1");
        }
        if (payloadVersion < 1) {
            throw new IllegalArgumentException("payloadVersion must be >= 1");
        }
    }
}
