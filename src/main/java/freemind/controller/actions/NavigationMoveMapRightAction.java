package freemind.controller.actions;

import freemind.controller.Controller;
import freemind.view.ImageFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class NavigationMoveMapRightAction extends AbstractAction {
    private final Controller controller;

    public NavigationMoveMapRightAction(Controller controller) {
        super(controller.getResourceString("move_map_right"),
                ImageFactory.getInstance().createIcon(controller.getResource("images/draw-arrow-forward.png")));
        setEnabled(false);
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent event) {
        JTabbedPane tabbedPane = controller.getTabbedPane();
        if (tabbedPane != null) {
            int selectedIndex = tabbedPane.getSelectedIndex();
            int previousIndex = (selectedIndex >= tabbedPane.getTabCount() - 1) ? 0
                    : (selectedIndex + 1);
            controller.moveTab(selectedIndex, previousIndex);
        }
    }
}
