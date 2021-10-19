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
/*$Id: Controller.java,v 1.40.14.21.2.64 2010/02/22 21:18:53 christianfoltin Exp $*/

package freemind.controller;

import freemind.common.BooleanProperty;
import freemind.common.JOptionalSplitPane;
import freemind.controller.actions.generated.instance.MindmapLastStateStorage;
import freemind.controller.filter.FilterController;
import freemind.controller.printpreview.PreviewDialog;
import freemind.main.*;
import freemind.model.MindMap;
import freemind.modes.Mode;
import freemind.modes.ModeController;
import freemind.modes.ModesCreator;
import freemind.modes.browsemode.BrowseMode;
import freemind.preferences.FreemindPropertyListener;
import freemind.preferences.layout.OptionPanel;
import freemind.view.ImageFactory;
import freemind.view.MapModule;
import freemind.view.mindmapview.MapView;
import lombok.extern.log4j.Log4j2;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;
import java.util.*;

import static java.lang.String.valueOf;
import static java.util.Collections.swap;
import static java.util.stream.IntStream.range;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 * Provides the methods to edit/change a Node.
 * Forwards all messages to MapModel(editing) or MapView(navigation).
 */
@Log4j2
public class Controller implements MapModuleChangeObserver {

    private static final String PAGE_FORMAT_PROPERTY = "page_format";

    private final HashSet<MapTitleChangeListener> mMapTitleChangeListenerSet = new HashSet<>();
    private final HashSet<MapTitleContributor> mMapTitleContributorSet = new HashSet<>();
    private final HashSet<ZoomListener> mZoomListenerSet = new HashSet<>();

    /**
     * Converts from a local link to the real file URL of the documentation map. (Used to change this behaviour under MacOSX).
     * Used for MAC!!!
     */
    public static LocalLinkConverter localDocumentationLinkConverter;
    private static final JColorChooser colorChooser = new JColorChooser();
    private LastOpenedList lastOpened; // A list of the path names of all the maps

    // that were opened in the last time
    private MapModuleManager mapModuleManager;

    /**
     * The current mode
     */
    private Mode mMode;

    private FreeMindMain frame;

    private MainToolBar toolbar;

    private NodeMouseMotionListener nodeMouseMotionListener;
    private NodeMotionListener nodeMotionListener;
    private NodeKeyListener nodeKeyListener;
    private NodeDragListener nodeDragListener;
    private NodeDropListener nodeDropListener;
    private MapMouseMotionListener mapMouseMotionListener;
    private MapMouseWheelListener mapMouseWheelListener;

    private final ModesCreator modesCreator = new ModesCreator(this);

    private PageFormat pageFormat = null;
    private PrinterJob printerJob = null;
    private final Map<String, Font> fontMap = new HashMap<>();

    private FilterController filterController;

    boolean isPrintingAllowed = true;
    boolean menubarVisible = true;
    boolean toolbarVisible = true;
    boolean leftToolbarVisible = true;

    public CloseAction close;
    public Action print;
    public Action printDirect;
    public Action printPreview;
    public Action page;
    public Action quit;

    public OptionAntialiasAction optionAntialiasAction;
    public Action optionHTMLExportFoldingAction;
    public Action optionSelectionMechanismAction;

    public Action about;
    public Action faq;
    public Action keyDocumentation;
    public Action webDocu;
    public Action documentation;
    public Action license;
    public Action showFilterToolbarAction;
    public Action navigationPreviousMap;
    public Action navigationNextMap;
    public Action navigationMoveMapLeftAction;
    public Action navigationMoveMapRightAction;

    public Action moveToRoot;
    public Action toggleMenubar;
    public Action toggleToolbar;
    public Action toggleLeftToolbar;

    public Action zoomIn;
    public Action zoomOut;

    public Action showSelectionAsRectangle;
    public PropertyAction propertyAction;
    public OpenURLAction freemindUrl;

    private static final float[] zoomValues = {0.25f, 0.5f, 0.75f, 1.0f, 1.5f, 2.0f, 3.0f, 4.0f};

    private static final List<FreemindPropertyListener> propertyChangeListeners = new ArrayList<>();

    private List<MapModule> tabbedPaneMapModules;
    private JTabbedPane tabbedPane;

    private boolean mTabbedPaneSelectionUpdate = true;

    public Controller(FreeMindMain frame) {
        this.frame = frame;
    }

    public void init() {
        initialization();

        nodeMouseMotionListener = new NodeMouseMotionListener(this);
        nodeMotionListener = new NodeMotionListener(this);
        nodeKeyListener = new NodeKeyListener(this);
        nodeDragListener = new NodeDragListener(this);
        nodeDropListener = new NodeDropListener(this);

        mapMouseMotionListener = new MapMouseMotionListener(this);
        mapMouseWheelListener = new MapMouseWheelListener(this);

        close = new CloseAction(this);
        print = new PrintAction(this, true);
        printDirect = new PrintAction(this, false);
        printPreview = new PrintPreviewAction(this);
        page = new PageAction(this);
        quit = new QuitAction(this);
        about = new AboutAction(this);
        freemindUrl = new OpenURLAction(this, getResourceString("FreeMind"), getProperty("webFreeMindLocation"));
        faq = new OpenURLAction(this, getResourceString("FAQ"), getProperty("webFAQLocation"));
        keyDocumentation = new KeyDocumentationAction(this);
        webDocu = new OpenURLAction(this, getResourceString("webDocu"), getProperty("webDocuLocation"));
        documentation = new DocumentationAction(this);
        license = new LicenseAction(this);
        navigationPreviousMap = new NavigationPreviousMapAction(this);
        navigationNextMap = new NavigationNextMapAction(this);
        navigationMoveMapLeftAction = new NavigationMoveMapLeftAction(this);
        navigationMoveMapRightAction = new NavigationMoveMapRightAction(this);
        showFilterToolbarAction = new ShowFilterToolbarAction(this);
        toggleMenubar = new ToggleMenubarAction(this);
        toggleToolbar = new ToggleToolbarAction(this);
        toggleLeftToolbar = new ToggleLeftToolbarAction(this);
        optionAntialiasAction = new OptionAntialiasAction();
        optionHTMLExportFoldingAction = new OptionHTMLExportFoldingAction(this);
        optionSelectionMechanismAction = new OptionSelectionMechanismAction(this);

        zoomIn = new ZoomInAction(this);
        zoomOut = new ZoomOutAction(this);
        propertyAction = new PropertyAction(this);

        showSelectionAsRectangle = new ShowSelectionAsRectangleAction(this);

        moveToRoot = new MoveToRootAction(this);

        // Create the ToolBar
        JPanel northToolbarPanel = new JPanel(new BorderLayout());
        toolbar = new MainToolBar(this);
        filterController = new FilterController(this);
        JToolBar filterToolbar = filterController.getFilterToolbar();

        getFrame().getContentPane().add(northToolbarPanel, BorderLayout.NORTH);
        northToolbarPanel.add(toolbar, BorderLayout.NORTH);
        northToolbarPanel.add(filterToolbar, BorderLayout.SOUTH);

        setAllActions(false);

    }

