package accessories.plugins.time;

import freemind.model.MindMapNode;
import freemind.modes.common.plugins.ReminderHookBase;
import freemind.modes.mindmapmode.MindMapController;

public class ReminderHook extends ReminderHookBase {

    public ReminderHook() {
        super();
    }

    protected void nodeRefresh(MindMapNode node) {
        getMindMapController().nodeRefresh(node);
    }

    private MindMapController getMindMapController() {
        return (MindMapController) getController();
    }

    protected void setToolTip(MindMapNode node, String key, String value) {
        getMindMapController().setToolTip(node, key, value);
    }

}
