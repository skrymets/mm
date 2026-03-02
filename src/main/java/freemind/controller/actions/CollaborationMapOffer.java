
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="collaboration_map_offer">
 *   &lt;xs:complexType>
 *     &lt;xs:attribute type="xs:string" use="required" name="map"/>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "collaboration_map_offer")
public class CollaborationMapOffer
{
    @XmlAttribute(name = "map")
    private String map;

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
}
