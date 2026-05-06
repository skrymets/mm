package freemind.diagram;

/** Emitted when a {@link Diagram}'s envelope-level state changes. */
public record DiagramChangeEvent(Diagram diagram, ChangeKind kind) {

    public enum ChangeKind {
        METADATA, STYLE_PALETTE, RESOURCES
    }
}
