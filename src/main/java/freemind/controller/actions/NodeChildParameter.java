
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="node_child_parameter">
 *   &lt;xs:complexType>
 *     &lt;xs:attribute type="xs:string" use="required" name="key"/>
 *     &lt;xs:attribute type="xs:string" use="required" name="value"/>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "node_child_parameter")
public class NodeChildParameter
{
    @XmlAttribute(name = "key")
    private String key;
    @XmlAttribute(name = "value")
    private String value;

    /**
     * Get the 'key' attribute value.
     *
     * @return value
     */
    public String getKey() {
        return key;
    }

    /**
     * Set the 'key' attribute value.
     *
     * @param key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Get the 'value' attribute value.
     *
     * @return value
     */
    public String getValue() {
        return value;
    }

    /**
     * Set the 'value' attribute value.
     *
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }
}
