package freemind.modes.mindmapmode.actions;

import freemind.controller.actions.AddIconAction;
import freemind.main.Tools;
import freemind.model.MindMapNode;
import freemind.modes.IconInformation;
import freemind.modes.MindIcon;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.actions.xml.actors.AddIconActor;

import javax.swing.*;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class IconAction extends MindmapAction implements IconInformation {
    public final MindIcon icon;
    private final MindMapController modeController;

    public IconAction(MindMapController controller, MindIcon _icon,
                      RemoveIconAction removeLastIconAction) {
        super(_icon.getDescription(), _icon.getIcon(), controller);
        this.modeController = controller;
        putValue(Action.SHORT_DESCRIPTION, _icon.getDescription());
        this.icon = _icon;

    }

    public void actionPerformed(ActionEvent e) {
        if (e.getID() == ActionEvent.ACTION_FIRST
                && (e.getModifiers() & ActionEvent.SHIFT_MASK
                & ~ActionEvent.CTRL_MASK & ~ActionEvent.ALT_MASK) != 0) {
            removeAllIcons();
            addLastIcon();
            return;
        }
        if (e == null
                || (e.getModifiers() & (ActionEvent.CTRL_MASK | ActionEvent.ALT_MASK)) == 0) {
            addLastIcon();
            return;
        }
        // e != null
        if ((e.getModifiers() & ~ActionEvent.SHIFT_MASK
                & ~ActionEvent.CTRL_MASK & ActionEvent.ALT_MASK) != 0) {
            removeIcon(false);
            return;
        }
        if ((e.getModifiers() & ~ActionEvent.SHIFT_MASK & ActionEvent.CTRL_MASK & ~ActionEvent.ALT_MASK) != 0) {
            removeIcon(true);
        }
    }

    private void addLastIcon() {
        for (MindMapNode selected : modeController.getSelecteds()) {
            getAddIconActor().addIcon(selected, icon);
        }
    }

    private void removeIcon(boolean removeFirst) {
        for (MindMapNode selected : modeController.getSelecteds()) {
            getAddIconActor().removeIcon(selected, icon, removeFirst);
        }
    }

    private void removeAllIcons() {
        for (MindMapNode selected : modeController.getSelecteds()) {
            if (!selected.getIcons().isEmpty()) {
                modeController.removeAllIcons(selected);
            }
        }
    }

    protected AddIconAction createAddIconAction(MindMapNode node,
                                                MindIcon icon, int iconIndex) {
        return getAddIconActor().createAddIconAction(node, icon, iconIndex);
    }

    protected AddIconActor getAddIconActor() {
        return getMindMapController().getActorFactory().getAddIconActor();
    }

    public Class<AddIconAction> getDoActionClass() {
        return AddIconAction.class;
    }

    public MindIcon getMindIcon() {
        return icon;
    }

    public KeyStroke getKeyStroke() {
        final String keystrokeResourceName = icon.getKeystrokeResourceName();
        final String keyStrokeDescription = getMindMapController().getFrame()
                .getAdjustableProperty(keystrokeResourceName);
        return Tools.getKeyStroke(keyStrokeDescription);
    }

    public String getDescription() {
        return icon.getDescription();
    }

    public ImageIcon getIcon() {
        return icon.getIcon();
    }

    public String getKeystrokeResourceName() {
        return icon.getKeystrokeResourceName();
    }

}
