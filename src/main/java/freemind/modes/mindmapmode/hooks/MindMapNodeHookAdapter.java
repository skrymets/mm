package freemind.modes.mindmapmode.hooks;

import freemind.extensions.NodeHookAdapter;
import freemind.modes.mindmapmode.MindMapController;

/**
 * This is a specialization adapted to the mindmap mode.
 * As currently, nearly all hooks belong to the mindmap mode,
 * all derive from this class.
 */
public class MindMapNodeHookAdapter extends NodeHookAdapter {

    public MindMapNodeHookAdapter() {
        super();

    }

    public MindMapController getMindMapController() {
        return (MindMapController) getController();

    }

}
