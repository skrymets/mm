package freemind.modes.mindmapmode.services;

import freemind.model.MindMapLink;
import freemind.model.MindMapNode;
import freemind.modes.MindMapArrowLink;
import freemind.modes.mindmapmode.actions.xml.actors.XmlActorFactory;

import java.awt.*;

/**
 * Service for link operations on mind map nodes.
 * Handles arrow links and node references.
 * Extracted from MindMapController to reduce class coupling.
 */
public class LinkService {

    private final XmlActorFactory actorFactory;

    public LinkService(XmlActorFactory actorFactory) {
        this.actorFactory = actorFactory;
    }

    public void addLink(MindMapNode source, MindMapNode target) {
        actorFactory.getAddArrowLinkActor().addLink(source, target);
    }

    public void removeReference(MindMapLink arrowLink) {
        actorFactory.getRemoveArrowLinkActor().removeReference(arrowLink);
    }

    public void setArrowLinkColor(MindMapLink arrowLink, Color color) {
        actorFactory.getColorArrowLinkActor().setArrowLinkColor(arrowLink, color);
    }

    public void changeArrowsOfArrowLink(MindMapArrowLink arrowLink, boolean hasStartArrow, boolean hasEndArrow) {
        actorFactory.getChangeArrowsInArrowLinkActor().changeArrowsOfArrowLink(arrowLink, hasStartArrow, hasEndArrow);
    }

    public void setArrowLinkEndPoints(MindMapArrowLink link, Point startPoint, Point endPoint) {
        actorFactory.getChangeArrowLinkEndPointsActor().setArrowLinkEndPoints(link, startPoint, endPoint);
    }

    public void setLink(MindMapNode node, String link) {
        actorFactory.getSetLinkActor().setLink(node, link);
    }
}
