package freemind.diagram.ui;

import java.util.Objects;
import java.util.Optional;

/**
 * Declarative description of a user-invocable action. The host's command bus
 * resolves {@link #command} when the user activates the action via menu,
 * toolbar, popup, or accelerator.
 *
 * @param id          stable identifier
 * @param labelKey    i18n key for the user-facing label
 * @param icon        optional icon reference
 * @param accelerator optional keyboard shortcut
 * @param command     command bus reference
 */
public record ActionDescriptor(
    ActionId id,
    String labelKey,
    Optional<IconRef> icon,
    Optional<KeyStrokeSpec> accelerator,
    CommandRef command
) {

    public ActionDescriptor {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(labelKey, "labelKey");
        Objects.requireNonNull(icon, "icon");
        Objects.requireNonNull(accelerator, "accelerator");
        Objects.requireNonNull(command, "command");
        if (labelKey.isBlank()) {
            throw new IllegalArgumentException("labelKey must be non-blank");
        }
    }
}
