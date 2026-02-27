/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
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
/*$Id: FreeMind.java,v 1.32.14.28.2.147 2011/01/09 21:03:13 christianfoltin Exp $*/
package freemind.main;

import com.inet.jortho.SpellChecker;
import freemind.controller.Controller;
import freemind.controller.LastStateStorageManagement;
import freemind.controller.MenuBar;
import freemind.controller.actions.MindmapLastStateStorage;
import freemind.events.FreeMindEventBus;
import freemind.main.services.EditServerService;
import freemind.main.services.WindowService;
import freemind.modes.ControllerAdapter;
import freemind.modes.ModeController;
import freemind.view.MapModule;
import freemind.view.mindmapview.MapView;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static javax.swing.JOptionPane.showMessageDialog;


@Slf4j
public class FreeMind extends JFrame implements FreeMindMain, ActionListener {

    public static final String J_SPLIT_PANE_SPLIT_TYPE = "JSplitPane.SPLIT_TYPE";

    public static final String VERTICAL_SPLIT_BELOW = "vertical_split_below";

    public static final String HORIZONTAL_SPLIT_RIGHT = "horizontal_split_right";

    private static final String PORT_FILE = "portFile";

    private static final String FREE_MIND_PROGRESS_LOAD_MAPS = "FreeMind.progress.loadMaps";

    private static final String FREE_MIND_PROGRESS_LOAD_MAPS_NAME = "FreeMind.progress.loadNamedMaps";



    public static final String RESOURCE_LOOKANDFEEL = "lookandfeel";

    public static final String RESOURCES_SELECTION_METHOD = "selection_method";

    public static final String RESOURCES_NODE_STYLE = "standardnodestyle";

    public static final String RESOURCES_ROOT_NODE_STYLE = "standardrootnodestyle";

    public static final String RESOURCES_NODE_TEXT_COLOR = "standardnodetextcolor";

    public static final String RESOURCES_SELECTED_NODE_COLOR = "standardselectednodecolor";

    public static final String RESOURCES_SELECTED_NODE_RECTANGLE_COLOR = "standardselectednoderectanglecolor";

    public static final String RESOURCE_DRAW_RECTANGLE_FOR_SELECTION = "standarddrawrectangleforselection";

    public static final String RESOURCES_EDGE_COLOR = "standardedgecolor";

    public static final String RESOURCES_EDGE_STYLE = "standardedgestyle";

    public static final String RESOURCES_CLOUD_COLOR = "standardcloudcolor";

    public static final String RESOURCES_LINK_COLOR = "standardlinkcolor";

    public static final String RESOURCES_BACKGROUND_COLOR = "standardbackgroundcolor";

    public static final String RESOURCE_PRINT_ON_WHITE_BACKGROUND = "printonwhitebackground";

    public static final String RESOURCES_WHEEL_VELOCITY = "wheel_velocity";

    public static final String RESOURCES_USE_TABBED_PANE = "use_tabbed_pane";

    public static final String RESOURCES_SHOW_NOTE_PANE = "use_split_pane";

    public static final String RESOURCES_SHOW_ATTRIBUTE_PANE = "show_attribute_pane";

    public static final String RESOURCES_DELETE_NODES_WITHOUT_QUESTION = "delete_nodes_without_question";

    public static final String RESOURCES_RELOAD_FILES_WITHOUT_QUESTION = "reload_files_without_question";

    protected static final VersionInformation VERSION = new VersionInformation("0.0.1 Alpha 1");

    public static final String XML_VERSION = "1.1.0";

    public static final String RESOURCES_REMIND_USE_RICH_TEXT_IN_NEW_LONG_NODES = "remind_use_rich_text_in_new_long_nodes";

    public static final String RESOURCES_EXECUTE_SCRIPTS_WITHOUT_ASKING = "resources_execute_scripts_without_asking";

    public static final String RESOURCES_EXECUTE_SCRIPTS_WITHOUT_FILE_RESTRICTION = "resources_execute_scripts_without_file_restriction";

    public static final String RESOURCES_EXECUTE_SCRIPTS_WITHOUT_NETWORK_RESTRICTION = "resources_execute_scripts_without_network_restriction";

