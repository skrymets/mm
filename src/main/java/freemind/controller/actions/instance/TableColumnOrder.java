
package freemind.controller.actions.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="table_column_order">
 *   &lt;xs:complexType>
 *     &lt;xs:attribute type="xs:int" use="required" name="column_index"/>
 *     &lt;xs:attribute type="xs:string" use="required" name="column_sorting"/>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class TableColumnOrder
{
    private int columnIndex;
    private String columnSorting;

    /** 
     * Get the 'column_index' attribute value.
     * 
     * @return value
     */
    public int getColumnIndex() {
        return columnIndex;
    }

    /** 
     * Set the 'column_index' attribute value.
     * 
     * @param columnIndex
     */
    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    /** 
     * Get the 'column_sorting' attribute value.
     * 
     * @return value
     */
    public String getColumnSorting() {
        return columnSorting;
    }

    /** 
     * Set the 'column_sorting' attribute value.
     * 
     * @param columnSorting
     */
    public void setColumnSorting(String columnSorting) {
        this.columnSorting = columnSorting;
    }
}
