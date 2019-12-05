
package freemind.controller.actions.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="bold_node_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="format_node_action">
 *         &lt;xs:attribute type="xs:boolean" use="required" name="bold"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class BoldNodeAction extends FormatNodeAction
{
    private boolean bold;

    /** 
     * Get the 'bold' attribute value.
     * 
     * @return value
     */
    public boolean isBold() {
        return bold;
    }

    /** 
     * Set the 'bold' attribute value.
     * 
     * @param bold
     */
    public void setBold(boolean bold) {
        this.bold = bold;
    }
}
