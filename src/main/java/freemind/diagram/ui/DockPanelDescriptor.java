package freemind.diagram.ui;

import java.util.Objects;

/**
 * Describes a dockable side panel a plugin contributes (e.g., outline view,
 * attribute editor). Host renders the panel using the plugin-provided
 * {@code panelComponentRef}; the panel content itself is rendered by the
 * plugin's host-specific extension when one exists. In Plan 1 nothing is
 * resolved — the descriptor is data only.
 */
public record DockPanelDescriptor(
    String id,
    String labelKey,
    DockSide side,
    String panelComponentRef
) {

    public enum DockSide { LEFT, RIGHT, BOTTOM }

    public DockPanelDescriptor {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(labelKey, "labelKey");
        Objects.requireNonNull(side, "side");
        Objects.requireNonNull(panelComponentRef, "panelComponentRef");
    }
}
