package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.NodeAction;
import freemind.controller.actions.RemoveAttributeAction;
import freemind.controller.actions.XmlAction;
import freemind.model.MindMapNode;
import freemind.model.NodeAdapter;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

public class RemoveAttributeActor extends XmlActorAdapter {

    public RemoveAttributeActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public void act(XmlAction action) {
        if (action instanceof RemoveAttributeAction) {
            RemoveAttributeAction removeAttributeAction = (RemoveAttributeAction) action;
            NodeAdapter node = getNodeFromID(removeAttributeAction.getNode());
            int position = removeAttributeAction.getPosition();
            node.checkAttributePosition(position);
            node.removeAttribute(position);
            getExMapFeedback().nodeChanged(node);
        }
    }

    public Class<RemoveAttributeAction> getDoActionClass() {
        return RemoveAttributeAction.class;
    }

    public ActionPair getActionPair(MindMapNode selected, int pPosition) {
        RemoveAttributeAction setAttributeAction = getRemoveAttributeAction(selected,
                pPosition);
        NodeAction undoRemoveAttributeAction;
        if (pPosition == selected.getAttributeTableLength() - 1) {
            undoRemoveAttributeAction = getXmlActorFactory().getAddAttributeActor().getAddAttributeAction(
                    selected, selected.getAttribute(pPosition));
        } else {
            undoRemoveAttributeAction = getXmlActorFactory().getInsertAttributeActor().getInsertAttributeAction(
                    selected, pPosition, selected.getAttribute(pPosition));
        }
        return new ActionPair(setAttributeAction, undoRemoveAttributeAction);
    }

    public RemoveAttributeAction getRemoveAttributeAction(MindMapNode pSelected,
                                                          int pPosition) {
        RemoveAttributeAction removeAttributeAction = new RemoveAttributeAction();
        removeAttributeAction.setNode(getNodeID(pSelected));
        removeAttributeAction.setPosition(pPosition);
        return removeAttributeAction;
    }

    public void removeAttribute(MindMapNode pNode, int pPosition) {
        ActionPair actionPair = getActionPair(pNode, pPosition);
        execute(actionPair);
    }
}
