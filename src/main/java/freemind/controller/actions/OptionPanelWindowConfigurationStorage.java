
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="option_panel_window_configuration_storage">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="window_configuration_storage">
 *         &lt;xs:attribute type="xs:string" use="optional" name="panel"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "option_panel_window_configuration_storage")
public class OptionPanelWindowConfigurationStorage
        extends
            WindowConfigurationStorage
{
    @XmlAttribute(name = "panel")
    private String panel;

    /**
     * Get the 'panel' attribute value.
     *
     * @return value
     */
    public String getPanel() {
        return panel;
    }

    /**
     * Set the 'panel' attribute value.
     */
    public void setPanel(String panel) {
        this.panel = panel;
    }
}
