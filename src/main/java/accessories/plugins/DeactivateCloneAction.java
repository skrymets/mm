package accessories.plugins;

import accessories.plugins.ClonePasteAction.Registration;
import freemind.controller.MenuItemEnabledListener;
import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.hooks.MindMapNodeHookAdapter;

import javax.swing.*;

public class DeactivateCloneAction extends MindMapNodeHookAdapter implements MenuItemEnabledListener {

    public void invoke(MindMapNode pNode) {
        super.invoke(pNode);
        ClonePlugin hook = ClonePlugin.getHook(pNode);
        if (hook != null) {
            // has it been removed due to the deregister in the meantime?
            hook = ClonePlugin.getHook(pNode);
            if (hook != null) {
                hook.removeHook();
            }
        }

    }

    public boolean isEnabled(JMenuItem pItem, Action pAction) {
        return getRegistration().isEnabled(pItem, pAction);
    }

    protected Registration getRegistration() {
        return (Registration) getPluginBaseClass();
    }

}
