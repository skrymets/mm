
package freemind.controller.actions.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="font_node_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="format_node_action">
 *         &lt;xs:attribute type="xs:string" use="required" name="font"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class FontNodeAction extends FormatNodeAction
{
    private String font;

    /** 
     * Get the 'font' attribute value.
     * 
     * @return value
     */
    public String getFont() {
        return font;
    }

    /** 
     * Set the 'font' attribute value.
     * 
     * @param font
     */
    public void setFont(String font) {
        this.font = font;
    }
}
