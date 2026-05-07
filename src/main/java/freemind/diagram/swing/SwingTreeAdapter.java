package freemind.diagram.swing;

import freemind.diagram.DiagramNode;
import freemind.diagram.DiagramNodeChangeEvent;
import freemind.diagram.DiagramNodeListener;
import freemind.diagram.topology.TreeDiagram;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Adapts any {@link TreeDiagram} to {@link TreeModel}. Listens for
 * per-node changes via {@link DiagramNodeListener} and translates them into
 * {@link TreeModelEvent}s on the registered {@link TreeModelListener}s.
 */
public final class SwingTreeAdapter<N extends DiagramNode> implements TreeModel {

    private final TreeDiagram<N> diagram;
    private final EventListenerList listenerList = new EventListenerList();
    private final DiagramNodeListener nodeListener = this::onNodeChanged;

    public SwingTreeAdapter(TreeDiagram<N> diagram) {
        this.diagram = Objects.requireNonNull(diagram, "diagram");
        diagram.allNodes().forEach(n -> n.addListener(nodeListener));
    }

    @Override public Object getRoot()                        { return diagram.rootNode(); }

    @Override
    public Object getChild(Object parent, int index) {
        return diagram.getChildren(cast(parent)).get(index);
    }

    @Override
    public int getChildCount(Object parent) {
        return diagram.getChildren(cast(parent)).size();
    }

    @Override
    public boolean isLeaf(Object node) {
        return diagram.getChildren(cast(node)).isEmpty();
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        // Editing flows through the diagram API, not through this adapter.
        throw new UnsupportedOperationException(
            "valueForPathChanged: edits must go through the diagram, not the TreeModel adapter");
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if (parent == null || child == null) return -1;
        return diagram.getChildren(cast(parent)).indexOf(cast(child));
    }

    @Override public void addTreeModelListener(TreeModelListener l)    { listenerList.add(TreeModelListener.class, l); }
    @Override public void removeTreeModelListener(TreeModelListener l) { listenerList.remove(TreeModelListener.class, l); }

    /**
     * Unregisters all node listeners and clears registered Swing
     * {@link TreeModelListener}s. Call when the adapter is replaced or
     * the diagram is closed; failure to call this leaves the adapter
     * (and its registered Swing components) reachable through the
     * diagram's nodes, preventing GC.
     */
    public void dispose() {
        diagram.allNodes().forEach(n -> n.removeListener(nodeListener));
        for (var l : listenerList.getListeners(TreeModelListener.class)) {
            listenerList.remove(TreeModelListener.class, l);
        }
    }

    private void onNodeChanged(DiagramNodeChangeEvent event) {
        var path = pathTo(cast(event.node()));
        var ev = new TreeModelEvent(this, path);
        for (var l : listenerList.getListeners(TreeModelListener.class)) {
            l.treeNodesChanged(ev);
        }
    }

    private TreePath pathTo(N node) {
        var rev = new ArrayList<N>();
        for (N cur = node; cur != null; cur = diagram.getParent(cur).orElse(null)) {
            rev.add(cur);
        }
        var path = new ArrayList<N>(rev.size());
        for (int i = rev.size() - 1; i >= 0; i--) path.add(rev.get(i));
        return new TreePath(path.toArray());
    }

    @SuppressWarnings("unchecked")
    private N cast(Object node) { return (N) node; }
}
