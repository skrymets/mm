package accessories.plugins;

import accessories.plugins.NodeAttributeTableRegistration.AttributeHolder;
import accessories.plugins.NodeAttributeTableRegistration.AttributeTableModel;
import freemind.common.ScalableJTable;

import java.awt.*;

/**
 * Taken from <a href="http://stackoverflow.com/questions/2316563/how-can-i-sort-java-jtable-with-an-empty-row-and-force-the-empty-row-always-be-l">...</a>
 * <p>
 */
public class NewLineTable extends ScalableJTable {

    @Override
    public int getRowCount() {
        // fake an additional row
        return super.getRowCount() + 1;
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (row < super.getRowCount()) {
            return super.getValueAt(row, column);
        }
        return ""; // value to display in new line
    }

    @Override
    public int convertRowIndexToModel(int viewRowIndex) {
        if (viewRowIndex < super.getRowCount()) {
            return super.convertRowIndexToModel(viewRowIndex);
        }
        return super.getRowCount(); // can't convert our faked row
    }

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        if (row < super.getRowCount()) {
            super.setValueAt(aValue, row, column);
        } else {
            AttributeHolder attribute = new AttributeHolder();
            switch (column) {
                case NodeAttributeTableRegistration.KEY_COLUMN:
                    attribute.mKey = (String) aValue;
                    attribute.mValue = "";
                    break;
                case NodeAttributeTableRegistration.VALUE_COLUMN:
                    // this couldn't be happen, as the new-value-field is write protected.
                    attribute.mKey = "";
                    attribute.mValue = (String) aValue;
                    break;
            }
            final int position = ((AttributeTableModel) getModel()).addAttributeHolder(attribute, true);
            // fix selection after sorting
            EventQueue.invokeLater(() -> {
                NewLineTable table = NewLineTable.this;
                int selRow = table.convertRowIndexToView(position);
                table.getSelectionModel().setSelectionInterval(selRow, selRow);
            });
        }
    }

    @Override
    public boolean isCellEditable(int pRow, int pColumn) {
        if (pRow < super.getRowCount()) {
            return super.isCellEditable(pRow, pColumn);
        }
        return pColumn == NodeAttributeTableRegistration.KEY_COLUMN;
    }
}
