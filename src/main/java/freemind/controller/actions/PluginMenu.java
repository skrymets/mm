
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="plugin_menu">
 *   &lt;xs:complexType>
 *     &lt;xs:attribute type="xs:string" use="required" name="location"/>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "plugin_menu")
public class PluginMenu
{
    @XmlAttribute(name = "location")
    private String location;

    /**
     * Get the 'location' attribute value.
     *
     * @return value
     */
    public String getLocation() {
        return location;
    }

    /**
     * Set the 'location' attribute value.
     */
    public void setLocation(String location) {
        this.location = location;
    }
}
