package freemind.controller.actions;

import freemind.controller.Controller;
import freemind.view.ImageFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ShowFilterToolbarAction extends AbstractAction {
    private final Controller controller;

    public ShowFilterToolbarAction(Controller controller) {
        super(controller.getResourceString("filter_toolbar"), ImageFactory.getInstance().createIcon(controller.getResource("images/filter.gif")));
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent event) {
        controller.getFilterController().showFilterToolbar(!controller.getFilterController().isVisible());
    }
}
