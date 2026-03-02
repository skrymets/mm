package freemind.modes.mindmapmode.actions;

import freemind.modes.mindmapmode.MindMapController;

import javax.swing.*;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class CopyAction extends AbstractAction {
    private final MindMapController controller;

    public CopyAction(MindMapController controller) {
        super(controller.getText("copy"), freemind.view.ImageFactory.getInstance().createIconWithSvgFallback(
                controller.getResource("images/editcopy.png")));
        this.controller = controller;
        setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
        if (controller.getMap() != null) {
            Transferable copy = controller.copy();
            if (copy != null) {
                controller.setClipboardContents(copy);
            }
        }
    }
}