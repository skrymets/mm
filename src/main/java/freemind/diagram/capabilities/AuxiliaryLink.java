package freemind.diagram.capabilities;

import freemind.diagram.DiagramNode;
import freemind.diagram.StyleReferences;
import java.util.Objects;
import java.util.Optional;

/**
 * Cross-cutting link between two nodes that is *not* part of the diagram's
 * primary topology. In a mind map, the primary topology is a tree (parent/child);
 * an {@code AuxiliaryLink} is what was historically called an ArrowLink — a
 * decorated arrow drawn between two nodes that aren't necessarily related
 * by parentage.
 */
public record AuxiliaryLink<N extends DiagramNode>(
    N source,
    N target,
    Optional<String> label,
    StyleReferences style
) {

    public AuxiliaryLink {
        Objects.requireNonNull(source, "source");
        Objects.requireNonNull(target, "target");
        Objects.requireNonNull(label, "label");
        Objects.requireNonNull(style, "style");
    }

    public static <N extends DiagramNode> AuxiliaryLink<N> of(N source, N target) {
        return new AuxiliaryLink<>(source, target, Optional.empty(), StyleReferences.none());
    }
}
