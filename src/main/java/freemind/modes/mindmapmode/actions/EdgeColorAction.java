package freemind.modes.mindmapmode.actions;

import freemind.controller.Controller;
import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;

import java.awt.*;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class EdgeColorAction extends MindmapAction {
    private final MindMapController controller;

    public EdgeColorAction(MindMapController controller) {
        super("edge_color", controller);
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        Color color = Controller.showCommonJColorChooserDialog(controller
                .getView().getSelectionService().getSelected(), controller
                .getText("choose_edge_color"), controller.getSelected()
                .getEdge().getColor());
        if (color == null)
            return;
        for (MindMapNode selected : controller.getSelecteds()) {

            controller.setEdgeColor(selected, color);
        }
    }

}