package freemind.modes.mindmapmode.actions;

import freemind.modes.mindmapmode.MindMapController;

import javax.swing.*;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class SetLinkByTextFieldAction extends MindmapAction {
    private final MindMapController controller;

    public SetLinkByTextFieldAction(MindMapController controller) {
        super("set_link_by_textfield", controller);
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        String inputValue = JOptionPane.showInputDialog(controller.getView()
                        .getSelectionService().getSelected(), controller.getText("edit_link_manually"),
                controller.getSelected().getLink());
        if (inputValue != null) {
            if (inputValue.isEmpty()) {
                inputValue = null; // In case of no entry unset link
            }
            controller.setLink(controller.getSelected(), inputValue);
        }
    }

}
