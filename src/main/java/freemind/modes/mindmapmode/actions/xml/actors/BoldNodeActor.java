package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.BoldNodeAction;
import freemind.controller.actions.XmlAction;
import freemind.model.MindMap;
import freemind.model.MindMapNode;
import freemind.model.NodeAdapter;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

public class BoldNodeActor extends NodeXmlActorAdapter {

    public BoldNodeActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public void act(XmlAction action) {
        if (action instanceof BoldNodeAction) {
            BoldNodeAction boldact = (BoldNodeAction) action;
            NodeAdapter node = getNodeFromID(boldact.getNode());
            if (node.isBold() != boldact.isBold()) {
                node.setBold(boldact.isBold());
                mMapFeedback.nodeChanged(node);
            }
        }
    }

    public Class<BoldNodeAction> getDoActionClass() {
        return BoldNodeAction.class;
    }

    public ActionPair apply(MindMap model, MindMapNode selected) {
        // every node is set to the inverse of the focussed node.
        boolean bold = getSelected().isBold();
        return getActionPair(selected, !bold);
    }

    private ActionPair getActionPair(MindMapNode selected, boolean bold) {
        BoldNodeAction boldAction = toggleBold(selected, bold);
        BoldNodeAction undoBoldAction = toggleBold(selected, selected.isBold());
        return new ActionPair(boldAction, undoBoldAction);
    }

    private BoldNodeAction toggleBold(MindMapNode selected, boolean bold) {
        BoldNodeAction boldAction = new BoldNodeAction();
        boldAction.setNode(getNodeID(selected));
        boldAction.setBold(bold);
        return boldAction;
    }

    public void setBold(MindMapNode node, boolean bold) {
        execute(getActionPair(node, bold));
    }
}
