package freemind.modes.mindmapmode.services;

import freemind.model.MindMapNode;
import freemind.modes.MindIcon;
import freemind.modes.mindmapmode.actions.xml.actors.XmlActorFactory;

/**
 * Service for icon operations on mind map nodes.
 * Extracted from MindMapController to reduce class coupling.
 */
public class IconService {

    private final XmlActorFactory actorFactory;

    public IconService(XmlActorFactory actorFactory) {
        this.actorFactory = actorFactory;
    }

    /**
     * Adds an icon to the specified node.
     */
    public void addIcon(MindMapNode node, MindIcon icon) {
        actorFactory.getAddIconActor().addIcon(node, icon);
    }

    /**
     * Removes all icons from the specified node.
     */
    public void removeAllIcons(MindMapNode node) {
        actorFactory.getRemoveAllIconsActor().removeAllIcons(node);
    }

    /**
     * Removes the last icon from the specified node.
     * @return the index of the removed icon, or -1 if no icon was removed
     */
    public int removeLastIcon(MindMapNode node) {
        return actorFactory.getRemoveIconActor().removeLastIcon(node);
    }
}
