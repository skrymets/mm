package freemind.diagram.mindmap;

import freemind.diagram.ui.*;
import java.util.List;

public final class MindMapUiContributions implements DiagramUiContributions {
    @Override public List<ActionDescriptor> actions()              { return List.of(); }
    @Override public List<MenuContribution> menuContributions()    { return List.of(); }
    @Override public List<ToolBarContribution> toolBarItems()      { return List.of(); }
    @Override public List<PopupContribution> popupItems()          { return List.of(); }
    @Override public List<DockPanelDescriptor> dockPanels()        { return List.of(); }
}
