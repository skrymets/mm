package freemind.diagram.persistence.envelope;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class FontEntryXml {

    @XmlAttribute(name = "id", required = true)
    public String id;

    @XmlAttribute(name = "family", required = true)
    public String family;

    @XmlAttribute(name = "size", required = true)
    public int size;

    @XmlAttribute(name = "weight", required = true)
    public String weight;          // "NORMAL" | "BOLD"
}
