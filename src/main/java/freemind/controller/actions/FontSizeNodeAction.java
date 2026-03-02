
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="font_size_node_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="format_node_action">
 *         &lt;xs:attribute type="xs:string" use="required" name="size"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "font_size_node_action")
public class FontSizeNodeAction extends FormatNodeAction
{
    @XmlAttribute(name = "size")
    private String size;

    /**
     * Get the 'size' attribute value.
     *
     * @return value
     */
    public String getSize() {
        return size;
    }

    /**
     * Set the 'size' attribute value.
     */
    public void setSize(String size) {
        this.size = size;
    }
}
