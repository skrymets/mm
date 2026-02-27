package freemind.controller.actions;

import freemind.controller.Controller;
import freemind.view.ImageFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class NavigationPreviousMapAction extends AbstractAction {
    private final Controller controller;

    public NavigationPreviousMapAction(Controller controller) {
        super(controller.getResourceString("previous_map"), ImageFactory.getInstance().createIcon(controller.getResource("images/1leftarrow.png")));
        setEnabled(false);
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent event) {
        controller.getMapModuleManager().previousMapModule();
    }
}
