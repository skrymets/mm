package freemind.diagram.style;

import java.util.Objects;
import java.util.regex.Pattern;

/** Color as an opaque hex string in the form {@code #RRGGBB} or {@code #RRGGBBAA}. */
public record ColorEntry(String hex) {

    private static final Pattern VALID = Pattern.compile("#[0-9A-Fa-f]{6}([0-9A-Fa-f]{2})?");

    public ColorEntry {
        Objects.requireNonNull(hex, "hex");
        if (!VALID.matcher(hex).matches()) {
            throw new IllegalArgumentException(
                "ColorEntry hex must match #RRGGBB or #RRGGBBAA; got: '" + hex + "'");
        }
    }
}
