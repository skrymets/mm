/*
 * Created on 28.03.2004
 *
 */
package freemind.controller;

import freemind.main.SwingUtils;

import javax.swing.*;
import java.awt.*;

public class FreeMindToolBar extends JToolBar {
    private static final Insets nullInsets = new Insets(0, 0, 0, 0);

    public FreeMindToolBar() {
        this("", JToolBar.HORIZONTAL);
    }

    public FreeMindToolBar(String arg0, int arg1) {
        super(arg0, arg1);
        this.setMargin(nullInsets);
        setFloatable(false);
    }

    public JButton add(Action action) {
        final Object actionName = action.getValue(Action.NAME);
        action.putValue(Action.SHORT_DESCRIPTION, SwingUtils.removeMnemonic(actionName.toString()));
        JButton returnValue = super.add(action);
        returnValue.setName(actionName.toString());
        returnValue.setText("");
        returnValue.setMargin(nullInsets);
        returnValue.setFocusable(false);

        // FlatLaf styling
        returnValue.putClientProperty("JButton.buttonType", "toolBarButton");

        return returnValue;
    }

}
