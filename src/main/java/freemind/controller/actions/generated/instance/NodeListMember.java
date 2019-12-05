
package freemind.controller.actions.generated.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="node_list_member">
 *   &lt;xs:complexType>
 *     &lt;xs:attribute type="xs:string" use="required" name="node"/>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class NodeListMember
{
    private String node;

    /** 
     * Get the 'node' attribute value.
     * 
     * @return value
     */
    public String getNode() {
        return node;
    }

    /** 
     * Set the 'node' attribute value.
     * 
     * @param node
     */
    public void setNode(String node) {
        this.node = node;
    }
}
