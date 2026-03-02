package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.EditNodeAction;
import freemind.controller.actions.XmlAction;
import freemind.model.MindMapNode;
import freemind.model.NodeAdapter;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

public class EditActor extends XmlActorAdapter {

    public EditActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public void act(XmlAction action) {
        EditNodeAction editAction = (EditNodeAction) action;
        NodeAdapter node = this.getNodeFromID(editAction
                .getNode());
        if (!node.toString().equals(editAction.getText())) {
            node.setUserObject(editAction.getText());
            getExMapFeedback().nodeChanged(node);
        }
    }

    public Class<EditNodeAction> getDoActionClass() {
        return EditNodeAction.class;
    }

    public void setNodeText(MindMapNode selected, String newText) {
        String oldText = selected.toString();

        EditNodeAction EditAction = new EditNodeAction();
        String nodeID = getNodeID(selected);
        EditAction.setNode(nodeID);
        EditAction.setText(newText);

        EditNodeAction undoEditAction = new EditNodeAction();
        undoEditAction.setNode(nodeID);
        undoEditAction.setText(oldText);

        execute(new ActionPair(EditAction, undoEditAction));
    }

}
