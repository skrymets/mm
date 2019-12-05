
package freemind.controller.actions.generated.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="collaboration_hello">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="collaboration_action_base">
 *         &lt;xs:attribute type="xs:string" use="required" name="map"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class CollaborationHello extends CollaborationActionBase
{
    private String map;

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
}
