
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="edge">
 *   &lt;xs:complexType>
 *     &lt;xs:attribute type="xs:string" use="optional" name="COLOR"/>
 *     &lt;xs:attribute type="xs:string" use="optional" name="STYLE"/>
 *     &lt;xs:attribute type="xs:string" use="optional" name="WIDTH"/>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "edge")
public class Edge
{
    @XmlAttribute(name = "COLOR")
    private String COLOR;
    @XmlAttribute(name = "STYLE")
    private String STYLE;
    @XmlAttribute(name = "WIDTH")
    private String WIDTH;

    /**
     * Get the 'COLOR' attribute value.
     *
     * @return value
     */
    public String getCOLOR() {
        return COLOR;
    }

    /**
     * Set the 'COLOR' attribute value.
     */
    public void setCOLOR(String COLOR) {
        this.COLOR = COLOR;
    }

    /**
     * Get the 'STYLE' attribute value.
     *
     * @return value
     */
    public String getSTYLE() {
        return STYLE;
    }

    /**
     * Set the 'STYLE' attribute value.
     */
    public void setSTYLE(String STYLE) {
        this.STYLE = STYLE;
    }

    /**
     * Get the 'WIDTH' attribute value.
     *
     * @return value
     */
    public String getWIDTH() {
        return WIDTH;
    }

    /**
     * Set the 'WIDTH' attribute value.
     */
    public void setWIDTH(String WIDTH) {
        this.WIDTH = WIDTH;
    }
}
