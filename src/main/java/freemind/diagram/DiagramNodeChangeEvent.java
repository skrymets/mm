package freemind.diagram;

/** Emitted when a {@link DiagramNode}'s state changes. */
public record DiagramNodeChangeEvent(DiagramNode node, ChangeKind kind) {

    public enum ChangeKind {
        CONTENT, STYLE, ATTRIBUTES
    }
}
