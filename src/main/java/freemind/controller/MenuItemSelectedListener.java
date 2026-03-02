package freemind.controller;

import javax.swing.*;

public interface MenuItemSelectedListener {
    boolean isSelected(JMenuItem checkItem, Action action);
}