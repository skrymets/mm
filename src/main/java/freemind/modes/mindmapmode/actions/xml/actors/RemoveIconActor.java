package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.AddIconAction;
import freemind.controller.actions.RemoveIconXmlAction;
import freemind.controller.actions.XmlAction;
import freemind.model.MindMap;
import freemind.model.MindMapNode;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.MindIcon;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

import java.util.List;

public class RemoveIconActor extends NodeXmlActorAdapter {

    public RemoveIconActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public Class<RemoveIconXmlAction> getDoActionClass() {
        return RemoveIconXmlAction.class;
    }

    public RemoveIconXmlAction createRemoveIconXmlAction(MindMapNode node,
                                                         int iconPosition) {
        RemoveIconXmlAction action = new RemoveIconXmlAction();
        action.setNode(getNodeID(node));
        action.setIconPosition(iconPosition);
        return action;
    }

    private ActionPair apply(MindMapNode selected) {
        List<MindIcon> icons = selected.getIcons();
        if (icons.isEmpty())
            return null;
        AddIconAction undoAction = getXmlActorFactory().getAddIconActor().createAddIconAction(selected,
                icons.get(icons.size() - 1), MindIcon.LAST);
        return new ActionPair(
                createRemoveIconXmlAction(selected, MindIcon.LAST), undoAction);
    }

    public int removeLastIcon(MindMapNode node) {
        execute(apply(node));
        return node.getIcons().size();
    }

    public void act(XmlAction action) {
        if (action instanceof RemoveIconXmlAction) {
            RemoveIconXmlAction removeAction = (RemoveIconXmlAction) action;
            MindMapNode node = getNodeFromID(removeAction
                    .getNode());
            int position = removeAction.getIconPosition();
            node.removeIcon(position);
            getExMapFeedback().nodeChanged(node);
        }
    }

    @Override
    public ActionPair apply(MindMap pModel, MindMapNode pSelected) {
        return apply(pSelected);
    }

}
