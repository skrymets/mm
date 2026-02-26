
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="add_icon_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="node_action">
 *         &lt;xs:attribute type="xs:string" use="required" name="icon_name"/>
 *         &lt;xs:attribute type="xs:int" use="required" name="icon_position"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "add_icon_action")
public class AddIconAction extends NodeAction
{
    @XmlAttribute(name = "icon_name")
    private String iconName;
    @XmlAttribute(name = "icon_position")
    private int iconPosition;

    /**
     * Get the 'icon_name' attribute value.
     *
     * @return value
     */
    public String getIconName() {
        return iconName;
    }

    /**
     * Set the 'icon_name' attribute value.
     *
     * @param iconName
     */
    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    /**
     * Get the 'icon_position' attribute value.
     *
     * @return value
     */
    public int getIconPosition() {
        return iconPosition;
    }

    /**
     * Set the 'icon_position' attribute value.
     *
     * @param iconPosition
     */
    public void setIconPosition(int iconPosition) {
        this.iconPosition = iconPosition;
    }
}
