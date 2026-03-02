package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.ArrowLinkPointXmlAction;
import freemind.controller.actions.XmlAction;
import freemind.main.PointUtils;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.MindMapArrowLink;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

import java.awt.*;

public class ChangeArrowLinkEndPointsActor extends XmlActorAdapter {

    public ChangeArrowLinkEndPointsActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public void setArrowLinkEndPoints(MindMapArrowLink link, Point startPoint,
                                      Point endPoint) {
        execute(getActionPair(link, startPoint, endPoint));
    }

    private ActionPair getActionPair(MindMapArrowLink link, Point startPoint,
                                     Point endPoint) {
        return new ActionPair(createArrowLinkPointXmlAction(link, startPoint,
                endPoint), createArrowLinkPointXmlAction(link,
                link.getStartInclination(), link.getEndInclination()));
    }

    public void act(XmlAction action) {
        if (action instanceof ArrowLinkPointXmlAction) {
            ArrowLinkPointXmlAction pointAction = (ArrowLinkPointXmlAction) action;
            MindMapArrowLink link = (MindMapArrowLink) getLinkRegistry()
                    .getLinkForId(pointAction.getId());
            link.setStartInclination(PointUtils.xmlToPoint(pointAction
                    .getStartPoint()));
            link.setEndInclination(PointUtils.xmlToPoint(pointAction.getEndPoint()));
            getExMapFeedback().nodeChanged(link.getSource());
            getExMapFeedback().nodeChanged(link.getTarget());
        }

    }

    public Class<ArrowLinkPointXmlAction> getDoActionClass() {
        return ArrowLinkPointXmlAction.class;
    }

    private ArrowLinkPointXmlAction createArrowLinkPointXmlAction(
            MindMapArrowLink arrowLink, Point startPoint, Point endPoint) {
        ArrowLinkPointXmlAction action = new ArrowLinkPointXmlAction();
        action.setStartPoint(PointUtils.PointToXml(startPoint));
        action.setEndPoint(PointUtils.PointToXml(endPoint));
        action.setId(arrowLink.getUniqueId());
        return action;
    }

}
