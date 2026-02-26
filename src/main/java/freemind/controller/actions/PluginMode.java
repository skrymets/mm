
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="plugin_mode">
 *   &lt;xs:complexType>
 *     &lt;xs:attribute type="xs:string" use="required" name="class_name"/>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "plugin_mode")
public class PluginMode
{
    @XmlAttribute(name = "class_name")
    private String className;

    /**
     * Get the 'class_name' attribute value.
     *
     * @return value
     */
    public String getClassName() {
        return className;
    }

    /**
     * Set the 'class_name' attribute value.
     *
     * @param className
     */
    public void setClassName(String className) {
        this.className = className;
    }
}
