
package freemind.controller.actions.instance;

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
public class MenuRadioAction extends MenuActionBase
{
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
