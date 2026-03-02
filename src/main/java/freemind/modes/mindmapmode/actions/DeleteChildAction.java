package freemind.modes.mindmapmode.actions;

import freemind.common.OptionalDontShowMeAgainDialog;
import freemind.controller.actions.DeleteNodeAction;
import freemind.main.FreeMind;
import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;

import javax.swing.*;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class DeleteChildAction extends MindmapAction {
    private final MindMapController mMindMapController;

    public DeleteChildAction(MindMapController modeController) {
        super("remove_node", "images/editdelete.png", modeController);
        this.mMindMapController = modeController;
    }

    public void actionPerformed(ActionEvent e) {
        // ask user if not root is selected:
        for (MindMapNode node : mMindMapController.getSelecteds()) {
            if (node.isRoot()) {
                mMindMapController.getController().errorMessage(
                        mMindMapController.getFrame().getResourceString(
                                "cannot_delete_root"));
                return;
            }
        }
        int showResult = new OptionalDontShowMeAgainDialog(mMindMapController
                .getFrame().getJFrame(), mMindMapController.getSelectedView(),
                "really_remove_node", "confirmation", mMindMapController,
                new OptionalDontShowMeAgainDialog.StandardPropertyHandler(
                        mMindMapController.getController(),
                        FreeMind.RESOURCES_DELETE_NODES_WITHOUT_QUESTION),
                OptionalDontShowMeAgainDialog.ONLY_OK_SELECTION_IS_STORED)
                .show().getResult();
        if (showResult != JOptionPane.OK_OPTION) {
            return;
        }
        // because of multiple selection, cut is better.
        mMindMapController.cut();
        // this.c.deleteNode(c.getSelected());
    }

    public Class<DeleteNodeAction> getDoActionClass() {
        return DeleteNodeAction.class;
    }

}
