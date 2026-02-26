
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="pattern_property_base">
 *   &lt;xs:attribute type="xs:string" use="optional" name="value"/>
 * &lt;/xs:complexType>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class PatternPropertyBase
{
    @XmlAttribute(name = "value")
    private String value;

    /**
     * Get the 'value' attribute value.
     *
     * @return value
     */
    public String getValue() {
        return value;
    }

    /**
     * Set the 'value' attribute value.
     *
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }
}
