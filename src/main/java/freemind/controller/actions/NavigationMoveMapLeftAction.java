package freemind.controller.actions;

import freemind.controller.Controller;
import freemind.view.ImageFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class NavigationMoveMapLeftAction extends AbstractAction {
    private final Controller controller;

    public NavigationMoveMapLeftAction(Controller controller) {
        super(controller.getResourceString("move_map_left"), ImageFactory.getInstance().createIcon(controller.getResource("images/draw-arrow-back.png")));
        setEnabled(false);
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent event) {
        JTabbedPane tabbedPane = controller.getTabbedPane();
        if (tabbedPane == null) {
            return;
        }

        int selectedIndex = tabbedPane.getSelectedIndex();
        int previousIndex = (selectedIndex > 0) ? (selectedIndex - 1) : (tabbedPane.getTabCount() - 1);
        controller.moveTab(selectedIndex, previousIndex);
    }
}
