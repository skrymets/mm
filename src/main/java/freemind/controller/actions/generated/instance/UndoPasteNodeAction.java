
package freemind.controller.actions.generated.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="undo_paste_node_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="node_action">
 *         &lt;xs:attribute type="xs:boolean" use="required" name="isLeft"/>
 *         &lt;xs:attribute type="xs:boolean" use="required" name="asSibling"/>
 *         &lt;xs:attribute type="xs:int" use="required" name="node_amount"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class UndoPasteNodeAction extends NodeAction
{
    private boolean isLeft;
    private boolean asSibling;
    private int nodeAmount;

    /** 
     * Get the 'isLeft' attribute value.
     * 
     * @return value
     */
    public boolean isIsLeft() {
        return isLeft;
    }

    /** 
     * Set the 'isLeft' attribute value.
     * 
     * @param isLeft
     */
    public void setIsLeft(boolean isLeft) {
        this.isLeft = isLeft;
    }

    /** 
     * Get the 'asSibling' attribute value.
     * 
     * @return value
     */
    public boolean isAsSibling() {
        return asSibling;
    }

    /** 
     * Set the 'asSibling' attribute value.
     * 
     * @param asSibling
     */
    public void setAsSibling(boolean asSibling) {
        this.asSibling = asSibling;
    }

    /** 
     * Get the 'node_amount' attribute value.
     * 
     * @return value
     */
    public int getNodeAmount() {
        return nodeAmount;
    }

    /** 
     * Set the 'node_amount' attribute value.
     * 
     * @param nodeAmount
     */
    public void setNodeAmount(int nodeAmount) {
        this.nodeAmount = nodeAmount;
    }
}
