
package freemind.controller.actions.generated.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="move_node_xml_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="node_action">
 *         &lt;xs:attribute type="xs:int" use="required" name="v_gap"/>
 *         &lt;xs:attribute type="xs:int" use="required" name="h_gap"/>
 *         &lt;xs:attribute type="xs:int" use="required" name="shift_y"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class MoveNodeXmlAction extends NodeAction
{
    private int VGap;
    private int HGap;
    private int shiftY;

    /** 
     * Get the 'v_gap' attribute value.
     * 
     * @return value
     */
    public int getVGap() {
        return VGap;
    }

    /** 
     * Set the 'v_gap' attribute value.
     * 
     * @param vGap
     */
    public void setVGap(int vGap) {
        VGap = vGap;
    }

    /** 
     * Get the 'h_gap' attribute value.
     * 
     * @return value
     */
    public int getHGap() {
        return HGap;
    }

    /** 
     * Set the 'h_gap' attribute value.
     * 
     * @param hGap
     */
    public void setHGap(int hGap) {
        HGap = hGap;
    }

    /** 
     * Get the 'shift_y' attribute value.
     * 
     * @return value
     */
    public int getShiftY() {
        return shiftY;
    }

    /** 
     * Set the 'shift_y' attribute value.
     * 
     * @param shiftY
     */
    public void setShiftY(int shiftY) {
        this.shiftY = shiftY;
    }
}
