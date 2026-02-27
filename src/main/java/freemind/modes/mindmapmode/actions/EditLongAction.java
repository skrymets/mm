package freemind.modes.mindmapmode.actions;

import freemind.modes.mindmapmode.MindMapController;

import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class EditLongAction extends MindmapAction {

    public EditLongAction(MindMapController controller) {
        super("edit_long_node", controller);
    }

    public void actionPerformed(ActionEvent e) {
        getMindMapController().edit(null, false, true);
    }
}
