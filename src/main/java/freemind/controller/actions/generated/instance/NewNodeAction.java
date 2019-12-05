
package freemind.controller.actions.generated.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="new_node_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="node_action">
 *         &lt;xs:attribute type="xs:string" use="optional" name="position"/>
 *         &lt;xs:attribute type="xs:int" use="optional" name="index"/>
 *         &lt;xs:attribute type="xs:string" use="optional" name="newId"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class NewNodeAction extends NodeAction
{
    private String position;
    private Integer index;
    private String newId;

    /** 
     * Get the 'position' attribute value.
     * 
     * @return value
     */
    public String getPosition() {
        return position;
    }

    /** 
     * Set the 'position' attribute value.
     * 
     * @param position
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /** 
     * Get the 'index' attribute value.
     * 
     * @return value
     */
    public Integer getIndex() {
        return index;
    }

    /** 
     * Set the 'index' attribute value.
     * 
     * @param index
     */
    public void setIndex(Integer index) {
        this.index = index;
    }

    /** 
     * Get the 'newId' attribute value.
     * 
     * @return value
     */
    public String getNewId() {
        return newId;
    }

    /** 
     * Set the 'newId' attribute value.
     * 
     * @param newId
     */
    public void setNewId(String newId) {
        this.newId = newId;
    }
}
