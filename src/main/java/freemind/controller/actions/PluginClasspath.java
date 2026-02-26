
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="plugin_classpath">
 *   &lt;xs:complexType>
 *     &lt;xs:attribute type="xs:string" use="required" name="jar"/>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "plugin_classpath")
public class PluginClasspath
{
    @XmlAttribute(name = "jar")
    private String jar;

    /**
     * Get the 'jar' attribute value.
     *
     * @return value
     */
    public String getJar() {
        return jar;
    }

    /**
     * Set the 'jar' attribute value.
     *
     * @param jar
     */
    public void setJar(String jar) {
        this.jar = jar;
    }
}
