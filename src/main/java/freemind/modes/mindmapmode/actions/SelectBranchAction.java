package freemind.modes.mindmapmode.actions;

import freemind.modes.mindmapmode.MindMapController;

import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class SelectBranchAction extends MindmapAction {

    public SelectBranchAction(final MindMapController modeController) {
        super("select_branch", (String) null, modeController);
    }

    public void actionPerformed(ActionEvent e) {
        getMindMapController().selectBranch(
                getMindMapController().getSelectedView(), true /* = extend */);
    }

}
