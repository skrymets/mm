package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.MoveNodeXmlAction;
import freemind.controller.actions.XmlAction;
import freemind.model.MindMap;
import freemind.model.MindMapNode;
import freemind.model.NodeAdapter;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

public class MoveNodeActor extends NodeXmlActorAdapter {

    public MoveNodeActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public void act(XmlAction action) {
        MoveNodeXmlAction moveAction = (MoveNodeXmlAction) action;
        NodeAdapter node = getNodeFromID(moveAction.getNode());
        node.setHGap(moveAction.getHGap());
        node.setShiftY(moveAction.getShiftY());
        if (!node.isRoot())
            node.getParentNode().setVGap(moveAction.getVGap());
        getExMapFeedback().nodeChanged(node);
    }

    public Class<MoveNodeXmlAction> getDoActionClass() {
        return MoveNodeXmlAction.class;
    }

    public ActionPair apply(MindMap model, MindMapNode selected) {
        // reset position
        if (selected.isRoot())
            return null;
        return getActionPair(selected, NodeAdapter.VGAP, NodeAdapter.HGAP, 0);
    }

    private ActionPair getActionPair(MindMapNode selected, int parentVGap,
                                     int hGap, int shiftY) {
        MoveNodeXmlAction moveAction = moveNode(selected, parentVGap, hGap,
                shiftY);
        MoveNodeXmlAction undoAction = moveNode(selected, selected
                        .getParentNode().getVGap(), selected.getHGap(),
                selected.getShiftY());
        return new ActionPair(moveAction, undoAction);
    }

    private MoveNodeXmlAction moveNode(MindMapNode selected, int parentVGap,
                                       int hGap, int shiftY) {
        MoveNodeXmlAction moveNodeAction = new MoveNodeXmlAction();
        moveNodeAction.setNode(getNodeID(selected));
        moveNodeAction.setHGap(hGap);
        moveNodeAction.setVGap(parentVGap);
        moveNodeAction.setShiftY(shiftY);
        return moveNodeAction;
    }

    public void moveNodeTo(MindMapNode node, int parentVGap, int hGap,
                           int shiftY) {
        if (parentVGap == node.getParentNode().getVGap()
                && hGap == node.getHGap() && shiftY == node.getShiftY()) {
            return;
        }
        execute(getActionPair(node, parentVGap, hGap, shiftY));
    }

}
