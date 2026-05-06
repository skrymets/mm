package freemind.diagram;

/**
 * A vertex in a diagram. Carries content, style references, and attributes.
 * Relationships (parent/children/edges) live on the owning {@link Diagram}, not on the node.
 */
public interface DiagramNode {

    NodeId nodeId();

    NodeContent content();

    StyleReferences styleReferences();

    AttributeBag attributes();

    void addListener(DiagramNodeListener listener);

    void removeListener(DiagramNodeListener listener);
}
