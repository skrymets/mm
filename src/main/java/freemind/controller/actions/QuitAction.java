package freemind.controller.actions;

import freemind.controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class QuitAction extends AbstractAction {
    private final Controller controller;

    public QuitAction(Controller controller) {
        super(controller.getResourceString("quit"));
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        controller.quit();
    }
}
