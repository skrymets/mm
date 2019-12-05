
package freemind.controller.actions.instance;

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
public class OptionPanelWindowConfigurationStorage
        extends
            WindowConfigurationStorage
{
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
     * 
     * @param panel
     */
    public void setPanel(String panel) {
        this.panel = panel;
    }
}
