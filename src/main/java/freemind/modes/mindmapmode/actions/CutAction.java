package freemind.modes.mindmapmode.actions;

import freemind.common.OptionalDontShowMeAgainDialog;
import freemind.main.FreeMind;
import freemind.modes.mindmapmode.MindMapController;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
@Slf4j
public class CutAction extends AbstractAction {
    private final MindMapController mMindMapController;

    public CutAction(MindMapController c) {
        super(c.getText("cut"), freemind.view.ImageFactory.getInstance().createIconWithSvgFallback(c.getResource("images/editcut.png")));
        this.mMindMapController = c;
        setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
        if (mMindMapController.getView().getSelectionService().isSelected(mMindMapController.getView().getRoot())) {
            mMindMapController.getController().errorMessage(
                    mMindMapController.getFrame().getResourceString(
                            "cannot_delete_root"));
            return;
        }
        int showResult = new OptionalDontShowMeAgainDialog(mMindMapController
                .getFrame().getJFrame(), mMindMapController.getSelectedView(),
                "really_cut_node", "confirmation", mMindMapController,
                new OptionalDontShowMeAgainDialog.StandardPropertyHandler(
                        mMindMapController.getController(),
                        FreeMind.RESOURCES_CUT_NODES_WITHOUT_QUESTION),
                OptionalDontShowMeAgainDialog.ONLY_OK_SELECTION_IS_STORED)
                .show().getResult();
        if (showResult != JOptionPane.OK_OPTION) {
            return;
        }
        Transferable copy = mMindMapController.cut();
        // and set it.
        mMindMapController.setClipboardContents(copy);
        mMindMapController.getController().obtainFocusForSelected();
    }

}
