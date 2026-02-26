
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="arrow_link_color_xml_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="xml_action">
 *         &lt;xs:attribute type="xs:string" use="required" name="id"/>
 *         &lt;xs:attribute type="xs:string" use="optional" name="color"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "arrow_link_color_xml_action")
public class ArrowLinkColorXmlAction extends XmlAction
{
    @XmlAttribute(name = "id")
    private String id;
    @XmlAttribute(name = "color")
    private String color;

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
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the 'color' attribute value.
     *
     * @return value
     */
    public String getColor() {
        return color;
    }

    /**
     * Set the 'color' attribute value.
     *
     * @param color
     */
    public void setColor(String color) {
        this.color = color;
    }
}
