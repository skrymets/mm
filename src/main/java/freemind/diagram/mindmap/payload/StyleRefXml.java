package freemind.diagram.mindmap.payload;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class StyleRefXml {

    @XmlAttribute(name = "colorRef")  public String colorRef;
    @XmlAttribute(name = "fontRef")   public String fontRef;
    @XmlAttribute(name = "strokeRef") public String strokeRef;
}
