package freemind.controller.actions;

import freemind.controller.Controller;
import freemind.view.ImageFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class NavigationNextMapAction extends AbstractAction {
    private final Controller controller;

    public NavigationNextMapAction(Controller controller) {
        super(controller.getResourceString("next_map"), ImageFactory.getInstance().createIcon(controller.getResource("images/1rightarrow.png")));
        setEnabled(false);
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent event) {
        controller.getMapModuleManager().nextMapModule();
    }
}
