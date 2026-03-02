package freemind.modes.mindmapmode.actions;

import freemind.controller.Controller;
import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;

import java.awt.*;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class NodeBackgroundColorAction extends MindmapAction {
    private final MindMapController controller;

    public NodeBackgroundColorAction(MindMapController controller) {
        super("node_background_color", (String) null, controller);
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        Color color = Controller.showCommonJColorChooserDialog(controller
                .getView().getSelectionService().getSelected(), controller
                .getText("choose_node_background_color"), controller
                .getSelected().getBackgroundColor());
        if (color == null) {
            return;
        }
        for (MindMapNode selected : controller.getSelecteds()) {
            controller.setNodeBackgroundColor(selected, color);
        }
    }

    public static class RemoveNodeBackgroundColorAction extends
            NodeGeneralAction {

        public RemoveNodeBackgroundColorAction(
                final MindMapController controller) {
            super(controller, "remove_node_background_color", null);
            setSingleNodeOperation((map, node) -> controller.setNodeBackgroundColor(node, null));
        }

    }

}
