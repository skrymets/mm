package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.DeleteNodeAction;
import freemind.controller.actions.NewNodeAction;
import freemind.controller.actions.XmlAction;
import freemind.extensions.PermanentNodeHook;
import freemind.model.MindMapNode;
import freemind.model.NodeAdapter;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

public class NewChildActor extends XmlActorAdapter {

    public NewChildActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public void act(XmlAction action) {
        NewNodeAction addNodeAction = (NewNodeAction) action;
        NodeAdapter parent = getNodeFromID(addNodeAction.getNode());
        int index = addNodeAction.getIndex();
        MindMapNode newNode = getExMapFeedback().newNode("", parent.getMap());
        newNode.setLeft(addNodeAction.getPosition().equals("left"));
        String newId = addNodeAction.getNewId();
        String givenId = getLinkRegistry()
                .registerLinkTarget(newNode, newId);
        if (!givenId.equals(newId)) {
            throw new IllegalArgumentException("Designated id '" + newId
                    + "' was not given to the node. It received '" + givenId
                    + "'.");
        }
        getExMapFeedback().insertNodeInto(newNode, parent, index);
        // call hooks:
        for (PermanentNodeHook hook : parent.getActivatedHooks()) {
            hook.onNewChild(newNode);
        }
        // done.
    }

    public Class<NewNodeAction> getDoActionClass() {
        return NewNodeAction.class;
    }

    public MindMapNode addNewNode(MindMapNode parent, int index,
                                  boolean newNodeIsLeft) {
        if (index == -1) {
            index = parent.getChildCount();
        }
        // bug fix from Dimitri.
        getLinkRegistry().registerLinkTarget(parent);
        String newId = getLinkRegistry().generateUniqueID(null);
        NewNodeAction newNodeAction = getAddNodeAction(parent, index, newId,
                newNodeIsLeft);
        // Undo-action
        DeleteNodeAction deleteAction = getExMapFeedback().getActorFactory().getDeleteChildActor()
                .getDeleteNodeAction(newId);
        getExMapFeedback().doTransaction(getExMapFeedback().getResourceString("new_child"),
                new ActionPair(newNodeAction, deleteAction));
        return (MindMapNode) parent.getChildAt(index);
    }

    public NewNodeAction getAddNodeAction(MindMapNode parent, int index,
                                          String newId, boolean newNodeIsLeft) {
        String pos = newNodeIsLeft ? "left" : "right";
        NewNodeAction newNodeAction = new NewNodeAction();
        newNodeAction.setNode(getNodeID(parent));
        newNodeAction.setPosition(pos);
        newNodeAction.setIndex(index);
        newNodeAction.setNewId(newId);
        return newNodeAction;
    }

}
