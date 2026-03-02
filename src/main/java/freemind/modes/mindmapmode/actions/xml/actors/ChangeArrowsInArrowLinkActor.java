package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.ArrowLinkArrowXmlAction;
import freemind.controller.actions.XmlAction;
import freemind.model.MindMapLink;
import freemind.modes.ArrowLinkAdapter;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.MindMapArrowLink;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

public class ChangeArrowsInArrowLinkActor extends XmlActorAdapter {

    public ChangeArrowsInArrowLinkActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public void changeArrowsOfArrowLink(MindMapArrowLink arrowLink,
                                        boolean hasStartArrow, boolean hasEndArrow) {
        execute(getActionPair(arrowLink, hasStartArrow, hasEndArrow));
    }

    private ActionPair getActionPair(MindMapArrowLink arrowLink2,
                                     boolean hasStartArrow2, boolean hasEndArrow2) {
        return new ActionPair(createArrowLinkArrowXmlAction(arrowLink2,
                hasStartArrow2, hasEndArrow2), createArrowLinkArrowXmlAction(
                arrowLink2, arrowLink2.getStartArrow(),
                arrowLink2.getEndArrow()));
    }

    public void act(XmlAction action) {
        if (action instanceof ArrowLinkArrowXmlAction) {
            ArrowLinkArrowXmlAction arrowAction = (ArrowLinkArrowXmlAction) action;
            MindMapLink link = getLinkRegistry().getLinkForId(
                    arrowAction.getId());
            ((ArrowLinkAdapter) link)
                    .setStartArrow(arrowAction.getStartArrow());
            ((ArrowLinkAdapter) link).setEndArrow(arrowAction.getEndArrow());
            getExMapFeedback().nodeChanged(link.getSource());
            getExMapFeedback().nodeChanged(link.getTarget());
        }
    }

    public Class<ArrowLinkArrowXmlAction> getDoActionClass() {
        return ArrowLinkArrowXmlAction.class;
    }

    private ArrowLinkArrowXmlAction createArrowLinkArrowXmlAction(
            MindMapArrowLink arrowLink, boolean hasStartArrow,
            boolean hasEndArrow) {
        return createArrowLinkArrowXmlAction(arrowLink,
                (hasStartArrow) ? "Default" : "None", (hasEndArrow) ? "Default"
                        : "None");
    }

    private ArrowLinkArrowXmlAction createArrowLinkArrowXmlAction(
            MindMapArrowLink arrowLink, String hasStartArrow,
            String hasEndArrow) {
        ArrowLinkArrowXmlAction action = new ArrowLinkArrowXmlAction();
        action.setStartArrow(hasStartArrow);
        action.setEndArrow(hasEndArrow);
        action.setId(arrowLink.getUniqueId());
        return action;
    }

}
