package freemind.controller;

import freemind.main.FreeMind;
import freemind.main.FreeMindMain;
import freemind.modes.ModeController;
import freemind.view.MapModule;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import static java.text.MessageFormat.format;

/**
 * This is the menu bar for FreeMind. Actions are defined in MenuListener.
 * Moreover, the StructuredMenuHolder of all menus are hold here.
 */
@Slf4j
public class MenuBar extends JMenuBar {

    public static final String MENU_BAR_PREFIX = "menu_bar/";
    public static final String GENERAL_POPUP_PREFIX = "popup/";
    public static final String POPUP_MENU = GENERAL_POPUP_PREFIX + "popup/";
    public static final String INSERT_MENU = MENU_BAR_PREFIX + "insert/";
    public static final String NAVIGATE_MENU = MENU_BAR_PREFIX + "navigate/";
    public static final String VIEW_MENU = MENU_BAR_PREFIX + "view/";
    public static final String HELP_MENU = MENU_BAR_PREFIX + "help/";
    public static final String MINDMAP_MENU = MENU_BAR_PREFIX + "mindmaps/";
    private static final String MENU_MINDMAP_CATEGORY = MINDMAP_MENU + "mindmaps";
    public static final String MODES_MENU = MINDMAP_MENU;
    // public static final String MODES_MENU = MENU_BAR_PREFIX+"modes/";
    public static final String EDIT_MENU = MENU_BAR_PREFIX + "edit/";
    public static final String FILE_MENU = MENU_BAR_PREFIX + "file/";
    public static final String FORMAT_MENU = MENU_BAR_PREFIX + "format/";
    public static final String EXTRAS_MENU = MENU_BAR_PREFIX + "extras/";

    @Getter
    private StructuredMenuHolder menuHolder;

    JPopupMenu mapsPopupMenu;
    final Controller controller;
    final ActionListener mapsMenuActionListener = new MapsMenuActionListener();

    public MenuBar(Controller controller) {
        this.controller = controller;
        // updateMenus();
    }// Constructor

