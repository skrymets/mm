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

import freemind.main.SwingUtils;

import freemind.common.BooleanProperty;
import freemind.common.JOptionalSplitPane;
import freemind.controller.actions.AboutAction;
import freemind.controller.actions.CloseAction;
import freemind.controller.actions.DocumentationAction;
import freemind.controller.actions.KeyDocumentationAction;
import freemind.controller.actions.LicenseAction;
import freemind.controller.actions.MoveToRootAction;
import freemind.controller.actions.NavigationMoveMapLeftAction;
import freemind.controller.actions.NavigationMoveMapRightAction;
import freemind.controller.actions.NavigationNextMapAction;
import freemind.controller.actions.NavigationPreviousMapAction;
import freemind.controller.actions.OpenURLAction;
import freemind.controller.actions.OptionAntialiasAction;
import freemind.controller.actions.OptionHTMLExportFoldingAction;
import freemind.controller.actions.OptionSelectionMechanismAction;
import freemind.controller.actions.PageAction;
import freemind.controller.actions.PrintAction;
import freemind.controller.actions.PrintPreviewAction;
import freemind.controller.actions.PropertyAction;
import freemind.controller.actions.QuitAction;
import freemind.controller.actions.ShowFilterToolbarAction;
import freemind.controller.actions.ShowSelectionAsRectangleAction;
import freemind.controller.actions.ToggleMenubarAction;
import freemind.controller.actions.ToggleToolbarAction;
import freemind.controller.actions.ZoomInAction;
import freemind.controller.actions.ZoomOutAction;
import freemind.controller.filter.FilterController;
import freemind.controller.services.DialogService;
import freemind.controller.services.PrintService;
import freemind.controller.services.SessionService;
import freemind.controller.services.TabbedPaneService;
import freemind.controller.services.ZoomService;
import freemind.main.*;
import freemind.model.FilterContext;
import freemind.model.MindMap;
import freemind.model.MindMapNode;
import freemind.modes.Mode;
import freemind.modes.ModeController;
import freemind.modes.ModesCreator;
import freemind.modes.browsemode.BrowseMode;
import freemind.preferences.FreemindPropertyListener;
import freemind.preferences.layout.OptionPanel;
import freemind.view.ImageFactory;
import freemind.view.MapModule;
import freemind.view.mindmapview.MapView;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.*;

import static java.lang.String.valueOf;

/**
 * Provides the methods to edit/change a Node.
 * Forwards all messages to MapModel(editing) or MapView(navigation).
 */
@Slf4j
public class Controller implements MapModuleChangeObserver, FilterContext {

    private static final String FREEMIND = "FreeMind";
    private final HashSet<MapTitleChangeListener> mMapTitleChangeListenerSet = new HashSet<>();
    private final HashSet<MapTitleContributor> mMapTitleContributorSet = new HashSet<>();

    /**
     * Converts from a local link to the real file URL of the documentation map. (Used to change this behaviour under MacOSX).
     * Used for MAC!!!
     */
    public static LocalLinkConverter localDocumentationLinkConverter;
    @Getter
    private TabbedPaneService tabbedPaneService;
    @Getter
    private SessionService sessionService;
    private LastOpenedList lastOpened; // A list of the path names of all the maps

    // that were opened in the last time
    @Getter
    private MapModuleManager mapModuleManager;

    /**
     * The current mode
     */
    private Mode mMode;

    @Setter
    @Getter
    private FreeMindMain frame;

    private MainToolBar toolbar;

    @Getter
    private NodeMouseMotionListener nodeMouseMotionListener;
    @Getter
    private NodeMotionListener nodeMotionListener;
    @Getter
    private NodeKeyListener nodeKeyListener;
    @Getter
    private NodeDragListener nodeDragListener;
    @Getter
    private NodeDropListener nodeDropListener;
    @Getter
    private MapMouseMotionListener mapMouseMotionListener;
    @Getter
    private MapMouseWheelListener mapMouseWheelListener;

    private final ModesCreator modesCreator = new ModesCreator(this);

    @Getter
    private PrintService printService;
    @Getter
    private ZoomService zoomService;

    private final Map<String, Font> fontMap = new HashMap<>();

    @Getter
    private FilterController filterController;

    @Getter
    boolean menubarVisible = true;
    @Getter
    boolean toolbarVisible = true;

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

    public Action zoomIn;
    public Action zoomOut;

    public Action showSelectionAsRectangle;
    public PropertyAction propertyAction;
    public OpenURLAction freemindUrl;


    public Controller(FreeMindMain frame) {
        this.frame = frame;
    }

