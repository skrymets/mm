package freemind.diagram.persistence.envelope;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
public class ResourceEntryXml {

    @XmlAttribute(name = "id", required = true)
    public String id;

    @XmlAttribute(name = "type", required = true)
    public String mimeType;

    /** Either an external URI (when set) ... */
    @XmlAttribute(name = "uri")
    public String externalUri;

    /** ... or an embedded base64 blob (when externalUri is absent). */
    @XmlValue
    public String embeddedBase64;
}
