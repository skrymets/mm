
package freemind.controller.actions.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="table_column_setting_store">
 *   &lt;xs:attribute type="xs:int" use="required" name="column_width"/>
 *   &lt;xs:attribute type="xs:int" use="optional" name="column_sorting"/>
 * &lt;/xs:complexType>
 * </pre>
 */
public class TableColumnSettingStore
{
    private int columnWidth;
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
     * 
     * @param columnWidth
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
     * 
     * @param columnSorting
     */
    public void setColumnSorting(Integer columnSorting) {
        this.columnSorting = columnSorting;
    }
}
