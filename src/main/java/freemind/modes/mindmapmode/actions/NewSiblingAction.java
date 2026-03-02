package freemind.modes.mindmapmode.actions;

import freemind.modes.mindmapmode.MindMapController;

import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class NewSiblingAction extends MindmapAction {
    private final MindMapController controller;

    public NewSiblingAction(MindMapController controller) {
        super("new_sibling_behind", controller);
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        controller.addNew(controller.getSelected(),
                MindMapController.NEW_SIBLING_BEHIND, null);
    }
}