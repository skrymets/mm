package freemind.modes.services;

import freemind.controller.MapModuleManager;
import freemind.controller.actions.MindmapLastStateStorage;
import freemind.controller.actions.NodeListMember;
import freemind.main.*;
import freemind.model.MapAdapter;
import freemind.model.MindMapNode;
import freemind.controller.LastStateStorageManagement;
import freemind.modes.ControllerAdapter;
import freemind.modes.FreeMindFileDialog;
import freemind.modes.ModeController;
import freemind.view.MapModule;
import freemind.view.mindmapview.IndependentMapViewCreator;
import freemind.view.mindmapview.NodeView;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages file I/O operations: open, save, load, close.
 * Extracted from ControllerAdapter to reduce god-object complexity.
 */
@Slf4j
public class FileIOService {

    private final ControllerAdapter adapter;
    private File lastCurrentDir = null;

    public FileIOService(ControllerAdapter adapter) {
        this.adapter = adapter;
    }

    public void setChosenDirectory(File dir) {
        lastCurrentDir = dir;
    }

    public void loadURL(String relative) {
        try {
            log.info("Trying to open {}", relative);
            URL absolute = null;
            if (new File(relative).isAbsolute()) {
                absolute = Tools.fileToUrl(new File(relative));
            } else if (relative.startsWith("#")) {
                // inner map link
                log.trace("found relative link to {}", relative);
                String target = relative.substring(1);
                try {
                    adapter.centerNode(adapter.getNodeFromID(target));
                } catch (RuntimeException e) {
                    log.error(e.getLocalizedMessage(), e);
                    adapter.getFrame().setStatusText(
                            Tools.expandPlaceholders(adapter.getText("link_not_found"),
                                    target));
                }
                return;
            } else {
                absolute = new URL(adapter.getMap().getURL(), relative);
            }
            // look for reference part in URL:
            URL originalURL = absolute;
            String ref = absolute.getRef();
            if (ref != null) {
                absolute = Tools.getURLWithoutReference(absolute);
            }
            String extension = FilenameUtils.getExtension(absolute.toString()).toLowerCase();
            if ((extension != null)
                    && extension.equals(FreeMindCommon.FREEMIND_FILE_EXTENSION_WITHOUT_DOT)) {
                log.info("Trying to open mind map {}", absolute);
                MapModuleManager mapModuleManager = adapter.getController()
                        .getMapModuleManager();
                String mapExtensionKey = mapModuleManager
                        .checkIfFileIsAlreadyOpened(absolute);
                if (mapExtensionKey == null) {
                    adapter.setWaitingCursor(true);
                    adapter.load(absolute);
                } else {
                    mapModuleManager.tryToChangeToMapModule(mapExtensionKey);
                }
                if (ref != null) {
                    try {
                        ModeController newModeController = adapter.getController()
                                .getModeController();
                        newModeController.centerNode(newModeController
                                .getNodeFromID(ref));
                    } catch (RuntimeException e) {
                        log.error(e.getLocalizedMessage(), e);
                        adapter.getFrame().setStatusText(
                                Tools.expandPlaceholders(
                                        adapter.getText("link_not_found"), ref));
                    }
                }
            } else {
                adapter.getFrame().openDocument(originalURL);
            }
        } catch (MalformedURLException ex) {
            log.error(ex.getLocalizedMessage(), ex);
            adapter.getController().errorMessage(adapter.getText("url_error") + "\n" + ex);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        } finally {
            adapter.setWaitingCursor(false);
        }
    }

    public boolean save() {
        if (adapter.getModel().isSaved())
            return true;
        if (adapter.getModel().getFile() == null || adapter.getModel().isReadOnly()) {
            return saveAs();
        } else {
            return save(adapter.getModel().getFile());
        }
    }

