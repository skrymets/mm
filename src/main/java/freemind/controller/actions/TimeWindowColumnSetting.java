
package freemind.controller.actions;

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
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "time_window_column_setting")
public class TimeWindowColumnSetting extends TableColumnSettingStore
{
    @XmlAttribute(name = "placeholder")
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
