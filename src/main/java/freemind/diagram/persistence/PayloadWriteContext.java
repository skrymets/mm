package freemind.diagram.persistence;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Context passed to a codec when writing a payload. The codec creates a
 * single root element via {@link #createPayloadRoot} (which is automatically
 * attached as the payload's content) and populates it with payload-specific
 * children.
 */
public final class PayloadWriteContext {

    private final Document owningDocument;
    private final Consumer<Element> payloadSink;

    public PayloadWriteContext(Document owningDocument, Consumer<Element> payloadSink) {
        this.owningDocument = Objects.requireNonNull(owningDocument, "owningDocument");
        this.payloadSink = Objects.requireNonNull(payloadSink, "payloadSink");
    }

    /** Create a new element in the owning document. Codec-internal use. */
    public Element createElement(String localName) {
        return owningDocument.createElement(localName);
    }

    /**
     * Sets the payload's root element. The root is what becomes the single
     * child of {@code <payload>} in the resulting envelope. Must be called
     * exactly once per write.
     */
    public void setPayloadRoot(Element root) {
        Objects.requireNonNull(root, "root");
        payloadSink.accept(root);
    }
}