    public boolean save(File file) {
        boolean result = false;
        try {
            adapter.setWaitingCursor(true);
            result = adapter.getModel().save(file);
            if (result && "true"
                    .equals(adapter.getProperty(FreeMindCommon.CREATE_THUMBNAIL_ON_SAVE))) {
                File baseFileName = adapter.getModel().getFile();
                String fileName = adapter.getResources()
                        .createThumbnailFileName(baseFileName);
                Tools.makeFileHidden(new File(fileName), false);
                IndependentMapViewCreator.printToFile(adapter.getView(), fileName,
                        true,
                        adapter.getIntProperty(FreeMindCommon.THUMBNAIL_SIZE, 800));
                Tools.makeFileHidden(new File(fileName), true);
            }
        } catch (FileNotFoundException e) {
            log.error(e.getLocalizedMessage(), e);
            String message = Tools.expandPlaceholders(adapter.getText("save_failed"),
                    file.getName());
            adapter.getController().errorMessage(message);
        } catch (IOException e) {
            log.error("Error in MindMapMapModel.save()", e);
        } finally {
            adapter.setWaitingCursor(false);
        }
        if (result) {
            adapter.setSaved(true);
        }
        return result;
    }

    public boolean saveAs() {
        File f;
        FreeMindFileDialog chooser = adapter.getFileChooser();
        if (getMapsParentFile() == null) {
            chooser.setSelectedFile(new File(getFileNameProposal()
                    + FreeMindCommon.FREEMIND_FILE_EXTENSION));
        }
        chooser.setDialogTitle(adapter.getText("save_as"));
        boolean repeatSaveAsQuestion;
        do {
            repeatSaveAsQuestion = false;
            int returnVal = chooser.showSaveDialog(adapter.getView());
            if (returnVal != JFileChooser.APPROVE_OPTION) {
                return false;
            }

            f = chooser.getSelectedFile();
            lastCurrentDir = f.getParentFile();
            String ext = FilenameUtils.getExtension(f.getName()).toLowerCase();
            if (!ext.equals(FreeMindCommon.FREEMIND_FILE_EXTENSION_WITHOUT_DOT)) {
                f = new File(f.getParent(), f.getName()
                        + FreeMindCommon.FREEMIND_FILE_EXTENSION);
            }

            if (f.exists()) {
                int overwriteMap = JOptionPane.showConfirmDialog(adapter.getView(),
                        adapter.getText("map_already_exists"), "FreeMind",
                        JOptionPane.YES_NO_OPTION);
                if (overwriteMap != JOptionPane.YES_OPTION) {
                    repeatSaveAsQuestion = true;
                }
            }
        } while (repeatSaveAsQuestion);
        try {
            String lockingUser = adapter.getModel().tryToLock(f);
            if (lockingUser != null) {
                adapter.getFrame().getController().informationMessage(
                        Tools.expandPlaceholders(
                                adapter.getText("map_locked_by_save_as"), f.getName(),
                                lockingUser));
                return false;
            }
        } catch (Exception e) {
            adapter.getFrame().getController().informationMessage(
                    Tools.expandPlaceholders(
                            adapter.getText("locking_failed_by_save_as"), f.getName()));
            return false;
        }

        save(f);
        adapter.getController().getMapModuleManager().updateMapModuleName();
        return true;
    }

