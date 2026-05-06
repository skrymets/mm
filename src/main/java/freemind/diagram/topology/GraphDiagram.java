package freemind.diagram.topology;

import freemind.diagram.Diagram;
import freemind.diagram.DiagramNode;
import java.util.List;

/**
 * A {@link Diagram} whose nodes are connected by labeled directed {@link Edge}s.
 * Cycles are permitted.
 *
 * <p>Graphs do not have a designated root; navigation is via {@link #outgoingEdges}
 * and {@link #incomingEdges}.
 *
 * @param <N> the concrete node type for this diagram
 */
public interface GraphDiagram<N extends DiagramNode> extends Diagram {

    /** All nodes in the graph (no traversal order guaranteed). */
    Iterable<N> allNodes();

    List<Edge<N>> outgoingEdges(N node);

    List<Edge<N>> incomingEdges(N node);

    /** All edges in the graph (no order guaranteed). */
    Iterable<Edge<N>> allEdges();
}
