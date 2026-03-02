package freemind.modes.mindmapmode.actions;

import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import freemind.view.mindmapview.MapView;

import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class ToggleChildrenFoldedAction extends MindmapAction {
    private final MindMapController modeController;

    public ToggleChildrenFoldedAction(MindMapController modeController) {
        super("toggle_children_folded", modeController);
        this.modeController = modeController;
    }

    public void actionPerformed(ActionEvent e) {
        MindMapNode selected = modeController.getSelected();
        modeController.getActorFactory().getToggleFoldedActor().toggleFolded(selected.childrenUnfolded());
        final MapView mapView = modeController.getView();
        mapView.getSelectionService().selectAsTheOnlyOneSelected(mapView.getViewerRegistryService().getNodeView(selected));
        modeController.getController().obtainFocusForSelected();
    }
}