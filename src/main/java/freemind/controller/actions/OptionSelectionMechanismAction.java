package freemind.controller.actions;

import freemind.controller.Controller;
import freemind.main.FreeMind;
import freemind.main.Resources;
import freemind.preferences.FreemindPropertyListener;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class OptionSelectionMechanismAction extends AbstractAction implements FreemindPropertyListener {
    private final Controller controller;

    public OptionSelectionMechanismAction(Controller controller) {
        this.controller = controller;
        Resources.addPropertyChangeListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        changeSelection(command);
    }

    private void changeSelection(String command) {
        controller.setProperty("selection_method", command);
        // and update the selection method in the NodeMouseMotionListener
        controller.getNodeMouseMotionListener().updateSelectionMethod();
        String statusBarString = controller.getResourceString(command);
        if (statusBarString != null) // should not happen
            controller.getFrame().setStatusText(statusBarString);
    }

    public void propertyChanged(String propertyName, String newValue,
                                String oldValue) {
        if (propertyName.equals(FreeMind.RESOURCES_SELECTION_METHOD)) {
            changeSelection(newValue);
        }
    }
}
