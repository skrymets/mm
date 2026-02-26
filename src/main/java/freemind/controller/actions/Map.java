
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="map">
 *   &lt;xs:complexType>
 *     &lt;xs:sequence>
 *       &lt;xs:element ref="node"/>
 *     &lt;/xs:sequence>
 *     &lt;xs:attribute type="xs:string" use="required" name="version"/>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "map")
public class Map
{
    private Node node;
    @XmlAttribute(name = "version")
    private String version;

    /**
     * Get the 'node' element value.
     *
     * @return value
     */
    public Node getNode() {
        return node;
    }

    /**
     * Set the 'node' element value.
     *
     * @param node
     */
    public void setNode(Node node) {
        this.node = node;
    }

    /**
     * Get the 'version' attribute value.
     *
     * @return value
     */
    public String getVersion() {
        return version;
    }

    /**
     * Set the 'version' attribute value.
     *
     * @param version
     */
    public void setVersion(String version) {
        this.version = version;
    }
}
