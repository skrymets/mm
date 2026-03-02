
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="plugin_property">
 *   &lt;xs:complexType>
 *     &lt;xs:attribute type="xs:string" use="required" name="name"/>
 *     &lt;xs:attribute type="xs:string" use="optional" name="value"/>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "plugin_property")
public class PluginProperty
{
    @XmlAttribute(name = "name")
    private String name;
    @XmlAttribute(name = "value")
    private String value;

    /**
     * Get the 'name' attribute value.
     *
     * @return value
     */
    public String getName() {
        return name;
    }

    /**
     * Set the 'name' attribute value.
     */
    public void setName(String name) {
        this.name = name;
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
     */
    public void setValue(String value) {
        this.value = value;
    }
}
