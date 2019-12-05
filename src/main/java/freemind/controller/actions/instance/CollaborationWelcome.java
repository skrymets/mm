
package freemind.controller.actions.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="collaboration_welcome">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="collaboration_action_base">
 *         &lt;xs:attribute type="xs:string" use="optional" name="map"/>
 *         &lt;xs:attribute type="xs:string" use="optional" name="filename"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class CollaborationWelcome extends CollaborationActionBase
{
    private String map;
    private String filename;

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
     * 
     * @param map
     */
    public void setMap(String map) {
        this.map = map;
    }

    /** 
     * Get the 'filename' attribute value.
     * 
     * @return value
     */
    public String getFilename() {
        return filename;
    }

    /** 
     * Set the 'filename' attribute value.
     * 
     * @param filename
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }
}
