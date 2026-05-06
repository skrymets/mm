package freemind.diagram.capabilities;

import freemind.diagram.DiagramNode;
import java.util.List;

/**
 * Capability marker for diagrams that carry auxiliary (non-topology) links
 * between nodes — e.g., mind-map ArrowLinks.
 *
 * @param <N> the concrete node type for this diagram
 */
public interface HasAuxiliaryLinks<N extends DiagramNode> {

    /** All auxiliary links in the diagram. */
    Iterable<AuxiliaryLink<N>> auxiliaryLinks();

    /** Auxiliary links whose source is {@code node}. */
    List<AuxiliaryLink<N>> outgoingAuxiliaryLinks(N node);

    /** Auxiliary links whose target is {@code node}. */
    List<AuxiliaryLink<N>> incomingAuxiliaryLinks(N node);
}
