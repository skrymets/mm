package freemind.controller.actions;

import freemind.controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static javax.swing.JOptionPane.showMessageDialog;

public class LicenseAction extends AbstractAction {
    private final Controller controller;

    public LicenseAction(Controller controller) {
        super(controller.getResourceString("license"));
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        showMessageDialog(controller.getView(),
                controller.getResourceString("license_text"),
                controller.getResourceString("license"),
                JOptionPane.INFORMATION_MESSAGE);
    }
}
