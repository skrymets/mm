
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="collaboration_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="xml_action">
 *         &lt;xs:attribute type="xs:string" use="optional" name="user"/>
 *         &lt;xs:attribute type="xs:string" use="optional" name="timestamp"/>
 *         &lt;xs:attribute use="required" name="cmd">
 *           &lt;xs:simpleType>
 *             &lt;!-- Reference to inner class Cmd -->
 *           &lt;/xs:simpleType>
 *         &lt;/xs:attribute>
 *         &lt;xs:attribute type="xs:string" use="optional" name="map"/>
 *         &lt;xs:attribute type="xs:string" use="optional" name="filename"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "collaboration_action")
public class CollaborationAction extends XmlAction
{
    @XmlAttribute(name = "user")
    private String user;
    @XmlAttribute(name = "timestamp")
    private String timestamp;
    @XmlAttribute(name = "cmd")
    private Cmd cmd;
    @XmlAttribute(name = "map")
    private String map;
    @XmlAttribute(name = "filename")
    private String filename;

    /**
     * Get the 'user' attribute value.
     *
     * @return value
     */
    public String getUser() {
        return user;
    }

    /**
     * Set the 'user' attribute value.
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Get the 'timestamp' attribute value.
     *
     * @return value
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Set the 'timestamp' attribute value.
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Get the 'cmd' attribute value.
     *
     * @return value
     */
    public Cmd getCmd() {
        return cmd;
    }

    /**
     * Set the 'cmd' attribute value.
     */
    public void setCmd(Cmd cmd) {
        this.cmd = cmd;
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
     * Get the 'filename' attribute value.
     *
     * @return value
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Set the 'filename' attribute value.
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }
    /**
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:simpleType xmlns:xs="http://www.w3.org/2001/XMLSchema">
     *   &lt;xs:restriction base="xs:string">
     *     &lt;xs:enumeration value="request_map_sharing"/>
     *     &lt;xs:enumeration value="accept_map_sharing"/>
     *     &lt;xs:enumeration value="stop_map_sharing"/>
     *     &lt;xs:enumeration value="decline_map_sharing"/>
     *   &lt;/xs:restriction>
     * &lt;/xs:simpleType>
     * </pre>
     */
    public static enum Cmd {
        REQUEST_MAP_SHARING("request_map_sharing"), ACCEPT_MAP_SHARING(
                "accept_map_sharing"), STOP_MAP_SHARING("stop_map_sharing"), DECLINE_MAP_SHARING(
                "decline_map_sharing");
        private final String value;

        private Cmd(String value) {
            this.value = value;
        }

        public String xmlValue() {
            return value;
        }

        public static Cmd convert(String value) {
            for (Cmd inst : values()) {
                if (inst.xmlValue().equals(value)) {
                    return inst;
                }
            }
            return null;
        }
    }
}
