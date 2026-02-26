
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="italic_node_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="format_node_action">
 *         &lt;xs:attribute type="xs:boolean" use="required" name="italic"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "italic_node_action")
public class ItalicNodeAction extends FormatNodeAction
{
    @XmlAttribute(name = "italic")
    private boolean italic;

    /**
     * Get the 'italic' attribute value.
     *
     * @return value
     */
    public boolean isItalic() {
        return italic;
    }

    /**
     * Set the 'italic' attribute value.
     *
     * @param italic
     */
    public void setItalic(boolean italic) {
        this.italic = italic;
    }
}
