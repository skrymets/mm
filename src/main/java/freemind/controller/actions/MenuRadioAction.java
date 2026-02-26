
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="menu_radio_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="menu_action_base">
 *         &lt;xs:attribute type="xs:boolean" use="optional" default="false" name="selected"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "menu_radio_action")
public class MenuRadioAction extends MenuActionBase
{
    @XmlAttribute(name = "selected")
    private Boolean selected;

    /**
     * Get the 'selected' attribute value.
     *
     * @return value
     */
    public Boolean getSelected() {
        return selected;
    }

    /**
     * Set the 'selected' attribute value.
     *
     * @param selected
     */
    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
