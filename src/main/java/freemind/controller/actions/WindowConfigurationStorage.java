
package freemind.controller.actions;

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
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class WindowConfigurationStorage extends XmlAction
{
    @XmlAttribute(name = "x")
    private int X;
    @XmlAttribute(name = "y")
    private int Y;
    @XmlAttribute(name = "width")
    private int width;
    @XmlAttribute(name = "height")
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
     */
    public void setHeight(int height) {
        this.height = height;
    }
}
