
package freemind.controller.actions.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="collaboration_user_information">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="collaboration_action_base">
 *         &lt;xs:attribute type="xs:string" use="required" name="user_ids"/>
 *         &lt;xs:attribute type="xs:string" use="required" name="master_ip"/>
 *         &lt;xs:attribute type="xs:int" use="required" name="master_port"/>
 *         &lt;xs:attribute type="xs:string" use="required" name="master_hostname"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class CollaborationUserInformation extends CollaborationActionBase
{
    private String userIds;
    private String masterIp;
    private int masterPort;
    private String masterHostname;

    /** 
     * Get the 'user_ids' attribute value.
     * 
     * @return value
     */
    public String getUserIds() {
        return userIds;
    }

    /** 
     * Set the 'user_ids' attribute value.
     * 
     * @param userIds
     */
    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }

    /** 
     * Get the 'master_ip' attribute value.
     * 
     * @return value
     */
    public String getMasterIp() {
        return masterIp;
    }

    /** 
     * Set the 'master_ip' attribute value.
     * 
     * @param masterIp
     */
    public void setMasterIp(String masterIp) {
        this.masterIp = masterIp;
    }

    /** 
     * Get the 'master_port' attribute value.
     * 
     * @return value
     */
    public int getMasterPort() {
        return masterPort;
    }

    /** 
     * Set the 'master_port' attribute value.
     * 
     * @param masterPort
     */
    public void setMasterPort(int masterPort) {
        this.masterPort = masterPort;
    }

    /** 
     * Get the 'master_hostname' attribute value.
     * 
     * @return value
     */
    public String getMasterHostname() {
        return masterHostname;
    }

    /** 
     * Set the 'master_hostname' attribute value.
     * 
     * @param masterHostname
     */
    public void setMasterHostname(String masterHostname) {
        this.masterHostname = masterHostname;
    }
}
