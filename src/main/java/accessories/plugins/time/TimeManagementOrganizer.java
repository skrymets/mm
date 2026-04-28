package accessories.plugins.time;

import freemind.controller.MenuItemEnabledListener;
import freemind.extensions.HookRegistration;
import freemind.extensions.PermanentNodeHook;
import freemind.model.MindMap;
import freemind.model.MindMapNode;
import freemind.modes.ModeController;
import freemind.modes.common.plugins.ReminderHookBase;
import freemind.modes.mindmapmode.actions.NodeHookAction;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;

/**
 * Enables the encrypt/decrypt menu item only if the map/node is encrypted.
 */
@Slf4j
public class TimeManagementOrganizer implements HookRegistration,
        MenuItemEnabledListener {

    private final ModeController controller;

    public TimeManagementOrganizer(ModeController controller, MindMap map) {
        this.controller = controller;
    }

    public void register() {
    }

    public void deRegister() {
    }

    public boolean isEnabled(JMenuItem item, Action action) {
        if (action instanceof NodeHookAction nodeHookAction) {
            String hookName = nodeHookAction.getHookName();
            if (hookName.equals("plugins/time/RemoveReminderHook.java")) {
                boolean visible = false;
                for (var node : controller.getSelecteds()) {
                    if (TimeManagementOrganizer.getHook(node) != null) {
                        visible = true;
                    }
                }
                item.setVisible(visible);
            }
        }
        return true;
    }

    public static ReminderHookBase getHook(MindMapNode node) {
        for (var element : node.getActivatedHooks()) {
            if (element instanceof ReminderHookBase reminderHook) {
                return reminderHook;
            }
        }
        return null;
    }
}
