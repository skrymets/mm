
package freemind.controller.actions.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="font_size_node_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="format_node_action">
 *         &lt;xs:attribute type="xs:string" use="required" name="size"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class FontSizeNodeAction extends FormatNodeAction
{
    private String size;

    /** 
     * Get the 'size' attribute value.
     * 
     * @return value
     */
    public String getSize() {
        return size;
    }

    /** 
     * Set the 'size' attribute value.
     * 
     * @param size
     */
    public void setSize(String size) {
        this.size = size;
    }
}
