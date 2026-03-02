
package freemind.controller.actions;

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
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "collaboration_publish_new_map")
public class CollaborationPublishNewMap extends CollaborationActionBase
{
    @XmlAttribute(name = "user_id")
    private String userId;
    @XmlAttribute(name = "password")
    private String password;
    @XmlAttribute(name = "map")
    private String map;
    @XmlAttribute(name = "map_name")
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
     */
    public void setMapName(String mapName) {
        this.mapName = mapName;
    }
}
