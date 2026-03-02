package freemind.modes.mindmapmode.actions;

import freemind.controller.actions.RemoveIconXmlAction;
import freemind.main.Tools;
import freemind.modes.IconInformation;
import freemind.modes.mindmapmode.MindMapController;

import javax.swing.*;

@SuppressWarnings("serial")
public class RemoveIconAction extends NodeGeneralAction implements
        IconInformation {

    public RemoveIconAction(MindMapController modeController) {
        super(modeController, "remove_last_icon", "images/remove.png");
        setDoActionClass(RemoveIconXmlAction.class);
    }

    /**
     * @param iconAction The addIconAction to set.
     */
    public void setIconAction(IconAction iconAction) {
    }

    public String getDescription() {
        return (String) getValue(Action.SHORT_DESCRIPTION);
    }

    public ImageIcon getIcon() {
        return (ImageIcon) getValue(Action.SMALL_ICON);
    }

    public KeyStroke getKeyStroke() {
        return Tools.getKeyStroke(getMindMapController().getFrame()
                .getAdjustableProperty(getKeystrokeResourceName()));
    }

    public String getKeystrokeResourceName() {
        return "keystroke_remove_last_icon";
    }
}
