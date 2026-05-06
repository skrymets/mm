package freemind.diagram.topology;

import freemind.diagram.Diagram;
import freemind.diagram.DiagramNode;
import java.util.List;
import java.util.Optional;

/**
 * A {@link Diagram} whose nodes are organized as a single-rooted tree.
 *
 * <p>Concrete tree diagrams (mind maps, fishbones) extend this contract.
 * Cross-cutting non-tree relationships (e.g., a mind map's auxiliary links)
 * are layered via capability mixins like
 * {@link freemind.diagram.capabilities.HasAuxiliaryLinks}.
 *
 * @param <N> the concrete node type for this diagram
 */
public interface TreeDiagram<N extends DiagramNode> extends Diagram {

    N rootNode();

    Optional<N> getParent(N node);

    List<N> getChildren(N node);

    /** Number of edges from {@link #rootNode()} to {@code node}. Root has depth 0. */
    int depthOf(N node);

    boolean isRoot(N node);

    /** All nodes reachable from {@link #rootNode()} via {@link #getChildren}. */
    Iterable<N> allNodes();
}
