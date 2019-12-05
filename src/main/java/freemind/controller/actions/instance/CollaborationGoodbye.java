
package freemind.controller.actions.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="collaboration_goodbye">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="collaboration_action_base">
 *         &lt;xs:attribute type="xs:string" use="required" name="user_id"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class CollaborationGoodbye extends CollaborationActionBase
{
    private String userId;

    /** 
     * Get the 'user_id' attribute value.
     * 
     * @return value
     */
    public String getUserId() {
        return userId;
    }

    /** 
     * Set the 'user_id' attribute value.
     * 
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
