package freemind.controller.actions;

import freemind.controller.Controller;
import freemind.controller.MenuItemSelectedListener;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.event.ActionEvent;

@Slf4j
public class ToggleToolbarAction extends AbstractAction implements MenuItemSelectedListener {
    private final Controller controller;

    public ToggleToolbarAction(Controller controller) {
        super(controller.getResourceString("toggle_toolbar"));
        setEnabled(true);
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent event) {
        controller.setToolbarVisible(!controller.isToolbarVisible());
    }

    public boolean isSelected(JMenuItem pCheckItem, Action pAction) {
        log.info("ToggleToolbar was asked for selectedness.");
        return controller.isToolbarVisible();
    }
}