    public void init() {
        initialization();

        printService = new PrintService(frame);
        zoomService = new ZoomService();
        tabbedPaneService = new TabbedPaneService(this);
        sessionService = new SessionService(this);

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
        freemindUrl = new OpenURLAction(this, getResourceString(FREEMIND), getProperty("webFreeMindLocation"));
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
        optionAntialiasAction = new OptionAntialiasAction(this);
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

        localDocumentationLinkConverter = new DefaultLocalLinkConverter(frame);
        lastOpened = new LastOpenedList(this, getProperty("lastOpened"));

        mapModuleManager = new MapModuleManager(this);
        mapModuleManager.addListener(this);

        final String DEFAULT_FONT_PROPERTY = "defaultfont";
        final String defaultFont = getProperty(DEFAULT_FONT_PROPERTY);

        if (!SwingUtils.isAvailableFontFamily(defaultFont)) {
            log.warn("The font you have set as standard - {} - is not available.", defaultFont);
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
        Resources.firePropertyChanged(property, value, oldValue);
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

    // -- FilterContext implementation --

    @Override
    public MindMapNode getRootNode() {
        MindMap model = getModel();
        return model == null ? null : model.getRootNode();
    }

    @Override
    public void setWaitingCursor(boolean waiting) {
        getFrame().setWaitingCursor(waiting);
    }

    Set<String> getModes() {
        return modesCreator.getAllModes();
    }

    public Mode getMode() {
        return mMode;
    }

    public String[] getZooms() {
        return zoomService.getZooms();
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
        return DialogService.getCommonJColorChooser();
    }

    public static Color showCommonJColorChooserDialog(Component component, String title, Color initialColor) throws HeadlessException {
        return DialogService.showCommonJColorChooserDialog(component, title, initialColor);
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
            zoomService.changeZoomValueProperty(newMapModule.getView().getZoom());
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
        toolbar.validate();
        toolbar.repaint();
        MenuBar menuBar = getFrame().getFreeMindMenuBar();
        menuBar.updateMenus(newModeController);
        menuBar.revalidate();
        menuBar.repaint();
        // new
        obtainFocusForSelected();
    }

    public void numberOfOpenMapInformation(int number, int pIndex) {
        navigationPreviousMap.setEnabled(number > 0);
        navigationNextMap.setEnabled(number > 0);
        log.info("number {}, pIndex {}", number, pIndex);
        navigationMoveMapLeftAction.setEnabled(number > 1 && pIndex > 0);
        navigationMoveMapRightAction.setEnabled(number > 1 && pIndex < number - 1);
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

    public void moveToRoot() {
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
        DialogService.informationMessage(getFrame().getContentPane(), message);
    }

    public void informationMessage(Object message, JComponent component) {
        DialogService.informationMessage(component, message);
    }

    public void errorMessage(Object message) {
        DialogService.errorMessage(getFrame().getContentPane(), message, getResourceString("undefined_error"));
    }

    public void errorMessage(Object message, JComponent component) {
        DialogService.errorMessage(component, message);
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
            log.trace("Requesting Focus for {} in model {}", getView(), getView().getModel());
            getView().requestFocusInWindow();
        }
    }

    public void setZoom(float zoom) {
        getView().setZoom(zoom);
        zoomService.changeZoomValueProperty(zoom);
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
        sessionService.setTitle(mMapTitleContributorSet, mMapTitleChangeListenerSet);
    }

    public void registerMapTitleChangeListener(MapTitleChangeListener pMapTitleChangeListener) {
        mMapTitleChangeListenerSet.add(pMapTitleChangeListener);
    }

    public void unregisterMapTitleChangeListener(MapTitleChangeListener pMapTitleChangeListener) {
        mMapTitleChangeListenerSet.remove(pMapTitleChangeListener);
    }

    public void registerZoomListener(ZoomListener pZoomListener) {
        zoomService.registerZoomListener(pZoomListener);
    }

    public void unregisterZoomListener(ZoomListener pZoomListener) {
        zoomService.unregisterZoomListener(pZoomListener);
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
        print.setEnabled(enabled && printService.isPrintingAllowed());
        printDirect.setEnabled(enabled && printService.isPrintingAllowed());
        printPreview.setEnabled(enabled && printService.isPrintingAllowed());
        page.setEnabled(enabled && printService.isPrintingAllowed());
        close.setEnabled(enabled);
        moveToRoot.setEnabled(enabled);
        ((MainToolBar) getToolBar()).setAllActions(enabled);
        showSelectionAsRectangle.setEnabled(enabled);
    }

    //
    // program/map control
    //

    public void quit() {
        sessionService.quit();
    }

    // ////////////
    // Inner Classes
    // //////////

    /**
     * Manages the history of visited maps. Maybe explicitly closed maps should
     * be removed from History too?
     */

    public interface LocalLinkConverter {
        URL convertLocalLink(String link) throws MalformedURLException;
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

    public boolean isSelectionAsRectangle() {
        return getProperty(FreeMind.RESOURCE_DRAW_RECTANGLE_FOR_SELECTION)
                .equalsIgnoreCase(BooleanProperty.TRUE_VALUE);
    }

    /**
     *
     */
    public MindMap getMap() {
        return getMapModule().getModel();
    }

    //
    // Preferences
    //

    /** @deprecated Use {@link Resources#addPropertyChangeListener(FreemindPropertyListener)} instead */
    @Deprecated
    public static void addPropertyChangeListener(FreemindPropertyListener listener) {
        Resources.addPropertyChangeListener(listener);
    }

    /** @deprecated Use {@link Resources#addPropertyChangeListenerAndPropagate(FreemindPropertyListener)} instead */
    @Deprecated
    public static void addPropertyChangeListenerAndPropagate(FreemindPropertyListener listener) {
        Resources.addPropertyChangeListenerAndPropagate(listener);
    }

    /** @deprecated Use {@link Resources#removePropertyChangeListener(FreemindPropertyListener)} instead */
    @Deprecated
    public static void removePropertyChangeListener(FreemindPropertyListener listener) {
        Resources.removePropertyChangeListener(listener);
    }

    public void addTabbedPane(JTabbedPane pTabbedPane) {
        tabbedPaneService.addTabbedPane(pTabbedPane);
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPaneService.getTabbedPane();
    }

    public void moveTab(int src, int dst) {
        tabbedPaneService.moveTab(src, dst);
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

    public void storeOptionSplitPanePosition() {
        if (mOptionalSplitPane != null) {
            setProperty(FreeMind.RESOURCES_OPTIONAL_SPLIT_DIVIDER_POSITION, "" + mOptionalSplitPane.getDividerPosition());
        }
    }


}
