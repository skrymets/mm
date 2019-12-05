
package freemind.controller.actions.generated.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="fold_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="node_action">
 *         &lt;xs:attribute type="xs:boolean" use="required" name="folded"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class FoldAction extends NodeAction
{
    private boolean folded;

    /** 
     * Get the 'folded' attribute value.
     * 
     * @return value
     */
    public boolean isFolded() {
        return folded;
    }

    /** 
     * Set the 'folded' attribute value.
     * 
     * @param folded
     */
    public void setFolded(boolean folded) {
        this.folded = folded;
    }
}
