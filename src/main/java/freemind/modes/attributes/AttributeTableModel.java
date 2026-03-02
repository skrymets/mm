/*
 * Created on 10.07.2005
 * Copyright (C) 2005 Dimitri Polivaev
 */
package freemind.modes.attributes;

import freemind.model.MindMapNode;

import javax.swing.table.TableModel;

public interface AttributeTableModel extends TableModel {
    int getRowCount();

    int getColumnWidth(int col);

    Object getValueAt(int row, int col);

    void setValueAt(Object o, int row, int col);

    MindMapNode getNode();

    void fireTableDataChanged();
}