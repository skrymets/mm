package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.StrikethroughNodeAction;
import freemind.controller.actions.XmlAction;
import freemind.model.MindMap;
import freemind.model.MindMapNode;
import freemind.model.NodeAdapter;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

public class StrikethroughNodeActor extends NodeXmlActorAdapter {

    public StrikethroughNodeActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public void act(XmlAction action) {
        if (action instanceof StrikethroughNodeAction) {
            StrikethroughNodeAction strikethroughact = (StrikethroughNodeAction) action;
            NodeAdapter node = getNodeFromID(strikethroughact.getNode());
            if (node.isStrikethrough() != strikethroughact.isStrikethrough()) {
                node.setStrikethrough(strikethroughact.isStrikethrough());
                mMapFeedback.nodeChanged(node);
            }
        }
    }

    public Class<StrikethroughNodeAction> getDoActionClass() {
        return StrikethroughNodeAction.class;
    }

    public ActionPair apply(MindMap model, MindMapNode selected) {
        // every node is set to the inverse of the focussed node.
        boolean Strikethrough = getSelected().isStrikethrough();
        return getActionPair(selected, !Strikethrough);
    }

    private ActionPair getActionPair(MindMapNode selected, boolean Strikethrough) {
        StrikethroughNodeAction StrikethroughAction = toggleStrikethrough(selected, Strikethrough);
        StrikethroughNodeAction undoStrikethroughAction = toggleStrikethrough(selected, selected.isStrikethrough());
        return new ActionPair(StrikethroughAction, undoStrikethroughAction);
    }

    private StrikethroughNodeAction toggleStrikethrough(MindMapNode selected, boolean strikethrough) {
        StrikethroughNodeAction StrikethroughAction = new StrikethroughNodeAction();
        StrikethroughAction.setNode(getNodeID(selected));
        StrikethroughAction.setStrikethrough(strikethrough);
        return StrikethroughAction;
    }

    public void setStrikethrough(MindMapNode node, boolean Strikethrough) {
        execute(getActionPair(node, Strikethrough));
    }
}
