package freemind.diagram;

import java.util.Objects;

public record ResourceId(String value) {

    public ResourceId {
        Objects.requireNonNull(value, "value");
        if (value.isBlank()) {
            throw new IllegalArgumentException("ResourceId must be non-blank");
        }
    }
}
