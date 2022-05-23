/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2006  Christian Foltin <christianfoltin@users.sourceforge.net>
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
/*$Id: FreeMindMainMock.java,v 1.1.2.16 2009/03/29 19:37:23 christianfoltin Exp $*/
package freemind.frok.patches;

import freemind.controller.Controller;
import freemind.controller.MenuBar;
import freemind.main.*;
import freemind.view.mindmapview.MapView;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Vector;

import static freemind.main.VersionInformation.Type.ALPHA;

@Log4j2
public class FreeMindMainMock implements FreeMindMain {

    private final Properties mProperties;

    public FreeMindMainMock() {
        super();
        try {
            mProperties = new FreeMindStarter().loadDefaultPreferences();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Resources.createInstance(this);

    }

    public JFrame getJFrame() {
        return null;
    }

    public boolean isApplet() {
        return false;
    }

    public MapView getView() {
        return null;
    }

    public void setView(MapView view) {
    }

    public Controller getController() {
        return null;
    }

    public void setWaitingCursor(boolean waiting) {
    }

    public String getPatternsXML() {
        return null;
    }

    public MenuBar getFreeMindMenuBar() {
        return null;
    }

    public ResourceBundle getResources() {
        return null;
    }

    public String getResourceString(String key) {
        return key;
    }

    public String getResourceString(String key, String resource) {
        return key;
    }

    public Container getContentPane() {
        return null;
    }

    public void setStatusText(String msg) {
    }

    public void err(String msg) {
    }

    public void openDocument(URL location) throws Exception {
    }

    public void repaint() {
    }

    public URL getResource(String name) {
        return ClassLoader.getSystemResource(name);
    }

    public int getIntProperty(String key, int defaultValue) {
        try {
            return Integer.parseInt(getProperty(key));
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public Properties getProperties() {
        return mProperties;
    }

    public String getProperty(String key) {
        return mProperties.getProperty(key);
    }

    public void setProperty(String key, String value) {
    }

    public void saveProperties(boolean pIsShutdown) {
    }

    public String getFreemindDirectory() {
        return ".";
    }

    public JLayeredPane getLayeredPane() {
        return null;
    }

    public void setTitle(String title) {
    }

    public int getWinHeight() {
        return 0;
    }

    public int getWinWidth() {
        return 0;
    }

    public int getWinState() {
        return 0;
    }

    public int getWinX() {
        return 0;
    }

    public int getWinY() {
        return 0;
    }

    public VersionInformation getFreemindVersion() {
        return new VersionInformation(1, 0, 0, ALPHA, 42);
    }

    public Logger getLogger(String forClass) {
        return LoggerFactory.getLogger(forClass);
    }

    public ClassLoader getFreeMindClassLoader() {
        ClassLoader classLoader = this.getClass().getClassLoader();
        try {
            return new URLClassLoader(new URL[]{Tools.fileToUrl(new File(
                    getFreemindBaseDir()))}, classLoader);
        } catch (MalformedURLException e) {
            log.error(e);
            return classLoader;
        }
    }

    public String getFreemindBaseDir() {
        return ".";
    }

    public String getAdjustableProperty(String pLabel) {
        // TODO Auto-generated method stub
        return null;
    }

    public void setDefaultProperty(String pKey, String pValue) {
        // TODO Auto-generated method stub

    }

    public JSplitPane insertComponentIntoSplitPane(JComponent pParameter) {
        // TODO Auto-generated method stub
        return null;
    }

    public void removeSplitPane() {
        // TODO Auto-generated method stub

    }

    public JComponent getContentComponent() {
        return null;
    }

    public JScrollPane getScrollPane() {
        return null;
    }

    public void registerStartupDoneListener(
            StartupDoneListener pStartupDoneListener) {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see freemind.main.FreeMindMain#getLoggerList()
     */
    public List getLoggerList() {
        return new Vector<>();
    }

}
