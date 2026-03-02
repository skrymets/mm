package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.UnderlinedNodeAction;
import freemind.controller.actions.XmlAction;
import freemind.model.MindMap;
import freemind.model.MindMapNode;
import freemind.model.NodeAdapter;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

public class UnderlineActor extends NodeXmlActorAdapter {

    public UnderlineActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public void act(XmlAction action) {
        UnderlinedNodeAction underlinedact = (UnderlinedNodeAction) action;
        NodeAdapter node = getNodeFromID(underlinedact.getNode());
        if (node.isUnderlined() != underlinedact.isUnderlined()) {
            node.setUnderlined(underlinedact.isUnderlined());
            getExMapFeedback().nodeChanged(node);
        }
    }

    public Class<UnderlinedNodeAction> getDoActionClass() {
        return UnderlinedNodeAction.class;
    }

    public ActionPair apply(MindMap model, MindMapNode selected) {
        // every node is set to the inverse of the focussed node.
        boolean underlined = getExMapFeedback().getSelected().isUnderlined();
        return getActionPair(selected, !underlined);
    }

    private ActionPair getActionPair(MindMapNode selected, boolean underlined) {
        UnderlinedNodeAction underlinedAction = toggleUnderlined(selected,
                underlined);
        UnderlinedNodeAction undoUnderlinedAction = toggleUnderlined(selected,
                !underlined);
        return new ActionPair(underlinedAction, undoUnderlinedAction);
    }

    private UnderlinedNodeAction toggleUnderlined(MindMapNode selected,
                                                  boolean underlined) {
        UnderlinedNodeAction underlinedAction = new UnderlinedNodeAction();
        underlinedAction.setNode(getNodeID(selected));
        underlinedAction.setUnderlined(underlined);
        return underlinedAction;
    }

    public void setUnderlined(MindMapNode node, boolean underlined) {
        execute(getActionPair(node, underlined));
    }

}
