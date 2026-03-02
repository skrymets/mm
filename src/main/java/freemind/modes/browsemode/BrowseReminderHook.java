package freemind.modes.browsemode;

import freemind.model.MindMapNode;
import freemind.modes.common.plugins.ReminderHookBase;

public class BrowseReminderHook extends ReminderHookBase {

    public BrowseReminderHook() {
        super();
    }

    protected void nodeRefresh(MindMapNode node) {
        getController().nodeChanged(node);
    }

    protected void setToolTip(MindMapNode node, String key, String value) {
        node.setToolTip(key, value);
        nodeRefresh(node);
    }

}
