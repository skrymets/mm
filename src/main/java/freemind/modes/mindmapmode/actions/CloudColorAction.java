package freemind.modes.mindmapmode.actions;

import freemind.controller.Controller;
import freemind.controller.MenuItemEnabledListener;
import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class CloudColorAction extends MindmapAction implements MenuItemEnabledListener {
    private final MindMapController controller;

    public CloudColorAction(MindMapController controller) {
        super("cloud_color", "images/Colors24.gif", controller);
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        Color selectedColor = null;
        if (controller.getSelected().getCloud() != null) {
            selectedColor = controller.getSelected().getCloud().getColor();
        }
        Color color = Controller.showCommonJColorChooserDialog(controller
                .getView().getSelectionService().getSelected(), controller
                .getText("choose_cloud_color"), selectedColor);
        if (color == null) {
            return;
        }
        for (MindMapNode selected : controller.getSelecteds()) {
            controller.setCloudColor(selected, color);
        }
    }

    public boolean isEnabled(JMenuItem item, Action action) {
        return super.isEnabled(item, action) && (controller != null) && (controller.getSelected() != null)
                && (controller.getSelected().getCloud() != null);
    }

}