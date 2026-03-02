/*
 * Created on 10.07.2005
 * Copyright (C) 2005 Dimitri Polivaev
 */
package freemind.controller.filter.util;

import freemind.model.SortedListModel;
import freemind.model.SortedMapListModel;
import lombok.Getter;

import javax.swing.*;

@Getter
@SuppressWarnings("serial")
public class SortedComboBoxModel extends SortedMapListModel implements
        SortedListModel, ComboBoxModel {
    private Object selectedItem;

    public void setSelectedItem(Object o) {
        selectedItem = o;
        fireContentsChanged(this, -1, -1);
    }

}