    /**
     * Does basic initializations of this class. Normally, init is called, but
     * if you don't need the actions, call this method instead.
     */
    public void initialization() {
        /* Arranges the keyboard focus especially after opening FreeMind */
        KeyboardFocusManager focusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        focusManager.addPropertyChangeListener(event -> {
            if ("focusOwner".equals(event.getPropertyName())) {
                Component comp = (Component) event.getNewValue();
                if (comp instanceof FreeMindMain) {
                    obtainFocusForSelected();
                }
            }
        });

        localDocumentationLinkConverter = new DefaultLocalLinkConverter();
        lastOpened = new LastOpenedList(this, getProperty("lastOpened"));

        mapModuleManager = new MapModuleManager(this);
        mapModuleManager.addListener(this);

        final String DEFAULT_FONT_PROPERTY = "defaultfont";
        final String defaultFont = getProperty(DEFAULT_FONT_PROPERTY);

        if (!Tools.isAvailableFontFamily(defaultFont)) {
            log.warn("The font you have set as standard - " + defaultFont + " - is not available.");
            frame.setProperty(DEFAULT_FONT_PROPERTY, "SansSerif");
        }
    }

    public String getProperty(String property) {
        return frame.getProperty(property);
    }

    public int getIntProperty(String property, int defaultValue) {
        return frame.getIntProperty(property, defaultValue);
    }

    public void setProperty(String property, String value) {
        String oldValue = getProperty(property);
        getFrame().setProperty(property, value);
        firePropertyChanged(property, value, oldValue);
    }

    private void firePropertyChanged(String property, String value, String oldValue) {
        if (oldValue == null || !oldValue.equals(value)) {
            for (FreemindPropertyListener listener : Controller.getPropertyChangeListeners()) {
                listener.propertyChanged(property, value, oldValue);
            }
        }
    }

    public FreeMindMain getFrame() {
        return frame;
    }

    public JFrame getJFrame() {
        FreeMindMain f = getFrame();
        if (f instanceof JFrame)
            return (JFrame) f;
        return null;
    }

    public URL getResource(String resource) {
        return getFrame().getResource(resource);
    }

    public String getResourceString(String resource) {
        return frame.getResourceString(resource);
    }

    /**
     * @return the current modeController, or null, if FreeMind is just starting
     * and there is no modeController present.
     */
    public ModeController getModeController() {
        if (getMapModule() != null) {
            return getMapModule().getModeController();
        }
        if (getMode() != null) {
            // no map present: we take the default:
            return getMode().getDefaultModeController();
        }
        return null;
    }

    /**
     * Returns the current model
     */
    public MindMap getModel() {
        if (getMapModule() == null) {
            return null;
        }

        return getMapModule().getModel();
    }

    public MapView getView() {
        return getMapModule() == null ? null : getMapModule().getView();
    }

    Set<String> getModes() {
        return modesCreator.getAllModes();
    }

    public Mode getMode() {
        return mMode;
    }

    public String[] getZooms() {
        String[] zooms = new String[zoomValues.length];
        for (int i = 0; i < zoomValues.length; i++) {
            float val = zoomValues[i];
            zooms[i] = (int) (val * 100f) + "%";
        }
        return zooms;
    }

    public MapModuleManager getMapModuleManager() {
        return mapModuleManager;
    }

    public LastOpenedList getLastOpenedList() {
        return lastOpened;
    }

    public MapModule getMapModule() {
        return getMapModuleManager().getMapModule();
    }

    private JToolBar getToolBar() {
        return toolbar;
    }

    public Font getFontThroughMap(Font font) {
        if (!fontMap.containsKey(getFontStringCode(font))) {
            fontMap.put(getFontStringCode(font), font);
        }
        return fontMap.get(getFontStringCode(font));
    }

    private String getFontStringCode(Font font) {
        return font.toString() + "/" + font.getAttributes().get(TextAttribute.STRIKETHROUGH);
    }

    public Font getDefaultFont() {
        // Maybe implement handling for cases when the font is not
        // available on this system.

        int fontSize = getDefaultFontSize();
        int fontStyle = getDefaultFontStyle();
        String fontFamily = getDefaultFontFamilyName();

        return getFontThroughMap(new Font(fontFamily, fontStyle, fontSize));
    }

    public String getDefaultFontFamilyName() {
        return getProperty("defaultfont");
    }

    public int getDefaultFontStyle() {
        return frame.getIntProperty("defaultfontstyle", 0);
    }

    public int getDefaultFontSize() {
        return frame.getIntProperty("defaultfontsize", 12);
    }

    /**
     * Static JColorChooser to have the recent colors feature.
     */
    static public JColorChooser getCommonJColorChooser() {
        return colorChooser;
    }

    public static Color showCommonJColorChooserDialog(Component component, String title, Color initialColor) throws HeadlessException {

        final JColorChooser pane = getCommonJColorChooser();
        pane.setColor(initialColor);

        ColorTracker ok = new ColorTracker(pane);
        JDialog dialog = JColorChooser.createDialog(component, title, true, pane, ok, null);
        dialog.addWindowListener(new Closer());
        dialog.addComponentListener(new DisposeOnClose());

        dialog.setVisible(true);
        // blocks until user brings dialog down...

        return ok.getColor();
    }

    private static class ColorTracker implements ActionListener, Serializable {
        JColorChooser chooser;
        Color color;

        public ColorTracker(JColorChooser c) {
            chooser = c;
        }

        public void actionPerformed(ActionEvent e) {
            color = chooser.getColor();
        }

        public Color getColor() {
            return color;
        }
    }

    static class Closer extends WindowAdapter implements Serializable {
        public void windowClosing(WindowEvent e) {
            Window w = e.getWindow();
            w.setVisible(false);
        }
    }

    static class DisposeOnClose extends ComponentAdapter implements Serializable {
        public void componentHidden(ComponentEvent e) {
            Window w = (Window) e.getComponent();
            w.dispose();
        }
    }

    public boolean isMapModuleChangeAllowed(MapModule oldMapModule, Mode oldMode, MapModule newMapModule, Mode newMode) {
        return true;
    }

    public void afterMapClose(MapModule pOldMapModule, Mode pOldMode) {
    }

    public void beforeMapModuleChange(MapModule oldMapModule, Mode oldMode, MapModule newMapModule, Mode newMode) {
        ModeController oldModeController;
        this.mMode = newMode;
        if (oldMapModule == null) {
            if (oldMode == null) {
                return;
            } else {
                oldModeController = oldMode.getDefaultModeController();
            }
        } else {
            // shut down screens of old view + frame
            oldModeController = oldMapModule.getModeController();
            oldModeController.setVisible(false);
            oldModeController.shutdownController();
        }

        if (oldModeController.getModeToolBar() != null) {
            toolbar.remove(oldModeController.getModeToolBar());
            toolbar.activate(true);
            // northToolbarPanel.remove(oldModeController.getModeToolBar());
            // northToolbarPanel.add(toolbar, BorderLayout.NORTH);
        }
        /* other toolbars are to be removed too. */
        if (oldModeController.getLeftToolBar() != null) {
            getFrame().getContentPane().remove(
                    oldModeController.getLeftToolBar());
        }
    }

