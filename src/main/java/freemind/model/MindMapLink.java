package freemind.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public interface MindMapLink extends MindMapLine {

    String getDestinationLabel();

    String getReferenceText();

    MindMapNode getTarget();

    MindMapNode getSource();

    /**
     * The id is automatically set on creation. Is saved and restored.
     */
    String getUniqueId();

    /**
     * Serialize this link as an XML element to be appended to the source node.
     * Default returns {@code null} — link types that don't participate in
     * node-level XML serialization (typical for non-arrow links) skip this.
     */
    default Element saveLink(Document doc) {
        return null;
    }

    /**
     * Serialize this link's target counterpart (the inverse arrow registration)
     * to be appended to the target node. Default returns {@code null}.
     */
    default Element saveTarget(Document doc, MindMapLinkRegistry registry) {
        return null;
    }
}
