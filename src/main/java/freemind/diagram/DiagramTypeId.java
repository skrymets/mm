package freemind.diagram;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Stable string identifier for a diagram type. Lowercase ASCII letters, digits,
 * and hyphens only. Examples: {@code "mindmap"}, {@code "concept-map"},
 * {@code "cause-effect"}.
 */
public record DiagramTypeId(String value) {

    private static final Pattern VALID = Pattern.compile("[a-z0-9]+(-[a-z0-9]+)*");

    public DiagramTypeId {
        Objects.requireNonNull(value, "value");
        if (!VALID.matcher(value).matches()) {
            throw new IllegalArgumentException(
                "DiagramTypeId must be lowercase ASCII letters/digits/hyphens; got: '" + value + "'");
        }
    }
}
