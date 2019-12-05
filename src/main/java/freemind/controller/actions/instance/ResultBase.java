
package freemind.controller.actions.instance;

import java.math.BigInteger;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="result_base">
 *   &lt;xs:attribute type="xs:double" use="required" name="lat"/>
 *   &lt;xs:attribute type="xs:double" use="required" name="lon"/>
 *   &lt;xs:attribute type="xs:integer" use="required" name="osm_id"/>
 *   &lt;xs:attribute type="xs:string" use="required" name="osm_type"/>
 *   &lt;xs:attribute type="xs:integer" use="required" name="place_id"/>
 *   &lt;xs:attribute type="xs:anySimpleType" use="optional" name="ref"/>
 * &lt;/xs:complexType>
 * </pre>
 */
public class ResultBase
{
    private Double lat;
    private Double lon;
    private BigInteger osmId;
    private String osmType;
    private BigInteger placeId;
    private String ref;

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
     * Get the 'osm_id' attribute value.
     * 
     * @return value
     */
    public BigInteger getOsmId() {
        return osmId;
    }

    /** 
     * Set the 'osm_id' attribute value.
     * 
     * @param osmId
     */
    public void setOsmId(BigInteger osmId) {
        this.osmId = osmId;
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
     * Get the 'place_id' attribute value.
     * 
     * @return value
     */
    public BigInteger getPlaceId() {
        return placeId;
    }

    /** 
     * Set the 'place_id' attribute value.
     * 
     * @param placeId
     */
    public void setPlaceId(BigInteger placeId) {
        this.placeId = placeId;
    }

    /** 
     * Get the 'ref' attribute value.
     * 
     * @return value
     */
    public String getRef() {
        return ref;
    }

    /** 
     * Set the 'ref' attribute value.
     * 
     * @param ref
     */
    public void setRef(String ref) {
        this.ref = ref;
    }
}
