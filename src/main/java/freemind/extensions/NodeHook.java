package freemind.extensions;

import accessories.plugins.ApplyFormatPlugin;
import accessories.plugins.AutomaticLayout;
import freemind.model.MindMap;
import freemind.model.MindMapNode;

/**
 * This is a general base interface for hooks that implement actions belonging to nodes.
 * They are normally started due to a user interaction.
 * <p>
 * There are two different types of NodeHooks:
 * <ol>
 * <li> non-permanent hooks: do something and terminate.
 * <li> permanent hooks: when started, they stick to the node, are stored with it and
 * 		recreated, when the map with the node is loaded the next time.
 * </ol>
 * Examples are
 * <ul>
 * <li> {@link ApplyFormatPlugin}: non-permanent
 * <li> {@link AutomaticLayout}: a permanent hook that formats the node depending on its depth.
 * </ul>
 */
public interface NodeHook extends MindMapHook {

    void setMap(MindMap map);

    void setNode(MindMapNode node);

    /* hooks */

    /**
     * Is called after creation:
     */
    void invoke(MindMapNode node);
}
