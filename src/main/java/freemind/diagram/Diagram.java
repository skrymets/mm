package freemind.diagram;

/**
 * Marker interface for any diagram model (mind map, concept map, fishbone, ...).
 * Topology-agnostic — see {@link freemind.diagram.topology.TreeDiagram} and
 * {@link freemind.diagram.topology.GraphDiagram} for topology-specific contracts.
 */
public interface Diagram {

    DocumentId documentId();

    DiagramTypeId typeId();

    DiagramMetadata metadata();

    StylePalette stylePalette();

    ResourceTable resources();

    void addListener(DiagramListener listener);

    void removeListener(DiagramListener listener);
}
