package freemind.diagram;

/** Receives per-node change notifications. */
@FunctionalInterface
public interface DiagramNodeListener {
    void onNodeChanged(DiagramNodeChangeEvent event);
}
