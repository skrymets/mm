package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.AddAttributeAction;
import freemind.controller.actions.RemoveAttributeAction;
import freemind.controller.actions.XmlAction;
import freemind.model.MindMapNode;
import freemind.model.NodeAdapter;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.attributes.Attribute;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

public class AddAttributeActor extends XmlActorAdapter {

    public AddAttributeActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public void act(XmlAction action) {
        if (action instanceof AddAttributeAction) {
            AddAttributeAction addAttributeAction = (AddAttributeAction) action;
            NodeAdapter node = getNodeFromID(addAttributeAction.getNode());
            Attribute newAttribute = new Attribute(
                    addAttributeAction.getName(), addAttributeAction.getValue());
            node.addAttribute(newAttribute);
            getExMapFeedback().nodeChanged(node);
        }
    }

    public Class<AddAttributeAction> getDoActionClass() {
        return AddAttributeAction.class;
    }

    public ActionPair getActionPair(MindMapNode selected, Attribute pAttribute) {
        AddAttributeAction setAttributeAction = getAddAttributeAction(selected,
                pAttribute);
        RemoveAttributeAction undoAddAttributeAction = getXmlActorFactory().getRemoveAttributeActor()
                .getRemoveAttributeAction(selected, selected.getAttributeTableLength());
        return new ActionPair(setAttributeAction, undoAddAttributeAction);
    }

    public AddAttributeAction getAddAttributeAction(MindMapNode pSelected,
                                                    Attribute pAttribute) {
        AddAttributeAction addAttributeAction = new AddAttributeAction();
        addAttributeAction.setNode(getNodeID(pSelected));
        addAttributeAction.setName(pAttribute.getName());
        addAttributeAction.setValue(pAttribute.getValue());
        return addAttributeAction;
    }

    public int addAttribute(MindMapNode pNode, Attribute pAttribute) {
        int retValue = pNode.getAttributeTableLength();
        ActionPair actionPair = getActionPair(pNode, pAttribute);
        execute(actionPair);
        return retValue;
    }

}
