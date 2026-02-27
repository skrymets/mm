package freemind.modes.mindmapmode.actions;

import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;

import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class FollowLinkAction extends LinkActionBase {

    public FollowLinkAction(MindMapController controller) {
        super("follow_link", controller);
    }

    public void actionPerformed(ActionEvent e) {
        for (MindMapNode selNode : getMindMapController().getSelecteds()) {
            if (selNode.getLink() != null) {
                getMindMapController().loadURL(selNode.getLink());
            }
        }
    }
}
