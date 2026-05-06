package freemind.diagram.ui;

import java.util.Objects;

/** Places an action on a named toolbar group at an integer rank within the group. */
public record ToolBarContribution(String group, ActionId actionId, int order) {

    public ToolBarContribution {
        Objects.requireNonNull(group, "group");
        Objects.requireNonNull(actionId, "actionId");
        if (group.isBlank()) {
            throw new IllegalArgumentException("group must be non-blank");
        }
    }
}
