
package freemind.controller.actions;

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
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "fold_action")
public class FoldAction extends NodeAction
{
    @XmlAttribute(name = "folded")
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
     */
    public void setFolded(boolean folded) {
        this.folded = folded;
    }
}
