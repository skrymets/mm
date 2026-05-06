package freemind.diagram;

import java.util.Objects;

/** Identifier for a named entry in a {@link StylePalette}. */
public record StyleId(String value) {

    public StyleId {
        Objects.requireNonNull(value, "value");
        if (value.isBlank()) {
            throw new IllegalArgumentException("StyleId must be non-blank");
        }
    }
}
