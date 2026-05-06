package freemind.diagram.persistence.external;

import java.util.Objects;

public record ExternalFormatId(String value) {
    public ExternalFormatId {
        Objects.requireNonNull(value, "value");
        if (value.isBlank()) throw new IllegalArgumentException("ExternalFormatId must be non-blank");
    }
}
