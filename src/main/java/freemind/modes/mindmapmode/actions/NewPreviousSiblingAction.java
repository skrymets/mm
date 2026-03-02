package freemind.modes.mindmapmode.actions;

import freemind.modes.mindmapmode.MindMapController;

import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class NewPreviousSiblingAction extends MindmapAction {
    private final MindMapController controller;

    public NewPreviousSiblingAction(MindMapController controller) {
        super("new_sibling_before", controller);
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        controller.addNew(controller.getSelected(),
                MindMapController.NEW_SIBLING_BEFORE, null);
    }
}