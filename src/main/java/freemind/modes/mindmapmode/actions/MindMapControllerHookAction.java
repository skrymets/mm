package freemind.modes.mindmapmode.actions;

import freemind.controller.MenuItemEnabledListener;
import freemind.modes.mindmapmode.MindMapController;

import javax.swing.*;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class MindMapControllerHookAction extends AbstractAction implements
        HookAction, MenuItemEnabledListener {
    final String mHookName;
    final MindMapController mindMapController;

    public MindMapControllerHookAction(String hookName,
                                       MindMapController mindMapController) {
        super(hookName);
        this.mHookName = hookName;
        this.mindMapController = mindMapController;
    }

    public void actionPerformed(ActionEvent arg0) {
        if (null == mindMapController.getMap()) {
            return;
        }
        mindMapController.createModeControllerHook(mHookName);
    }

    public String getHookName() {
        return mHookName;
    }

    public boolean isEnabled(JMenuItem pItem, Action pAction) {
        return mindMapController.getMap() != null;
    }

}
