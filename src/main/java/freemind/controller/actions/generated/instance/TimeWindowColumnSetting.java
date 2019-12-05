
package freemind.controller.actions.generated.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="time_window_column_setting">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="table_column_setting_store">
 *         &lt;xs:attribute type="xs:int" use="optional" default="0" name="placeholder"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class TimeWindowColumnSetting extends TableColumnSettingStore
{
    private Integer placeholder;

    /** 
     * Get the 'placeholder' attribute value.
     * 
     * @return value
     */
    public Integer getPlaceholder() {
        return placeholder;
    }

    /** 
     * Set the 'placeholder' attribute value.
     * 
     * @param placeholder
     */
    public void setPlaceholder(Integer placeholder) {
        this.placeholder = placeholder;
    }
}
