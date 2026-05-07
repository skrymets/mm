package freemind.diagram.mindmap.payload;

import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class MindMapNodeXml {

    @XmlAttribute(name = "nodeId", required = true)
    public String nodeId;

    @XmlElement(name = "content", required = true)
    public ContentXml content;

    @XmlElement(name = "style")
    public StyleRefXml style;

    @XmlElement(name = "attributes")
    public AttributesXml attributes;

    @XmlElement(name = "node")
    public List<MindMapNodeXml> children = new ArrayList<>();

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class ContentXml {
        @XmlAttribute(name = "format", required = true)
        public String format;            // PLAIN | HTML | MARKDOWN
        @XmlValue
        public String text;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class AttributesXml {
        @XmlElement(name = "a")
        public List<AttributeXml> entries = new ArrayList<>();
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class AttributeXml {
        @XmlAttribute(name = "k", required = true) public String key;
        @XmlAttribute(name = "v", required = true) public String value;
    }
}
