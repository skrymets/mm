
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="add_arrow_link_xml_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="node_action">
 *         &lt;xs:attribute type="xs:string" use="required" name="destination"/>
 *         &lt;xs:attribute type="xs:string" use="optional" name="new_id"/>
 *         &lt;xs:attribute type="xs:string" use="optional" name="color"/>
 *         &lt;xs:attribute type="xs:string" use="optional" name="startInclination"/>
 *         &lt;xs:attribute type="xs:string" use="optional" name="endInclination"/>
 *         &lt;xs:attribute type="xs:string" use="optional" name="startArrow"/>
 *         &lt;xs:attribute type="xs:string" use="optional" name="endArrow"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "add_arrow_link_xml_action")
public class AddArrowLinkXmlAction extends NodeAction
{
    @XmlAttribute(name = "destination")
    private String destination;
    @XmlAttribute(name = "new_id")
    private String newId;
    @XmlAttribute(name = "color")
    private String color;
    @XmlAttribute(name = "startInclination")
    private String startInclination;
    @XmlAttribute(name = "endInclination")
    private String endInclination;
    @XmlAttribute(name = "startArrow")
    private String startArrow;
    @XmlAttribute(name = "endArrow")
    private String endArrow;

    /**
     * Get the 'destination' attribute value.
     *
     * @return value
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Set the 'destination' attribute value.
     *
     * @param destination
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * Get the 'new_id' attribute value.
     *
     * @return value
     */
    public String getNewId() {
        return newId;
    }

    /**
     * Set the 'new_id' attribute value.
     *
     * @param newId
     */
    public void setNewId(String newId) {
        this.newId = newId;
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

    /**
     * Get the 'startInclination' attribute value.
     *
     * @return value
     */
    public String getStartInclination() {
        return startInclination;
    }

    /**
     * Set the 'startInclination' attribute value.
     *
     * @param startInclination
     */
    public void setStartInclination(String startInclination) {
        this.startInclination = startInclination;
    }

    /**
     * Get the 'endInclination' attribute value.
     *
     * @return value
     */
    public String getEndInclination() {
        return endInclination;
    }

    /**
     * Set the 'endInclination' attribute value.
     *
     * @param endInclination
     */
    public void setEndInclination(String endInclination) {
        this.endInclination = endInclination;
    }

    /**
     * Get the 'startArrow' attribute value.
     *
     * @return value
     */
    public String getStartArrow() {
        return startArrow;
    }

    /**
     * Set the 'startArrow' attribute value.
     *
     * @param startArrow
     */
    public void setStartArrow(String startArrow) {
        this.startArrow = startArrow;
    }

    /**
     * Get the 'endArrow' attribute value.
     *
     * @return value
     */
    public String getEndArrow() {
        return endArrow;
    }

    /**
     * Set the 'endArrow' attribute value.
     *
     * @param endArrow
     */
    public void setEndArrow(String endArrow) {
        this.endArrow = endArrow;
    }
}
