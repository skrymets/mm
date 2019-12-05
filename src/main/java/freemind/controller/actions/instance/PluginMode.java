
package freemind.controller.actions.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="plugin_mode">
 *   &lt;xs:complexType>
 *     &lt;xs:attribute type="xs:string" use="required" name="class_name"/>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class PluginMode
{
    private String className;

    /** 
     * Get the 'class_name' attribute value.
     * 
     * @return value
     */
    public String getClassName() {
        return className;
    }

    /** 
     * Set the 'class_name' attribute value.
     * 
     * @param className
     */
    public void setClassName(String className) {
        this.className = className;
    }
}
