package freemind.modes.mindmapmode.hooks;

import freemind.extensions.PermanentNodeHook;
import freemind.extensions.PermanentNodeHookAdapter;
import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;

/**
 * Normal Permanent... enhanced by the getMindMapController method.
 * <p>
 * This is a specialization adapted to the mindmap mode.
 * As currently, nearly all hooks belong to the mindmap mode,
 * all derive from this class.
 */
public class PermanentMindMapNodeHookAdapter extends PermanentNodeHookAdapter {

    public PermanentMindMapNodeHookAdapter() {
        super();

    }

    public MindMapController getMindMapController() {
        return (MindMapController) getController();
    }

    /**
     * @param child the child node the hook should be propagated to.
     * @return returns the new hook or null if there is already such a hook.
     */
    protected PermanentNodeHook propagate(MindMapNode child) {
        PermanentNodeHook hook = (PermanentNodeHook) getMindMapController()
                .createNodeHook(getName(), child);
        // invocation:
        child.invokeHook(hook);
        return hook;
    }

}
