package freemind.modes.mindmapmode.actions;

import freemind.controller.actions.MoveNodeXmlAction;
import freemind.modes.mindmapmode.MindMapController;

@SuppressWarnings("serial")
public class MoveNodeAction extends NodeGeneralAction {

    public MoveNodeAction(MindMapController modeController) {
        super(modeController, "reset_node_position", null);
        setDoActionClass(MoveNodeXmlAction.class);
    }

}
