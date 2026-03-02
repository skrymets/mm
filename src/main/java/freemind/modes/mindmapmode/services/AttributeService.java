package freemind.modes.mindmapmode.services;

import freemind.model.MindMapNode;
import freemind.modes.attributes.Attribute;
import freemind.modes.mindmapmode.actions.xml.actors.XmlActorFactory;

/**
 * Service for attribute operations on mind map nodes.
 * Extracted from MindMapController to reduce class coupling.
 */
public class AttributeService {

    private final XmlActorFactory actorFactory;

    public AttributeService(XmlActorFactory actorFactory) {
        this.actorFactory = actorFactory;
    }

    /**
     * Sets an attribute at the given position on the specified node.
     */
    public void setAttribute(MindMapNode node, int position, Attribute attribute) {
        actorFactory.getSetAttributeActor().setAttribute(node, position, attribute);
    }

    /**
     * Inserts an attribute at the given position on the specified node.
     */
    public void insertAttribute(MindMapNode node, int position, Attribute attribute) {
        actorFactory.getInsertAttributeActor().insertAttribute(node, position, attribute);
    }

    /**
     * Adds an attribute to the specified node.
     * @return the index of the added attribute
     */
    public int addAttribute(MindMapNode node, Attribute attribute) {
        return actorFactory.getAddAttributeActor().addAttribute(node, attribute);
    }

    /**
     * Removes the attribute at the given position from the specified node.
     */
    public void removeAttribute(MindMapNode node, int position) {
        actorFactory.getRemoveAttributeActor().removeAttribute(node, position);
    }
}
