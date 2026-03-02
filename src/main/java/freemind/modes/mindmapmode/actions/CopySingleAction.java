package freemind.modes.mindmapmode.actions;

import freemind.modes.mindmapmode.MindMapController;

import javax.swing.*;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class CopySingleAction extends AbstractAction {
    private final MindMapController controller;

    public CopySingleAction(MindMapController controller) {
        super(controller.getText("copy_single"));
        this.controller = controller;
        setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
        if (controller.getMap() != null) {
            Transferable copy = controller.copySingle();
            if (copy != null) {
                controller.setClipboardContents(copy);
            }
        }
    }
}