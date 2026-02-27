package freemind.controller.actions;

import freemind.controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static javax.swing.JOptionPane.showMessageDialog;

public class AboutAction extends AbstractAction {
    private final Controller controller;

    public AboutAction(Controller controller) {
        super(controller.getResourceString("about"));
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        showMessageDialog(controller.getView(),
                controller.getResourceString("about_text") + controller.getFrame().getFreemindVersion(),
                controller.getResourceString("about"),
                JOptionPane.INFORMATION_MESSAGE);
    }
}
