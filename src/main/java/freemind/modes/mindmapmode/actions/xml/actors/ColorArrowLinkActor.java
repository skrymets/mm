package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.ArrowLinkColorXmlAction;
import freemind.controller.actions.XmlAction;
import freemind.main.ColorUtils;
import freemind.model.LineAdapter;
import freemind.model.MindMapLink;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

import java.awt.*;

public class ColorArrowLinkActor extends XmlActorAdapter {

    public ColorArrowLinkActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public void setArrowLinkColor(MindMapLink arrowLink, Color color) {
        execute(getActionPair(arrowLink, color));
    }

    private ActionPair getActionPair(MindMapLink arrowLink, Color color) {
        return new ActionPair(createArrowLinkColorXmlAction(arrowLink, color),
                createArrowLinkColorXmlAction(arrowLink, arrowLink.getColor()));
    }

    public void act(XmlAction action) {
        if (action instanceof ArrowLinkColorXmlAction) {
            ArrowLinkColorXmlAction colorAction = (ArrowLinkColorXmlAction) action;
            MindMapLink link = getLinkRegistry().getLinkForId(
                    colorAction.getId());
            ((LineAdapter) link).setColor(ColorUtils.xmlToColor(colorAction
                    .getColor()));
            getExMapFeedback().nodeChanged(link.getSource());
        }
    }

    public Class<ArrowLinkColorXmlAction> getDoActionClass() {
        return ArrowLinkColorXmlAction.class;
    }

    private ArrowLinkColorXmlAction createArrowLinkColorXmlAction(
            MindMapLink arrowLink, Color color) {
        ArrowLinkColorXmlAction action = new ArrowLinkColorXmlAction();
        action.setColor(ColorUtils.colorToXml(color));
        action.setId(arrowLink.getUniqueId());
        return action;
    }

}
