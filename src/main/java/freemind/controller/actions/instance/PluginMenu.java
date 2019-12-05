
package freemind.controller.actions.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="plugin_menu">
 *   &lt;xs:complexType>
 *     &lt;xs:attribute type="xs:string" use="required" name="location"/>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class PluginMenu
{
    private String location;

    /** 
     * Get the 'location' attribute value.
     * 
     * @return value
     */
    public String getLocation() {
        return location;
    }

    /** 
     * Set the 'location' attribute value.
     * 
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }
}
