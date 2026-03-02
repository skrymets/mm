package freemind.modes.mindmapmode.actions;

import freemind.main.MindMapUtils;
import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@SuppressWarnings("serial")
@Slf4j
public class AddArrowLinkAction extends MindmapAction {

    private final MindMapController modeController;

    public AddArrowLinkAction(MindMapController modeController) {
        super("paste_as_link", "images/designer.png", modeController);
        this.modeController = modeController;
    }

    public void actionPerformed(ActionEvent e) {
        MindMapNode selected = modeController.getSelected();
        List<MindMapNode> nodesFromClipboard = MindMapUtils.getMindMapNodesFromClipboard(modeController);
        if (isNotEmpty(nodesFromClipboard)) {
            modeController.getController().errorMessage(modeController.getText("no_copied_nodes"));
            return;
        }

        boolean identicalError = false;

        for (MindMapNode destination : nodesFromClipboard) {
            if (selected != destination) {
                getMindMapController().addLink(selected, destination);
            } else {
                // give an error afterwards?
                identicalError = true;
                log.warn("Can't create a link from the node '{}' to itself. Skipped.", selected);
            }
        }
        if (identicalError) {
            modeController.getController().errorMessage(modeController.getText("paste_as_link_identity_not_possible"));
        }
    }

    @Override
    public boolean isEnabled(JMenuItem pItem, Action pAction) {
        return super.isEnabled(pItem, pAction)
                && (modeController != null)
                && !MindMapUtils.getMindMapNodesFromClipboard(modeController).isEmpty();
    }
}
