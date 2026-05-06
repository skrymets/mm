package freemind.diagram;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

/**
 * Entry in a {@link ResourceTable}: either an embedded blob or an external URI,
 * never both. Created via the {@code embedded} or {@code external} factory methods.
 */
public record ResourceEntry(
    String mimeType,
    Optional<byte[]> embeddedBlob,
    Optional<URI> externalUri
) {

    public ResourceEntry {
        Objects.requireNonNull(mimeType, "mimeType");
        Objects.requireNonNull(embeddedBlob, "embeddedBlob");
        Objects.requireNonNull(externalUri, "externalUri");
        if (embeddedBlob.isPresent() == externalUri.isPresent()) {
            throw new IllegalArgumentException(
                "exactly one of embeddedBlob/externalUri must be present");
        }
    }

    public static ResourceEntry embedded(String mimeType, byte[] blob) {
        return new ResourceEntry(mimeType, Optional.of(blob), Optional.empty());
    }

    public static ResourceEntry external(String mimeType, URI uri) {
        return new ResourceEntry(mimeType, Optional.empty(), Optional.of(uri));
    }
}
