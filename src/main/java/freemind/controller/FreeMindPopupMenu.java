package freemind.controller;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.event.MenuListener;
import java.util.HashSet;

@Slf4j
public class FreeMindPopupMenu extends JPopupMenu implements StructuredMenuHolder.MenuEventSupplier {
    private final HashSet<MenuListener> listeners = new HashSet<>();

    public FreeMindPopupMenu() {
    }

    protected void firePopupMenuWillBecomeVisible() {
        super.firePopupMenuWillBecomeVisible();
        log.trace("Popup firePopupMenuWillBecomeVisible called.");
        for (MenuListener listener : listeners) {
            listener.menuSelected(null);
        }
    }

    public void addMenuListener(MenuListener listener) {
        listeners.add(listener);
    }

    public void removeMenuListener(MenuListener listener) {
        listeners.remove(listener);
    }

    protected void firePopupMenuCanceled() {
        super.firePopupMenuCanceled();
        for (MenuListener listener : listeners) {
            listener.menuCanceled(null);
        }
    }

    protected void firePopupMenuWillBecomeInvisible() {
        super.firePopupMenuWillBecomeInvisible();
        for (MenuListener listener : listeners) {
            listener.menuDeselected(null);
        }
    }

}
