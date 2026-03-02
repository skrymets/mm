package freemind.modes.mindmapmode.actions;

import freemind.controller.MenuItemSelectedListener;
import freemind.controller.actions.StrikethroughNodeAction;
import freemind.modes.mindmapmode.MindMapController;

import javax.swing.*;

@SuppressWarnings("serial")
public class StrikethroughAction extends NodeGeneralAction implements
        MenuItemSelectedListener {
    public StrikethroughAction(MindMapController modeController) {
        super(modeController, "Strikethrough", "images/format-text-strikethrough.png");
        setDoActionClass(StrikethroughNodeAction.class);
    }

    public boolean isSelected(JMenuItem item, Action action) {
        return modeController.getSelected().isStrikethrough();
    }

}
