
package freemind.controller.actions.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="arrow_link_point_xml_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="xml_action">
 *         &lt;xs:attribute type="xs:string" use="required" name="id"/>
 *         &lt;xs:attribute type="xs:string" use="optional" name="end_point"/>
 *         &lt;xs:attribute type="xs:string" use="optional" name="start_point"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class ArrowLinkPointXmlAction extends XmlAction
{
    private String id;
    private String endPoint;
    private String startPoint;

    /** 
     * Get the 'id' attribute value.
     * 
     * @return value
     */
    public String getId() {
        return id;
    }

    /** 
     * Set the 'id' attribute value.
     * 
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /** 
     * Get the 'end_point' attribute value.
     * 
     * @return value
     */
    public String getEndPoint() {
        return endPoint;
    }

    /** 
     * Set the 'end_point' attribute value.
     * 
     * @param endPoint
     */
    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    /** 
     * Get the 'start_point' attribute value.
     * 
     * @return value
     */
    public String getStartPoint() {
        return startPoint;
    }

    /** 
     * Set the 'start_point' attribute value.
     * 
     * @param startPoint
     */
    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }
}
