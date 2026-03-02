package freemind.modes.mindmapmode.actions;

import freemind.main.MindMapUtils;
import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Slf4j
public class AddLocalLinkAction extends MindmapAction {

    private final MindMapController modeController;

    public AddLocalLinkAction(MindMapController modeController) {
        super("paste_as_local_link", "images/stock_right.png", modeController);
        this.modeController = modeController;
    }

    public void actionPerformed(ActionEvent e) {
        MindMapNode source = modeController.getSelected();
        List<MindMapNode> nodesFromClipboard = MindMapUtils.getMindMapNodesFromClipboard(modeController);
        if (isNotEmpty(nodesFromClipboard)) {
            modeController.getController().errorMessage(modeController.getText("no_copied_nodes"));
            return;
        }
        boolean first = true;
        for (MindMapNode destination : nodesFromClipboard) {
            if (!first) {
                log.warn("Can't link the node '{}' to more than one destination. Only the last is used.", source);
            }
            if (source != destination) {
                modeController.setLink(source, "#" + modeController.getNodeID(destination));
            } else {
                // hmm, give an error?
                log.warn("Can't link the node '{}' onto itself. Skipped.", source);
            }
            first = false;
        }
    }

    @Override
    public boolean isEnabled(JMenuItem pItem, Action pAction) {
        return super.isEnabled(pItem, pAction)
                && (modeController != null)
                && MindMapUtils.getMindMapNodesFromClipboard(modeController).size() == 1;
    }

}
