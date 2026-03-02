
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="underlined_node_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="format_node_action">
 *         &lt;xs:attribute type="xs:boolean" use="required" name="underlined"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "underlined_node_action")
public class UnderlinedNodeAction extends FormatNodeAction
{
    @XmlAttribute(name = "underlined")
    private boolean underlined;

    /**
     * Get the 'underlined' attribute value.
     *
     * @return value
     */
    public boolean isUnderlined() {
        return underlined;
    }

    /**
     * Set the 'underlined' attribute value.
     */
    public void setUnderlined(boolean underlined) {
        this.underlined = underlined;
    }
}
