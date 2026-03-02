package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.InsertAttributeAction;
import freemind.controller.actions.RemoveAttributeAction;
import freemind.controller.actions.XmlAction;
import freemind.model.MindMapNode;
import freemind.model.NodeAdapter;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.attributes.Attribute;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

public class InsertAttributeActor extends XmlActorAdapter {

    public InsertAttributeActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public void act(XmlAction action) {
        if (action instanceof InsertAttributeAction) {
            InsertAttributeAction setAttributeAction = (InsertAttributeAction) action;
            NodeAdapter node = getNodeFromID(setAttributeAction.getNode());
            Attribute newAttribute = new Attribute(
                    setAttributeAction.getName(), setAttributeAction.getValue());
            int position = setAttributeAction.getPosition();
            node.checkAttributePosition(position);
            node.insertAttribute(position, newAttribute);
            getExMapFeedback().nodeChanged(node);
        }
    }

    public Class<InsertAttributeAction> getDoActionClass() {
        return InsertAttributeAction.class;
    }

    public ActionPair getActionPair(MindMapNode selected, int pPosition,
                                    Attribute pAttribute) {
        InsertAttributeAction insertAttributeAction = getInsertAttributeAction(selected,
                pPosition, pAttribute);
        RemoveAttributeAction undoInsertAttributeAction = getXmlActorFactory().getRemoveAttributeActor().getRemoveAttributeAction(selected, pPosition);
        return new ActionPair(insertAttributeAction, undoInsertAttributeAction);
    }

    public InsertAttributeAction getInsertAttributeAction(MindMapNode pSelected,
                                                          int pPosition, Attribute pAttribute) {
        InsertAttributeAction insertAttributeAction = new InsertAttributeAction();
        insertAttributeAction.setNode(getNodeID(pSelected));
        insertAttributeAction.setName(pAttribute.getName());
        insertAttributeAction.setValue(pAttribute.getValue());
        insertAttributeAction.setPosition(pPosition);
        return insertAttributeAction;
    }

    public void insertAttribute(MindMapNode pNode, int pPosition, Attribute pAttribute) {
        ActionPair actionPair = getActionPair(pNode, pPosition, pAttribute);
        execute(actionPair);

    }

}
