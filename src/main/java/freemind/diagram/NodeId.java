package freemind.diagram;

import java.util.Objects;

/**
 * Identifier for a {@link DiagramNode}, unique within its owning {@link Diagram}.
 * Format is opaque to the abstraction; concrete diagrams may impose constraints.
 */
public record NodeId(String value) {

    public NodeId {
        Objects.requireNonNull(value, "value");
        if (value.isBlank()) {
            throw new IllegalArgumentException("NodeId must be non-blank");
        }
    }
}
