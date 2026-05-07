package freemind.diagram.mindmap;

import freemind.diagram.AttributeBag;
import freemind.diagram.DiagramNodeChangeEvent;
import freemind.diagram.DiagramNodeChangeEvent.ChangeKind;
import freemind.diagram.DiagramNodeListener;
import freemind.diagram.NodeContent;
import freemind.diagram.NodeId;
import freemind.diagram.StyleReferences;

import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/** Mutable, in-memory {@link MindMapNode} with content, style, and attributes. */
public final class MindMapNodeImpl implements MindMapNode {

    private final NodeId id;
    private NodeContent content;
    private StyleReferences styleReferences;
    private AttributeBag attributes;
    private final CopyOnWriteArrayList<DiagramNodeListener> listeners = new CopyOnWriteArrayList<>();

    public MindMapNodeImpl(NodeId id, NodeContent content) {
        this.id = Objects.requireNonNull(id, "id");
        this.content = Objects.requireNonNull(content, "content");
        this.styleReferences = StyleReferences.none();
        this.attributes = AttributeBag.empty();
    }

    @Override public NodeId nodeId()                     { return id; }
    @Override public NodeContent content()               { return content; }
    @Override public StyleReferences styleReferences()   { return styleReferences; }
    @Override public AttributeBag attributes()           { return attributes; }

    public void setContent(NodeContent content) {
        this.content = Objects.requireNonNull(content, "content");
        fire(ChangeKind.CONTENT);
    }

    public void setStyleReferences(StyleReferences styleReferences) {
        this.styleReferences = Objects.requireNonNull(styleReferences, "styleReferences");
        fire(ChangeKind.STYLE);
    }

    public void setAttributes(AttributeBag attributes) {
        this.attributes = Objects.requireNonNull(attributes, "attributes");
        fire(ChangeKind.ATTRIBUTES);
    }

    @Override public void addListener(DiagramNodeListener l)    { listeners.add(Objects.requireNonNull(l)); }
    @Override public void removeListener(DiagramNodeListener l) { listeners.remove(l); }

    private void fire(ChangeKind kind) {
        var ev = new DiagramNodeChangeEvent(this, kind);
        for (var l : listeners) l.onNodeChanged(ev);
    }
}
