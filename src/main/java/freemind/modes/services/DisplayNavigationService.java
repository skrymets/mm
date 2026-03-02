package freemind.modes.services;

import freemind.model.MindMapNode;
import freemind.modes.ControllerAdapter;
import freemind.view.mindmapview.NodeView;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

/**
 * Manages node display and navigation: centering, unfolding paths,
 * resolving node views, and link text resolution.
 * Extracted from ControllerAdapter to reduce god-object complexity.
 */
@Slf4j
public class DisplayNavigationService {

    private final ControllerAdapter adapter;

    public DisplayNavigationService(ControllerAdapter adapter) {
        this.adapter = adapter;
    }

    public String getLinkShortText(MindMapNode node) {
        String adaptedText = node.getLink();
        if (adaptedText == null)
            return null;
        if (adaptedText.startsWith("#")) {
            try {
                MindMapNode dest = adapter.getNodeFromID(adaptedText.substring(1));
                return dest.getShortText(adapter);
            } catch (RuntimeException e) {
                return adapter.getText("link_not_available_any_more");
            }
        }
        return adaptedText;
    }

    public void displayNode(MindMapNode node) {
        displayNode(node, null);
    }

    /**
     * Display a node in the display (used by find and the goto action by arrow
     * link actions).
     */
    public void displayNode(MindMapNode node, ArrayList<MindMapNode> nodesUnfoldedByDisplay) {
        // Unfold the path to the node
        Object[] path = adapter.getMap().getPathToRoot(node);
        // Iterate the path with the exception of the last node
        for (int i = 0; i < path.length - 1; i++) {
            MindMapNode nodeOnPath = (MindMapNode) path[i];
            if (nodeOnPath.isFolded()) {
                if (nodesUnfoldedByDisplay != null)
                    nodesUnfoldedByDisplay.add(nodeOnPath);
                adapter.setFolded(nodeOnPath, false);
            }
        }
    }

    /**
     * Select the node and scroll to it.
     */
    private void centerNode(NodeView node) {
        adapter.getView().getScrollService().centerNode(node);
        adapter.getView().getSelectionService().selectAsTheOnlyOneSelected(node);
    }

    public void centerNode(MindMapNode node) {
        if (node == null) {
            return;
        }
        NodeView view = adapter.getController().getView().getViewerRegistryService().getNodeView(node);
        if (view == null) {
            displayNode(node);
            view = adapter.getController().getView().getViewerRegistryService().getNodeView(node);
        }
        centerNode(view);
    }

    public NodeView getNodeView(MindMapNode node) {
        return adapter.getView().getViewerRegistryService().getNodeView(node);
    }
}
