
package freemind.controller.actions.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="edge_width_format_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="format_node_action">
 *         &lt;xs:attribute type="xs:int" use="optional" name="width"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class EdgeWidthFormatAction extends FormatNodeAction
{
    private Integer width;

    /** 
     * Get the 'width' attribute value.
     * 
     * @return value
     */
    public Integer getWidth() {
        return width;
    }

    /** 
     * Set the 'width' attribute value.
     * 
     * @param width
     */
    public void setWidth(Integer width) {
        this.width = width;
    }
}
