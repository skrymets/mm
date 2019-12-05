
package freemind.controller.actions.generated.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="strikethrough_node_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="format_node_action">
 *         &lt;xs:attribute type="xs:boolean" use="required" name="strikethrough"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class StrikethroughNodeAction extends FormatNodeAction
{
    private boolean strikethrough;

    /** 
     * Get the 'strikethrough' attribute value.
     * 
     * @return value
     */
    public boolean isStrikethrough() {
        return strikethrough;
    }

    /** 
     * Set the 'strikethrough' attribute value.
     * 
     * @param strikethrough
     */
    public void setStrikethrough(boolean strikethrough) {
        this.strikethrough = strikethrough;
    }
}
