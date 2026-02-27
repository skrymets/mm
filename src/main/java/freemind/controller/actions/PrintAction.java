package freemind.controller.actions;

import freemind.controller.Controller;
import freemind.view.ImageFactory;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.event.ActionEvent;

@Slf4j
public class PrintAction extends AbstractAction {
    private final Controller controller;
    private final boolean isDlg;

    public PrintAction(Controller controller, boolean isDlg) {
        super(isDlg ? controller.getResourceString("print_dialog")
                : controller.getResourceString("print"), ImageFactory.getInstance().createIcon(
                controller.getResource("images/fileprint.png")));
        setEnabled(false);
        this.controller = controller;
        this.isDlg = isDlg;
    }

    public void actionPerformed(ActionEvent e) {
        if (!controller.getPrintService().acquirePrinterJobAndPageFormat()) {
            return;
        }

        controller.getPrintService().getPrinterJob().setPrintable(controller.getView(), controller.getPrintService().getPageFormat());

        if (!isDlg || controller.getPrintService().getPrinterJob().printDialog()) {
            try {
                controller.getFrame().setWaitingCursor(true);
                controller.getPrintService().getPrinterJob().print();
                controller.getPrintService().storePageFormat(controller::setProperty);
            } catch (Exception ex) {
                log.error(ex.getLocalizedMessage(), ex);
            } finally {
                controller.getFrame().setWaitingCursor(false);
            }
        }
    }
}
