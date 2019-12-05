
package freemind.controller.actions.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="window_configuration_storage">
 *   &lt;xs:complexContent>
 *     &lt;xs:extension base="xml_action">
 *       &lt;xs:attribute type="xs:int" use="required" name="x"/>
 *       &lt;xs:attribute type="xs:int" use="required" name="y"/>
 *       &lt;xs:attribute type="xs:int" use="required" name="width"/>
 *       &lt;xs:attribute type="xs:int" use="required" name="height"/>
 *     &lt;/xs:extension>
 *   &lt;/xs:complexContent>
 * &lt;/xs:complexType>
 * </pre>
 */
public class WindowConfigurationStorage extends XmlAction
{
    private int X;
    private int Y;
    private int width;
    private int height;

    /** 
     * Get the 'x' attribute value.
     * 
     * @return value
     */
    public int getX() {
        return X;
    }

    /** 
     * Set the 'x' attribute value.
     * 
     * @param x
     */
    public void setX(int x) {
        X = x;
    }

    /** 
     * Get the 'y' attribute value.
     * 
     * @return value
     */
    public int getY() {
        return Y;
    }

    /** 
     * Set the 'y' attribute value.
     * 
     * @param y
     */
    public void setY(int y) {
        Y = y;
    }

    /** 
     * Get the 'width' attribute value.
     * 
     * @return value
     */
    public int getWidth() {
        return width;
    }

    /** 
     * Set the 'width' attribute value.
     * 
     * @param width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /** 
     * Get the 'height' attribute value.
     * 
     * @return value
     */
    public int getHeight() {
        return height;
    }

    /** 
     * Set the 'height' attribute value.
     * 
     * @param height
     */
    public void setHeight(int height) {
        this.height = height;
    }
}
