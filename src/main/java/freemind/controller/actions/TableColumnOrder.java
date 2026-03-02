
package freemind.controller.actions;

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
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "table_column_order")
public class TableColumnOrder
{
    @XmlAttribute(name = "column_index")
    private int columnIndex;
    @XmlAttribute(name = "column_sorting")
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
     */
    public void setColumnSorting(String columnSorting) {
        this.columnSorting = columnSorting;
    }
}
