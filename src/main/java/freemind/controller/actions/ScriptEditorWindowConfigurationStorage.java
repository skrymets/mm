
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="script_editor_window_configuration_storage">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="window_configuration_storage">
 *         &lt;xs:attribute type="xs:int" use="optional" name="left_ratio"/>
 *         &lt;xs:attribute type="xs:int" use="optional" name="top_ratio"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "script_editor_window_configuration_storage")
public class ScriptEditorWindowConfigurationStorage
        extends
            WindowConfigurationStorage
{
    @XmlAttribute(name = "left_ratio")
    private Integer leftRatio;
    @XmlAttribute(name = "top_ratio")
    private Integer topRatio;

    /**
     * Get the 'left_ratio' attribute value.
     *
     * @return value
     */
    public Integer getLeftRatio() {
        return leftRatio;
    }

    /**
     * Set the 'left_ratio' attribute value.
     *
     * @param leftRatio
     */
    public void setLeftRatio(Integer leftRatio) {
        this.leftRatio = leftRatio;
    }

    /**
     * Get the 'top_ratio' attribute value.
     *
     * @return value
     */
    public Integer getTopRatio() {
        return topRatio;
    }

    /**
     * Set the 'top_ratio' attribute value.
     *
     * @param topRatio
     */
    public void setTopRatio(Integer topRatio) {
        this.topRatio = topRatio;
    }
}
