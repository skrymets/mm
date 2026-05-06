package freemind.diagram.style;

import java.util.Objects;

public record FontEntry(String family, int size, FontWeight weight) {

    public FontEntry {
        Objects.requireNonNull(family, "family");
        Objects.requireNonNull(weight, "weight");
        if (family.isBlank()) {
            throw new IllegalArgumentException("family must be non-blank");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("size must be positive; got " + size);
        }
    }
}
