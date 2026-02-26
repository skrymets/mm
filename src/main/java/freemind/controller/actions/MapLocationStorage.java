
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="map_location_storage">
 *   &lt;xs:complexType>
 *     &lt;xs:attribute type="xs:double" use="optional" name="cursor_longitude"/>
 *     &lt;xs:attribute type="xs:double" use="optional" name="cursor_latitude"/>
 *     &lt;xs:attribute type="xs:int" use="optional" name="zoom"/>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "map_location_storage")
public class MapLocationStorage
{
    @XmlAttribute(name = "cursor_longitude")
    private Double cursorLongitude;
    @XmlAttribute(name = "cursor_latitude")
    private Double cursorLatitude;
    @XmlAttribute(name = "zoom")
    private Integer zoom;

    /**
     * Get the 'cursor_longitude' attribute value.
     *
     * @return value
     */
    public Double getCursorLongitude() {
        return cursorLongitude;
    }

    /**
     * Set the 'cursor_longitude' attribute value.
     *
     * @param cursorLongitude
     */
    public void setCursorLongitude(Double cursorLongitude) {
        this.cursorLongitude = cursorLongitude;
    }

    /**
     * Get the 'cursor_latitude' attribute value.
     *
     * @return value
     */
    public Double getCursorLatitude() {
        return cursorLatitude;
    }

    /**
     * Set the 'cursor_latitude' attribute value.
     *
     * @param cursorLatitude
     */
    public void setCursorLatitude(Double cursorLatitude) {
        this.cursorLatitude = cursorLatitude;
    }

    /**
     * Get the 'zoom' attribute value.
     *
     * @return value
     */
    public Integer getZoom() {
        return zoom;
    }

    /**
     * Set the 'zoom' attribute value.
     *
     * @param zoom
     */
    public void setZoom(Integer zoom) {
        this.zoom = zoom;
    }
}
