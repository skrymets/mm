package freemind.diagram.topology;

import java.util.Objects;

/** Semantic label on a graph edge (e.g., "is-a", "causes", "includes"). */
public record EdgeLabel(String value) {

    public EdgeLabel {
        Objects.requireNonNull(value, "value");
        if (value.isBlank()) {
            throw new IllegalArgumentException("EdgeLabel must be non-blank");
        }
    }
}
