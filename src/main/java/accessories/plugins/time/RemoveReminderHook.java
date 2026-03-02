package accessories.plugins.time;

import freemind.model.MindMapNode;
import freemind.modes.common.plugins.ReminderHookBase;
import freemind.modes.mindmapmode.hooks.MindMapNodeHookAdapter;

import java.util.Arrays;
import java.util.List;

public class RemoveReminderHook extends MindMapNodeHookAdapter {

    public RemoveReminderHook() {
        super();
    }

    public void invoke(MindMapNode node) {
        super.invoke(node);
        ReminderHookBase hook = TimeManagementOrganizer.getHook(node);
        if (hook != null) {
            List<MindMapNode> selected = Arrays.asList(node);
            // adding the hook the second time, it is removed.
            getMindMapController().addHook(node, selected, TimeManagement.REMINDER_HOOK_NAME, null);
        }
    }
}
