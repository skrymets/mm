package freemind.diagram.mindmap.payload;

import jakarta.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "mindmap-payload")
@XmlAccessorType(XmlAccessType.FIELD)
public class MindMapPayloadXml {

    @XmlElement(name = "root", required = true)
    public MindMapNodeXml root;

    @XmlElement(name = "auxiliary-link")
    public List<MindMapAuxLinkXml> auxiliaryLinks = new ArrayList<>();
}
