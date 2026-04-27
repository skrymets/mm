package freemind.controller;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;

@Getter
public class StructuredMenuItemHolder {
    @Setter
    private JMenuItem menuItem;
    @Setter
    private MenuItemEnabledListener enabledListener;
    @Setter
    private MenuItemSelectedListener selectionListener;
    private Action action;

    public StructuredMenuItemHolder() {
    }

    public void setAction(Action action) {
        this.action = action;
        if (action instanceof MenuItemEnabledListener listener) {
            setEnabledListener(listener);
        }
        if (action instanceof MenuItemSelectedListener listener) {
            setSelectionListener(listener);
        }
    }
}
