
package freemind.controller.actions.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="text_node_action">
 *   &lt;xs:complexContent>
 *     &lt;xs:extension base="node_action">
 *       &lt;xs:sequence>
 *         &lt;xs:element type="xs:string" name="text" minOccurs="0" maxOccurs="1"/>
 *       &lt;/xs:sequence>
 *     &lt;/xs:extension>
 *   &lt;/xs:complexContent>
 * &lt;/xs:complexType>
 * </pre>
 */
public class TextNodeAction extends NodeAction
{
    private String text;

    /** 
     * Get the 'text' element value.
     * 
     * @return value
     */
    public String getText() {
        return text;
    }

    /** 
     * Set the 'text' element value.
     * 
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }
}
