
package freemind.controller.actions.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="collaboration_transaction">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="collaboration_action_base">
 *         &lt;xs:attribute type="xs:string" use="required" name="id"/>
 *         &lt;xs:attribute type="xs:string" use="required" name="do_action"/>
 *         &lt;xs:attribute type="xs:string" use="required" name="undo_action"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class CollaborationTransaction extends CollaborationActionBase
{
    private String id;
    private String doAction;
    private String undoAction;

    /** 
     * Get the 'id' attribute value.
     * 
     * @return value
     */
    public String getId() {
        return id;
    }

    /** 
     * Set the 'id' attribute value.
     * 
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /** 
     * Get the 'do_action' attribute value.
     * 
     * @return value
     */
    public String getDoAction() {
        return doAction;
    }

    /** 
     * Set the 'do_action' attribute value.
     * 
     * @param doAction
     */
    public void setDoAction(String doAction) {
        this.doAction = doAction;
    }

    /** 
     * Get the 'undo_action' attribute value.
     * 
     * @return value
     */
    public String getUndoAction() {
        return undoAction;
    }

    /** 
     * Set the 'undo_action' attribute value.
     * 
     * @param undoAction
     */
    public void setUndoAction(String undoAction) {
        this.undoAction = undoAction;
    }
}
