
package freemind.controller.actions.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="add_cloud_xml_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="node_action">
 *         &lt;xs:attribute type="xs:boolean" use="required" name="enabled"/>
 *         &lt;xs:attribute type="xs:string" use="optional" name="color"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class AddCloudXmlAction extends NodeAction
{
    private boolean enabled;
    private String color;

    /** 
     * Get the 'enabled' attribute value.
     * 
     * @return value
     */
    public boolean isEnabled() {
        return enabled;
    }

    /** 
     * Set the 'enabled' attribute value.
     * 
     * @param enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /** 
     * Get the 'color' attribute value.
     * 
     * @return value
     */
    public String getColor() {
        return color;
    }

    /** 
     * Set the 'color' attribute value.
     * 
     * @param color
     */
    public void setColor(String color) {
        this.color = color;
    }
}
