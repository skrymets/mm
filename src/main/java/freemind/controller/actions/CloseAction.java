package freemind.controller.actions;

import freemind.controller.Controller;
import freemind.main.SwingUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CloseAction extends AbstractAction {
    private final Controller controller;

    public CloseAction(Controller controller) {
        SwingUtils.setLabelAndMnemonic(this, controller.getResourceString("close"));
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        controller.close(false);
    }
}
