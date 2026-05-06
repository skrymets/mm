package freemind.diagram.persistence.envelope;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class DiagramSlotXml {

    @XmlAttribute(name = "type", required = true)
    public String type;            // DiagramTypeId.value()

    @XmlAttribute(name = "payloadVersion", required = true)
    public int payloadVersion;

    @XmlElement(name = "payload", required = true)
    public PayloadXml payload;
}