    /**
     * This is the only public method. It restores all menus.
     *
     */
    public void updateMenus(ModeController newModeController) {
        this.removeAll();

        menuHolder = new StructuredMenuHolder();

        // filemenu
        menuHolder.addMenu(new JMenu(controller.getResourceString("file")), FILE_MENU + ".");
        // filemenu.setMnemonic(KeyEvent.VK_F);

        menuHolder.addCategory(FILE_MENU + "open");
        menuHolder.addCategory(FILE_MENU + "close");
        menuHolder.addSeparator(FILE_MENU);
        menuHolder.addCategory(FILE_MENU + "export");
        menuHolder.addSeparator(FILE_MENU);
        menuHolder.addCategory(FILE_MENU + "import");
        menuHolder.addSeparator(FILE_MENU);
        menuHolder.addCategory(FILE_MENU + "print");
        menuHolder.addSeparator(FILE_MENU);
        menuHolder.addCategory(FILE_MENU + "last");
        menuHolder.addSeparator(FILE_MENU);
        menuHolder.addCategory(FILE_MENU + "quit");

        // editmenu
        menuHolder.addMenu(new JMenu(controller.getResourceString("edit")), EDIT_MENU + ".");
        menuHolder.addCategory(EDIT_MENU + "undo");
        menuHolder.addSeparator(EDIT_MENU);
        menuHolder.addCategory(EDIT_MENU + "select");
        menuHolder.addSeparator(EDIT_MENU);
        menuHolder.addCategory(EDIT_MENU + "paste");
        menuHolder.addSeparator(EDIT_MENU);
        menuHolder.addCategory(EDIT_MENU + "edit");
        menuHolder.addSeparator(EDIT_MENU);
        menuHolder.addCategory(EDIT_MENU + "find");

        // view menu
        menuHolder.addMenu(new JMenu(controller.getResourceString("menu_view")), VIEW_MENU + ".");

        // insert menu
        menuHolder.addMenu(new JMenu(controller.getResourceString("menu_insert")), INSERT_MENU + ".");
        menuHolder.addCategory(INSERT_MENU + "nodes");
        menuHolder.addSeparator(INSERT_MENU);
        menuHolder.addCategory(INSERT_MENU + "icons");
        menuHolder.addSeparator(INSERT_MENU);

        // format menu
        menuHolder.addMenu(new JMenu(controller.getResourceString("menu_format")), FORMAT_MENU + ".");

        // navigate menu
        menuHolder.addMenu(new JMenu(controller.getResourceString("menu_navigate")),
                NAVIGATE_MENU + ".");

        // extras menu
        menuHolder.addMenu(new JMenu(controller.getResourceString("menu_extras")), EXTRAS_MENU + ".");
        menuHolder.addCategory(EXTRAS_MENU + "first");

        // Mapsmenu
        menuHolder.addMenu(new JMenu(controller.getResourceString("mindmaps")), MINDMAP_MENU + ".");
        // mapsmenu.setMnemonic(KeyEvent.VK_M);
        menuHolder.addCategory(MINDMAP_MENU + "navigate");
        menuHolder.addSeparator(MINDMAP_MENU);
        menuHolder.addCategory(MENU_MINDMAP_CATEGORY);
        menuHolder.addSeparator(MINDMAP_MENU);
        // Modesmenu
        menuHolder.addCategory(MODES_MENU);

        // maps popup menu
        mapsPopupMenu = new FreeMindPopupMenu();
        mapsPopupMenu.setName(controller.getResourceString("mindmaps"));
        menuHolder.addCategory(POPUP_MENU + "navigate");
        // menuHolder.addSeparator(POPUP_MENU);

        // formerly, the modes menu was an own menu, but to need less place for
        // the menus,
        // we integrated it into the maps menu.
        // JMenu modesmenu = menuHolder.addMenu(new
        // JMenu(controller.getResourceString("modes")), MODES_MENU+".");

        menuHolder.addMenu(new JMenu(controller.getResourceString("help")), HELP_MENU + ".");
        menuHolder.addAction(controller.documentation, HELP_MENU + "doc/documentation");
        menuHolder.addAction(controller.freemindUrl, HELP_MENU + "doc/freemind");
        menuHolder.addAction(controller.faq, HELP_MENU + "doc/faq");
        menuHolder.addAction(controller.keyDocumentation, HELP_MENU + "doc/keyDocumentation");
        menuHolder.addSeparator(HELP_MENU);
        menuHolder.addCategory(HELP_MENU + "bugs");
        menuHolder.addSeparator(HELP_MENU);
        menuHolder.addAction(controller.license, HELP_MENU + "about/license");
        menuHolder.addAction(controller.about, HELP_MENU + "about/about");

        updateFileMenu();
        updateViewMenu();
        updateEditMenu();
        updateModeMenu();
        updateMapsMenu(menuHolder, MENU_MINDMAP_CATEGORY + "/");
        updateMapsMenu(menuHolder, POPUP_MENU);
        addAdditionalPopupActions();
        // the modes:
        newModeController.updateMenus(menuHolder);
        menuHolder.updateMenus(this, MENU_BAR_PREFIX);
        menuHolder.updateMenus(mapsPopupMenu, GENERAL_POPUP_PREFIX);

    }

    private void updateModeMenu() {
        ButtonGroup group = new ButtonGroup();
        ActionListener modesMenuActionListener = new ModesMenuActionListener();
        List<String> keys = new LinkedList<>(controller.getModes());
        for (String key : keys) {
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(controller.getResourceString("mode_" + key));
            item.setActionCommand(key);
            JRadioButtonMenuItem newItem = (JRadioButtonMenuItem) menuHolder.addMenuItem(item, MODES_MENU + key);
            group.add(newItem);
            if (controller.getMode() == null) {
                newItem.setSelected(false);
            } else {
                newItem.setSelected(controller.getMode().toString().equals(key));
            }
            String keystroke = controller.getFrame().getAdjustableProperty("keystroke_mode_" + key);
            if (keystroke != null) {
                newItem.setAccelerator(KeyStroke.getKeyStroke(keystroke));
            }
            newItem.addActionListener(modesMenuActionListener);
        }
    }

