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
    void openDocument(URL location);

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

    /**
     * @return false for desktop application, true if running as applet (legacy support)
     */
    default boolean isApplet() {
        return false;
    }

}
