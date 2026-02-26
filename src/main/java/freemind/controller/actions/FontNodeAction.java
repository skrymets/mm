
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="font_node_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="format_node_action">
 *         &lt;xs:attribute type="xs:string" use="required" name="font"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "font_node_action")
public class FontNodeAction extends FormatNodeAction
{
    @XmlAttribute(name = "font")
    private String font;

    /**
     * Get the 'font' attribute value.
     *
     * @return value
     */
    public String getFont() {
        return font;
    }

    /**
     * Set the 'font' attribute value.
     *
     * @param font
     */
    public void setFont(String font) {
        this.font = font;
    }
}
