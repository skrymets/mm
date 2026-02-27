package freemind.controller.actions;

import freemind.controller.Controller;
import freemind.main.FreeMindCommon;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class OptionAntialiasAction extends AbstractAction {
    private final Controller controller;

    public OptionAntialiasAction(Controller controller) {
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        changeAntialias(command);
    }

    public void changeAntialias(String command) {
        if (command == null) {
            return;
        }
        controller.setProperty(FreeMindCommon.RESOURCE_ANTIALIAS, command);
        if (controller.getView() != null)
            controller.getView().repaint();
    }
}
