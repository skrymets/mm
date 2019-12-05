
package freemind.controller.actions.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="cloud_color_xml_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="node_action">
 *         &lt;xs:attribute type="xs:string" use="required" name="color"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class CloudColorXmlAction extends NodeAction
{
    private String color;

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
