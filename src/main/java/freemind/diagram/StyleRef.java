package freemind.diagram;

import java.util.Objects;

/**
 * Reference from any payload into a {@link StylePalette}.
 * The referenced {@link StyleId} must exist in the diagram's palette.
 */
public record StyleRef(StyleId paletteId) {

    public StyleRef {
        Objects.requireNonNull(paletteId, "paletteId");
    }
}
