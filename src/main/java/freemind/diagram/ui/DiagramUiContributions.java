package freemind.diagram.ui;

import java.util.List;

/**
 * Aggregate of UI contributions a plugin offers. All lists may be empty.
 * Returned values must be immutable (use {@link List#copyOf} or {@link List#of}).
 */
public interface DiagramUiContributions {

    List<ActionDescriptor> actions();

    List<MenuContribution> menuContributions();

    List<ToolBarContribution> toolBarItems();

    List<PopupContribution> popupItems();

    List<DockPanelDescriptor> dockPanels();

    /** Convenience empty contributions (useful for plugins still being fleshed out). */
    static DiagramUiContributions empty() {
        return new DiagramUiContributions() {
            @Override public List<ActionDescriptor> actions()              { return List.of(); }
            @Override public List<MenuContribution> menuContributions()    { return List.of(); }
            @Override public List<ToolBarContribution> toolBarItems()      { return List.of(); }
            @Override public List<PopupContribution> popupItems()          { return List.of(); }
            @Override public List<DockPanelDescriptor> dockPanels()        { return List.of(); }
        };
    }
}
