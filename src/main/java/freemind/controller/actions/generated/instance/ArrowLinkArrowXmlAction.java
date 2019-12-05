
package freemind.controller.actions.generated.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="arrow_link_arrow_xml_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="xml_action">
 *         &lt;xs:attribute type="xs:string" use="required" name="id"/>
 *         &lt;xs:attribute type="xs:string" use="optional" name="end_arrow"/>
 *         &lt;xs:attribute type="xs:string" use="optional" name="start_arrow"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class ArrowLinkArrowXmlAction extends XmlAction
{
    private String id;
    private String endArrow;
    private String startArrow;

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
     * Get the 'end_arrow' attribute value.
     * 
     * @return value
     */
    public String getEndArrow() {
        return endArrow;
    }

    /** 
     * Set the 'end_arrow' attribute value.
     * 
     * @param endArrow
     */
    public void setEndArrow(String endArrow) {
        this.endArrow = endArrow;
    }

    /** 
     * Get the 'start_arrow' attribute value.
     * 
     * @return value
     */
    public String getStartArrow() {
        return startArrow;
    }

    /** 
     * Set the 'start_arrow' attribute value.
     * 
     * @param startArrow
     */
    public void setStartArrow(String startArrow) {
        this.startArrow = startArrow;
    }
}
