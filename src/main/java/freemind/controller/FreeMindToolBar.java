/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
 *
 *See COPYING for Details
 *
 *This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
/*
 * Created on 28.03.2004
 *
 */
package freemind.controller;

import freemind.main.Tools;

import javax.swing.*;
import java.awt.*;

/**
 * @author Stefan Zechmeister
 */
@SuppressWarnings("serial")
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
        action.putValue(Action.SHORT_DESCRIPTION, Tools.removeMnemonic(actionName.toString()));
        JButton returnValue = super.add(action);
        returnValue.setName(actionName.toString());
        returnValue.setText("");
        returnValue.setMargin(nullInsets);
        returnValue.setFocusable(false);

        // fc, 20.6.2004: try to make the toolbar looking good under Mac OS X.
        if (Tools.isMacOsX()) {
            returnValue.setBorderPainted(false);
        }
        returnValue.setContentAreaFilled(false);

        return returnValue;
    }

}
