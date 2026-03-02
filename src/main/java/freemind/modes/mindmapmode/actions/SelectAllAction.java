package freemind.modes.mindmapmode.actions;

import freemind.modes.mindmapmode.MindMapController;

@SuppressWarnings("serial")
public class SelectAllAction extends NodeGeneralAction {

    public SelectAllAction(final MindMapController modeController) {
        super(modeController, "select_all", null, (map, node) -> modeController.selectBranch(modeController.getView().getRoot(),
                false));
    }

}
