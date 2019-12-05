
package freemind.controller.actions.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="remove_attribute_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="node_action">
 *         &lt;xs:attribute type="xs:int" use="required" name="position"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class RemoveAttributeAction extends NodeAction
{
    private int position;

    /** 
     * Get the 'position' attribute value.
     * 
     * @return value
     */
    public int getPosition() {
        return position;
    }

    /** 
     * Set the 'position' attribute value.
     * 
     * @param position
     */
    public void setPosition(int position) {
        this.position = position;
    }
}
