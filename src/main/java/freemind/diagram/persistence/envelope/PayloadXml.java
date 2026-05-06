package freemind.diagram.persistence.envelope;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAnyElement;
import org.w3c.dom.Element;

/**
 * Holds the plugin-owned payload as an opaque DOM subtree. The envelope
 * marshaller passes through whatever the plugin codec writes in
 * {@link freemind.diagram.persistence.DiagramPayloadCodec#writePayload}.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class PayloadXml {

    @XmlAnyElement
    public Element root;
}