    public void open() {
        FreeMindFileDialog chooser = adapter.getFileChooser();
        int returnVal = chooser.showOpenDialog(adapter.getView());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles;
            if (chooser.isMultiSelectionEnabled()) {
                selectedFiles = chooser.getSelectedFiles();
            } else {
                selectedFiles = new File[]{chooser.getSelectedFile()};
            }
            for (File theFile : selectedFiles) {
                try {
                    lastCurrentDir = theFile.getParentFile();
                    adapter.load(theFile);
                } catch (Exception ex) {
                    handleLoadingException(ex);
                    break;
                }
            }
        }
        adapter.getController().setTitle();
    }

    public boolean close(boolean force, MapModuleManager mapModuleManager) {
        adapter.getFrame().setStatusText("");
        if (!force && !adapter.getModel().isSaved()) {
            String text = adapter.getText("save_unsaved") + "\n"
                    + mapModuleManager.getMapModule().toString();
            String title = SwingUtils.removeMnemonic(adapter.getText("save"));
            int returnVal = JOptionPane.showOptionDialog(adapter.getFrame()
                            .getContentPane(), text, title,
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (returnVal == JOptionPane.YES_OPTION) {
                boolean savingNotCancelled = save();
                if (!savingNotCancelled) {
                    return false;
                }
            } else if ((returnVal == JOptionPane.CANCEL_OPTION)
                    || (returnVal == JOptionPane.CLOSED_OPTION)) {
                return false;
            }
        }
        final String lastStateMapStorage = adapter.getFrame().getProperty(FreeMindCommon.MINDMAP_LAST_STATE_MAP_STORAGE);
        LastStateStorageManagement management = new LastStateStorageManagement(lastStateMapStorage);

        String restorable = adapter.getModel().getRestorable();
        if (restorable != null) {
            MindmapLastStateStorage store = management.getStorage(restorable);
            if (store == null) {
                store = new MindmapLastStateStorage();
            }
            store.setRestorableName(restorable);
            store.setLastZoom(adapter.getView().getZoom());
            Point viewLocation = adapter.getView().getGeometryService().getViewPosition();
            if (viewLocation != null) {
                store.setX(viewLocation.x);
                store.setY(viewLocation.y);
            }
            String lastSelected = adapter.getNodeID(adapter.getSelected());
            store.setLastSelected(lastSelected);
            store.clearNodeListMemberList();
            List<MindMapNode> selecteds = adapter.getSelecteds();
            for (MindMapNode node : selecteds) {
                NodeListMember member = new NodeListMember();
                member.setNode(adapter.getNodeID(node));
                store.addNodeListMember(member);
            }
            management.changeOrAdd(store);
            adapter.getFrame().setProperty(FreeMindCommon.MINDMAP_LAST_STATE_MAP_STORAGE, management.getXml());
        }

        adapter.getModel().destroy();
        return true;
    }

    public FreeMindFileDialog getFileChooser(FileFilter filter) {
        FreeMindFileDialog chooser = adapter.getResources().getStandardFileChooser(filter);
        chooser.registerDirectoryResultListener(adapter);
        File parentFile = getMapsParentFile();
        if (parentFile != null && lastCurrentDir == null) {
            lastCurrentDir = parentFile;
        }
        if (lastCurrentDir != null) {
            chooser.setCurrentDirectory(lastCurrentDir);
        }
        return chooser;
    }

    public void handleLoadingException(Exception ex) {
        String exceptionType = ex.getClass().getName();
        if (exceptionType.equals("freemind.main.XMLParseException")) {
            int showDetail = JOptionPane.showConfirmDialog(adapter.getView(),
                    adapter.getText("map_corrupted"), "FreeMind",
                    JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
            if (showDetail == JOptionPane.YES_OPTION) {
                adapter.getController().errorMessage(ex);
            }
        } else if (exceptionType.equals("java.io.FileNotFoundException")) {
            adapter.getController().errorMessage(ex.getMessage());
        } else {
            log.error(ex.getLocalizedMessage(), ex);
            adapter.getController().errorMessage(ex);
        }
    }

    public void restoreMapsLastState(ModeController modeController, MapAdapter model) {
        String lastStateMapXml = adapter.getFrame().getProperty(FreeMindCommon.MINDMAP_LAST_STATE_MAP_STORAGE);
        LastStateStorageManagement management = new LastStateStorageManagement(lastStateMapXml);
        MindmapLastStateStorage store = management.getStorage(model.getRestorable());
        if (store != null) {
            adapter.getController().setZoom(store.getLastZoom());
            try {
                MindMapNode sel = modeController.getNodeFromID(store.getLastSelected());
                modeController.centerNode(sel);
                List<MindMapNode> selected = new ArrayList<>();
                for (NodeListMember member : store.getNodeListMemberList()) {
                    MindMapNode selNode = modeController.getNodeFromID(member.getNode());
                    selected.add(selNode);
                }
                modeController.select(sel, selected);
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
                modeController.getView().getNavigationService().moveToRoot();
            }
        } else {
            modeController.getView().getNavigationService().moveToRoot();
        }
    }

    private String getFileNameProposal() {
        return MindMapUtils.getFileNameProposal(adapter.getMap().getRootNode());
    }

    private File getMapsParentFile() {
        if ((adapter.getMap() != null) && (adapter.getMap().getFile() != null)
                && (adapter.getMap().getFile().getParentFile() != null)) {
            return adapter.getMap().getFile().getParentFile();
        }
        return null;
    }
}
