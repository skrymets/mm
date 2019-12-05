
package freemind.controller.actions.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="collaboration_publish_new_map">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="collaboration_action_base">
 *         &lt;xs:attribute type="xs:string" use="required" name="user_id"/>
 *         &lt;xs:attribute type="xs:string" use="required" name="password"/>
 *         &lt;xs:attribute type="xs:string" use="required" name="map"/>
 *         &lt;xs:attribute type="xs:string" use="required" name="map_name"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class CollaborationPublishNewMap extends CollaborationActionBase
{
    private String userId;
    private String password;
    private String map;
    private String mapName;

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

    /** 
     * Get the 'password' attribute value.
     * 
     * @return value
     */
    public String getPassword() {
        return password;
    }

    /** 
     * Set the 'password' attribute value.
     * 
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /** 
     * Get the 'map' attribute value.
     * 
     * @return value
     */
    public String getMap() {
        return map;
    }

    /** 
     * Set the 'map' attribute value.
     * 
     * @param map
     */
    public void setMap(String map) {
        this.map = map;
    }

    /** 
     * Get the 'map_name' attribute value.
     * 
     * @return value
     */
    public String getMapName() {
        return mapName;
    }

    /** 
     * Set the 'map_name' attribute value.
     * 
     * @param mapName
     */
    public void setMapName(String mapName) {
        this.mapName = mapName;
    }
}
