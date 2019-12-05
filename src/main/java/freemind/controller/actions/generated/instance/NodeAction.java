
package freemind.controller.actions.generated.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="node_action">
 *   &lt;xs:complexContent>
 *     &lt;xs:extension base="xml_action">
 *       &lt;xs:attribute type="xs:string" use="required" name="node"/>
 *     &lt;/xs:extension>
 *   &lt;/xs:complexContent>
 * &lt;/xs:complexType>
 * </pre>
 */
public class NodeAction extends XmlAction
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
