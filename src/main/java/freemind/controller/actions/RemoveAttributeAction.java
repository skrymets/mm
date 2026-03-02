
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="remove_attribute_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="node_action">
 *         &lt;xs:attribute type="xs:int" use="required" name="position"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "remove_attribute_action")
public class RemoveAttributeAction extends NodeAction
{
    @XmlAttribute(name = "position")
    private int position;

    /**
     * Get the 'position' attribute value.
     *
     * @return value
     */
    public int getPosition() {
        return position;
    }

    /**
     * Set the 'position' attribute value.
     */
    public void setPosition(int position) {
        this.position = position;
    }
}
