/*
 * Created on 15.11.2005
 * Copyright (C) 2005 Dimitri Polivaev
 */
package freemind.modes.mindmapmode;

import javax.swing.*;
import java.awt.*;

public class JAutoScrollBarPane extends JScrollPane {

    public JAutoScrollBarPane(Component view) {
        super(view, VERTICAL_SCROLLBAR_NEVER, HORIZONTAL_SCROLLBAR_NEVER);
    }

    public void doLayout() {
        super.doLayout();
        Insets insets = getInsets();
        int insetHeight = insets.top + insets.bottom;
        Dimension prefSize = getViewport().getPreferredSize();
        int height = getHeight() - insetHeight;
        if (getHorizontalScrollBar().isVisible()) {
            height -= getHorizontalScrollBar().getHeight();
        }
        boolean isVsbNeeded = height < prefSize.height;
        boolean layoutAgain = false;

        if (isVsbNeeded
                && getVerticalScrollBarPolicy() == VERTICAL_SCROLLBAR_NEVER) {
            setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
            layoutAgain = true;
        } else if (!isVsbNeeded
                && getVerticalScrollBarPolicy() == VERTICAL_SCROLLBAR_ALWAYS) {
            setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
            layoutAgain = true;
        }

        if (layoutAgain) {
            super.doLayout();
        }
    }

    public Dimension getPreferredSize() {
        if (!isValid()) {
            doLayout();
        }
        return super.getPreferredSize();
    }
}
