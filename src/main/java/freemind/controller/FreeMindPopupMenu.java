/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2008 Christian Foltin, Dimitri Polivaev and others.
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
 * Created on 03.01.2008
 *
 */
package freemind.controller;

import lombok.extern.log4j.Log4j2;

import javax.swing.*;
import javax.swing.event.MenuListener;
import java.util.HashSet;

/**
 * @author foltin
 */
@SuppressWarnings("serial")
@Log4j2
public class FreeMindPopupMenu extends JPopupMenu implements StructuredMenuHolder.MenuEventSupplier {
    private HashSet<MenuListener> listeners = new HashSet<>();

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
        // log.info("Popup firePopupMenuCanceled called.");
        for (MenuListener listener : listeners) {
            listener.menuCanceled(null);
        }
    }

    protected void firePopupMenuWillBecomeInvisible() {
        super.firePopupMenuWillBecomeInvisible();
        // log.info("Popup firePopupMenuWillBecomeInvisible called.");
        for (MenuListener listener : listeners) {
            listener.menuDeselected(null);
        }
    }

}
