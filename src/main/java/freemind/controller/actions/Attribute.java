
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="attribute">
 *   &lt;xs:complexType>
 *     &lt;xs:attribute type="xs:string" use="required" name="NAME"/>
 *     &lt;xs:attribute type="xs:string" use="required" name="VALUE"/>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "attribute")
public class Attribute
{
    @XmlAttribute(name = "NAME")
    private String NAME;
    @XmlAttribute(name = "VALUE")
    private String VALUE;

    /**
     * Get the 'NAME' attribute value.
     *
     * @return value
     */
    public String getNAME() {
        return NAME;
    }

    /**
     * Set the 'NAME' attribute value.
     */
    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    /**
     * Get the 'VALUE' attribute value.
     *
     * @return value
     */
    public String getVALUE() {
        return VALUE;
    }

    /**
     * Set the 'VALUE' attribute value.
     */
    public void setVALUE(String VALUE) {
        this.VALUE = VALUE;
    }
}
