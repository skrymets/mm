package freemind.diagram.mindmap;

import freemind.diagram.DiagramListener;
import freemind.diagram.DiagramMetadata;
import freemind.diagram.DiagramTypeId;
import freemind.diagram.DocumentId;
import freemind.diagram.ResourceTable;
import freemind.diagram.StylePalette;
import freemind.diagram.capabilities.AuxiliaryLink;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * In-memory implementation of {@link MindMapDiagram}: a single-rooted tree of
 * {@link MindMapNodeImpl}s with optional auxiliary links between any two nodes.
 *
 * <p>Mutators are expected to be called from a single thread; listeners are
 * notified synchronously.
 */
public final class MindMapDiagramImpl implements MindMapDiagram {

    public static final DiagramTypeId TYPE_ID = new DiagramTypeId("mindmap");

    private final DocumentId documentId;
    private final MindMapNodeImpl root;
    private DiagramMetadata metadata;
    private StylePalette stylePalette = StylePalette.empty();
    private ResourceTable resources = ResourceTable.empty();

    private final Map<MindMapNode, MindMapNode> parents = new HashMap<>();
    private final Map<MindMapNode, List<MindMapNodeImpl>> children = new LinkedHashMap<>();
    private final List<AuxiliaryLink<MindMapNode>> auxiliaryLinks = new ArrayList<>();
    private final CopyOnWriteArrayList<DiagramListener> listeners = new CopyOnWriteArrayList<>();

    public MindMapDiagramImpl(DocumentId documentId, MindMapNodeImpl root, DiagramMetadata metadata) {
        this.documentId = Objects.requireNonNull(documentId, "documentId");
        this.root = Objects.requireNonNull(root, "root");
        this.metadata = Objects.requireNonNull(metadata, "metadata");
        children.put(root, new ArrayList<>());
    }

    public static MindMapDiagramImpl createEmpty(MindMapNodeImpl root) {
        return new MindMapDiagramImpl(DocumentId.newRandom(), root,
            DiagramMetadata.empty(Instant.now()));
    }

    // --- Diagram ---
    @Override public DocumentId documentId()         { return documentId; }
    @Override public DiagramTypeId typeId()          { return TYPE_ID; }
    @Override public DiagramMetadata metadata()      { return metadata; }
    @Override public StylePalette stylePalette()     { return stylePalette; }
    @Override public ResourceTable resources()       { return resources; }
    @Override public void addListener(DiagramListener l)    { listeners.add(Objects.requireNonNull(l)); }
    @Override public void removeListener(DiagramListener l) { listeners.remove(l); }

    public void setMetadata(DiagramMetadata m) {
        this.metadata = Objects.requireNonNull(m, "metadata");
        fire(freemind.diagram.DiagramChangeEvent.ChangeKind.METADATA);
    }

    public void setStylePalette(StylePalette p) {
        this.stylePalette = Objects.requireNonNull(p, "stylePalette");
        fire(freemind.diagram.DiagramChangeEvent.ChangeKind.STYLE_PALETTE);
    }

    public void setResources(ResourceTable r) {
        this.resources = Objects.requireNonNull(r, "resources");
        fire(freemind.diagram.DiagramChangeEvent.ChangeKind.RESOURCES);
    }

    // --- TreeDiagram ---
    @Override public MindMapNode rootNode()                                     { return root; }
    @Override public Optional<MindMapNode> getParent(MindMapNode node)          { return Optional.ofNullable(parents.get(node)); }
    @Override public List<MindMapNode> getChildren(MindMapNode node)            {
        return List.copyOf(children.getOrDefault(node, List.of()));
    }

    @Override public int depthOf(MindMapNode node) {
        int d = 0;
        for (var n = node; parents.get(n) != null; n = parents.get(n)) d++;
        return d;
    }

    @Override public boolean isRoot(MindMapNode node)                           { return node == root; }

    @Override public Iterable<MindMapNode> allNodes() {
        var all = new ArrayList<MindMapNode>();
        collect(root, all);
        return all;
    }

    private void collect(MindMapNode n, List<MindMapNode> acc) {
        acc.add(n);
        for (var c : children.getOrDefault(n, List.of())) collect(c, acc);
    }

    /** Adds {@code child} under {@code parent} at the end of its child list. */
    public void addChild(MindMapNode parent, MindMapNodeImpl child) {
        Objects.requireNonNull(parent, "parent");
        Objects.requireNonNull(child, "child");
        if (parents.containsKey(child)) {
            throw new IllegalArgumentException(
                "Node " + child.nodeId().value() + " is already attached");
        }
        children.computeIfAbsent(parent, k -> new ArrayList<>()).add(child);
        children.put(child, new ArrayList<>());
        parents.put(child, parent);
    }

    /** Removes {@code node} (and its subtree) from the diagram. */
    public void removeNode(MindMapNode node) {
        Objects.requireNonNull(node, "node");
        if (node == root) {
            throw new IllegalArgumentException("Cannot remove the root node");
        }
        var parent = parents.get(node);
        if (parent == null) {
            throw new IllegalArgumentException("Node is not attached: " + node.nodeId().value());
        }
        children.get(parent).remove(node);
        removeSubtree(node);
    }

    private void removeSubtree(MindMapNode n) {
        for (var c : new ArrayList<>(children.getOrDefault(n, List.of()))) {
            removeSubtree(c);
        }
        parents.remove(n);
        children.remove(n);
        auxiliaryLinks.removeIf(l -> l.source() == n || l.target() == n);
    }

    // --- HasAuxiliaryLinks ---
    @Override public Iterable<AuxiliaryLink<MindMapNode>> auxiliaryLinks() {
        return List.copyOf(auxiliaryLinks);
    }

    @Override public List<AuxiliaryLink<MindMapNode>> outgoingAuxiliaryLinks(MindMapNode node) {
        return auxiliaryLinks.stream().filter(l -> l.source() == node).toList();
    }

    @Override public List<AuxiliaryLink<MindMapNode>> incomingAuxiliaryLinks(MindMapNode node) {
        return auxiliaryLinks.stream().filter(l -> l.target() == node).toList();
    }

    public void addAuxiliaryLink(AuxiliaryLink<MindMapNode> link) {
        Objects.requireNonNull(link, "link");
        auxiliaryLinks.add(link);
    }

    private void fire(freemind.diagram.DiagramChangeEvent.ChangeKind kind) {
        var ev = new freemind.diagram.DiagramChangeEvent(this, kind);
        for (var l : listeners) l.onDiagramChanged(ev);
    }
}
