
package freemind.controller.actions.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="paste_node_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="node_action">
 *         &lt;xs:sequence>
 *           &lt;xs:element ref="transferable_content" minOccurs="1" maxOccurs="1"/>
 *         &lt;/xs:sequence>
 *         &lt;xs:attribute type="xs:boolean" use="required" name="isLeft"/>
 *         &lt;xs:attribute type="xs:boolean" use="required" name="asSibling"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class PasteNodeAction extends NodeAction
{
    private TransferableContent transferableContent;
    private boolean isLeft;
    private boolean asSibling;

    /** 
     * Get the 'transferable_content' element value.
     * 
     * @return value
     */
    public TransferableContent getTransferableContent() {
        return transferableContent;
    }

    /** 
     * Set the 'transferable_content' element value.
     * 
     * @param transferableContent
     */
    public void setTransferableContent(TransferableContent transferableContent) {
        this.transferableContent = transferableContent;
    }

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
}
