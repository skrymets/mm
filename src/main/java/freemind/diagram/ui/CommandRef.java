package freemind.diagram.ui;

import java.util.Objects;

/**
 * Reference to a host-dispatchable command. The host's command bus resolves
 * the {@code value} to an actual handler.
 */
public record CommandRef(String value) {

    public CommandRef {
        Objects.requireNonNull(value, "value");
        if (value.isBlank()) {
            throw new IllegalArgumentException("CommandRef must be non-blank");
        }
    }
}
