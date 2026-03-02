
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="collaboration_get_offers">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="collaboration_action_base">
 *         &lt;xs:attribute type="xs:string" use="required" name="user_id"/>
 *         &lt;xs:attribute type="xs:string" use="required" name="password"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "collaboration_get_offers")
public class CollaborationGetOffers extends CollaborationActionBase
{
    @XmlAttribute(name = "user_id")
    private String userId;
    @XmlAttribute(name = "password")
    private String password;

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
}
