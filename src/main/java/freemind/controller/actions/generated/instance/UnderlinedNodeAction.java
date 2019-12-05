
package freemind.controller.actions.generated.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="underlined_node_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="format_node_action">
 *         &lt;xs:attribute type="xs:boolean" use="required" name="underlined"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class UnderlinedNodeAction extends FormatNodeAction
{
    private boolean underlined;

    /** 
     * Get the 'underlined' attribute value.
     * 
     * @return value
     */
    public boolean isUnderlined() {
        return underlined;
    }

    /** 
     * Set the 'underlined' attribute value.
     * 
     * @param underlined
     */
    public void setUnderlined(boolean underlined) {
        this.underlined = underlined;
    }
}
