package freemind.diagram;

import java.util.Objects;
import java.util.UUID;

/** Globally unique identifier for a {@link Diagram} document. */
public record DocumentId(UUID value) {

    public DocumentId {
        Objects.requireNonNull(value, "value");
    }

    public static DocumentId newRandom() {
        return new DocumentId(UUID.randomUUID());
    }
}
