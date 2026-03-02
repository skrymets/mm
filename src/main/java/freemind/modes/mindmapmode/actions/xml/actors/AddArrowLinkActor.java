package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.AddArrowLinkXmlAction;
import freemind.controller.actions.RemoveArrowLinkXmlAction;
import freemind.controller.actions.XmlAction;
import freemind.main.PointUtils;
import freemind.main.ColorUtils;
import freemind.model.MindMapNode;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.mindmapmode.MindMapArrowLinkModel;
import freemind.modes.mindmapmode.actions.xml.ActionPair;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AddArrowLinkActor extends XmlActorAdapter {

    public AddArrowLinkActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public void act(XmlAction action) {
        if (action instanceof AddArrowLinkXmlAction) {
            AddArrowLinkXmlAction arrowAction = (AddArrowLinkXmlAction) action;
            MindMapNode source = getNodeFromID(arrowAction.getNode());
            MindMapNode target = getNodeFromID(arrowAction.getDestination());
            if (source == target) {
                log.warn("Can't create link between itself. ({}).", source);
                return;
            }
            String proposedId = arrowAction.getNewId();

            if (getLinkRegistry().getLabel(target) == null) {
                // call registry to give new label
                getLinkRegistry().registerLinkTarget(target);
            }
            MindMapArrowLinkModel linkModel = new MindMapArrowLinkModel(source,
                    target, getExMapFeedback());
            linkModel.setDestinationLabel(getLinkRegistry().getLabel(target));
            // give label:
            linkModel.setUniqueId(getLinkRegistry().generateUniqueLinkId(
                    proposedId));
            // check for other attributes:
            if (arrowAction.getColor() != null) {
                linkModel.setColor(ColorUtils.xmlToColor(arrowAction.getColor()));
            }
            if (arrowAction.getEndArrow() != null) {
                linkModel.setEndArrow(arrowAction.getEndArrow());
            }
            if (arrowAction.getEndInclination() != null) {
                linkModel.setEndInclination(PointUtils.xmlToPoint(arrowAction
                        .getEndInclination()));
            }
            if (arrowAction.getStartArrow() != null) {
                linkModel.setStartArrow(arrowAction.getStartArrow());
            }
            if (arrowAction.getStartInclination() != null) {
                linkModel.setStartInclination(PointUtils.xmlToPoint(arrowAction
                        .getStartInclination()));
            }
            // register link.
            getLinkRegistry().registerLink(linkModel);
            getExMapFeedback().nodeChanged(target);
            getExMapFeedback().nodeChanged(source);

        }
    }

    public Class<AddArrowLinkXmlAction> getDoActionClass() {
        return AddArrowLinkXmlAction.class;
    }

    private ActionPair getActionPair(MindMapNode source, MindMapNode target) {
        AddArrowLinkXmlAction doAction = createAddArrowLinkXmlAction(source,
                target, getLinkRegistry().generateUniqueLinkId(null));
        // now, the id is clear:
        RemoveArrowLinkXmlAction undoAction = getExMapFeedback()
                .getActorFactory().getRemoveArrowLinkActor()
                .createRemoveArrowLinkXmlAction(doAction.getNewId());
        return new ActionPair(doAction, undoAction);
    }

    public AddArrowLinkXmlAction createAddArrowLinkXmlAction(
            MindMapNode source, MindMapNode target, String proposedID) {
        AddArrowLinkXmlAction action = new AddArrowLinkXmlAction();
        action.setNode(getNodeID(source));
        action.setDestination(getNodeID(target));
        action.setNewId(proposedID);
        return action;
    }

    /**
     * Source holds the MindMapArrowLinkModel and points to the id placed in
     * target.
     */
    public void addLink(MindMapNode source, MindMapNode target) {
        execute(getActionPair(source, target));
    }

}