    @Override
    public void afterMapModuleChange(MapModule oldMapModule, Mode oldMode, MapModule newMapModule, Mode newMode) {
        ModeController newModeController;
        if (newMapModule == null) {
            newModeController = newMode.getDefaultModeController();
            getFrame().setView(null);
            setAllActions(false);
        } else {
            getFrame().setView(newMapModule.getView());
            setAllActions(true);
            if ((getView().getSelected() == null)) {
                // moveToRoot();
                getView().selectAsTheOnlyOneSelected(getView().getRoot());
            }
            lastOpened.mapOpened(newMapModule);
            changeZoomValueProperty(newMapModule.getView().getZoom());
            // ((MainToolBar) getToolbar()).setZoomComboBox(zoomValue);
            // old
            // obtainFocusForSelected();
            newModeController = newMapModule.getModeController();
            newModeController.startupController();
            newModeController.setVisible(true);
            // old
            // obtainFocusForSelected();
        }

        setTitle();

        JToolBar newToolBar = newModeController.getModeToolBar();
        if (newToolBar != null) {
            toolbar.activate(false);
            toolbar.add(newToolBar, 0);
            // northToolbarPanel.remove(toolbar);
            // northToolbarPanel.add(newToolBar, BorderLayout.NORTH);
            newToolBar.repaint();
        }
        /* new left toolbar. */
        Component newLeftToolBar = newModeController.getLeftToolBar();
        if (newLeftToolBar != null) {
            getFrame().getContentPane().add(newLeftToolBar, BorderLayout.WEST);
            if (leftToolbarVisible) {
                newLeftToolBar.setVisible(true);
                newLeftToolBar.repaint();
            } else {
                newLeftToolBar.setVisible(false);
            }
        }
        toolbar.validate();
        toolbar.repaint();
        MenuBar menuBar = getFrame().getFreeMindMenuBar();
        menuBar.updateMenus(newModeController);
        menuBar.revalidate();
        menuBar.repaint();
        // new
        obtainFocusForSelected();
    }

    protected void changeZoomValueProperty(final float zoomValue) {
        for (ZoomListener listener : mZoomListenerSet) {
            listener.setZoom(zoomValue);
        }
    }

    public void numberOfOpenMapInformation(int number, int pIndex) {
        navigationPreviousMap.setEnabled(number > 0);
        navigationNextMap.setEnabled(number > 0);
        log.info("number " + number + ", pIndex " + pIndex);
        navigationMoveMapLeftAction.setEnabled(number > 1 && pIndex > 0);
        navigationMoveMapRightAction.setEnabled(number > 1
                && pIndex < number - 1);
    }

    /**
     * Creates a new modeName (controller), activates the toolbars, title and
     * deactivates all actions. Does nothing, if the modeName is identical to the
     * current modeName.
     *
     * @return false if the change was not successful.
     */
    public boolean createNewMode(String modeName) {
        final Mode mode = getMode();
        if (mode != null && modeName.equals(mode.toString())) {
            return true;
        }

        // Check if the modeName is available and create ModeController.
        Mode newMode = modesCreator.getMode(modeName);
        if (newMode == null) {
            errorMessage(getResourceString("mode_na") + ": " + modeName);
            return false;
        }

        // change the map module to get changed toolbars etc.:
        getMapModuleManager().setMapModule(null, newMode);

        setTitle();
        getMode().activate();

        MessageFormat formatter = new MessageFormat(getResourceString("mode_status"));
        getFrame().setStatusText(formatter.format(new Object[]{getMode().toLocalizedString()}));

        return true;
    }

    public void setMenubarVisible(boolean visible) {
        menubarVisible = visible;
        getFrame().getFreeMindMenuBar().setVisible(menubarVisible);
    }

    public void setToolbarVisible(boolean visible) {
        toolbarVisible = visible;
        toolbar.setVisible(toolbarVisible);
    }

    /**
     * @return Returns the main toolbar.
     */
    public JToolBar getToolbar() {
        return toolbar;
    }

    public void setLeftToolbarVisible(boolean visible) {
        leftToolbarVisible = visible;
        if (getMode() == null) {
            return;
        }
        final Component leftToolBar = getModeController().getLeftToolBar();
        if (leftToolBar != null) {
            leftToolBar.setVisible(leftToolbarVisible);
            leftToolBar.getParent().revalidate();
        }
    }

    public NodeKeyListener getNodeKeyListener() {
        return nodeKeyListener;
    }

    public NodeMouseMotionListener getNodeMouseMotionListener() {
        return nodeMouseMotionListener;
    }

    public NodeMotionListener getNodeMotionListener() {
        return nodeMotionListener;
    }

    public MapMouseMotionListener getMapMouseMotionListener() {
        return mapMouseMotionListener;
    }

    public MapMouseWheelListener getMapMouseWheelListener() {
        return mapMouseWheelListener;
    }

    public NodeDragListener getNodeDragListener() {
        return nodeDragListener;
    }

    public NodeDropListener getNodeDropListener() {
        return nodeDropListener;
    }

    public void setFrame(FreeMindMain frame) {
        this.frame = frame;
    }

    void moveToRoot() {
        if (getMapModule() != null) {
            getView().moveToRoot();
        }
    }

    /**
     * Closes the actual map.
     *
     * @param force true= without save.
     */
    public void close(boolean force) {
        getMapModuleManager().close(force, null);
    }

    public void informationMessage(Object message) {
        showMessageDialog(getFrame().getContentPane(), message.toString(), "FreeMind", JOptionPane.INFORMATION_MESSAGE);
    }

    public void informationMessage(Object message, JComponent component) {
        showMessageDialog(component, message.toString(), "FreeMind", JOptionPane.INFORMATION_MESSAGE);
    }

    public void errorMessage(Object message) {
        String myMessage;

        if (message != null) {
            myMessage = message.toString();
        } else {
            myMessage = getResourceString("undefined_error");
            if (myMessage == null) {
                myMessage = "Undefined error";
            }
        }
        showMessageDialog(getFrame().getContentPane(), myMessage, "FreeMind", JOptionPane.ERROR_MESSAGE);

    }

    public void errorMessage(Object message, JComponent component) {
        showMessageDialog(component, message.toString(), "FreeMind", JOptionPane.ERROR_MESSAGE);
    }

