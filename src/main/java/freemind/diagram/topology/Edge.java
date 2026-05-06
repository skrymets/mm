package freemind.diagram.topology;

import freemind.diagram.DiagramNode;
import java.util.Objects;
import java.util.Optional;

/**
 * Directed edge from {@code source} to {@code target} with an optional label.
 * Edges are first-class citizens of {@link GraphDiagram}; trees model
 * relationships through {@link TreeDiagram#getParent} / {@link TreeDiagram#getChildren}
 * instead.
 */
public record Edge<N extends DiagramNode>(N source, N target, Optional<EdgeLabel> label) {

    public Edge {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(label, "label");
    }

    public static <N extends DiagramNode> Edge<N> unlabeled(N source, N target) {
        return new Edge<>(source, target, Optional.empty());
    }

    public static <N extends DiagramNode> Edge<N> labeled(N source, N target, String label) {
        return new Edge<>(source, target, Optional.of(new EdgeLabel(label)));
    }
}
