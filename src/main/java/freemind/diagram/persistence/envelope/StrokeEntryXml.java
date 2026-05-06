package freemind.diagram.persistence.envelope;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class StrokeEntryXml {

    @XmlAttribute(name = "id", required = true)
    public String id;

    @XmlAttribute(name = "width", required = true)
    public double width;

    @XmlAttribute(name = "style", required = true)
    public String style;           // "SOLID" | "DASHED" | "DOTTED"
}
