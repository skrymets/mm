
package freemind.controller.actions.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="insert_attribute_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="node_action">
 *         &lt;xs:attribute type="xs:int" use="required" name="position"/>
 *         &lt;xs:attribute type="xs:string" use="required" name="name"/>
 *         &lt;xs:attribute type="xs:string" use="required" name="value"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class InsertAttributeAction extends NodeAction
{
    private int position;
    private String name;
    private String value;

    /** 
     * Get the 'position' attribute value.
     * 
     * @return value
     */
    public int getPosition() {
        return position;
    }

    /** 
     * Set the 'position' attribute value.
     * 
     * @param position
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /** 
     * Get the 'name' attribute value.
     * 
     * @return value
     */
    public String getName() {
        return name;
    }

    /** 
     * Set the 'name' attribute value.
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /** 
     * Get the 'value' attribute value.
     * 
     * @return value
     */
    public String getValue() {
        return value;
    }

    /** 
     * Set the 'value' attribute value.
     * 
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }
}