    private void addAdditionalPopupActions() {
        menuHolder.addSeparator(POPUP_MENU);
        JMenuItem newPopupItem;

        if (controller.getFrame().isApplet()) {
            // We have enabled hiding of menubar only in applets. It it because
            // when we hide menubar in application, the key accelerators from
            // menubar do not work.
            newPopupItem = menuHolder.addAction(controller.toggleMenubar, POPUP_MENU + "toggleMenubar");
            newPopupItem.setForeground(new Color(100, 80, 80));
        }

        newPopupItem = menuHolder.addAction(controller.toggleToolbar, POPUP_MENU + "toggleToolbar");
        newPopupItem.setForeground(new Color(100, 80, 80));
    }

    private void updateMapsMenu(StructuredMenuHolder holder, String basicKey) {
        MapModuleManager mapModuleManager = controller.getMapModuleManager();
        List<MapModule> mapModuleVector = mapModuleManager.getMapModuleList();
        if (mapModuleVector == null) {
            return;
        }
        ButtonGroup group = new ButtonGroup();
        for (MapModule mapModule : mapModuleVector) {
            String displayName = mapModule.getDisplayName();
            JRadioButtonMenuItem newItem = new JRadioButtonMenuItem(displayName);
            newItem.setSelected(false);
            group.add(newItem);

            newItem.addActionListener(mapsMenuActionListener);
            newItem.setMnemonic(displayName.charAt(0));

            MapModule currentMapModule = mapModuleManager.getMapModule();
            if (currentMapModule != null) {
                if (mapModule == currentMapModule) {
                    newItem.setSelected(true);
                }
            }
            holder.addMenuItem(newItem, basicKey + displayName);
        }
    }

    private void updateFileMenu() {

        menuHolder.addAction(controller.page, FILE_MENU + "print/pageSetup");
        JMenuItem print = menuHolder.addAction(controller.print, FILE_MENU + "print/print");

        final FreeMindMain controllerFrame = controller.getFrame();

        print.setAccelerator(KeyStroke.getKeyStroke(controllerFrame.getAdjustableProperty("keystroke_print")));

        JMenuItem printPreview = menuHolder.addAction(controller.printPreview, FILE_MENU + "print/printPreview");
        printPreview.setAccelerator(KeyStroke.getKeyStroke(controllerFrame.getAdjustableProperty("keystroke_print_preview")));

        JMenuItem close = menuHolder.addAction(controller.close, FILE_MENU + "close/close");
        close.setAccelerator(KeyStroke.getKeyStroke(controllerFrame.getAdjustableProperty("keystroke_close")));

        JMenuItem quit = menuHolder.addAction(controller.quit, FILE_MENU + "quit/quit");
        quit.setAccelerator(KeyStroke.getKeyStroke(controllerFrame.getAdjustableProperty("keystroke_quit")));
        updateLastOpenedList();
    }

    private void updateLastOpenedList() {
        menuHolder.addMenu(new JMenu(controller.getResourceString("most_recent_files")), FILE_MENU + "last/.");
        boolean firstElement = true;
        LastOpenedList lst = controller.getLastOpenedList();

        // Populates a recent files menu with accelerated first item and listeners
        for (ListIterator<String> it = lst.listIterator(); it.hasNext(); ) {
            String key = it.next();
            JMenuItem item = new JMenuItem(key);
            if (firstElement) {
                firstElement = false;
                item.setAccelerator(KeyStroke.getKeyStroke(controller.getFrame().getAdjustableProperty("keystroke_open_first_in_history")));
            }
            item.addActionListener(new LastOpenedActionListener(key));

            menuHolder.addMenuItem(item, FILE_MENU + "last/" + (key.replace('/', '_')));
        }
    }

