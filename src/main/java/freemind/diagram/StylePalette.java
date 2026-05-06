package freemind.diagram;

import freemind.diagram.style.ColorEntry;
import freemind.diagram.style.FontEntry;
import freemind.diagram.style.StrokeEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Named style primitives (colors, fonts, strokes) referenced by id from
 * any plugin's payload. Immutable — {@code with*} methods return a new instance.
 */
public record StylePalette(
    Map<StyleId, ColorEntry> colors,
    Map<StyleId, FontEntry> fonts,
    Map<StyleId, StrokeEntry> strokes
) {

    public StylePalette {
        Objects.requireNonNull(colors, "colors");
        Objects.requireNonNull(fonts, "fonts");
        Objects.requireNonNull(strokes, "strokes");
        colors = Map.copyOf(colors);
        fonts = Map.copyOf(fonts);
        strokes = Map.copyOf(strokes);
    }

    public static StylePalette empty() {
        return new StylePalette(Map.of(), Map.of(), Map.of());
    }

    public Optional<ColorEntry> findColor(StyleId id) {
        return Optional.ofNullable(colors.get(id));
    }

    public Optional<FontEntry> findFont(StyleId id) {
        return Optional.ofNullable(fonts.get(id));
    }

    public Optional<StrokeEntry> findStroke(StyleId id) {
        return Optional.ofNullable(strokes.get(id));
    }

    public StylePalette withColor(StyleId id, ColorEntry color) {
        var next = new HashMap<>(colors);
        next.put(id, color);
        return new StylePalette(next, fonts, strokes);
    }

    public StylePalette withFont(StyleId id, FontEntry font) {
        var next = new HashMap<>(fonts);
        next.put(id, font);
        return new StylePalette(colors, next, strokes);
    }

    public StylePalette withStroke(StyleId id, StrokeEntry stroke) {
        var next = new HashMap<>(strokes);
        next.put(id, stroke);
        return new StylePalette(colors, fonts, next);
    }
}
