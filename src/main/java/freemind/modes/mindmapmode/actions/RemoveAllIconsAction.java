package freemind.modes.mindmapmode.actions;

import freemind.controller.actions.RemoveAllIconsXmlAction;
import freemind.main.Tools;
import freemind.modes.IconInformation;
import freemind.modes.mindmapmode.MindMapController;

import javax.swing.*;

@SuppressWarnings("serial")
public class RemoveAllIconsAction extends NodeGeneralAction implements
        IconInformation {

    public RemoveAllIconsAction(MindMapController modeController,
                                IconAction addIconAction) {
        super(modeController, "remove_all_icons", "images/edittrash.png");
        setDoActionClass(RemoveAllIconsXmlAction.class);
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
        return "keystroke_remove_all_icons";
    }
}
