package freemind.diagram.ui;

import java.util.Objects;

/** Stable identifier for a {@link ActionDescriptor}. */
public record ActionId(String value) {

    public ActionId {
        Objects.requireNonNull(value, "value");
        if (value.isBlank()) {
            throw new IllegalArgumentException("ActionId must be non-blank");
        }
    }
}
