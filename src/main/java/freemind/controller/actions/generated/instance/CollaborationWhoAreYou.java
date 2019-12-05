
package freemind.controller.actions.generated.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="collaboration_who_are_you">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="collaboration_action_base">
 *         &lt;xs:attribute type="xs:string" use="optional" name="server_version"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class CollaborationWhoAreYou extends CollaborationActionBase
{
    private String serverVersion;

    /** 
     * Get the 'server_version' attribute value.
     * 
     * @return value
     */
    public String getServerVersion() {
        return serverVersion;
    }

    /** 
     * Set the 'server_version' attribute value.
     * 
     * @param serverVersion
     */
    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }
}
