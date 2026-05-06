package freemind.diagram.ui;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/**
 * Library-agnostic key-stroke specification. Modifier set + base key name.
 * Host UI layer translates to {@code javax.swing.KeyStroke},
 * {@code javafx.scene.input.KeyCombination}, etc.
 */
public record KeyStrokeSpec(Set<Modifier> modifiers, String key) {

    public enum Modifier { CTRL, ALT, SHIFT, META }

    public KeyStrokeSpec {
        Objects.requireNonNull(modifiers, "modifiers");
        Objects.requireNonNull(key, "key");
        if (key.isBlank()) {
            throw new IllegalArgumentException("key must be non-blank");
        }
        modifiers = EnumSet.copyOf(modifiers.isEmpty() ? EnumSet.noneOf(Modifier.class) : modifiers);
    }

    public static KeyStrokeSpec of(String key, Modifier... mods) {
        var set = mods.length == 0
            ? EnumSet.noneOf(Modifier.class)
            : EnumSet.copyOf(java.util.Arrays.asList(mods));
        return new KeyStrokeSpec(set, key);
    }
}
