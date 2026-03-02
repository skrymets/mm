package freemind.modes.mindmapmode.services;

import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.actions.xml.actors.XmlActorFactory;

import java.util.List;
import java.util.ListIterator;

/**
 * Service for tree structure operations.
 * Handles node creation, deletion, movement, and folding.
 * Extracted from MindMapController to reduce class coupling.
 */
public class TreeStructureService {

    private final XmlActorFactory actorFactory;

    public TreeStructureService(XmlActorFactory actorFactory) {
        this.actorFactory = actorFactory;
    }

    /**
     * Creates a new node as a child of the parent at the specified index.
     */
    public MindMapNode addNewNode(MindMapNode parent, int index, boolean newNodeIsLeft) {
        return actorFactory.getNewChildActor().addNewNode(parent, index, newNodeIsLeft);
    }

    /**
     * Deletes the specified node.
     */
    public void deleteNode(MindMapNode selectedNode) {
        actorFactory.getDeleteChildActor().deleteNode(selectedNode);
    }

    /**
     * Toggles the folded state of the selected nodes.
     */
    public void toggleFolded(ListIterator<MindMapNode> selectedsIterator) {
        actorFactory.getToggleFoldedActor().toggleFolded(selectedsIterator);
    }

    /**
     * Sets the folded state of a node.
     */
    public void setFolded(MindMapNode node, boolean folded) {
        actorFactory.getToggleFoldedActor().setFolded(node, folded);
    }

    /**
     * Moves nodes up or down in the tree.
     */
    public void moveNodes(MindMapNode selected, List<MindMapNode> selecteds, int direction) {
        actorFactory.getNodeUpActor().moveNodes(selected, selecteds, direction);
    }
}