    public void obtainFocusForSelected() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
        if (getView() == null) {
            // fc, 6.1.2004: bug fix, that open and quit are not working if no
            // map is present.
            // to avoid this, the menu bar gets the focus, and everything seems
            // to be all right!!
            // but I cannot avoid thinking of this change to be a bad hack ....
            log.info("No view present. No focus!");
            getFrame().getFreeMindMenuBar().requestFocus();
        } else { // is null if the last map was closed.
            log.trace("Requesting Focus for " + getView() + " in model " + getView().getModel());
            getView().requestFocusInWindow();
        }
    }

    public void setZoom(float zoom) {
        getView().setZoom(zoom);
        changeZoomValueProperty(zoom);
        // ((MainToolBar) toolbar).setZoomComboBox(zoom);
        // show text in status bar:
        Object[] messageArguments = {valueOf(zoom * 100f)};
        String stringResult = Resources.getInstance().format("user_defined_zoom_status_bar", messageArguments);
        getFrame().setStatusText(stringResult);
    }

    /**
     * Set the Frame title with mode and file if exist
     */
    public void setTitle() {
        Object[] messageArguments = {getMode().toLocalizedString()};
        MessageFormat formatter = new MessageFormat(getResourceString("mode_title"));

        String title = formatter.format(messageArguments);

        String rawTitle = "";
        MindMap model = null;
        MapModule mapModule = getMapModule();
        if (mapModule != null) {
            model = mapModule.getModel();
            rawTitle = mapModule.toString();
            title = rawTitle
                    + (model.isSaved() ? "" : "*")
                    + " - "
                    + title
                    + (model.isReadOnly() ? " ("
                    + getResourceString("read_only") + ")" : "");
            File file = model.getFile();
            if (file != null) {
                title += " " + file.getAbsolutePath();
            }
            for (MapTitleContributor contributor : mMapTitleContributorSet) {
                title = contributor.getMapTitle(title, mapModule, model);
            }

        }
        getFrame().setTitle(title);
        for (MapTitleChangeListener listener : mMapTitleChangeListenerSet) {
            listener.setMapTitle(rawTitle, mapModule, model);
        }
    }

    public void registerMapTitleChangeListener(MapTitleChangeListener pMapTitleChangeListener) {
        mMapTitleChangeListenerSet.add(pMapTitleChangeListener);
    }

    public void unregisterMapTitleChangeListener(MapTitleChangeListener pMapTitleChangeListener) {
        mMapTitleChangeListenerSet.remove(pMapTitleChangeListener);
    }

    public void registerZoomListener(ZoomListener pZoomListener) {
        mZoomListenerSet.add(pZoomListener);
    }

    public void unregisterZoomListener(ZoomListener pZoomListener) {
        mZoomListenerSet.remove(pZoomListener);
    }

    public void registerMapTitleContributor(MapTitleContributor pMapTitleContributor) {
        mMapTitleContributorSet.add(pMapTitleContributor);
    }

    public void unregisterMapTitleContributor(MapTitleContributor pMapTitleContributor) {
        mMapTitleContributorSet.remove(pMapTitleContributor);
    }

    /**
     * Manage the availability of all Actions dependent of whether there is a map or not
     */
    public void setAllActions(boolean enabled) {
        print.setEnabled(enabled && isPrintingAllowed);
        printDirect.setEnabled(enabled && isPrintingAllowed);
        printPreview.setEnabled(enabled && isPrintingAllowed);
        page.setEnabled(enabled && isPrintingAllowed);
        close.setEnabled(enabled);
        moveToRoot.setEnabled(enabled);
        ((MainToolBar) getToolBar()).setAllActions(enabled);
        showSelectionAsRectangle.setEnabled(enabled);
    }

    //
    // program/map control
    //

    private void quit() {
        String currentMapRestorable = (getModel() != null) ? getModel()
                .getRestorable() : null;
        storeOptionSplitPanePosition();
        // collect all maps:
        List<String> restorables = new ArrayList<>();
        // move to first map in the window.
        List<MapModule> mapModuleList = getMapModuleManager().getMapModuleList();
        if (mapModuleList.size() > 0) {
            String displayName = mapModuleList.get(0)
                    .getDisplayName();
            getMapModuleManager().changeToMapModule(displayName);
        }
        while (mapModuleList.size() > 0) {
            if (getMapModule() != null) {
                StringBuffer restorableBuffer = new StringBuffer();
                boolean closingNotCancelled = getMapModuleManager().close(
                        false, restorableBuffer);
                if (!closingNotCancelled) {
                    return;
                }
                if (restorableBuffer.length() != 0) {
                    String restorableString = restorableBuffer.toString();
                    log.info("Closed the map " + restorableString);
                    restorables.add(restorableString);
                }
            } else {
                // map module without view open.
                // FIXME: This seems to be a bad hack. correct me!
                getMapModuleManager().nextMapModule();
            }
        }
        // store last tab session:
        int index = 0;

        String lastStateMapXml = getProperty(FreeMindCommon.MINDMAP_LAST_STATE_MAP_STORAGE);
        LastStateStorageManagement management = new LastStateStorageManagement(lastStateMapXml);
        management.setLastFocussedTab(-1);
        management.clearTabIndices();
        for (String restorable : restorables) {
            MindmapLastStateStorage storage = management.getStorage(restorable);
            if (storage != null) {
                storage.setTabIndex(index);
            }
            if (Tools.safeEquals(restorable, currentMapRestorable)) {
                management.setLastFocussedTab(index);
            }
            index++;
        }
        setProperty(FreeMindCommon.MINDMAP_LAST_STATE_MAP_STORAGE, management.getXml());

        String lastOpenedString = lastOpened.save();
        setProperty("lastOpened", lastOpenedString);
        getFrame().setProperty(FreeMindCommon.ON_START_IF_NOT_SPECIFIED,
                currentMapRestorable != null ? currentMapRestorable : "");
        // getFrame().setProperty("menubarVisible",menubarVisible ? "true" :
        // "false");
        // ^ Not allowed in application because of problems with not working key
        // shortcuts
        setProperty("toolbarVisible", toolbarVisible ? "true" : "false");
        setProperty("leftToolbarVisible", leftToolbarVisible ? "true" : "false");
        if (!getFrame().isApplet()) {
            final int winState = getFrame().getWinState();
            if (JFrame.MAXIMIZED_BOTH != (winState & JFrame.MAXIMIZED_BOTH)) {
                setProperty("appwindow_x", valueOf(getFrame().getWinX()));
                setProperty("appwindow_y", valueOf(getFrame().getWinY()));
                setProperty("appwindow_width",
                        valueOf(getFrame().getWinWidth()));
                setProperty("appwindow_height",
                        valueOf(getFrame().getWinHeight()));
            }
            setProperty("appwindow_state", valueOf(winState));
        }
        // Stop edit server!
        getFrame().saveProperties(true);
        // save to properties
        System.exit(0);
    }

    private boolean acquirePrinterJobAndPageFormat() {
        if (printerJob == null) {
            try {
                printerJob = PrinterJob.getPrinterJob();
            } catch (SecurityException ex) {
                isPrintingAllowed = false;
                return false;
            }
        }
        if (pageFormat == null) {
            pageFormat = printerJob.defaultPage();
        }
        if (Tools.safeEquals(getProperty("page_orientation"), "landscape")) {
            pageFormat.setOrientation(PageFormat.LANDSCAPE);
        } else if (Tools
                .safeEquals(getProperty("page_orientation"), "portrait")) {
            pageFormat.setOrientation(PageFormat.PORTRAIT);
        } else if (Tools.safeEquals(getProperty("page_orientation"),
                "reverse_landscape")) {
            pageFormat.setOrientation(PageFormat.REVERSE_LANDSCAPE);
        }
        String pageFormatProperty = getProperty(PAGE_FORMAT_PROPERTY);
        if (!pageFormatProperty.isEmpty()) {
            log.info("Page format (stored): " + pageFormatProperty);
            final Paper storedPaper = new Paper();
            Tools.setPageFormatFromString(storedPaper, pageFormatProperty);
            pageFormat.setPaper(storedPaper);
        }
        return true;
    }

    // ////////////
    // Inner Classes
    // //////////

    /**
     * Manages the history of visited maps. Maybe explicitly closed maps should
     * be removed from History too?
     */

    //
    // program/map control
    //

    private class QuitAction extends AbstractAction {
        QuitAction(Controller controller) {
            super(controller.getResourceString("quit"));
        }

        public void actionPerformed(ActionEvent e) {
            quit();
        }
    }

    /**
     * This closes only the current map
     */
    public static class CloseAction extends AbstractAction {
        private final Controller controller;

        CloseAction(Controller controller) {
            Tools.setLabelAndMnemonic(this, controller.getResourceString("close"));
            this.controller = controller;
        }

        public void actionPerformed(ActionEvent e) {
            controller.close(false);
        }
    }

    private class PrintAction extends AbstractAction {

        boolean isDlg;

        PrintAction(Controller controller, boolean isDlg) {
            super(isDlg ? controller.getResourceString("print_dialog")
                    : controller.getResourceString("print"), freemind.view.ImageFactory.getInstance().createIcon(
                    getResource("images/fileprint.png")));
            setEnabled(false);
            this.isDlg = isDlg;
        }

        public void actionPerformed(ActionEvent e) {
            if (!acquirePrinterJobAndPageFormat()) {
                return;
            }

            printerJob.setPrintable(getView(), pageFormat);

            if (!isDlg || printerJob.printDialog()) {
                try {
                    frame.setWaitingCursor(true);
                    printerJob.print();
                    storePageFormat();
                } catch (Exception ex) {
                    log.error(ex);
                } finally {
                    frame.setWaitingCursor(false);
                }
            }
        }
    }

    private class PrintPreviewAction extends AbstractAction {
        Controller controller;

        PrintPreviewAction(Controller controller) {
            super(controller.getResourceString("print_preview"));
            this.controller = controller;
        }

        public void actionPerformed(ActionEvent e) {
            if (!acquirePrinterJobAndPageFormat()) {
                return;
            }
            PreviewDialog previewDialog = new PreviewDialog(
                    controller.getResourceString("print_preview_title"),
                    getView(),
                    getPageFormat());
            previewDialog.pack();
            previewDialog.setLocationRelativeTo(JOptionPane
                    .getFrameForComponent(getView()));
            previewDialog.setVisible(true);
        }
    }

    private class PageAction extends AbstractAction {

        PageAction(Controller controller) {
            super(controller.getResourceString("page"));
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            if (!acquirePrinterJobAndPageFormat()) {
                return;
            }

            // Ask about custom printing settings
            final JDialog dialog = new JDialog((JFrame) getFrame(),
                    getResourceString("printing_settings"), /* modal= */true);
            final JCheckBox fitToPage = new JCheckBox(getResourceString("fit_to_page"), Resources.getInstance().getBoolProperty("fit_to_page"));
            final JLabel userZoomL = new JLabel(getResourceString("user_zoom"));
            final JTextField userZoom = new JTextField(getProperty("user_zoom"), 3);
            userZoom.setEditable(!fitToPage.isSelected());
            final JButton okButton = new JButton();
            Tools.setLabelAndMnemonic(okButton, getResourceString("ok"));

            JPanel panel = new JPanel();

            GridBagLayout gridbag = new GridBagLayout();
            GridBagConstraints c = new GridBagConstraints();

            final Integer[] eventSource = new Integer[]{0};
            okButton.addActionListener(event -> {
                eventSource[0] = 1;
                dialog.dispose();
            });
            fitToPage.addItemListener(e12 -> userZoom.setEditable(e12.getStateChange() == ItemEvent.DESELECTED));

            // c.weightx = 0.5;
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 2;
            gridbag.setConstraints(fitToPage, c);
            panel.add(fitToPage);
            c.gridy = 1;
            c.gridwidth = 1;
            gridbag.setConstraints(userZoomL, c);
            panel.add(userZoomL);
            c.gridx = 1;
            c.gridwidth = 1;
            gridbag.setConstraints(userZoom, c);
            panel.add(userZoom);
            c.gridy = 2;
            c.gridx = 0;
            c.gridwidth = 3;
            c.insets = new Insets(10, 0, 0, 0);
            gridbag.setConstraints(okButton, c);
            panel.add(okButton);
            panel.setLayout(gridbag);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setContentPane(panel);
            dialog.setLocationRelativeTo((JFrame) getFrame());
            dialog.getRootPane().setDefaultButton(okButton);
            dialog.pack(); // calculate the size
            dialog.setVisible(true);

            if (eventSource[0] == 1) {
                setProperty("user_zoom", userZoom.getText());
                setProperty("fit_to_page", fitToPage.isSelected() ? "true" : "false");
            } else
                return;

            // Ask user for page format (e.g., portrait/landscape)
            pageFormat = printerJob.pageDialog(pageFormat);
            storePageFormat();
        }
    }

    public interface LocalLinkConverter {
        URL convertLocalLink(String link) throws MalformedURLException;
    }

    private class DefaultLocalLinkConverter implements LocalLinkConverter {

        public URL convertLocalLink(String map) throws MalformedURLException {
            /* new handling for relative urls. fc, 29.10.2003. */
            String applicationPath = frame.getFreemindBaseDir();
            // remove "." and make url
            return Tools.fileToUrl(new File(applicationPath + map.substring(1)));
            /* end: new handling for relative urls. fc, 29.10.2003. */
        }
    }

    private class DocumentationAction extends AbstractAction {
        Controller controller;

        DocumentationAction(Controller controller) {
            super(controller.getResourceString("documentation"));
            this.controller = controller;
        }

        public void actionPerformed(ActionEvent event) {
            try {
                String map = controller.getFrame().getResourceString("browsemode_initial_map");
                // if the current language does not provide its own translation,
                // POSTFIX_TRANSLATE_ME is appended:
                map = Tools.removeTranslateComment(map);
                final URL endUrl = map != null && map.startsWith(".")
                        ? localDocumentationLinkConverter.convertLocalLink(map)
                        : Tools.fileToUrl(new File(map));
                // invokeLater is necessary, as the mode changing removes
                // all
                // menus (inclusive this action!).
                SwingUtilities.invokeLater(() -> {
                    try {
                        createNewMode(BrowseMode.MODENAME);
                        controller.getModeController().load(endUrl);
                    } catch (Exception e) {
                        log.error(e);
                    }
                });
            } catch (MalformedURLException e) {
                log.error(e);
            }
        }
    }

    private static class KeyDocumentationAction extends AbstractAction {
        Controller controller;

        KeyDocumentationAction(Controller controller) {
            super(controller.getResourceString("KeyDoc"));
            this.controller = controller;
        }

        public void actionPerformed(ActionEvent event) {
            String urlText = controller.getFrame().getResourceString("pdfKeyDocLocation");
            // if the current language does not provide its own translation,
            // POSTFIX_TRANSLATE_ME is appended:
            urlText = Tools.removeTranslateComment(urlText);
            try {
                URL url = urlText != null && urlText.startsWith(".") ? localDocumentationLinkConverter.convertLocalLink(urlText) : Tools.fileToUrl(new File(urlText));
                log.info("Opening key docs under " + url);
                controller.getFrame().openDocument(url);
            } catch (Exception e) {
                log.error(e);
            }
        }
    }

    private class AboutAction extends AbstractAction {
        Controller controller;

        AboutAction(Controller controller) {
            super(controller.getResourceString("about"));
            this.controller = controller;
        }

        public void actionPerformed(ActionEvent e) {
            showMessageDialog(getView(),
                    controller.getResourceString("about_text") + getFrame().getFreemindVersion(),
                    controller.getResourceString("about"),
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private class LicenseAction extends AbstractAction {
        Controller controller;

        LicenseAction(Controller controller) {
            super(controller.getResourceString("license"));
            this.controller = controller;
        }

        public void actionPerformed(ActionEvent e) {
            showMessageDialog(getView(),
                    controller.getResourceString("license_text"),
                    controller.getResourceString("license"),
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private class NavigationPreviousMapAction extends AbstractAction {
        NavigationPreviousMapAction(Controller controller) {
            super(controller.getResourceString("previous_map"), ImageFactory.getInstance().createIcon(getResource("images/1leftarrow.png")));
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent event) {
            mapModuleManager.previousMapModule();
        }
    }

    private class ShowFilterToolbarAction extends AbstractAction {
        ShowFilterToolbarAction(Controller controller) {
            super(getResourceString("filter_toolbar"), ImageFactory.getInstance().createIcon(getResource("images/filter.gif")));
        }

        public void actionPerformed(ActionEvent event) {
            getFilterController().showFilterToolbar(!getFilterController().isVisible());
        }
    }

    private class NavigationNextMapAction extends AbstractAction {
        NavigationNextMapAction(Controller controller) {
            super(controller.getResourceString("next_map"), ImageFactory.getInstance().createIcon(getResource("images/1rightarrow.png")));
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent event) {
            mapModuleManager.nextMapModule();
        }
    }

    private class NavigationMoveMapLeftAction extends AbstractAction {
        NavigationMoveMapLeftAction(Controller controller) {
            super(controller.getResourceString("move_map_left"), ImageFactory.getInstance().createIcon(getResource("images/draw-arrow-back.png")));
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent event) {
            if (tabbedPane == null) {
                return;
            }

            int selectedIndex = tabbedPane.getSelectedIndex();
            int previousIndex = (selectedIndex > 0) ? (selectedIndex - 1) : (tabbedPane.getTabCount() - 1);
            moveTab(selectedIndex, previousIndex);
        }
    }

    private class NavigationMoveMapRightAction extends AbstractAction {
        NavigationMoveMapRightAction(Controller controller) {
            super(controller.getResourceString("move_map_right"),
                    freemind.view.ImageFactory.getInstance().createIcon(getResource("images/draw-arrow-forward.png")));
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent event) {
            if (tabbedPane != null) {
                int selectedIndex = tabbedPane.getSelectedIndex();
                int previousIndex = (selectedIndex >= tabbedPane.getTabCount() - 1) ? 0
                        : (selectedIndex + 1);
                moveTab(selectedIndex, previousIndex);
            }
        }
    }

    public void moveTab(int src, int dst) {
        // snippet taken from
        // http://www.exampledepot.com/egs/javax.swing/tabbed_TpMove.html
        // Get all the properties
        Component comp = tabbedPane.getComponentAt(src);
        String label = tabbedPane.getTitleAt(src);
        Icon icon = tabbedPane.getIconAt(src);
        Icon iconDis = tabbedPane.getDisabledIconAt(src);
        String tooltip = tabbedPane.getToolTipTextAt(src);
        boolean enabled = tabbedPane.isEnabledAt(src);
        int keycode = tabbedPane.getMnemonicAt(src);
        int mnemonicLoc = tabbedPane.getDisplayedMnemonicIndexAt(src);
        Color fg = tabbedPane.getForegroundAt(src);
        Color bg = tabbedPane.getBackgroundAt(src);

        mTabbedPaneSelectionUpdate = false;
        // Remove the tab
        tabbedPane.remove(src);
        // Add a new tab
        tabbedPane.insertTab(label, icon, comp, tooltip, dst);
        swap(tabbedPaneMapModules, src, dst);
        getMapModuleManager().swapModules(src, dst);
        tabbedPane.setSelectedIndex(dst);
        mTabbedPaneSelectionUpdate = true;

        // Restore all properties
        tabbedPane.setDisabledIconAt(dst, iconDis);
        tabbedPane.setEnabledAt(dst, enabled);
        tabbedPane.setMnemonicAt(dst, keycode);
        tabbedPane.setDisplayedMnemonicIndexAt(dst, mnemonicLoc);
        tabbedPane.setForegroundAt(dst, fg);
        tabbedPane.setBackgroundAt(dst, bg);
    }


    private class MoveToRootAction extends AbstractAction {
        MoveToRootAction(Controller controller) {
            super(controller.getResourceString("move_to_root"));
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent event) {
            moveToRoot();
        }
    }

    private class ToggleMenubarAction extends AbstractAction implements
            MenuItemSelectedListener {
        ToggleMenubarAction(Controller controller) {
            super(controller.getResourceString("toggle_menubar"));
            setEnabled(true);
        }

        public void actionPerformed(ActionEvent event) {
            menubarVisible = !menubarVisible;
            setMenubarVisible(menubarVisible);
        }

        public boolean isSelected(JMenuItem pCheckItem, Action pAction) {
            return menubarVisible;
        }
    }

    private class ToggleToolbarAction extends AbstractAction implements
            MenuItemSelectedListener {
        ToggleToolbarAction(Controller controller) {
            super(controller.getResourceString("toggle_toolbar"));
            setEnabled(true);
        }

        public void actionPerformed(ActionEvent event) {
            toolbarVisible = !toolbarVisible;
            setToolbarVisible(toolbarVisible);
        }

        public boolean isSelected(JMenuItem pCheckItem, Action pAction) {
            log.info("ToggleToolbar was asked for selectedness.");
            return toolbarVisible;
        }
    }

    private class ToggleLeftToolbarAction extends AbstractAction implements
            MenuItemSelectedListener {
        ToggleLeftToolbarAction(Controller controller) {
            super(controller.getResourceString("toggle_left_toolbar"));
            setEnabled(true);
        }

        public void actionPerformed(ActionEvent event) {
            leftToolbarVisible = !leftToolbarVisible;
            setLeftToolbarVisible(leftToolbarVisible);
        }

        public boolean isSelected(JMenuItem pCheckItem, Action pAction) {
            return leftToolbarVisible;
        }
    }

    protected class ZoomInAction extends AbstractAction {
        public ZoomInAction(Controller controller) {
            super(controller.getResourceString("zoom_in"));
        }

        public void actionPerformed(ActionEvent e) {
            // log.info("ZoomInAction actionPerformed");
            float currentZoom = getView().getZoom();
            for (int i = 0; i < zoomValues.length; i++) {
                float val = zoomValues[i];
                if (val > currentZoom) {
                    setZoom(val);
                    return;
                }
            }
            setZoom(zoomValues[zoomValues.length - 1]);
        }
    }

    protected class ZoomOutAction extends AbstractAction {
        public ZoomOutAction(Controller controller) {
            super(controller.getResourceString("zoom_out"));
        }

        public void actionPerformed(ActionEvent e) {
            float currentZoom = getView().getZoom();
            float lastZoom = zoomValues[0];
            for (float val : zoomValues) {
                if (val >= currentZoom) {
                    setZoom(lastZoom);
                    return;
                }
                lastZoom = val;
            }
            setZoom(lastZoom);
        }
    }

    protected class ShowSelectionAsRectangleAction extends AbstractAction
            implements MenuItemSelectedListener {
        public ShowSelectionAsRectangleAction(Controller controller) {
            super(controller.getResourceString("selection_as_rectangle"));
        }

        public void actionPerformed(ActionEvent e) {
            // log.info("ShowSelectionAsRectangleAction action Performed");
            toggleSelectionAsRectangle();
        }

        public boolean isSelected(JMenuItem pCheckItem, Action pAction) {
            return isSelectionAsRectangle();
        }
    }

    //
    // Preferences
    //

    public static Collection<FreemindPropertyListener> getPropertyChangeListeners() {
        return Collections.unmodifiableCollection(propertyChangeListeners);
    }

    public void toggleSelectionAsRectangle() {
        if (isSelectionAsRectangle()) {
            setProperty(FreeMind.RESOURCE_DRAW_RECTANGLE_FOR_SELECTION,
                    BooleanProperty.FALSE_VALUE);
        } else {
            setProperty(FreeMind.RESOURCE_DRAW_RECTANGLE_FOR_SELECTION,
                    BooleanProperty.TRUE_VALUE);
        }
    }

    private boolean isSelectionAsRectangle() {
        return getProperty(FreeMind.RESOURCE_DRAW_RECTANGLE_FOR_SELECTION)
                .equalsIgnoreCase(BooleanProperty.TRUE_VALUE);
    }

    /**
     *
     */
    public MindMap getMap() {
        return getMapModule().getModel();
    }

    public static void addPropertyChangeListener(
            FreemindPropertyListener listener) {
        Controller.propertyChangeListeners.add(listener);
    }

    /**
     * @param listener The new listener. All currently available properties are sent
     *                 to the listener after registration. Here, the oldValue
     *                 parameter is set to null.
     */
    public static void addPropertyChangeListenerAndPropagate(FreemindPropertyListener listener) {
        Controller.addPropertyChangeListener(listener);
        Properties properties = Resources.getInstance().getProperties();
        for (Object key : properties.keySet()) {
            listener.propertyChanged((String) key, properties.getProperty((String) key), null);
        }
    }

    public static void removePropertyChangeListener(
            FreemindPropertyListener listener) {
        Controller.propertyChangeListeners.remove(listener);
    }

    /**
     * @author foltin
     */
    public class PropertyAction extends AbstractAction {

        private final Controller controller;

        /**
         *
         */
        public PropertyAction(Controller controller) {
            super(controller.getResourceString("property_dialog"));
            this.controller = controller;
        }

        public void actionPerformed(ActionEvent arg0) {
            JDialog dialog = new JDialog(getFrame().getJFrame(), true /* modal */);
            dialog.setResizable(true);
            dialog.setUndecorated(false);
            final OptionPanel options = new OptionPanel((FreeMind) getFrame(),
                    dialog, props -> {
                List<String> sortedKeys = new ArrayList<>();
                sortedKeys.addAll(props.stringPropertyNames());
                Collections.sort(sortedKeys);
                boolean propertiesChanged = false;
                for (String key : sortedKeys) {
                    // save only changed keys:
                    String newProperty = props.getProperty(key);
                    propertiesChanged = propertiesChanged
                            || !newProperty.equals(controller
                            .getProperty(key));
                    controller.setProperty(key, newProperty);
                }

                if (propertiesChanged) {
                    showMessageDialog(null,
                            getResourceString("option_changes_may_require_restart"));
                    controller.getFrame().saveProperties(false);
                }
            });
            options.buildPanel();
            options.setProperties();
            dialog.setTitle("Freemind Properties");
            dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            dialog.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent event) {
                    options.closeWindow();
                }
            });
            Action action = new AbstractAction() {

                public void actionPerformed(ActionEvent arg0) {
                    options.closeWindow();
                }
            };
            Tools.addEscapeActionToDialog(dialog, action);
            dialog.setVisible(true);

        }

    }

    public class OptionAntialiasAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            changeAntialias(command);
        }

        /**
         *
         */
        public void changeAntialias(String command) {
            if (command == null) {
                return;
            }
            setProperty(FreeMindCommon.RESOURCE_ANTIALIAS, command);
            if (getView() != null)
                getView().repaint();
        }

    }

    private class OptionHTMLExportFoldingAction extends AbstractAction {
        OptionHTMLExportFoldingAction(Controller controller) {
        }

        public void actionPerformed(ActionEvent e) {
            setProperty("html_export_folding", e.getActionCommand());
        }
    }

    // switch auto properties for selection mechanism fc, 7.12.2003.
    private class OptionSelectionMechanismAction extends AbstractAction
            implements FreemindPropertyListener {
        Controller c;

        OptionSelectionMechanismAction(Controller controller) {
            c = controller;
            Controller.addPropertyChangeListener(this);
        }

        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            changeSelection(command);
        }

        /**
         *
         */
        private void changeSelection(String command) {
            setProperty("selection_method", command);
            // and update the selection method in the NodeMouseMotionListener
            c.getNodeMouseMotionListener().updateSelectionMethod();
            String statusBarString = c.getResourceString(command);
            if (statusBarString != null) // should not happen
                c.getFrame().setStatusText(statusBarString);
        }

        public void propertyChanged(String propertyName, String newValue,
                                    String oldValue) {
            if (propertyName.equals(FreeMind.RESOURCES_SELECTION_METHOD)) {
                changeSelection(newValue);
            }
        }
    }

    // open faq url from freeminds page:
    private static class OpenURLAction extends AbstractAction {
        Controller c;
        private final String url;

        OpenURLAction(Controller controller, String description, String url) {
            super(description, freemind.view.ImageFactory.getInstance().createIcon(
                    controller.getResource("images/Link.png")));
            c = controller;
            this.url = url;
        }

        public void actionPerformed(ActionEvent e) {
            try {
                c.getFrame().openDocument(new URL(url));
            } catch (MalformedURLException ex) {
                c.errorMessage(c.getResourceString("url_error") + "\n" + ex);
            } catch (Exception ex) {
                c.errorMessage(ex);
            }
        }
    }

    public FilterController getFilterController() {
        return filterController;
    }

    public PageFormat getPageFormat() {
        return pageFormat;
    }

    public void addTabbedPane(JTabbedPane pTabbedPane) {
        tabbedPane = pTabbedPane;
        tabbedPaneMapModules = new ArrayList<>();
        tabbedPane.addChangeListener(new ChangeListener() {

            public synchronized void stateChanged(ChangeEvent pE) {
                tabSelectionChanged();
            }

        });
        getMapModuleManager().addListener(new MapModuleChangeObserver() {

            public void afterMapModuleChange(MapModule pOldMapModule,
                                             Mode pOldMode, MapModule pNewMapModule, Mode pNewMode) {
                int selectedIndex = tabbedPane.getSelectedIndex();
                if (pNewMapModule == null) {
                    return;
                }
                // search, if already present:
                for (int i = 0; i < tabbedPaneMapModules.size(); ++i) {
                    if (tabbedPaneMapModules.get(i) == pNewMapModule) {
                        if (selectedIndex != i) {
                            tabbedPane.setSelectedIndex(i);
                        }
                        return;
                    }
                }
                // create new tab:
                tabbedPaneMapModules.add(pNewMapModule);
                tabbedPane.addTab(pNewMapModule.toString(), new JPanel());
                tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
            }

            public void beforeMapModuleChange(MapModule pOldMapModule,
                                              Mode pOldMode, MapModule pNewMapModule, Mode pNewMode) {
            }

            public boolean isMapModuleChangeAllowed(MapModule pOldMapModule,
                                                    Mode pOldMode, MapModule pNewMapModule, Mode pNewMode) {
                return true;
            }

            public void numberOfOpenMapInformation(int pNumber, int pIndex) {
            }

            public void afterMapClose(MapModule pOldMapModule, Mode pOldMode) {
                for (int i = 0; i < tabbedPaneMapModules.size(); ++i) {
                    if (tabbedPaneMapModules.get(i) == pOldMapModule) {
                        log.trace("Remove tab:" + i + " with title:"
                                + tabbedPane.getTitleAt(i));
                        mTabbedPaneSelectionUpdate = false;
                        tabbedPane.removeTabAt(i);
                        tabbedPaneMapModules.remove(i);
                        mTabbedPaneSelectionUpdate = true;
                        tabSelectionChanged();
                        return;
                    }
                }
            }
        });
        registerMapTitleChangeListener((pNewMapTitle, pMapModule, pModel) ->
                range(0, tabbedPaneMapModules.size())
                        .filter(i -> tabbedPaneMapModules.get(i) == pMapModule)
                        .forEachOrdered(i -> tabbedPane.setTitleAt(i, pNewMapTitle + ((pModel.isSaved()) ? "" : "*"))));

    }

    private void tabSelectionChanged() {
        if (!mTabbedPaneSelectionUpdate)
            return;

        int selectedIndex = tabbedPane.getSelectedIndex();
        // display nothing on the other tabs:
        range(0, tabbedPane.getTabCount())
                .filter(j -> j != selectedIndex)
                .forEachOrdered(j -> tabbedPane.setComponentAt(j, new JPanel()));
        if (selectedIndex < 0) {
            // nothing selected. probably, the last map was closed
            return;
        }
        MapModule module = tabbedPaneMapModules.get(selectedIndex);
        log.trace("Selected index of tab is now: " + selectedIndex + " with title:" + module.toString());
        if (module != getMapModule()) {
            // we have to change the active map actively:
            getMapModuleManager().changeToMapModule(module.toString());
        }
        // mScrollPane could be set invisible by JTabbedPane
        frame.getScrollPane().setVisible(true);
        tabbedPane.setComponentAt(selectedIndex, frame.getContentComponent());
        // double call, due to mac strangeness.
        obtainFocusForSelected();
    }

    protected void storePageFormat() {
        switch (pageFormat.getOrientation()) {
            case PageFormat.LANDSCAPE:
                setProperty("page_orientation", "landscape");
                break;
            case PageFormat.PORTRAIT:
                setProperty("page_orientation", "portrait");
                break;
            case PageFormat.REVERSE_LANDSCAPE:
                setProperty("page_orientation", "reverse_landscape");
                break;
        }

        setProperty(PAGE_FORMAT_PROPERTY, Tools.getPageFormatAsString(pageFormat.getPaper()));
    }

    public enum SplitComponentType {
        NOTE_PANEL(0),
        ATTRIBUTE_PANEL(1);

        private final int mIndex;

        SplitComponentType(int index) {
            mIndex = index;
        }

        public int getIndex() {
            return mIndex;
        }

    }

    private JOptionalSplitPane mOptionalSplitPane = null;

    /**
     * Inserts a (south) component into the split pane. If the screen isn't
     * split yet, a split pane should be created on the fly.
     *
     * @param pMindMapComponent south panel to be inserted
     */
    public void insertComponentIntoSplitPane(JComponent pMindMapComponent, SplitComponentType pSplitComponentType) {
        if (mOptionalSplitPane == null) {
            mOptionalSplitPane = new JOptionalSplitPane();
            mOptionalSplitPane.setLastDividerPosition(getIntProperty(FreeMind.RESOURCES_OPTIONAL_SPLIT_DIVIDER_POSITION, -1));
            mOptionalSplitPane.setComponent(pMindMapComponent, pSplitComponentType.getIndex());
            getFrame().insertComponentIntoSplitPane(mOptionalSplitPane);
        } else {
            mOptionalSplitPane.setComponent(pMindMapComponent, pSplitComponentType.getIndex());
        }
    }

    /**
     * Indicates that the south panel should be made invisible.
     */
    public void removeSplitPane(SplitComponentType pSplitComponentType) {
        if (mOptionalSplitPane == null) {
            return;
        }

        mOptionalSplitPane.removeComponent(pSplitComponentType.getIndex());
        if (mOptionalSplitPane.getAmountOfComponents() == 0) {
            getFrame().removeSplitPane();
            mOptionalSplitPane = null;
        }
    }

    private void storeOptionSplitPanePosition() {
        if (mOptionalSplitPane != null) {
            setProperty(FreeMind.RESOURCES_OPTIONAL_SPLIT_DIVIDER_POSITION, "" + mOptionalSplitPane.getDividerPosition());
        }
    }


}
