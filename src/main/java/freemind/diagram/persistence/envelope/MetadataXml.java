package freemind.diagram.persistence.envelope;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class MetadataXml {

    @XmlElement(name = "documentId", required = true)
    public String documentId;

    @XmlElement(name = "title")
    public String title;

    @XmlElement(name = "author")
    public String author;

    @XmlElement(name = "createdAt", required = true)
    public String createdAt;       // ISO-8601 instant

    @XmlElement(name = "modifiedAt", required = true)
    public String modifiedAt;      // ISO-8601 instant
}
