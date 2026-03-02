package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.SetAttributeAction;
import freemind.controller.actions.XmlAction;
import freemind.model.MindMapNode;
import freemind.model.NodeAdapter;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.attributes.Attribute;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

public class SetAttributeActor extends XmlActorAdapter {

    public SetAttributeActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public void act(XmlAction action) {
        if (action instanceof SetAttributeAction) {
            SetAttributeAction setAttributeAction = (SetAttributeAction) action;
            NodeAdapter node = getNodeFromID(setAttributeAction.getNode());
            Attribute newAttribute = new Attribute(setAttributeAction.getName(), setAttributeAction.getValue());
            int position = setAttributeAction.getPosition();
            node.checkAttributePosition(position);
            if (!node.getAttribute(position).equals(newAttribute)) {
                node.setAttribute(position, newAttribute);
                getExMapFeedback().nodeChanged(node);
            }
        }
    }

    public Class<SetAttributeAction> getDoActionClass() {
        return SetAttributeAction.class;
    }

    public ActionPair getActionPair(MindMapNode selected, int pPosition, Attribute pAttribute) {
        SetAttributeAction setAttributeAction = getSetAttributeAction(selected, pPosition, pAttribute);
        SetAttributeAction undoSetAttributeAction = getSetAttributeAction(selected, pPosition, selected.getAttribute(pPosition));
        return new ActionPair(setAttributeAction, undoSetAttributeAction);
    }

    public void setAttribute(MindMapNode pSelected,
                             int pPosition, Attribute pAttribute) {
        ActionPair actionPair = getActionPair(pSelected, pPosition, pAttribute);
        execute(actionPair);
    }

    public SetAttributeAction getSetAttributeAction(MindMapNode pSelected,
                                                    int pPosition, Attribute pAttribute) {
        SetAttributeAction setAttributeAction = new SetAttributeAction();
        setAttributeAction.setNode(getNodeID(pSelected));
        setAttributeAction.setName(pAttribute.getName());
        setAttributeAction.setValue(pAttribute.getValue());
        setAttributeAction.setPosition(pPosition);
        return setAttributeAction;
    }

}
