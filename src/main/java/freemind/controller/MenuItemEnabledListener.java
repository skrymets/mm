package freemind.controller;

import javax.swing.*;

public interface MenuItemEnabledListener {
    /**
     * @return true, if the menu item should be accessible. False otherwise.
     */
    boolean isEnabled(JMenuItem pItem, Action pAction);
}
