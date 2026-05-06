package freemind.diagram;

import java.util.Objects;
import java.util.Optional;

/** Per-node references into the diagram's {@link StylePalette}. All optional. */
public record StyleReferences(
    Optional<StyleRef> color,
    Optional<StyleRef> font,
    Optional<StyleRef> stroke
) {

    public StyleReferences {
        Objects.requireNonNull(color, "color");
        Objects.requireNonNull(font, "font");
        Objects.requireNonNull(stroke, "stroke");
    }

    public static StyleReferences none() {
        return new StyleReferences(Optional.empty(), Optional.empty(), Optional.empty());
    }
}
