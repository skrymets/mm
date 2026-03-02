package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.AddIconAction;
import freemind.controller.actions.CompoundAction;
import freemind.controller.actions.RemoveAllIconsXmlAction;
import freemind.controller.actions.XmlAction;
import freemind.frok.patches.JIBXGeneratedUtil;
import freemind.model.MindMap;
import freemind.model.MindMapNode;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.MindIcon;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

public class RemoveAllIconsActor extends NodeXmlActorAdapter {

    public RemoveAllIconsActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public RemoveAllIconsXmlAction createRemoveAllIconsXmlAction(
            MindMapNode node) {
        RemoveAllIconsXmlAction action = new RemoveAllIconsXmlAction();
        action.setNode(getNodeID(node));
        return action;
    }

    public void act(XmlAction action) {
        if (action instanceof RemoveAllIconsXmlAction) {
            RemoveAllIconsXmlAction removeAction = (RemoveAllIconsXmlAction) action;
            MindMapNode node = getNodeFromID(removeAction
                    .getNode());
            while (!node.getIcons().isEmpty()) {
                node.removeIcon(MindIcon.LAST);
            }
            getExMapFeedback().nodeChanged(node);
        }
    }

    public void removeAllIcons(MindMapNode node) {
        getExMapFeedback().doTransaction(
                this.getClass().getName(), apply(getExMapFeedback().getMap(), node));
    }

    public Class<RemoveAllIconsXmlAction> getDoActionClass() {
        return RemoveAllIconsXmlAction.class;
    }

    public ActionPair apply(MindMap model, MindMapNode selected) {
        CompoundAction undoAction = new CompoundAction();
        for (MindIcon icon : selected.getIcons()) {
            final AddIconAction createAddIconAction = getExMapFeedback().getActorFactory().getAddIconActor().createAddIconAction(
                    selected,
                    icon,
                    MindIcon.LAST);
            CompoundAction.Choice choice = JIBXGeneratedUtil.choiceFromXmlActions(createAddIconAction);
            undoAction.addChoice(choice);
        }
        return new ActionPair(createRemoveAllIconsXmlAction(selected),
                undoAction);
    }

}
