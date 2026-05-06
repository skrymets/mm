package freemind.diagram.ui;

import java.util.Objects;

/**
 * Places an action in a popup-menu context (e.g., "node-popup", "canvas-popup")
 * at an integer rank within that context.
 */
public record PopupContribution(String context, ActionId actionId, int order) {

    public PopupContribution {
        Objects.requireNonNull(context, "context");
        Objects.requireNonNull(actionId, "actionId");
        if (context.isBlank()) {
            throw new IllegalArgumentException("context must be non-blank");
        }
    }
}
