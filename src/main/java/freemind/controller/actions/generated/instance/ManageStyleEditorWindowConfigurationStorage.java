
package freemind.controller.actions.generated.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="manage_style_editor_window_configuration_storage">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="window_configuration_storage">
 *         &lt;xs:attribute type="xs:int" use="optional" name="divider_position"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class ManageStyleEditorWindowConfigurationStorage
        extends
            WindowConfigurationStorage
{
    private Integer dividerPosition;

    /** 
     * Get the 'divider_position' attribute value.
     * 
     * @return value
     */
    public Integer getDividerPosition() {
        return dividerPosition;
    }

    /** 
     * Set the 'divider_position' attribute value.
     * 
     * @param dividerPosition
     */
    public void setDividerPosition(Integer dividerPosition) {
        this.dividerPosition = dividerPosition;
    }
}
