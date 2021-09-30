package freemind.model;


public interface MindMapLink extends MindMapLine {

    String getDestinationLabel();

    String getReferenceText();

    MindMapNode getTarget();

    MindMapNode getSource();

    /**
     * The id is automatically set on creation. Is saved and restored.
     */
    String getUniqueId();

}
