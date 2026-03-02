
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="remove_arrow_link_xml_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="xml_action">
 *         &lt;xs:attribute type="xs:string" use="required" name="id"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "remove_arrow_link_xml_action")
public class RemoveArrowLinkXmlAction extends XmlAction
{
    @XmlAttribute(name = "id")
    private String id;

    /**
     * Get the 'id' attribute value.
     *
     * @return value
     */
    public String getId() {
        return id;
    }

    /**
     * Set the 'id' attribute value.
     */
    public void setId(String id) {
        this.id = id;
    }
}
