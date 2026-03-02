
package freemind.controller.actions;

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
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class ResultBase
{
    @XmlAttribute(name = "lat")
    private Double lat;
    @XmlAttribute(name = "lon")
    private Double lon;
    @XmlAttribute(name = "osm_id")
    private BigInteger osmId;
    @XmlAttribute(name = "osm_type")
    private String osmType;
    @XmlAttribute(name = "place_id")
    private BigInteger placeId;
    @XmlAttribute(name = "ref")
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
     */
    public void setRef(String ref) {
        this.ref = ref;
    }
}
