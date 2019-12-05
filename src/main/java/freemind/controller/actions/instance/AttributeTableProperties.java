
package freemind.controller.actions.instance;

import java.util.ArrayList;
import java.util.List;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:xs="http://www.w3.org/2001/XMLSchema" name="attribute_table_properties">
 *   &lt;xs:complexContent>
 *     &lt;xs:extension base="xml_action">
 *       &lt;xs:sequence>
 *         &lt;xs:element ref="table_column_order" minOccurs="0" maxOccurs="unbounded"/>
 *       &lt;/xs:sequence>
 *     &lt;/xs:extension>
 *   &lt;/xs:complexContent>
 * &lt;/xs:complexType>
 * </pre>
 */
public class AttributeTableProperties extends XmlAction
{
    private List<TableColumnOrder> tableColumnOrderList = new ArrayList<TableColumnOrder>();

    /** 
     * Get the list of 'table_column_order' element items.
     * 
     * @return list
     */
    public List<TableColumnOrder> getTableColumnOrderList() {
        return tableColumnOrderList;
    }

    /** 
     * Set the list of 'table_column_order' element items.
     * 
     * @param list
     */
    public void setTableColumnOrderList(List<TableColumnOrder> list) {
        tableColumnOrderList = list;
    }

    /** 
     * Get the number of 'table_column_order' element items.
     * @return count
     */
    public int sizeTableColumnOrderList() {
        return tableColumnOrderList.size();
    }

    /** 
     * Add a 'table_column_order' element item.
     * @param item
     */
    public void addTableColumnOrder(TableColumnOrder item) {
        tableColumnOrderList.add(item);
    }

    /** 
     * Get 'table_column_order' element item by position.
     * @return item
     * @param index
     */
    public TableColumnOrder getTableColumnOrder(int index) {
        return tableColumnOrderList.get(index);
    }

    /** 
     * Remove all 'table_column_order' element items.
     */
    public void clearTableColumnOrderList() {
        tableColumnOrderList.clear();
    }
}
