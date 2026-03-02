
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="table_column_setting_store">
 *   &lt;xs:attribute type="xs:int" use="required" name="column_width"/>
 *   &lt;xs:attribute type="xs:int" use="optional" name="column_sorting"/>
 * &lt;/xs:complexType>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "table_column_setting_store")
public class TableColumnSettingStore
{
    @XmlAttribute(name = "column_width")
    private int columnWidth;
    @XmlAttribute(name = "column_sorting")
    private Integer columnSorting;

    /**
     * Get the 'column_width' attribute value.
     *
     * @return value
     */
    public int getColumnWidth() {
        return columnWidth;
    }

    /**
     * Set the 'column_width' attribute value.
     */
    public void setColumnWidth(int columnWidth) {
        this.columnWidth = columnWidth;
    }

    /**
     * Get the 'column_sorting' attribute value.
     *
     * @return value
     */
    public Integer getColumnSorting() {
        return columnSorting;
    }

    /**
     * Set the 'column_sorting' attribute value.
     */
    public void setColumnSorting(Integer columnSorting) {
        this.columnSorting = columnSorting;
    }
}
