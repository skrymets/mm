
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="cloud">
 *   &lt;xs:complexType>
 *     &lt;xs:attribute type="xs:string" use="optional" name="COLOR"/>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "cloud")
public class Cloud
{
    @XmlAttribute(name = "COLOR")
    private String COLOR;

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
     *
     * @param COLOR
     */
    public void setCOLOR(String COLOR) {
        this.COLOR = COLOR;
    }
}
