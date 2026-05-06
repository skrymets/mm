package freemind.diagram.persistence.envelope;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class StylesXml {

    @XmlElementWrapper(name = "colors")
    @XmlElement(name = "color")
    public List<ColorEntryXml> colors = new ArrayList<>();

    @XmlElementWrapper(name = "fonts")
    @XmlElement(name = "font")
    public List<FontEntryXml> fonts = new ArrayList<>();

    @XmlElementWrapper(name = "strokes")
    @XmlElement(name = "stroke")
    public List<StrokeEntryXml> strokes = new ArrayList<>();
}
