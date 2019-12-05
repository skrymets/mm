
package freemind.controller.actions.instance;

import java.util.ArrayList;
import java.util.List;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="searchresults">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="xml_action">
 *         &lt;xs:sequence>
 *           &lt;xs:element ref="place" minOccurs="0" maxOccurs="unbounded"/>
 *         &lt;/xs:sequence>
 *         &lt;xs:attribute type="xs:string" use="optional" name="timestamp"/>
 *         &lt;xs:attribute type="xs:string" use="optional" name="attribution"/>
 *         &lt;xs:attribute type="xs:string" use="optional" name="querystring"/>
 *         &lt;xs:attribute type="xs:string" use="optional" name="polygon"/>
 *         &lt;xs:attribute type="xs:string" use="optional" name="exclude_place_ids"/>
 *         &lt;xs:attribute type="xs:string" use="optional" name="more_url"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class Searchresults extends XmlAction
{
    private List<Place> placeList = new ArrayList<Place>();
    private String timestamp;
    private String attribution;
    private String querystring;
    private String polygon;
    private String excludePlaceIds;
    private String moreUrl;

    /** 
     * Get the list of 'place' element items.
     * 
     * @return list
     */
    public List<Place> getPlaceList() {
        return placeList;
    }

    /** 
     * Set the list of 'place' element items.
     * 
     * @param list
     */
    public void setPlaceList(List<Place> list) {
        placeList = list;
    }

    /** 
     * Get the number of 'place' element items.
     * @return count
     */
    public int sizePlaceList() {
        return placeList.size();
    }

    /** 
     * Add a 'place' element item.
     * @param item
     */
    public void addPlace(Place item) {
        placeList.add(item);
    }

    /** 
     * Get 'place' element item by position.
     * @return item
     * @param index
     */
    public Place getPlace(int index) {
        return placeList.get(index);
    }

    /** 
     * Remove all 'place' element items.
     */
    public void clearPlaceList() {
        placeList.clear();
    }

    /** 
     * Get the 'timestamp' attribute value.
     * 
     * @return value
     */
    public String getTimestamp() {
        return timestamp;
    }

    /** 
     * Set the 'timestamp' attribute value.
     * 
     * @param timestamp
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /** 
     * Get the 'attribution' attribute value.
     * 
     * @return value
     */
    public String getAttribution() {
        return attribution;
    }

    /** 
     * Set the 'attribution' attribute value.
     * 
     * @param attribution
     */
    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

    /** 
     * Get the 'querystring' attribute value.
     * 
     * @return value
     */
    public String getQuerystring() {
        return querystring;
    }

    /** 
     * Set the 'querystring' attribute value.
     * 
     * @param querystring
     */
    public void setQuerystring(String querystring) {
        this.querystring = querystring;
    }

    /** 
     * Get the 'polygon' attribute value.
     * 
     * @return value
     */
    public String getPolygon() {
        return polygon;
    }

    /** 
     * Set the 'polygon' attribute value.
     * 
     * @param polygon
     */
    public void setPolygon(String polygon) {
        this.polygon = polygon;
    }

    /** 
     * Get the 'exclude_place_ids' attribute value.
     * 
     * @return value
     */
    public String getExcludePlaceIds() {
        return excludePlaceIds;
    }

    /** 
     * Set the 'exclude_place_ids' attribute value.
     * 
     * @param excludePlaceIds
     */
    public void setExcludePlaceIds(String excludePlaceIds) {
        this.excludePlaceIds = excludePlaceIds;
    }

    /** 
     * Get the 'more_url' attribute value.
     * 
     * @return value
     */
    public String getMoreUrl() {
        return moreUrl;
    }

    /** 
     * Set the 'more_url' attribute value.
     * 
     * @param moreUrl
     */
    public void setMoreUrl(String moreUrl) {
        this.moreUrl = moreUrl;
    }
}
