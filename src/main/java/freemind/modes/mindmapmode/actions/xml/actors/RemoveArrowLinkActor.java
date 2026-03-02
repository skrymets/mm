package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.AddArrowLinkXmlAction;
import freemind.controller.actions.RemoveArrowLinkXmlAction;
import freemind.controller.actions.XmlAction;
import freemind.main.PointUtils;
import freemind.main.ColorUtils;
import freemind.model.MindMapLink;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.MindMapArrowLink;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

public class RemoveArrowLinkActor extends XmlActorAdapter {

    public RemoveArrowLinkActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public void removeReference(MindMapLink arrowLink) {
        execute(getActionPair(arrowLink));
    }

    private ActionPair getActionPair(MindMapLink arrowLink) {
        return new ActionPair(
                createRemoveArrowLinkXmlAction(arrowLink.getUniqueId()),
                createAddArrowLinkXmlAction(arrowLink));
    }

    public void act(XmlAction action) {
        if (action instanceof RemoveArrowLinkXmlAction) {
            RemoveArrowLinkXmlAction removeAction = (RemoveArrowLinkXmlAction) action;
            MindMapLink arrowLink = getLinkRegistry().getLinkForId(
                    removeAction.getId());
            if (arrowLink == null) {
                // strange: link not found:
                throw new IllegalArgumentException("Unknown link to id "
                        + removeAction.getId() + " should be deleted.");
            }
            getLinkRegistry().deregisterLink(arrowLink);
            getExMapFeedback().nodeChanged(arrowLink.getSource());
            getExMapFeedback().nodeChanged(arrowLink.getTarget());
        }
    }

    public Class<RemoveArrowLinkXmlAction> getDoActionClass() {
        return RemoveArrowLinkXmlAction.class;
    }

    public RemoveArrowLinkXmlAction createRemoveArrowLinkXmlAction(String id) {
        RemoveArrowLinkXmlAction action = new RemoveArrowLinkXmlAction();
        action.setId(id);
        return action;
    }

    public AddArrowLinkXmlAction createAddArrowLinkXmlAction(MindMapLink link) {
        AddArrowLinkXmlAction action = new AddArrowLinkXmlAction();
        action.setNode(getNodeID(link.getSource()));
        action.setDestination(getNodeID(link.getTarget()));
        action.setNewId(link.getUniqueId());
        action.setColor(ColorUtils.colorToXml(link.getColor()));
        if (link instanceof MindMapArrowLink) {
            MindMapArrowLink arrowLink = (MindMapArrowLink) link;
            action.setEndArrow(arrowLink.getEndArrow());
            action.setEndInclination(PointUtils.PointToXml(arrowLink
                    .getEndInclination()));
            action.setStartArrow(arrowLink.getStartArrow());
            action.setStartInclination(PointUtils.PointToXml(arrowLink
                    .getStartInclination()));
        }
        return action;
    }

}
