package freemind.view.mindmapview.services;

import freemind.model.MindMapNode;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.NodeView;
import freemind.view.mindmapview.NodeViewVisitor;

import java.util.*;

public class ViewerRegistryService {

    private final MapView mapView;
    private HashMap<MindMapNode, List<NodeView>> views = null;

    public ViewerRegistryService(MapView mapView) {
        this.mapView = mapView;
    }

    public NodeView getNodeView(MindMapNode node) {
        if (node == null) {
            return null;
        }
        Collection<NodeView> viewers = getViewers(node);
        final Iterator<NodeView> iterator = viewers.iterator();
        while (iterator.hasNext()) {
            NodeView candidateView = iterator.next();
            if (candidateView.getMap() == mapView) {
                return candidateView;
            }
        }
        return null;
    }

    public Collection<NodeView> getViewers(MindMapNode mindMapNode) {
        if (views == null) {
            views = new HashMap<>();
        }
        if (!views.containsKey(mindMapNode)) {
            views.put(mindMapNode, new ArrayList<>());
        }
        return views.get(mindMapNode);
    }

    public void addViewer(MindMapNode mindMapNode, NodeView viewer) {
        getViewers(mindMapNode).add(viewer);
        mindMapNode.addTreeModelListener(viewer);
    }

    public void removeViewer(MindMapNode mindMapNode, NodeView viewer) {
        Collection<NodeView> viewers = getViewers(mindMapNode);
        viewers.remove(viewer);
        if (viewers.isEmpty()) {
            views.remove(mindMapNode);
        }
        mindMapNode.removeTreeModelListener(viewer);
    }

    public void acceptViewVisitor(MindMapNode mindMapNode, NodeViewVisitor visitor) {
        getViewers(mindMapNode).forEach(visitor::visit);
    }
}
