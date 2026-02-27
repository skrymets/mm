package freemind.controller.actions;

import freemind.controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MoveToRootAction extends AbstractAction {
    private final Controller controller;

    public MoveToRootAction(Controller controller) {
        super(controller.getResourceString("move_to_root"));
        setEnabled(false);
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent event) {
        controller.moveToRoot();
    }
}