    public static final String RESOURCES_EXECUTE_SCRIPTS_WITHOUT_EXEC_RESTRICTION = "resources_execute_scripts_without_exec_restriction";

    public static final String RESOURCES_SCRIPT_USER_KEY_NAME_FOR_SIGNING = "resources_script_user_key_name_for_signing";

    public static final String RESOURCES_CONVERT_TO_CURRENT_VERSION = "resources_convert_to_current_version";

    public static final String RESOURCES_CUT_NODES_WITHOUT_QUESTION = "resources_cut_nodes_without_question";

    public static final String RESOURCES_DON_T_SHOW_NOTE_ICONS = "resources_don_t_show_note_icons";

    public static final String RESOURCES_USE_COLLABORATION_SERVER_WITH_DIFFERENT_VERSION = "resources_use_collaboration_server_with_different_version";

    public static final String RESOURCES_REMOVE_NOTES_WITHOUT_QUESTION = "resources_remove_notes_without_question";

    public static final String RESOURCES_SAVE_FOLDING_STATE = "resources_save_folding_state";

    public static final String RESOURCES_SIGNED_SCRIPT_ARE_TRUSTED = "resources_signed_script_are_trusted";

    public static final String RESOURCES_USE_DEFAULT_FONT_FOR_NOTES_TOO = "resources_use_default_font_for_notes_too";

    public static final String RESOURCES_USE_MARGIN_TOP_ZERO_FOR_NOTES = "resources_use_margin_top_zero_for_notes";

    public static final String RESOURCES_DON_T_SHOW_CLONE_ICONS = "resources_don_t_show_clone_icons";

    public static final String RESOURCES_DON_T_OPEN_PORT = "resources_don_t_open_port";

    public static final String KEYSTROKE_MOVE_MAP_LEFT = "keystroke_MoveMapLeft";

    public static final String KEYSTROKE_MOVE_MAP_RIGHT = "keystroke_MoveMapRight";

    public static final String KEYSTROKE_PREVIOUS_MAP = "keystroke_previousMap";

    public static final String KEYSTROKE_NEXT_MAP = "keystroke_nextMap";

    public static final String RESOURCES_SEARCH_IN_NOTES_TOO = "resources_search_in_notes_too";

    public static final String RESOURCES_DON_T_SHOW_NOTE_TOOLTIPS = "resources_don_t_show_note_tooltips";

    public static final String RESOURCES_SEARCH_FOR_NODE_TEXT_WITHOUT_QUESTION = "resources_search_for_node_text_without_question";

    public static final String RESOURCES_COMPLETE_CLONING = "complete_cloning";

    public static final String RESOURCES_CLONE_TYPE_COMPLETE_CLONE = "COMPLETE_CLONE";

    public static final String TOOLTIP_DISPLAY_TIME = "tooltip_display_time";

    public static final String PROXY_PORT = "proxy.port";

    public static final String PROXY_HOST = "proxy.host";

    public static final String PROXY_PASSWORD = "proxy.password";

    public static final String PROXY_USER = "proxy.user";

    public static final String PROXY_IS_AUTHENTICATED = "proxy.is_authenticated";

    public static final String PROXY_USE_SETTINGS = "proxy.use_settings";

    public static final String RESOURCES_DISPLAY_FOLDING_BUTTONS = "resources_display_folding_buttons";

    private static final int TIME_TO_DISPLAY_MESSAGES = 10000;

    public static final String ICON_BAR_COLUMN_AMOUNT = "icon_bar_column_amount";

    public static final String RESOURCES_OPTIONAL_SPLIT_DIVIDER_POSITION = "resources_optional_split_divider_position";

    public static final String RESOURCES_PASTE_HTML_STRUCTURE = "paste_html_structure";

    public static final String PROXY_EXCEPTION = "proxy.exception";

    public static final String SCALING_FACTOR_PROPERTY = "scaling_factor_property";

    public static final String RESOURCES_CALENDAR_FONT_SIZE = "calendar_font_size";

    public final Properties userPreferences;

    private final Properties defaultPreferences;

    private MenuBar menuBar;

    private JLabel statusLabel;

    private Timer statusMessageDisplayTimer;

    @Getter
    private String patternsXML;

    @Getter
    Controller controller;// the one and only controller

