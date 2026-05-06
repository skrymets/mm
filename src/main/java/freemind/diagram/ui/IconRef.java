package freemind.diagram.ui;

import java.util.Objects;

/** Logical icon reference (palette key or resource path). */
public record IconRef(String value) {

    public IconRef {
        Objects.requireNonNull(value, "value");
        if (value.isBlank()) {
            throw new IllegalArgumentException("IconRef must be non-blank");
        }
    }
}
