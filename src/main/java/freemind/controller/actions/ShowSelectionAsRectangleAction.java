package freemind.controller.actions;

import freemind.controller.Controller;
import freemind.controller.MenuItemSelectedListener;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ShowSelectionAsRectangleAction extends AbstractAction implements MenuItemSelectedListener {
    private final Controller controller;

    public ShowSelectionAsRectangleAction(Controller controller) {
        super(controller.getResourceString("selection_as_rectangle"));
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        controller.toggleSelectionAsRectangle();
    }

    public boolean isSelected(JMenuItem pCheckItem, Action pAction) {
        return controller.isSelectionAsRectangle();
    }
}
