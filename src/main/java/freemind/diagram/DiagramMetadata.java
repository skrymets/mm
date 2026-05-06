package freemind.diagram;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/** Envelope-level metadata: title, author, timestamps. */
public record DiagramMetadata(
    Optional<String> title,
    Optional<String> author,
    Instant createdAt,
    Instant modifiedAt
) {

    public DiagramMetadata {
        Objects.requireNonNull(title, "title");
        Objects.requireNonNull(author, "author");
        Objects.requireNonNull(createdAt, "createdAt");
        Objects.requireNonNull(modifiedAt, "modifiedAt");
    }

    public static DiagramMetadata empty(Instant timestamp) {
        return new DiagramMetadata(Optional.empty(), Optional.empty(), timestamp, timestamp);
    }

    public DiagramMetadata withTitle(String title) {
        return new DiagramMetadata(Optional.of(title), author, createdAt, modifiedAt);
    }

    public DiagramMetadata withAuthor(String author) {
        return new DiagramMetadata(title, Optional.of(author), createdAt, modifiedAt);
    }

    public DiagramMetadata withModifiedAt(Instant modifiedAt) {
        return new DiagramMetadata(title, author, createdAt, modifiedAt);
    }
}
