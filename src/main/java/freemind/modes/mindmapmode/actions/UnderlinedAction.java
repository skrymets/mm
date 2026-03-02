package freemind.modes.mindmapmode.actions;

import freemind.controller.MenuItemSelectedListener;
import freemind.controller.actions.UnderlinedNodeAction;
import freemind.modes.mindmapmode.MindMapController;

import javax.swing.*;

@SuppressWarnings("serial")
public class UnderlinedAction extends NodeGeneralAction implements
        MenuItemSelectedListener {
    private final MindMapController modeController;

    public UnderlinedAction(MindMapController modeController) {
        super(modeController, "underlined", "images/Underline24.gif");
        this.modeController = modeController;
        setDoActionClass(UnderlinedNodeAction.class);
    }

    public boolean isSelected(JMenuItem item, Action action) {
        return modeController.getSelected().isUnderlined();
    }
}
