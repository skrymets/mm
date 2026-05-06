package freemind.diagram.persistence.envelope;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "diagram-document")
@XmlAccessorType(XmlAccessType.FIELD)
public class EnvelopeXml {

    @XmlAttribute(name = "formatVersion", required = true)
    public int formatVersion;

    @XmlAttribute(name = "applicationVersion", required = true)
    public String applicationVersion;

    @XmlElement(name = "metadata", required = true)
    public MetadataXml metadata;

    @XmlElement(name = "styles")
    public StylesXml styles;

    @XmlElement(name = "resources")
    public ResourcesXml resources;

    @XmlElement(name = "diagram", required = true)
    public DiagramSlotXml diagram;
}
