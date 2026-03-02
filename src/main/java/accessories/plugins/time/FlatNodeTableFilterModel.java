package accessories.plugins.time;

import accessories.plugins.time.TimeList.NodeHolder;
import accessories.plugins.time.TimeList.NotesHolder;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.regex.Pattern;

@SuppressWarnings("serial")
public class FlatNodeTableFilterModel extends AbstractTableModel {

    private final TableModel mTableModel;
    /**
     * Contains indices or rows matching the filter criteria.
     */
    private ArrayList<Integer> mIndexArray;
    private Pattern mPattern;
    /**
     * The column that contains the NodeHolder items
     */
    private final int mNodeTextColumn;
    private final int mNoteTextColumn;

    public FlatNodeTableFilterModel(TableModel tableModel, int node_text_column, int note_text_column) {
        super();
        this.mTableModel = tableModel;
        this.mNodeTextColumn = node_text_column;
        mNoteTextColumn = note_text_column;
        tableModel.addTableModelListener(new TableModelHandler());
        resetFilter();
    }

    public void resetFilter() {
        setFilter(".*");
    }

    public void setFilter(String filterRegexp) {
        // System.out.println("Setting filter to '"+mFilterRegexp+"'");
        mPattern = Pattern.compile(filterRegexp, Pattern.CASE_INSENSITIVE);
        updateIndexArray();
        fireTableDataChanged();
    }

    private void updateIndexArray() {
        ArrayList<Integer> newIndexArray = new ArrayList<>();
        for (int i = 0; i < mTableModel.getRowCount(); i++) {
            NodeHolder nodeContent = (NodeHolder) mTableModel.getValueAt(i, mNodeTextColumn);
            if (mPattern.matcher(nodeContent.toString()).matches()) {
                // add index to array:
                newIndexArray.add(i);
            } else {
                // only check notes, when not already a hit.
                NotesHolder noteContent = (NotesHolder) mTableModel.getValueAt(i,
                        mNoteTextColumn);
                if (mPattern.matcher(noteContent.toString()).matches()) {
                    // add index to array:
                    newIndexArray.add(Integer.valueOf(i));
                }
            }
        }
        mIndexArray = newIndexArray;
    }

    public int getRowCount() {
        return mIndexArray.size();
    }

    public int getColumnCount() {
        return mTableModel.getColumnCount();
    }

    public String getColumnName(int pColumnIndex) {
        return mTableModel.getColumnName(pColumnIndex);
    }

    public Class getColumnClass(int arg0) {
        return mTableModel.getColumnClass(arg0);
    }

    public Object getValueAt(int row, int column) {
        if (row < 0 || row >= getRowCount()) {
            throw new IllegalArgumentException("Illegal Row specified: " + row);
        }
        int origRow = mIndexArray.get(row);
        return mTableModel.getValueAt(origRow, column);
    }

    private class TableModelHandler implements TableModelListener {

        public void tableChanged(TableModelEvent arg0) {
//			updateIndexArray();
            fireTableDataChanged();
        }
    }
}
