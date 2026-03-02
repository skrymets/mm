package freemind.modes.mindmapmode.actions;

import freemind.controller.Controller;
import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;

import java.awt.*;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class NodeColorAction extends MindmapAction {
    private final MindMapController controller;

    public NodeColorAction(MindMapController controller) {
        super("node_color", (String) null, controller);
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        Color color = Controller.showCommonJColorChooserDialog(controller
                .getView().getSelectionService().getSelected(), controller
                .getText("choose_node_color"), controller.getSelected()
                .getColor());
        if (color == null) {
            return;
        }
        for (MindMapNode selected : controller.getSelecteds()) {

            controller.setNodeColor(selected, color);
        }
    }

}