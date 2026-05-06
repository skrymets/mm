package freemind.diagram.style;

import java.util.Objects;

public record StrokeEntry(double width, StrokeStyle style) {

    public StrokeEntry {
        Objects.requireNonNull(style, "style");
        if (width <= 0) {
            throw new IllegalArgumentException("width must be positive; got " + width);
        }
    }
}
