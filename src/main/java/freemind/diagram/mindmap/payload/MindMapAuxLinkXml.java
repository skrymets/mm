package freemind.diagram.mindmap.payload;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class MindMapAuxLinkXml {

    @XmlAttribute(name = "source", required = true) public String source;
    @XmlAttribute(name = "target", required = true) public String target;
    @XmlAttribute(name = "label")                    public String label;
}
