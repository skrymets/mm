
package freemind.controller.actions.generated.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="edge_style_format_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="format_node_action">
 *         &lt;xs:attribute type="xs:string" use="optional" name="style"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class EdgeStyleFormatAction extends FormatNodeAction
{
    private String style;

    /** 
     * Get the 'style' attribute value.
     * 
     * @return value
     */
    public String getStyle() {
        return style;
    }

    /** 
     * Set the 'style' attribute value.
     * 
     * @param style
     */
    public void setStyle(String style) {
        this.style = style;
    }
}
