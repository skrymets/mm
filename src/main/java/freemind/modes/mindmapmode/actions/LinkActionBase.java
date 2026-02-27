package freemind.modes.mindmapmode.actions;

import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;

import javax.swing.*;

@SuppressWarnings("serial")
public abstract class LinkActionBase extends MindmapAction {

    public LinkActionBase(String pText, MindMapController controller) {
        super(pText, controller);
    }

    public boolean isEnabled(JMenuItem pItem, Action pAction) {
        if (!super.isEnabled(pItem, pAction)) {
            return false;
        }
        for (MindMapNode selNode : getMindMapController().getSelecteds()) {
            if (selNode.getLink() != null) {
                return true;
            }
        }
        return false;
    }
}
