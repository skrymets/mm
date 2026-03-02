package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.AddIconAction;
import freemind.controller.actions.XmlAction;
import freemind.main.MindMapUtils;
import freemind.model.MindMapNode;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.MindIcon;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

public class AddIconActor extends XmlActorAdapter {

    public AddIconActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public void addIcon(MindMapNode node, MindIcon icon) {
        execute(getAddLastIconActionPair(node, icon));
    }

    public void act(XmlAction action) {
        if (action instanceof AddIconAction iconAction) {
            MindMapNode node = getNodeFromID(iconAction.getNode());
            String iconName = iconAction.getIconName();
            int position = iconAction.getIconPosition();
            MindIcon icon = MindIcon.factory(iconName);
            node.addIcon(icon, position);
            getExMapFeedback().nodeChanged(node);
        }
    }

    public Class<AddIconAction> getDoActionClass() {
        return AddIconAction.class;
    }

    public AddIconAction createAddIconAction(MindMapNode node, MindIcon icon,
                                             int iconPosition) {
        AddIconAction action = new AddIconAction();
        action.setNode(getNodeID(node));
        action.setIconName(icon.getName());
        action.setIconPosition(iconPosition);
        return action;
    }

    private ActionPair getAddLastIconActionPair(MindMapNode node, MindIcon icon) {
        int iconIndex = MindIcon.LAST;
        return getAddIconActionPair(node, icon, iconIndex);
    }

    private ActionPair getAddIconActionPair(MindMapNode node, MindIcon icon,
                                            int iconIndex) {
        AddIconAction doAction = createAddIconAction(node, icon, iconIndex);
        XmlAction undoAction = getXmlActorFactory().getRemoveIconActor().createRemoveIconXmlAction(
                node, iconIndex);
        return new ActionPair(doAction, undoAction);
    }

    private ActionPair getToggleIconActionPair(MindMapNode node, MindIcon icon) {
        int iconIndex = MindMapUtils.iconFirstIndex(node,
                icon.getName());
        if (iconIndex == -1) {
            return getAddLastIconActionPair(node, icon);
        } else {
            return getRemoveIconActionPair(node, icon, iconIndex);
        }
    }

    private ActionPair getRemoveIconActionPair(MindMapNode node, MindIcon icon,
                                               boolean removeFirst) {
        int iconIndex = removeFirst ? MindMapUtils.iconFirstIndex(
                node, icon.getName()) : MindMapUtils.iconLastIndex(
                node, icon.getName());
        return iconIndex >= 0 ? getRemoveIconActionPair(node, icon, iconIndex)
                : null;
    }

    private ActionPair getRemoveIconActionPair(MindMapNode node, MindIcon icon,
                                               int iconIndex) {
        XmlAction doAction = getXmlActorFactory().getRemoveIconActor().createRemoveIconXmlAction(
                node, iconIndex);
        XmlAction undoAction = createAddIconAction(node, icon, iconIndex);
        return new ActionPair(doAction, undoAction);
    }

    public void toggleIcon(MindMapNode node, MindIcon icon) {
        getExMapFeedback().doTransaction(
                this.getClass().getName() + "/toggle", getToggleIconActionPair(node, icon));
    }

    public void removeIcon(MindMapNode node, MindIcon icon, boolean removeFirst) {
        final ActionPair removeIconActionPair = getRemoveIconActionPair(node,
                icon, removeFirst);
        if (removeIconActionPair == null) {
            return;
        }
        getExMapFeedback().doTransaction(
                this.getClass().getName() + "/remove", removeIconActionPair);
    }

}
