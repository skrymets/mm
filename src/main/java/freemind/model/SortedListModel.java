/*
 * Created on 18.06.2005
 * Copyright (C) 2005 Dimitri Polivaev
 *
 */
package freemind.model;

import javax.swing.*;

public interface SortedListModel extends ListModel {

    void clear();

    boolean contains(Object o);

    void add(Object o);

    void replace(Object oldO, Object newO);

    void remove(Object o);

    int getIndexOf(Object o);
}
