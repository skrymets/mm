
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="icon">
 *   &lt;xs:complexType>
 *     &lt;xs:attribute type="xs:string" use="required" name="BUILTIN"/>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "icon")
public class Icon
{
    @XmlAttribute(name = "BUILTIN")
    private String BUILTIN;

    /**
     * Get the 'BUILTIN' attribute value.
     *
     * @return value
     */
    public String getBUILTIN() {
        return BUILTIN;
    }

    /**
     * Set the 'BUILTIN' attribute value.
     *
     * @param BUILTIN
     */
    public void setBUILTIN(String BUILTIN) {
        this.BUILTIN = BUILTIN;
    }
}
