package freemind.controller.services;

import freemind.controller.Controller;
import freemind.controller.LastStateStorageManagement;
import freemind.controller.MapTitleChangeListener;
import freemind.controller.MapTitleContributor;
import freemind.controller.actions.MindmapLastStateStorage;
import freemind.main.FreeMindCommon;
import freemind.main.Resources;
import freemind.model.MindMap;
import freemind.view.MapModule;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.lang.String.valueOf;

/**
 * Manages session lifecycle including quit/save logic and window title computation.
 */
@Slf4j
public class SessionService {

    private static final String TRUE = "true";
    private static final String FALSE = "false";

    private final Controller controller;
    private final Resources resources;

    public SessionService(Controller controller, Resources resources) {
        this.controller = controller;
        this.resources = resources;
    }

    /**
     * Saves all open maps, persists session state, and exits the application.
     * If the user cancels closing any map, the quit is aborted.
     */
    public void quit() {
        String currentMapRestorable = (controller.getModel() != null) ? controller.getModel().getRestorable() : null;
        controller.storeOptionSplitPanePosition();
        // collect all maps:
        List<String> restorables = new ArrayList<>();
        // move to first map in the window.
        List<MapModule> mapModuleList = controller.getMapModuleManager().getMapModuleList();
        if (!mapModuleList.isEmpty()) {
            String displayName = mapModuleList.get(0).getDisplayName();
            controller.getMapModuleManager().changeToMapModule(displayName);
        }
        while (!mapModuleList.isEmpty()) {
            if (controller.getMapModule() != null) {
                StringBuilder restorableBuffer = new StringBuilder();
                boolean closingNotCancelled = controller.getMapModuleManager().close(false, restorableBuffer);
                if (!closingNotCancelled) {
                    return;
                }
                if (!restorableBuffer.isEmpty()) {
                    String restorableString = restorableBuffer.toString();
                    log.info("Closed the map {}", restorableString);
                    restorables.add(restorableString);
                }
            } else {
                // map module without view open.
                // FIXME: This seems to be a bad hack. correct me!
                controller.getMapModuleManager().nextMapModule();
            }
        }
        // store the last tab session:
        int index = 0;

        String lastStateMapXml = controller.getProperty(FreeMindCommon.MINDMAP_LAST_STATE_MAP_STORAGE);
        LastStateStorageManagement management = new LastStateStorageManagement(lastStateMapXml);
        management.setLastFocussedTab(-1);
        management.clearTabIndices();
        for (String restorable : restorables) {
            MindmapLastStateStorage storage = management.getStorage(restorable);
            if (storage != null) {
                storage.setTabIndex(index);
            }
            if (Objects.equals(restorable, currentMapRestorable)) {
                management.setLastFocussedTab(index);
            }
            index++;
        }
        controller.setProperty(FreeMindCommon.MINDMAP_LAST_STATE_MAP_STORAGE, management.getXml());

        String lastOpenedString = controller.getLastOpenedList().save();
        controller.setProperty("lastOpened", lastOpenedString);
        controller.getFrame().setProperty(FreeMindCommon.ON_START_IF_NOT_SPECIFIED, currentMapRestorable == null ? "" : currentMapRestorable);
        controller.setProperty("toolbarVisible", controller.isToolbarVisible() ? TRUE : FALSE);
        final int winState = controller.getFrame().getWinState();
        if (JFrame.MAXIMIZED_BOTH != (winState & JFrame.MAXIMIZED_BOTH)) {
            controller.setProperty("appwindow_x", valueOf(controller.getFrame().getWinX()));
            controller.setProperty("appwindow_y", valueOf(controller.getFrame().getWinY()));
            controller.setProperty("appwindow_width", valueOf(controller.getFrame().getWinWidth()));
            controller.setProperty("appwindow_height", valueOf(controller.getFrame().getWinHeight()));
        }
        controller.setProperty("appwindow_state", valueOf(winState));
        // Stop edit server!
        controller.getFrame().saveProperties(true);
        // save to properties
        System.exit(0);
    }

    /**
     * Computes and sets the frame title based on the current mode, map module, and
     * registered title contributors. Also notifies all title change listeners.
     */
    public void setTitle(Set<MapTitleContributor> titleContributorSet, Set<MapTitleChangeListener> titleChangeListenerSet) {
        Object[] messageArguments = {controller.getMode().toLocalizedString()};
        MessageFormat formatter = new MessageFormat(controller.getResourceString("mode_title"));

        String title = formatter.format(messageArguments);

        String rawTitle = "";
        MindMap model = null;
        MapModule mapModule = controller.getMapModule();
        if (mapModule != null) {
            model = mapModule.getModel();
            rawTitle = mapModule.toString();
            title = rawTitle
                    + (model.isSaved() ? "" : "*")
                    + " - "
                    + title
                    + (model.isReadOnly() ? " ("
                    + controller.getResourceString("read_only") + ")" : "");
            File file = model.getFile();
            if (file != null) {
                title += " " + file.getAbsolutePath();
            }
            for (MapTitleContributor contributor : titleContributorSet) {
                title = contributor.getMapTitle(title, mapModule, model);
            }

        }
        controller.getFrame().setTitle(title);
        for (MapTitleChangeListener listener : titleChangeListenerSet) {
            listener.setMapTitle(rawTitle, mapModule, model);
        }
    }
}
