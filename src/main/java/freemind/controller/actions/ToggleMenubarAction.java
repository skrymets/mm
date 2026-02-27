package freemind.controller.actions;

import freemind.controller.Controller;
import freemind.controller.MenuItemSelectedListener;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ToggleMenubarAction extends AbstractAction implements MenuItemSelectedListener {
    private final Controller controller;

    public ToggleMenubarAction(Controller controller) {
        super(controller.getResourceString("toggle_menubar"));
        setEnabled(true);
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent event) {
        controller.setMenubarVisible(!controller.isMenubarVisible());
    }

    public boolean isSelected(JMenuItem pCheckItem, Action pAction) {
        return controller.isMenubarVisible();
    }
}
