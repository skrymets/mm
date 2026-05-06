package freemind.diagram.ui;

import java.util.Objects;

/**
 * Places an action under a menu path (e.g. "File/Export") at an integer
 * order rank within that path's group.
 */
public record MenuContribution(String menuPath, ActionId actionId, int order) {

    public MenuContribution {
        Objects.requireNonNull(menuPath, "menuPath");
        Objects.requireNonNull(actionId, "actionId");
        if (menuPath.isBlank()) {
            throw new IllegalArgumentException("menuPath must be non-blank");
        }
    }
}
