
package freemind.controller.actions;

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
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "collaboration_who_are_you")
public class CollaborationWhoAreYou extends CollaborationActionBase
{
    @XmlAttribute(name = "server_version")
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
     */
    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }
}
