
package freemind.controller.actions.generated.instance;

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
public class MapLocationStorage
{
    private Double cursorLongitude;
    private Double cursorLatitude;
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
