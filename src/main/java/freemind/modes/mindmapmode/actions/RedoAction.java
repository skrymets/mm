package freemind.modes.mindmapmode.actions;

import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

@SuppressWarnings("serial")
public class RedoAction extends UndoAction {
    private final MindMapController controller;

    public RedoAction(MindMapController controller) {
        super(controller, controller.getText("redo"), freemind.view.ImageFactory.getInstance().createIconWithSvgFallback(
                controller.getResource("images/redo.png")), controller);
        this.controller = controller;
    }

    protected void informUndoPartner(ActionPair pair) {
        this.controller.getActions().undo.add(pair.reverse());
        this.controller.getActions().undo.setEnabled(true);
    }

}