    private final FreeMindCommon freeMindCommon;

    private static final boolean FILE_HANDLER_ERROR = false;

    /**
     * The main map's scroll pane.
     */
    @Getter
    private JScrollPane scrollPane = null;

    private JTabbedPane tabbedPane = null;

    private ImageIcon windowIcon;

    private boolean startupDone = false;

    private final List<StartupDoneListener> startupDoneListeners = new ArrayList<>();

    private EditServerService editServerService;

    private WindowService windowService;

    private final FreeMindEventBus eventBus;

    @Inject
    public FreeMind(@Named("default") Properties defaultPreferences,
                    @Named("user") Properties userPreferences,
                    FreeMindEventBus eventBus) {
        super("FreeMind");
        // Focus searcher - SecurityManager is deprecated in Java 17+ and removed in Java 18+
        this.defaultPreferences = defaultPreferences;
        this.userPreferences = userPreferences;
        this.eventBus = eventBus;

        printEnvironmentInfo();

        freeMindCommon = new FreeMindCommon(this);
        Resources.createInstance(this);
    }

    private void printEnvironmentInfo() {
        StringBuffer info = new StringBuffer();
        info.append("freemind_version = ");
        info.append(VERSION);
        info.append("; freemind_xml_version = ");
        info.append(XML_VERSION);

        String propsLoc = "version.properties";
        URL versionUrl = FreeMind.class.getClassLoader().getResource(propsLoc);
        try (InputStream stream = versionUrl.openStream()) {
            Properties buildNumberPros = new Properties();
            buildNumberPros.load(stream);
            info.append("\nBuild: ").append(buildNumberPros.getProperty("build.number")).append("\n");
        } catch (Exception e) {
            info.append("Problems reading build number file: ").append(e);
        }

        info.append("\njava_version = ");
        info.append(System.getProperty("java.version"));
        info.append("; os_name = ");
        info.append(System.getProperty("os.name"));
        info.append("; os_version = ");
        info.append(System.getProperty("os.version"));
        log.info(info.toString());

        try {
            StringBuffer b = new StringBuffer();
            // print all java/sun properties
            Properties properties = System.getProperties();
            List<String> list = new ArrayList<>();
            for (Object key : properties.keySet()) {
                list.add((String) key);
            }
            Collections.sort(list);
            for (String key : list) {
                if (key.startsWith("java") || key.startsWith("sun")) {
                    b.append("Environment key ").append(key).append(" = ").append(properties.getProperty(key)).append("\n");
                }
            }
            log.info(b.toString());
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
    }

    void init(FeedBack feedback) {
        /* This is only for apple but does not harm for the others. */
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        loadPatternsFile();

        feedback.increase("FreeMind.progress.updateLookAndFeel", null);
        updateLookAndFeel();

        feedback.increase("FreeMind.progress.createController", null);

        setIconImage(windowIcon.getImage());
        // Layout everything
        getContentPane().setLayout(new BorderLayout());

        controller = new Controller(this);
        controller.init();
        controller.getMapModuleManager().setEventBus(eventBus);

        feedback.increase("FreeMind.progress.settingPreferences", null);
        // add a listener for the controller, resource bundle:
        Controller.addPropertyChangeListener((propertyName, newValue, oldValue) -> {
            if (propertyName.equals(FreeMindCommon.RESOURCE_LANGUAGE)) {
                // re-read resources:
                freeMindCommon.clearLanguageResources();
                getResources();
            }
        });

        // fc, disabled with purpose (see java look and feel styleguides).
        // http://java.sun.com/products/jlf/ed2/book/index.html
        // // add a listener for the controller, look and feel:
        // Controller.addPropertyChangeListener(new FreemindPropertyListener() {
        //
        // public void propertyChanged(String propertyName, String newValue, String oldValue) {
        // if (propertyName.equals(RESOURCE_LOOKANDFEEL)) {
        // updateLookAndFeel();
        // }
        // }
        // });

        controller.optionAntialiasAction.changeAntialias(getProperty(FreeMindCommon.RESOURCE_ANTIALIAS));

        setupSpellChecking();
        setupProxy();
        feedback.increase("FreeMind.progress.propageteLookAndFeel", null);
        SwingUtilities.updateComponentTreeUI(this); // Propagate LookAndFeel to

        feedback.increase("FreeMind.progress.buildScreen", null);
        setScreenBounds();

        // JComponents
        feedback.increase("FreeMind.progress.createInitialMode", null);
        controller.createNewMode(getProperty("initial_mode"));
        if (controller.getModeController() instanceof ControllerAdapter modeAdapter) {
            modeAdapter.setEventBus(eventBus);
        }

//		EventQueue eventQueue = Toolkit.getDefaultToolkit().getSystemEventQueue();
//		eventQueue.push(new MyEventQueue());
    }

    private void loadPatternsFile() {
        try (InputStream inputStream = FreeMind.class.getClassLoader().getResourceAsStream("patterns.xml")) {
            patternsXML = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Couldn't load patterns.xml");
            patternsXML = "<patterns/>";
        }
    }

    private void updateLookAndFeel() {
        try {
            String lookAndFeel = userPreferences.getProperty(RESOURCE_LOOKANDFEEL);
            switch (lookAndFeel) {
                case "flatlaf_light":
                    UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
                    break;
                case "flatlaf_dark":
                    UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarkLaf());
                    break;
                case "flatlaf_intellij":
                    UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatIntelliJLaf());
                    break;
                case "flatlaf_darcula":
                    UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarculaLaf());
                    break;
                case "windows":
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                    break;
                case "motif":
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                    break;
                case "mac":
                    UIManager.setLookAndFeel("javax.swing.plaf.mac.MacLookAndFeel");
                    break;
                case "metal":
                    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                    break;
                case "gtk":
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
                    break;
                case "nothing":
                    break;
                default:
                    if (lookAndFeel.indexOf('.') != -1) {
                        UIManager.setLookAndFeel(lookAndFeel);
                    } else {
                        log.info("Default Look & Feel: FlatLaf Light");
                        UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
                    }
                    break;
            }
        } catch (Exception ex) {
            log.warn("Unable to set Look & Feel, falling back to system default", ex);
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception fallbackEx) {
                log.error("System L&F also failed", fallbackEx);
            }
        }
        freeMindCommon.loadUIProperties(defaultPreferences);
    }

    public VersionInformation getFreemindVersion() {
        return VERSION;
    }

    // maintain this methods to keep the last state/size of the window (PN)
    public int getWinHeight() {
        return getHeight();
    }

    public int getWinWidth() {
        return getWidth();
    }

    public int getWinX() {
        return getX();
    }

    public int getWinY() {
        return getY();
    }

    public int getWinState() {
        return getExtendedState();
    }

    public URL getResource(String name) {
        return this.getClass().getClassLoader().getResource(name);
    }

    public String getProperty(String key) {
        return userPreferences.getProperty(key);
    }

    public int getIntProperty(String key, int defaultValue) {
        try {
            return parseInt(getProperty(key));
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    public Properties getProperties() {
        return userPreferences;
    }

    public void setProperty(String key, String value) {
        userPreferences.setProperty(key, value);
    }

    public String getDefaultProperty(String key) {
        return defaultPreferences.getProperty(key);
    }

    public void setDefaultProperty(String key, String value) {
        defaultPreferences.setProperty(key, value);
    }

    public String getFreemindDirectory() {
        return System.getProperty("user.home") + File.separator + getProperty("properties_folder");
    }

    public void saveProperties(boolean pIsShutdown) {
//        try {
//            OutputStream out = new FileOutputStream(autoPropertiesFile);
//            final OutputStreamWriter outputStreamWriter = new OutputStreamWriter(out, "8859_1");
//            outputStreamWriter.write("#FreeMind ");
//            outputStreamWriter.write(VERSION.toString());
//            outputStreamWriter.write('\n');
//            outputStreamWriter.flush();
//            //to save as few props as possible.
//            Properties toBeStored = Tools.copyChangedProperties(userPreferences, defaultPreferences);
//            toBeStored.store(out, null);
//            out.close();
//        } catch (Exception ex) {
//            log.error(ex.getLocalizedMessage(), ex);
//        }
//        getController().getFilterController().saveConditions();
//        if (pIsShutdown && editServerService != null) {
//            editServerService.stopServer();
//        }
    }

    public MapView getView() {
        return controller.getView();
    }

    public void setView(MapView view) {
        scrollPane.setViewportView(view);
    }

    public MenuBar getFreeMindMenuBar() {
        return menuBar;
    }

    public void setStatusText(String msg) {
        if (statusLabel == null) {
            return;
        }

        statusLabel.setText(msg);
        statusMessageDisplayTimer.restart();
    }

    public void err(String msg) {
        setStatusText(msg);
    }

    public void actionPerformed(ActionEvent pE) {
        setStatusText("");
        statusMessageDisplayTimer.stop();
    }

    /**
     * Open URL in system browser
     * <p>
     * Opens the specified URL in the default browser for the operating system.
     *
     * @param url a url pointing to where the browser should open
     * @see URL
     */
    public void openDocument(URL url) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                // fix for https://sourceforge.net/p/freemind/discussion/22102/thread/cf032151/?limit=25#c631
                URI uri = new URI(url.toString().replaceAll("^file:////", "file://"));
                desktop.browse(uri);
            } catch (Exception e) {
                log.error("Caught: {}", String.valueOf(e));
            }
        }
    }

    public void setWaitingCursor(boolean waiting) {
        if (waiting) {
            getRootPane().getGlassPane().setCursor(
                    Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            getRootPane().getGlassPane().setVisible(true);
        } else {
            getRootPane().getGlassPane().setCursor(
                    Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            getRootPane().getGlassPane().setVisible(false);
        }
    }

    /**
     * Returns the ResourceBundle with the current language
     */
    public ResourceBundle getResources() {
        return freeMindCommon.getResources();
    }

    public String getResourceString(String resource) {
        return freeMindCommon.getResourceString(resource);
    }

    public String getResourceString(String key, String pDefault) {
        return freeMindCommon.getResourceString(key, pDefault);
    }

    public void go(String[] args) {
        try {
            int scale = this.getIntProperty(SCALING_FACTOR_PROPERTY, 100);
            if (scale != 100) {
                SwingUtils.scaleAllFonts(scale / 100f);
                Font SEGOE_UI_PLAIN_12 = new Font("Segoe UI", Font.PLAIN, 12 * scale / 100);
                UIManager.put("MenuItem.acceleratorFont", SEGOE_UI_PLAIN_12);
                UIManager.put("Menu.acceleratorFont", SEGOE_UI_PLAIN_12);
                UIManager.put("CheckBoxMenuItem.acceleratorFont", SEGOE_UI_PLAIN_12);
                UIManager.put("RadioButtonMenuItem.acceleratorFont", SEGOE_UI_PLAIN_12);
            }

            IFreeMindSplash splash = null;
            editServerService = new EditServerService(this);
            editServerService.checkForAnotherInstance(args);
            editServerService.initServer();
            final FeedBack feedBack;
            splash = new FreeMindSplashModern(this);
            splash.setVisible(true);
            feedBack = splash.getFeedBack();
            this.windowIcon = splash.getWindowIcon();

            feedBack.setMaximumValue(10 + this.getMaximumNumberOfMapsToLoad(args));
            this.init(feedBack);

            feedBack.increase("FreeMind.progress.startCreateController", null);
            final ModeController ctrl = this.createModeController(args);

            feedBack.increase(FREE_MIND_PROGRESS_LOAD_MAPS, null);

            this.loadMaps(args, ctrl, feedBack);

            SwingUtils.waitForEventQueue();
            feedBack.increase("FreeMind.progress.endStartup", null);
            // focus fix after startup.
            this.addWindowFocusListener(new WindowFocusListener() {

                public void windowLostFocus(WindowEvent e) {
                }

                public void windowGainedFocus(WindowEvent e) {
                    FreeMind.this.getController().obtainFocusForSelected();
                    FreeMind.this.removeWindowFocusListener(this);
                }
            });
            this.setVisible(true);
            if (splash != null) {
                splash.setVisible(false);
            }

            this.fireStartupDone();

        } catch (Exception e) {
            cantGo(e);
        }

    }

    static void cantGo(Exception e) {
        log.error(e.getLocalizedMessage(), e);
        showMessageDialog(
                null,
                format("FreeMind can't start: %s", e.getLocalizedMessage()),
                "Startup problem",
                JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }

    private void setupSpellChecking() {
        boolean checkSpelling
                = //			Resources.getInstance().getBoolProperty(FreeMindCommon.CHECK_SPELLING);
                Objects.equals("true", userPreferences.getProperty(FreeMindCommon.CHECK_SPELLING));
        if (checkSpelling) {
            try {
                // TODO filter languages in dictionaries.properties like this:
//				String[] languages = "en,de,es,fr,it,nl,pl,ru,ar".split(",");
//				for (int i = 0; i < languages.length; i++) {
//					System.out.println(new File("dictionary_" + languages[i] + ".ortho").exists());
//				}
                String decodedPath = Tools.getFreeMindBasePath();
                URL url = null;
                if (new File(decodedPath).exists()) {
                    url = new URL("file", null, decodedPath);
                }
                SpellChecker.registerDictionaries(url, Locale.getDefault().getLanguage());
            } catch (MalformedURLException e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    private void setupProxy() {
        // proxy settings
        if ("true".equals(userPreferences.getProperty(PROXY_USE_SETTINGS))) {
            if ("true".equals(userPreferences.getProperty(PROXY_IS_AUTHENTICATED))) {
                Authenticator.setDefault(new ProxyAuthenticator(userPreferences
                        .getProperty(PROXY_USER), EncryptionUtils.decompress(userPreferences
                        .getProperty(PROXY_PASSWORD))));
            }
            System.setProperty("http.proxyHost", userPreferences.getProperty(PROXY_HOST));
            System.setProperty("http.proxyPort", userPreferences.getProperty(PROXY_PORT));
            System.setProperty("https.proxyHost", userPreferences.getProperty(PROXY_HOST));
            System.setProperty("https.proxyPort", userPreferences.getProperty(PROXY_PORT));
            System.setProperty("http.nonProxyHosts", userPreferences.getProperty(PROXY_EXCEPTION));
        }
    }


    private void fireStartupDone() {
        startupDone = true;
        for (StartupDoneListener listener : startupDoneListeners) {
            listener.startupDone();
        }
    }

    private void setScreenBounds() {
        // Create the MenuBar

        menuBar = new MenuBar(controller);
        setJMenuBar(menuBar);

        // Create the scroll pane
        scrollPane = new MapView.ScrollPane();

        if (Resources.getInstance().getBoolProperty("no_scrollbar")) {
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        } else {
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        }
        statusLabel = new JLabel("!");
        statusLabel.setPreferredSize(statusLabel.getPreferredSize());
        statusLabel.setText("");

        statusMessageDisplayTimer = new Timer(TIME_TO_DISPLAY_MESSAGES, this);

        boolean shouldUseTabbedPane = Resources.getInstance().getBoolProperty(RESOURCES_USE_TABBED_PANE);

        if (shouldUseTabbedPane) {
            // tabbed panes eat control up. This is corrected here.
            InputMap map = (InputMap) UIManager.get("TabbedPane.ancestorInputMap");
            KeyStroke keyStrokeCtrlUp = KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.CTRL_DOWN_MASK);
            map.remove(keyStrokeCtrlUp);

            tabbedPane = new JTabbedPane();
            tabbedPane.setFocusable(false);
            controller.addTabbedPane(tabbedPane);
            getContentPane().add(tabbedPane, BorderLayout.CENTER);
        } else {
            // don't use tabbed panes.
            getContentPane().add(scrollPane, BorderLayout.CENTER);
        }

        windowService = new WindowService(this, scrollPane, tabbedPane);

        getContentPane().add(statusLabel, BorderLayout.SOUTH);

        // Disable the default close button, instead use windowListener
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                controller.quit.actionPerformed(new ActionEvent(this, 0, "quit"));
            }

            /*
             * fc, 14.3.2008: Completely removed, as it damaged the focus if for
             * example the note window was active.
             */
            // public void windowActivated(WindowEvent e) {
            // // This doesn't work the first time, it's called too early to
            // // get Focus
            // log.info("windowActivated");
            // if ((getView() != null) && (getView().getSelected() != null)) {
            // getView().getSelected().requestFocus();
            // }
            // }
        });

        if (Objects.equals(getProperty("toolbarVisible"), "false")) {
            controller.setToolbarVisible(false);
        }

        if (Objects.equals(getProperty("leftToolbarVisible"), "false")) {
            controller.setLeftToolbarVisible(false);
        }

        // first define the final layout of the screen:
        setFocusTraversalKeysEnabled(false);
        // and now, determine size, position and state.
        pack();
        // set the default size (PN)
        int windowWidth = getIntProperty("appwindow_width", 0);
        windowWidth = (windowWidth > 0) ? windowWidth : 640;

        int windowHeight = getIntProperty("appwindow_height", 0);
        windowHeight = (windowHeight > 0) ? windowHeight : 440;

        final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        final Insets screenInsets = defaultToolkit.getScreenInsets(getGraphicsConfiguration());

        Dimension screenSize = defaultToolkit.getScreenSize();

        final int screenWidth = screenSize.width - screenInsets.left - screenInsets.right;
        windowWidth = Math.min(windowWidth, screenWidth);

        final int screenHeight = screenSize.height - screenInsets.top - screenInsets.bottom;
        windowHeight = Math.min(windowHeight, screenHeight);

        int windowX = getIntProperty("appwindow_x", 0);
        windowX = Math.max(screenInsets.left, windowX);
        windowX = Math.min(screenWidth + screenInsets.left - windowWidth, windowX);

        int windowY = getIntProperty("appwindow_y", 0);
        windowY = Math.max(screenInsets.top, windowY);
        windowY = Math.min(screenWidth + screenInsets.top - windowHeight, windowY);
        setBounds(windowX, windowY, windowWidth, windowHeight);

        // set the default state (normal/maximized) (PN) (note: this must be done later when partucular initalizations
        // of the windows are ready, perhaps after setVisible is it enough... :-?

        int win_state = parseInt(userPreferences.getProperty("appwindow_state", "0"));
        win_state = ((win_state & ICONIFIED) == 0) ? win_state : NORMAL;
        setExtendedState(win_state);
    }

    private ModeController createModeController(final String[] args) {
        ModeController ctrl = controller.getModeController();
        // try to load mac module:
        try {
            Class<?> macClass = Class.forName("accessories.plugins.MacChanges");
            // lazy programming.
            // the mac class has exactly one constructor with a modeController.
            macClass.getConstructors()[0].newInstance(this);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return ctrl;
    }

    private int getMaximumNumberOfMapsToLoad(String[] args) {
        LastStateStorageManagement management = getLastStateStorageManagement();
        return Math.max(args.length + management.getLastOpenList().size(), 1);
    }

    private void loadMaps(final String[] args, ModeController pModeController, FeedBack pFeedBack) {

        boolean fileLoaded = false;
        if (Tools.isPreferenceTrue(getProperty(FreeMindCommon.LOAD_LAST_MAPS_AND_LAYOUT))) {

            MapModule mapToFocus = null;
            LastStateStorageManagement management = getLastStateStorageManagement();

            int index = 0;
            for (MindmapLastStateStorage store : management.getLastOpenList()) {
                String restorable = store.getRestorableName();
                pFeedBack.increase(FREE_MIND_PROGRESS_LOAD_MAPS_NAME, new Object[]{restorable.replaceAll(".*/", "")});
                try {
                    if (controller.getLastOpenedList().open(restorable)) {
                        if (index == management.getLastFocussedTab()) {
                            mapToFocus = controller.getMapModule();
                        }
                    }
                    fileLoaded = true;
                } catch (Exception e) {
                    log.error(e.getLocalizedMessage(), e);
                }
                index++;
            }
            if (mapToFocus != null) {
                controller.getMapModuleManager().changeToMapModule(mapToFocus.getDisplayName());
            }
        }

        for (String arg : args) {
            String fileArgument = arg;
            pFeedBack.increase(FREE_MIND_PROGRESS_LOAD_MAPS_NAME, new Object[]{fileArgument.replaceAll(".*/", "")});

            if (fileArgument.toLowerCase().endsWith(FreeMindCommon.FREEMIND_FILE_EXTENSION)) {

                if (!new File(fileArgument).isAbsolute()) {
                    fileArgument = format("%s%s%s", System.getProperty("user.dir"), System.getProperty("file.separator"), fileArgument);
                }

                try {
                    pModeController.load(new File(fileArgument));
                    fileLoaded = true;
                } catch (Exception ex) {
                    log.error("File {} not found error", fileArgument);
                }
            }
        }

        if (!fileLoaded) {
            fileLoaded = processLoadEventFromStartupPhase();
        }

        if (!fileLoaded) {
            String restoreable = getProperty(FreeMindCommon.ON_START_IF_NOT_SPECIFIED);
            if (Tools
                    .isPreferenceTrue(getProperty(FreeMindCommon.LOAD_LAST_MAP))
                    && restoreable != null && !restoreable.isEmpty()) {
                pFeedBack.increase(FREE_MIND_PROGRESS_LOAD_MAPS_NAME,
                        new Object[]{restoreable.replaceAll(".*/", "")});
                try {
                    controller.getLastOpenedList().open(restoreable);
                    controller.getModeController().getView().moveToRoot();
                    fileLoaded = true;
                } catch (Exception e) {
                    log.error(e.getLocalizedMessage(), e);
                    setStatusText("An error occured on opening the file: " + restoreable + ".");
                }
            }
        }

        if (!fileLoaded && Tools.isPreferenceTrue(getProperty(FreeMindCommon.LOAD_NEW_MAP))) {
            /*
             * nothing loaded so far. Perhaps, we should display a new map...
             * According to Summary: On first start FreeMind should show new map
             * to newbies
             * https://sourceforge.net/tracker/?func=detail&atid=107118
             * &aid=1752516&group_id=7118
             */
            pModeController.newMap();
            pFeedBack.increase(FREE_MIND_PROGRESS_LOAD_MAPS, null);
        }
    }

    private LastStateStorageManagement getLastStateStorageManagement() {
        String lastStateMapXml = getProperty(FreeMindCommon.MINDMAP_LAST_STATE_MAP_STORAGE);
        LastStateStorageManagement management = new LastStateStorageManagement(lastStateMapXml);
        return management;
    }

    /**
     * Iterates over the load events from the startup phase
     * <p>
     * More than one file can be opened during startup. The filenames are stored
     * in numbered properties, i.e.
     * <ul>
     * loadEventDuringStartup0=/Users/alex/Desktop/test1.mm
     * loadEventDuringStartup1=/Users/alex/Desktop/test2.mm
     * </ul>
     *
     * @return true if at least one file has been loaded
     */
    private boolean processLoadEventFromStartupPhase() {
        boolean atLeastOneFileHasBeenLoaded = false;
        int count = 0;
        while (true) {
            String propertyKey = FreeMindCommon.LOAD_EVENT_DURING_STARTUP + count;
            if (getProperty(propertyKey) == null) {
                break;
            } else {
                if (processLoadEventFromStartupPhase(propertyKey)) {
                    atLeastOneFileHasBeenLoaded = true;
                }
                ++count;
            }
        }
        return atLeastOneFileHasBeenLoaded;
    }

    private boolean processLoadEventFromStartupPhase(String propertyKey) {
        String filename = getProperty(propertyKey);
        try {
            controller.getModeController().load(Tools.fileToUrl(new File(filename)));
            // remove temporary property because we do not want to store in a
            // file and survive restart
            getProperties().remove(propertyKey);
            return true;
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            setStatusText("An error occured on opening the file: " + filename + ".");
            return false;
        }
    }

    public JFrame getJFrame() {
        return this;
    }

    public ClassLoader getFreeMindClassLoader() {
        return freeMindCommon.getFreeMindClassLoader();
    }

    public String getFreemindBaseDir() {
        return freeMindCommon.getFreemindBaseDir();
    }

    public String getAdjustableProperty(String label) {
        return freeMindCommon.getAdjustableProperty(label);
    }

    public JSplitPane insertComponentIntoSplitPane(JComponent pMindMapComponent) {
        return windowService.insertComponentIntoSplitPane(pMindMapComponent);
    }

    public void removeSplitPane() {
        windowService.removeSplitPane();
    }

    public JComponent getContentComponent() {
        return windowService.getContentComponent();
    }

    public void registerStartupDoneListener(StartupDoneListener pStartupDoneListener) {
        if (!startupDone) {
            startupDoneListeners.add(pStartupDoneListener);
        }
    }

}