    private void updateEditMenu() {

        final FreeMindMain controllerFrame = controller.getFrame();

        JMenuItem moveToRoot = menuHolder.addAction(controller.moveToRoot, NAVIGATE_MENU + "nodes/moveToRoot");
        moveToRoot.setAccelerator(KeyStroke.getKeyStroke(controllerFrame.getAdjustableProperty("keystroke_moveToRoot")));

        JMenuItem previousMap = menuHolder.addAction(controller.navigationPreviousMap, MINDMAP_MENU + "navigate/navigationPreviousMap");
        previousMap.setAccelerator(KeyStroke.getKeyStroke(controllerFrame.getAdjustableProperty(FreeMind.KEYSTROKE_PREVIOUS_MAP)));

        JMenuItem nextMap = menuHolder.addAction(controller.navigationNextMap, MINDMAP_MENU + "navigate/navigationNextMap");
        nextMap.setAccelerator(KeyStroke.getKeyStroke(controllerFrame.getAdjustableProperty(FreeMind.KEYSTROKE_NEXT_MAP)));

        JMenuItem MoveMapLeft = menuHolder.addAction(controller.navigationMoveMapLeftAction, MINDMAP_MENU + "navigate/navigationMoveMapLeft");
        MoveMapLeft.setAccelerator(KeyStroke.getKeyStroke(controllerFrame.getAdjustableProperty(FreeMind.KEYSTROKE_MOVE_MAP_LEFT)));

        JMenuItem MoveMapRight = menuHolder.addAction(controller.navigationMoveMapRightAction, MINDMAP_MENU + "navigate/navigationMoveMapRight");
        MoveMapRight.setAccelerator(KeyStroke.getKeyStroke(controllerFrame.getAdjustableProperty(FreeMind.KEYSTROKE_MOVE_MAP_RIGHT)));

    }

    private void updateViewMenu() {

        menuHolder.addAction(controller.toggleToolbar, VIEW_MENU + "toolbars/toggleToolbar");
        menuHolder.addSeparator(VIEW_MENU);
        menuHolder.addAction(controller.showSelectionAsRectangle, VIEW_MENU + "general/selectionAsRectangle");

        final FreeMindMain controllerFrame = controller.getFrame();

        JMenuItem zoomIn = menuHolder.addAction(controller.zoomIn, VIEW_MENU + "zoom/zoomIn");
        zoomIn.setAccelerator(KeyStroke.getKeyStroke(controllerFrame.getAdjustableProperty("keystroke_zoom_in")));

        JMenuItem zoomOut = menuHolder.addAction(controller.zoomOut, VIEW_MENU + "zoom/zoomOut");
        zoomOut.setAccelerator(KeyStroke.getKeyStroke(controllerFrame.getAdjustableProperty("keystroke_zoom_out")));

        menuHolder.addSeparator(VIEW_MENU);
        menuHolder.addCategory(VIEW_MENU + "note_window");
    }

    JPopupMenu getMapsPopupMenu() { // visible only in controller package
        return mapsPopupMenu;
    }

    private class MapsMenuActionListener implements ActionListener {
        public void actionPerformed(final ActionEvent e) {
            SwingUtilities.invokeLater(() -> controller.getMapModuleManager().changeToMapModule(e.getActionCommand()));
        }
    }

    private class LastOpenedActionListener implements ActionListener {
        private final String mKey;

        public LastOpenedActionListener(String pKey) {
            mKey = pKey;
        }

        public void actionPerformed(ActionEvent e) {

            String restorable = mKey;
            try {
                controller.getLastOpenedList().open(restorable);
            } catch (Exception ex) {
                controller.errorMessage(format("An error occurred on opening the file: {0}.", restorable));
                log.error(ex.getLocalizedMessage(), ex);
            }
        }
    }

    private class ModesMenuActionListener implements ActionListener {
        public void actionPerformed(final ActionEvent e) {
            SwingUtilities.invokeLater(() -> controller.createNewMode(e.getActionCommand()));
        }
    }

    public boolean processKeyBinding(KeyStroke ks, KeyEvent e, int condition, boolean pressed) {
        return super.processKeyBinding(ks, e, condition, pressed);
    }

}
