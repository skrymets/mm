package freemind.modes.mindmapmode.actions;

import freemind.modes.mindmapmode.MindMapController;

import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class SetLinkByFileChooserAction extends MindmapAction {

    public SetLinkByFileChooserAction(MindMapController controller) {
        super("set_link_by_filechooser", controller);
    }

    public void actionPerformed(ActionEvent e) {
        getMindMapController().getFileManagementService().setLinkByFileChooser();
    }
}
