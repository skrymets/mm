
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="bold_node_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="format_node_action">
 *         &lt;xs:attribute type="xs:boolean" use="required" name="bold"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "bold_node_action")
public class BoldNodeAction extends FormatNodeAction
{
    @XmlAttribute(name = "bold")
    private boolean bold;

    /**
     * Get the 'bold' attribute value.
     *
     * @return value
     */
    public boolean isBold() {
        return bold;
    }

    /**
     * Set the 'bold' attribute value.
     */
    public void setBold(boolean bold) {
        this.bold = bold;
    }
}
