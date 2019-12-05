
package freemind.controller.actions.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="place">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="xml_action">
 *         &lt;xs:attribute type="xs:string" use="optional" name="place_id"/>
 *         &lt;xs:attribute type="xs:string" use="optional" name="osm_type"/>
 *         &lt;xs:attribute type="xs:string" use="optional" name="osm_id"/>
 *         &lt;xs:attribute type="xs:string" use="optional" name="place_rank"/>
 *         &lt;xs:attribute type="xs:string" use="optional" name="boundingbox"/>
 *         &lt;xs:attribute type="xs:double" use="optional" name="lat"/>
 *         &lt;xs:attribute type="xs:double" use="optional" name="lon"/>
 *         &lt;xs:attribute type="xs:string" use="optional" name="display_name"/>
 *         &lt;xs:attribute type="xs:string" use="optional" name="class" id="ClassMember"/>
 *         &lt;xs:attribute type="xs:string" use="optional" name="type"/>
 *         &lt;xs:attribute type="xs:string" use="optional" name="icon"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class Place extends XmlAction
{
    private String placeId;
    private String osmType;
    private String osmId;
    private String placeRank;
    private String boundingbox;
    private Double lat;
    private Double lon;
    private String displayName;
    private String _Class;
    private String type;
    private String icon;

    /** 
     * Get the 'place_id' attribute value.
     * 
     * @return value
     */
    public String getPlaceId() {
        return placeId;
    }

    /** 
     * Set the 'place_id' attribute value.
     * 
     * @param placeId
     */
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    /** 
     * Get the 'osm_type' attribute value.
     * 
     * @return value
     */
    public String getOsmType() {
        return osmType;
    }

    /** 
     * Set the 'osm_type' attribute value.
     * 
     * @param osmType
     */
    public void setOsmType(String osmType) {
        this.osmType = osmType;
    }

    /** 
     * Get the 'osm_id' attribute value.
     * 
     * @return value
     */
    public String getOsmId() {
        return osmId;
    }

    /** 
     * Set the 'osm_id' attribute value.
     * 
     * @param osmId
     */
    public void setOsmId(String osmId) {
        this.osmId = osmId;
    }

    /** 
     * Get the 'place_rank' attribute value.
     * 
     * @return value
     */
    public String getPlaceRank() {
        return placeRank;
    }

    /** 
     * Set the 'place_rank' attribute value.
     * 
     * @param placeRank
     */
    public void setPlaceRank(String placeRank) {
        this.placeRank = placeRank;
    }

    /** 
     * Get the 'boundingbox' attribute value.
     * 
     * @return value
     */
    public String getBoundingbox() {
        return boundingbox;
    }

    /** 
     * Set the 'boundingbox' attribute value.
     * 
     * @param boundingbox
     */
    public void setBoundingbox(String boundingbox) {
        this.boundingbox = boundingbox;
    }

    /** 
     * Get the 'lat' attribute value.
     * 
     * @return value
     */
    public Double getLat() {
        return lat;
    }

    /** 
     * Set the 'lat' attribute value.
     * 
     * @param lat
     */
    public void setLat(Double lat) {
        this.lat = lat;
    }

    /** 
     * Get the 'lon' attribute value.
     * 
     * @return value
     */
    public Double getLon() {
        return lon;
    }

    /** 
     * Set the 'lon' attribute value.
     * 
     * @param lon
     */
    public void setLon(Double lon) {
        this.lon = lon;
    }

    /** 
     * Get the 'display_name' attribute value.
     * 
     * @return value
     */
    public String getDisplayName() {
        return displayName;
    }

    /** 
     * Set the 'display_name' attribute value.
     * 
     * @param displayName
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /** 
     * Get the 'class' attribute value.
     * 
     * @return value
     */
    public String get_Class() {
        return _Class;
    }

    /** 
     * Set the 'class' attribute value.
     * 
     * @param _class
     */
    public void set_Class(String _class) {
        _Class = _class;
    }

    /** 
     * Get the 'type' attribute value.
     * 
     * @return value
     */
    public String getType() {
        return type;
    }

    /** 
     * Set the 'type' attribute value.
     * 
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /** 
     * Get the 'icon' attribute value.
     * 
     * @return value
     */
    public String getIcon() {
        return icon;
    }

    /** 
     * Set the 'icon' attribute value.
     * 
     * @param icon
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }
}
