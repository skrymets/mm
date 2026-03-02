
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="node_style_format_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="format_node_action">
 *         &lt;xs:attribute type="xs:string" use="optional" name="style"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "node_style_format_action")
public class NodeStyleFormatAction extends FormatNodeAction
{
    @XmlAttribute(name = "style")
    private String style;

    /**
     * Get the 'style' attribute value.
     *
     * @return value
     */
    public String getStyle() {
        return style;
    }

    /**
     * Set the 'style' attribute value.
     */
    public void setStyle(String style) {
        this.style = style;
    }
}
