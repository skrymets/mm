package freemind.diagram.swing;

import freemind.diagram.AttributeBag;
import freemind.diagram.Diagram;
import freemind.diagram.DiagramListener;
import freemind.diagram.DiagramMetadata;
import freemind.diagram.DiagramNode;
import freemind.diagram.DiagramNodeChangeEvent;
import freemind.diagram.DiagramNodeListener;
import freemind.diagram.DiagramTypeId;
import freemind.diagram.DocumentId;
import freemind.diagram.NodeContent;
import freemind.diagram.NodeId;
import freemind.diagram.ResourceTable;
import freemind.diagram.StylePalette;
import freemind.diagram.StyleReferences;
import freemind.diagram.topology.TreeDiagram;
import org.junit.jupiter.api.Test;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SwingTreeAdapterTest {

    @Test
    void rootChildLookupReflectsDiagram() {
        var d = sampleTree();   // root → [a, b], a → [a1]
        var adapter = new SwingTreeAdapter<>(d);
        assertSame(d.rootNode(), adapter.getRoot());
        assertEquals(2, adapter.getChildCount(d.rootNode()));
        assertSame(d.getChildren(d.rootNode()).get(0), adapter.getChild(d.rootNode(), 0));
    }

    @Test
    void leafCheckMatchesDiagram() {
        var d = sampleTree();
        var adapter = new SwingTreeAdapter<>(d);
        var a1 = d.getChildren(d.getChildren(d.rootNode()).get(0)).get(0);
        assertTrue(adapter.isLeaf(a1));
        assertFalse(adapter.isLeaf(d.rootNode()));
    }

    @Test
    void nodeChangeFiresTreeModelEvent() {
        var d = sampleTree();
        var adapter = new SwingTreeAdapter<>(d);
        var events = new CopyOnWriteArrayList<TreeModelEvent>();
        adapter.addTreeModelListener(new TreeModelListener() {
            @Override public void treeNodesChanged(TreeModelEvent e)   { events.add(e); }
            @Override public void treeNodesInserted(TreeModelEvent e)  { /* unused */ }
            @Override public void treeNodesRemoved(TreeModelEvent e)   { /* unused */ }
            @Override public void treeStructureChanged(TreeModelEvent e) { /* unused */ }
        });

        var a = d.getChildren(d.rootNode()).get(0);
        ((TestNode) a).fireChange(DiagramNodeChangeEvent.ChangeKind.CONTENT);

        assertEquals(1, events.size());
        // path is root → a
        var path = events.get(0).getPath();
        assertEquals(2, path.length);
        assertSame(d.rootNode(), path[0]);
        assertSame(a, path[1]);
    }

    @Test
    void valueForPathChangedThrows() {
        var adapter = new SwingTreeAdapter<>(sampleTree());
        assertThrows(UnsupportedOperationException.class,
            () -> adapter.valueForPathChanged(null, "x"));
    }

    @Test
    void disposeUnregistersNodeListenersAndClearsTreeModelListeners() {
        var d = sampleTree();
        var adapter = new SwingTreeAdapter<>(d);
        var events = new CopyOnWriteArrayList<TreeModelEvent>();
        adapter.addTreeModelListener(new TreeModelListener() {
            @Override public void treeNodesChanged(TreeModelEvent e)   { events.add(e); }
            @Override public void treeNodesInserted(TreeModelEvent e)  { /* unused */ }
            @Override public void treeNodesRemoved(TreeModelEvent e)   { /* unused */ }
            @Override public void treeStructureChanged(TreeModelEvent e) { /* unused */ }
        });

        adapter.dispose();

        // After dispose, mutating a node fires no TreeModelEvent on the registered listener.
        var a = d.getChildren(d.rootNode()).get(0);
        ((TestNode) a).fireChange(DiagramNodeChangeEvent.ChangeKind.CONTENT);

        assertEquals(0, events.size());
    }

    // --- in-memory test fixture: tiny TreeDiagram with mutable nodes ---

    private static TreeDiagram<TestNode> sampleTree() {
        var root = new TestNode("root");
        var a = new TestNode("a");
        var b = new TestNode("b");
        var a1 = new TestNode("a1");
        var children = new HashMap<TestNode, List<TestNode>>();
        children.put(root, List.of(a, b));
        children.put(a, List.of(a1));
        children.put(b, List.of());
        children.put(a1, List.of());
        var parents = new HashMap<TestNode, TestNode>();
        parents.put(a, root);
        parents.put(b, root);
        parents.put(a1, a);
        return new TestTreeDiagram(root, children, parents);
    }

    private static final class TestNode implements DiagramNode {
        private final NodeId id;
        private final List<DiagramNodeListener> listeners = new ArrayList<>();
        TestNode(String id) { this.id = new NodeId(id); }
        @Override public NodeId nodeId()                     { return id; }
        @Override public NodeContent content()               { return NodeContent.plain(id.value()); }
        @Override public StyleReferences styleReferences()   { return StyleReferences.none(); }
        @Override public AttributeBag attributes()           { return AttributeBag.empty(); }
        @Override public void addListener(DiagramNodeListener l)    { listeners.add(l); }
        @Override public void removeListener(DiagramNodeListener l) { listeners.remove(l); }
        void fireChange(DiagramNodeChangeEvent.ChangeKind kind) {
            var ev = new DiagramNodeChangeEvent(this, kind);
            for (var l : listeners) l.onNodeChanged(ev);
        }
    }

    private static final class TestTreeDiagram implements TreeDiagram<TestNode> {
        private final TestNode root;
        private final Map<TestNode, List<TestNode>> children;
        private final Map<TestNode, TestNode> parents;
        private final DocumentId docId = DocumentId.newRandom();
        TestTreeDiagram(TestNode root, Map<TestNode, List<TestNode>> children,
                        Map<TestNode, TestNode> parents) {
            this.root = root; this.children = children; this.parents = parents;
        }
        @Override public TestNode rootNode()                                  { return root; }
        @Override public Optional<TestNode> getParent(TestNode node)          { return Optional.ofNullable(parents.get(node)); }
        @Override public List<TestNode> getChildren(TestNode node)            { return children.getOrDefault(node, List.of()); }
        @Override public int depthOf(TestNode node)                           {
            int d = 0; for (var n = node; parents.get(n) != null; n = parents.get(n)) d++;
            return d;
        }
        @Override public boolean isRoot(TestNode node)                        { return node == root; }
        @Override public Iterable<TestNode> allNodes()                        {
            var all = new ArrayList<TestNode>();
            collect(root, all);
            return all;
        }
        private void collect(TestNode n, List<TestNode> acc) {
            acc.add(n);
            for (var c : getChildren(n)) collect(c, acc);
        }
        @Override public DocumentId documentId()                              { return docId; }
        @Override public DiagramTypeId typeId()                               { return new DiagramTypeId("test"); }
        @Override public DiagramMetadata metadata()                           { return DiagramMetadata.empty(Instant.EPOCH); }
        @Override public StylePalette stylePalette()                          { return StylePalette.empty(); }
        @Override public ResourceTable resources()                            { return ResourceTable.empty(); }
        @Override public void addListener(DiagramListener l)                  { /* unused */ }
        @Override public void removeListener(DiagramListener l)               { /* unused */ }
    }
}
