
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="place_node_xml_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="node_action">
 *         &lt;xs:attribute type="xs:double" use="optional" name="map_center_longitude"/>
 *         &lt;xs:attribute type="xs:double" use="optional" name="map_center_latitude"/>
 *         &lt;xs:attribute type="xs:double" use="optional" name="cursor_longitude"/>
 *         &lt;xs:attribute type="xs:double" use="optional" name="cursor_latitude"/>
 *         &lt;xs:attribute type="xs:int" use="optional" name="zoom"/>
 *         &lt;xs:attribute type="xs:string" use="optional" name="tile_source"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "place_node_xml_action")
public class PlaceNodeXmlAction extends NodeAction
{
    @XmlAttribute(name = "map_center_longitude")
    private Double mapCenterLongitude;
    @XmlAttribute(name = "map_center_latitude")
    private Double mapCenterLatitude;
    @XmlAttribute(name = "cursor_longitude")
    private Double cursorLongitude;
    @XmlAttribute(name = "cursor_latitude")
    private Double cursorLatitude;
    @XmlAttribute(name = "zoom")
    private Integer zoom;
    @XmlAttribute(name = "tile_source")
    private String tileSource;

    /**
     * Get the 'map_center_longitude' attribute value.
     *
     * @return value
     */
    public Double getMapCenterLongitude() {
        return mapCenterLongitude;
    }

    /**
     * Set the 'map_center_longitude' attribute value.
     *
     * @param mapCenterLongitude
     */
    public void setMapCenterLongitude(Double mapCenterLongitude) {
        this.mapCenterLongitude = mapCenterLongitude;
    }

    /**
     * Get the 'map_center_latitude' attribute value.
     *
     * @return value
     */
    public Double getMapCenterLatitude() {
        return mapCenterLatitude;
    }

    /**
     * Set the 'map_center_latitude' attribute value.
     *
     * @param mapCenterLatitude
     */
    public void setMapCenterLatitude(Double mapCenterLatitude) {
        this.mapCenterLatitude = mapCenterLatitude;
    }

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

    /**
     * Get the 'tile_source' attribute value.
     *
     * @return value
     */
    public String getTileSource() {
        return tileSource;
    }

    /**
     * Set the 'tile_source' attribute value.
     *
     * @param tileSource
     */
    public void setTileSource(String tileSource) {
        this.tileSource = tileSource;
    }
}
