package freemind.diagram.persistence.envelope;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class ColorEntryXml {

    @XmlAttribute(name = "id", required = true)
    public String id;

    @XmlAttribute(name = "value", required = true)
    public String value;
}
