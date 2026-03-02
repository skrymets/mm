package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.AddLinkXmlAction;
import freemind.controller.actions.XmlAction;
import freemind.model.MindMapNode;
import freemind.model.NodeAdapter;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

public class SetLinkActor extends XmlActorAdapter {

    public SetLinkActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public void setLink(MindMapNode node, String link) {
        execute(getActionPair(node, link));
    }

    public void act(XmlAction action) {
        if (action instanceof AddLinkXmlAction) {
            AddLinkXmlAction linkAction = (AddLinkXmlAction) action;
            NodeAdapter node = getNodeFromID(linkAction.getNode());
            node.setLink(linkAction.getDestination());
            getExMapFeedback().nodeChanged(node);
        }
    }

    public Class<AddLinkXmlAction> getDoActionClass() {
        return AddLinkXmlAction.class;
    }

    private ActionPair getActionPair(MindMapNode node, String link) {
        return new ActionPair(createAddLinkXmlAction(node, link),
                createAddLinkXmlAction(node, node.getLink()));
    }

    private AddLinkXmlAction createAddLinkXmlAction(MindMapNode node,
                                                    String link) {
        AddLinkXmlAction action = new AddLinkXmlAction();
        action.setNode(getNodeID(node));
        action.setDestination(link);
        return action;
    }
}
