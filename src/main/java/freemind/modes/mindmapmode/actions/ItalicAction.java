package freemind.modes.mindmapmode.actions;

import freemind.controller.MenuItemSelectedListener;
import freemind.controller.actions.ItalicNodeAction;
import freemind.modes.mindmapmode.MindMapController;

import javax.swing.*;

@SuppressWarnings("serial")
public class ItalicAction extends NodeGeneralAction implements
        MenuItemSelectedListener {
    private final MindMapController modeController;

    public ItalicAction(MindMapController modeController) {
        super(modeController, "italic", "images/Italic16.gif");
        this.modeController = modeController;
        setDoActionClass(ItalicNodeAction.class);
    }

    public boolean isSelected(JMenuItem item, Action action) {
        return modeController.getSelected().isItalic();
    }

}
