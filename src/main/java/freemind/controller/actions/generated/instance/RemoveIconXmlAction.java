
package freemind.controller.actions.generated.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="remove_icon_xml_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="node_action">
 *         &lt;xs:attribute type="xs:int" use="required" name="icon_position"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class RemoveIconXmlAction extends NodeAction
{
    private int iconPosition;

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
