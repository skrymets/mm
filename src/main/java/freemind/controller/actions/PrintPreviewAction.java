package freemind.controller.actions;

import freemind.controller.Controller;
import freemind.controller.printpreview.PreviewDialog;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class PrintPreviewAction extends AbstractAction {
    private final Controller controller;

    public PrintPreviewAction(Controller controller) {
        super(controller.getResourceString("print_preview"));
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        if (!controller.getPrintService().acquirePrinterJobAndPageFormat()) {
            return;
        }
        PreviewDialog previewDialog = new PreviewDialog(
                controller.getResourceString("print_preview_title"),
                controller.getView(),
                controller.getPrintService().getPageFormat()
        );
        previewDialog.pack();
        previewDialog.setLocationRelativeTo(JOptionPane.getFrameForComponent(controller.getView()));
        previewDialog.setVisible(true);
    }
}
