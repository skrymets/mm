package freemind.controller.actions;

import freemind.controller.Controller;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class OptionHTMLExportFoldingAction extends AbstractAction {
    private final Controller controller;

    public OptionHTMLExportFoldingAction(Controller controller) {
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        controller.setProperty("html_export_folding", e.getActionCommand());
    }
}
