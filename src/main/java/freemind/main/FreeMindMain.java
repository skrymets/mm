/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2001  Joerg Mueller <joergmueller@bigfoot.com>
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
/*$Id: FreeMindMain.java,v 1.12.14.5.2.12 2008/07/17 19:16:33 christianfoltin Exp $*/

package freemind.main;

import freemind.controller.Controller;
import freemind.controller.MenuBar;
import freemind.view.mindmapview.MapView;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public interface FreeMindMain {

    interface StartupDoneListener {
        void startupDone();
    }

    JFrame getJFrame();

    boolean isApplet();

    MapView getView();

    void setView(MapView view);

    Controller getController();

    void setWaitingCursor(boolean waiting);

    String getPatternsXML();

    MenuBar getFreeMindMenuBar();

    /**
     * Returns the ResourceBundle with the current language
     */
    ResourceBundle getResources();

    String getResourceString(String key);

    String getResourceString(String key, String defaultResource);

    Container getContentPane();

    void setStatusText(String msg);

    void err(String msg);

    /**
     * Open url in WWW browser. This method hides some differences between
     * operating systems.
     */
    void openDocument(URL location) throws Exception;

    /**
     * remove this!
     */
    void repaint();

    URL getResource(String name);

    int getIntProperty(String key, int defaultValue);

    /**
     * @return returns the list of all properties.
     */
    Properties getProperties();

    /**
     * Properties are stored in freemind.properties (internally) and
     * ~/.freemind/auto.properties for user changed values. This method returns
     * the user value (if changed) or the original.
     *
     * @param key The property key as specified in freemind.properties
     * @return the value of the property or null, if not found.
     */
    String getProperty(String key);

    void setProperty(String key, String value);

    void saveProperties(boolean pIsShutdown);

    /**
     * Returns the path to the directory the freemind auto properties are in, or
     * null, if not present.
     */
    String getFreemindDirectory();

    JLayeredPane getLayeredPane();

    void setTitle(String title);

    // to keep last win size (PN)
    int getWinHeight();

    int getWinWidth();

    int getWinState();

    int getWinX();

    int getWinY();

    String ENABLE_NODE_MOVEMENT = "enable_node_movement";

    /**
     * version info:
     */
    VersionInformation getFreemindVersion();

    /**
     * Inserts a (south) component into the split pane. If the screen isn't
     * split yet, a split pane should be created on the fly.
     *
     * @param pMindMapComponent
     * @return the split pane in order to move the dividers.
     */
    JSplitPane insertComponentIntoSplitPane(JComponent pMindMapComponent);

    /**
     * Indicates that the south panel should be made invisible.
     */
    void removeSplitPane();

    /**
     * @return a ClassLoader derived from the standard, with freeminds base dir
     * included.
     */
    ClassLoader getFreeMindClassLoader();

    /**
     * @return default ".", but on different os this differs.
     */
    String getFreemindBaseDir();

    /**
     * Makes it possible to have a property different for different
     * localizations. Common example is to put keystrokes to different keys as
     * some are better reachable than others depending on the locale.
     */
    String getAdjustableProperty(String label);

    void setDefaultProperty(String key, String value);

    JComponent getContentComponent();

    JScrollPane getScrollPane();

    void registerStartupDoneListener(StartupDoneListener pStartupDoneListener);

}
