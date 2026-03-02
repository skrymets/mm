package freemind.extensions;

import freemind.model.MindMap;
import freemind.model.MindMapNode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Straight forward implementation with some helpers.
 */
@Setter
@Slf4j
public abstract class NodeHookAdapter extends HookAdapter implements NodeHook {

    private MindMap map;

    /**
     * -- SETTER --
     *

     */
    @Getter
    private MindMapNode node;

    public NodeHookAdapter() {
        super();
    }

    public void invoke(MindMapNode node) {
        log.trace("invoke(node) called.");
    }

    protected MindMap getMap() {
        return map;
    }

    protected void nodeChanged(MindMapNode node) {
        getController().nodeChanged(node);
    }

    public String getNodeId() {
        return getController().getNodeID(getNode());
    }

}
