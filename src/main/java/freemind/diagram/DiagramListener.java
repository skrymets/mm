package freemind.diagram;

/**
 * Receives whole-diagram change notifications (metadata, style palette, resources).
 * Per-node changes are delivered via {@link DiagramNodeListener}.
 */
@FunctionalInterface
public interface DiagramListener {
    void onDiagramChanged(DiagramChangeEvent event);
}
