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

package freemind.modes;

import freemind.main.SwingUtils;
import freemind.controller.*;
import freemind.events.FreeMindEventBus;
import freemind.events.NodeModifiedEvent;
import freemind.main.*;
import freemind.model.MapAdapter;
import freemind.model.MindMap;
import freemind.model.MindMapNode;
import freemind.model.NodeAdapter;
import freemind.modes.FreeMindFileDialog.DirectoryResultListener;
import freemind.modes.services.DisplayNavigationService;
import freemind.modes.services.FileIOService;
import freemind.modes.services.NodeLifecycleService;
import freemind.modes.common.listeners.MindMapMouseWheelEventHandler;
import freemind.view.MapModule;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.NodeView;
import freemind.view.mindmapview.ViewFeedback;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.List;

/**
 * Derive from this class to implement the Controller for your mode. Overload
 * the methods you need for your data model, or use the defaults. There are some
 * default Actions you may want to use for easy editing of your model. Take
 * MindMapController as a sample.
 */
@SuppressWarnings("serial")
@Slf4j
public abstract class ControllerAdapter extends MapFeedbackAdapter implements ModeController, DirectoryResultListener {

    @Getter
    private Mode mode;

    /**
     *
     */
    @Getter
    private final Color selectionColor = new Color(200, 220, 200);
    /**
     * The model, this controller belongs to. It may be null, if it is the
     * default controller that does not show a map.
     */
    private MapAdapter mModel;
    @Getter
    private final NodeLifecycleService nodeLifecycleService = new NodeLifecycleService();
    @Getter
    private final FileIOService fileIOService = new FileIOService(this);
    @Getter
    private final DisplayNavigationService displayNavigationService = new DisplayNavigationService(this);
    private FreeMindEventBus eventBus;

    /**
     * Instantiation order: first me and then the model.
     */
    public ControllerAdapter(Mode mode) {
        this.setMode(mode);
    }

    public void setEventBus(FreeMindEventBus eventBus) {
        this.eventBus = eventBus;
        nodeLifecycleService.setEventBus(eventBus);
    }

    public void setModel(MapAdapter model) {
        mModel = model;
    }

    public abstract MindMapNode newNode(Object userObject, MindMap map);

    /**
     * You _must_ implement this if you use one of the following actions:
     * OpenAction, NewMapAction.
     *
     */
    public MapAdapter newModel(ModeController modeController) {
        throw new java.lang.UnsupportedOperationException();
    }

    /**
     * You may want to implement this... It returns the FileFilter that is used
     * by the open() and save() JFileChoosers.
     */
    protected FileFilter getFileFilter() {
        return null;
    }

    /**
     * Currently, this method is called by the mapAdapter. This is buggy, and is
     * to be changed.
     */
    public void nodeChanged(MindMapNode node) {
        setSaved(false);
        nodeRefresh(node, true);
        if (eventBus != null) {
            eventBus.post(new NodeModifiedEvent(node, null, null, null));
        }
    }

    public void setSaved(boolean pIsClean) {
        boolean stateChanged = getMap().setSaved(pIsClean);
        if (stateChanged) {
            getController().setTitle();
        }
    }


    public void nodeRefresh(MindMapNode node) {
        nodeRefresh(node, false);
    }

    private void nodeRefresh(MindMapNode node, boolean isUpdate) {
        log.trace("nodeChanged called for node {} parent={}", node, node.getParentNode());
        if (isUpdate) {
            // update modification times:
            if (node.getHistoryInformation() != null) {
                node.getHistoryInformation().setLastModifiedAt(new Date());
            }
            // Tell any node hooks that the node is changed:
            updateNode(node);
        }
        // fc, 10.10.06: Dirty hack in order to keep this method away from being
        // used by everybody.
        ((MapAdapter) getMap()).nodeChangedInternal(node);
    }

    public void refreshMap() {
        final MindMapNode root = getMap().getRootNode();
        refreshMapFrom(root);
    }

    public void refreshMapFrom(MindMapNode node) {
        for (MindMapNode child : node.getChildren()) {
            refreshMapFrom(child);
        }
        ((MapAdapter) getMap()).nodeChangedInternal(node);

    }

    public void nodeStructureChanged(MindMapNode node) {
        getMap().nodeStructureChanged(node);
    }

    /**
     * Overwrite this method to perform additional operations to an node update.
     */
    protected void updateNode(MindMapNode node) {
        nodeLifecycleService.updateNode(node);
    }

    public void onLostFocusNode(NodeView node) {
        nodeLifecycleService.onLostFocusNode(node);
    }

    public void onFocusNode(NodeView node) {
        nodeLifecycleService.onFocusNode(node);
    }

    public void changeSelection(NodeView pNode, boolean pIsSelected) {
        nodeLifecycleService.changeSelection(pNode, pIsSelected);
    }

    public void onViewCreatedHook(NodeView node) {
        nodeLifecycleService.onViewCreatedHook(node);
    }

    public void onViewRemovedHook(NodeView node) {
        nodeLifecycleService.onViewRemovedHook(node);
    }

    public void registerNodeSelectionListener(NodeSelectionListener listener,
                                              boolean pCallWithCurrentSelection) {
        nodeLifecycleService.registerNodeSelectionListener(listener,
                pCallWithCurrentSelection, getSelectedView(),
                getView().getSelectionService().getSelecteds());
    }

    public void deregisterNodeSelectionListener(NodeSelectionListener listener) {
        nodeLifecycleService.removeSelectionListener(listener);
    }

    public void registerNodeLifetimeListener(NodeLifetimeListener listener, boolean pFireCreateEvent) {
        nodeLifecycleService.registerNodeLifetimeListener(listener, pFireCreateEvent, getRootNode());
    }

    public void deregisterNodeLifetimeListener(NodeLifetimeListener listener) {
        nodeLifecycleService.removeLifetimeListener(listener);
    }

    public HashSet<NodeLifetimeListener> getNodeLifetimeListeners() {
        return nodeLifecycleService.getLifetimeListeners();
    }

    public void fireNodePreDeleteEvent(MindMapNode node) {
        nodeLifecycleService.fireNodePreDeleteEvent(node);
    }

    public void fireNodePostDeleteEvent(MindMapNode node, MindMapNode parent) {
        nodeLifecycleService.fireNodePostDeleteEvent(node, parent);
    }

    public void fireRecursiveNodeCreateEvent(MindMapNode node) {
        nodeLifecycleService.fireRecursiveNodeCreateEvent(node);
    }

    public void firePreSaveEvent(MindMapNode node) {
        nodeLifecycleService.firePreSaveEvent(node);
    }

    //
    // Map Management
    //

    public String getText(String textId) {
        return getController().getResourceString(textId);
    }

    public ModeController newMap() {
        ModeController newModeController = getMode().createModeController();
        MapAdapter newModel = newModel(newModeController);
        newMap(newModel, newModeController);
        newModeController.getView().getNavigationService().moveToRoot();
        return newModeController;
    }

    public void newMap(final MindMap mapModel, ModeController pModeController) {
        getController().getMapModuleManager().newMapModule(mapModel,
                pModeController);
        pModeController.setSaved(false);
    }

    /**
     * You may decide to overload this or take the default and implement the
     * functionality in your MapModel (implements MindMap)
     */
    public MapFeedback load(URL file) throws
            IOException, URISyntaxException {
        String mapDisplayName = getController().getMapModuleManager()
                .checkIfFileIsAlreadyOpened(file);
        if (null != mapDisplayName) {
            getController().getMapModuleManager().changeToMapModule(
                    mapDisplayName);
            return getController().getModeController();
        } else {
            final ModeController newModeController = getMode().createModeController();
            final MapAdapter model = newModel(newModeController);
            ((ControllerAdapter) newModeController).loadInternally(file, model);
            newMap(model, newModeController);
            newModeController.setSaved(true);
            restoreMapsLastState(newModeController, model);
            return newModeController;
        }
    }

    /**
     *
     */
    abstract protected void loadInternally(URL url, MapAdapter model) throws URISyntaxException, IOException;

    /**
     * You may decide to overload this or take the default and implement the
     * functionality in your MapModel (implements MindMap)
     */
    public MapFeedback load(File file) {
        try {
            return load(Tools.fileToUrl(file));
        } catch (URISyntaxException | IOException e) {
            log.error(e.getLocalizedMessage(), e);
            throw new RuntimeException(e);
        }
    }

    protected void restoreMapsLastState(final ModeController modeController, final MapAdapter model) {
        fileIOService.restoreMapsLastState(modeController, model);
    }

    public boolean save() {
        return fileIOService.save();
    }

    public void loadURL(String relative) {
        fileIOService.loadURL(relative);
    }

    /* (non-Javadoc)
     * @see freemind.modes.ExtendedMapFeedback#setWaitingCursor(boolean)
     */
    public void setWaitingCursor(boolean pWaiting) {
        getFrame().setWaitingCursor(pWaiting);
    }


    /**
     * fc, 24.1.2004: having two methods getSelecteds with different return
     * values (linkedlists of models resp. views) is asking for trouble. @see
     * MapView
     *
     * @return returns a list of MindMapNode s.
     */
    public List<MindMapNode> getSelecteds() {
        LinkedList<MindMapNode> selecteds = new LinkedList<>();
        ListIterator<NodeView> it = getView().getSelectionService().getSelecteds().listIterator();
        if (it != null) {
            while (it.hasNext()) {
                NodeView selected = it.next();
                selecteds.add(selected.getModel());
            }
        }
        return selecteds;
    }

    @Override
    public void select(NodeView node) {
        getView().getSelectionService().select(node);
    }

    public void select(MindMapNode primarySelected, List<MindMapNode> selecteds) {
        // are they visible?
        for (MindMapNode node : selecteds) {
            displayNode(node);
        }
        final NodeView focussedNodeView = getNodeView(primarySelected);
        if (focussedNodeView != null) {
            getView().getSelectionService().selectAsTheOnlyOneSelected(focussedNodeView);
            getView().getScrollService().scrollNodeToVisible(focussedNodeView);
            for (MindMapNode node : selecteds) {
                NodeView nodeView = getNodeView(node);
                if (nodeView != null) {
                    getView().getSelectionService().makeTheSelected(nodeView);
                }
            }
        }
        getController().obtainFocusForSelected();
    }

    public void selectBranch(NodeView selected, boolean extend) {
        displayNode(selected.getModel());
        getView().getSelectionService().selectBranch(selected, extend);
    }

    public List<MindMapNode> getSelectedsByDepth() {
        // return an ArrayList of MindMapNodes.
        List<MindMapNode> result = getSelecteds();
        sortNodesByDepth(result);
        return result;
    }

    /**
     * Return false is the action was cancelled, e.g. when it has to lead to
     * saving as.
     */
    public boolean save(File file) {
        return fileIOService.save(file);
    }

    /**
     * @return returns the new JMenuItem.
     */
    protected JMenuItem add(JMenu menu, Action action, String keystroke) {
        JMenuItem item = menu.add(action);
        item.setAccelerator(KeyStroke.getKeyStroke(getFrame()
                .getAdjustableProperty(keystroke)));
        return item;
    }

    /**
     * @param keystroke can be null, if no keystroke should be assigned.
     * @return returns the new JMenuItem.
     */
    protected JMenuItem add(StructuredMenuHolder holder, String category,
                            Action action, String keystroke) {
        JMenuItem item = holder.addAction(action, category);
        if (keystroke != null) {
            String keyProperty = getFrame().getAdjustableProperty(keystroke);
            log.trace("Found key stroke: {}", keyProperty);
            item.setAccelerator(KeyStroke.getKeyStroke(keyProperty));
        }
        return item;
    }

    /**
     * @param keystroke can be null, if no keystroke should be assigned.
     * @return returns the new JCheckBoxMenuItem.
     */
    protected JMenuItem addCheckBox(StructuredMenuHolder holder,
                                    String category, Action action, String keystroke) {
        JCheckBoxMenuItem item = (JCheckBoxMenuItem) holder.addMenuItem(
                new JCheckBoxMenuItem(action), category);
        if (keystroke != null) {
            item.setAccelerator(KeyStroke.getKeyStroke(getFrame()
                    .getAdjustableProperty(keystroke)));
        }
        return item;
    }

    protected JMenuItem addRadioItem(StructuredMenuHolder holder,
                                     String category, Action action, String keystroke, boolean isSelected) {
        JRadioButtonMenuItem item = (JRadioButtonMenuItem) holder.addMenuItem(
                new JRadioButtonMenuItem(action), category);
        if (keystroke != null) {
            item.setAccelerator(KeyStroke.getKeyStroke(getFrame()
                    .getAdjustableProperty(keystroke)));
        }
        item.setSelected(isSelected);
        return item;
    }

    protected void add(JMenu menu, Action action) {
        menu.add(action);
    }

    //
    // Dialogs with user
    //

    public void open() {
        fileIOService.open();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * freemind.modes.FreeMindFileDialog.DirectoryResultListener#setChosenDirectory
     * (java.io.File)
     */
    public void setChosenDirectory(File pDir) {
        fileIOService.setChosenDirectory(pDir);
    }

    /**
     * Creates a file chooser with the last selected directory as default.
     */
    public FreeMindFileDialog getFileChooser(FileFilter filter) {
        return fileIOService.getFileChooser(filter);
    }

    public FreeMindFileDialog getFileChooser() {
        return getFileChooser(getFileFilter());
    }

    public void handleLoadingException(Exception ex) {
        fileIOService.handleLoadingException(ex);
    }

    /**
     * Save as; return false is the action was cancelled
     */
    public boolean saveAs() {
        return fileIOService.saveAs();
    }


    /**
     * Return false if user has canceled.
     */
    public boolean close(boolean force, MapModuleManager mapModuleManager) {
        return fileIOService.close(force, mapModuleManager);
    }


    public void setVisible(boolean visible) {
        NodeView node = getSelectedView();
        if (visible) {
            onFocusNode(node);
        } else {
            // bug fix, fc 18.5.2004. This should not be here.
            if (node != null) {
                onLostFocusNode(node);
            }
        }
        changeSelection(node, !visible);
    }

    /**
     * Overwrite this to set all of your actions which are dependent on whether
     * there is a map or not.
     */
    protected void setAllActions(boolean enabled) {
        // controller actions:
        getController().zoomIn.setEnabled(enabled);
        getController().zoomOut.setEnabled(enabled);
        getController().showFilterToolbarAction.setEnabled(enabled);
    }

    //
    // Node editing
    //

    /**
     * listener, that blocks the controler if the menu is active (PN) Take care!
     * This listener is also used for modelpopups (as for graphical links).
     */
    private class ControllerPopupMenuListener implements PopupMenuListener {
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            setBlocked(true); // block controller
        }

        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            setBlocked(false); // unblock controller
        }

        public void popupMenuCanceled(PopupMenuEvent e) {
            setBlocked(false); // unblock controller
        }

    }

    /**
     * Take care! This listener is also used for modelpopups (as for graphical
     * links).
     */
    protected final ControllerPopupMenuListener popupListenerSingleton = new ControllerPopupMenuListener();

    public void showPopupMenu(MouseEvent e) {
        if (e.isPopupTrigger()) {
            JPopupMenu popupmenu = getPopupMenu();
            if (popupmenu != null) {
                // adding listener could be optimized but without much profit...
                popupmenu.addPopupMenuListener(this.popupListenerSingleton);
                popupmenu.show(e.getComponent(), e.getX(), e.getY());
                e.consume();
            }
        }
    }

    /**
     * Default implementation: no context menu.
     */
    public JPopupMenu getPopupForModel(java.lang.Object obj) {
        return null;
    }

    /**
     * Overwrite this, if you have one.
     */
    public JToolBar getModeToolBar() {
        return null;
    }

    // status, currently: default, blocked (PN)
    // (blocked to protect against particular events e.g. in edit mode)
    @Setter
    @Getter
    private boolean isBlocked = false;

    private MapView mView;

    //
    // Convenience methods
    //

    protected void setMode(Mode mode) {
        this.mode = mode;
    }

    public MindMap getMap() {
        return mModel;
    }

    public MindMapNode getRootNode() {
        return (MindMapNode) getMap().getRoot();
    }

    public URL getResource(String name) {
        return getFrame().getResource(name);
    }

    /* (non-Javadoc)
     * @see freemind.model.MindMap.MapFeedback#getResourceString(java.lang.String)
     */
    @Override
    public String getResourceString(String pTextId) {
        return getFrame().getResourceString(pTextId);
    }

    public Controller getController() {
        return getMode().getController();
    }

    public FreeMindMain getFrame() {
        return getController().getFrame();
    }

    /**
     * This was inserted by fc, 10.03.04 to enable all actions to refer to its
     * controller easily.
     */
    public ControllerAdapter getModeController() {
        return this;
    }

    // fc, 29.2.2004: there is no sense in having this private and the
    // controller public,
    // because the getController().getModel() method is available anyway.
    public MapAdapter getModel() {
        return mModel;
    }

    public MapView getView() {
        return mView;
    }

    /* (non-Javadoc)
     * @see freemind.modes.MapFeedback#getMapView()
     */
    @Override
    public MapView getMapView() {
        return getView();
    }

    /* (non-Javadoc)
     * @see freemind.modes.MapFeedback#getViewFeedback()
     */
    @Override
    public ViewFeedback getViewFeedback() {
        return this;
    }

    public void setView(MapView pView) {
        mView = pView;
    }

    protected void updateMapModuleName() {
        getController().getMapModuleManager().updateMapModuleName();
    }

    public MindMapNode getSelected() {
        final NodeView selectedView = getSelectedView();
        if (selectedView != null)
            return selectedView.getModel();
        return null;
    }

    public NodeView getSelectedView() {
        if (getView() != null)
            return getView().getSelectionService().getSelected();
        return null;
    }


    public class OpenAction extends AbstractAction {
        final ControllerAdapter mc;

        public OpenAction(ControllerAdapter modeController) {
            super(getText("open"), freemind.view.ImageFactory.getInstance().createIcon(
                    getResource("images/fileopen.png")));
            mc = modeController;
        }

        public void actionPerformed(ActionEvent e) {
            mc.open();
            getController().setTitle(); // Possible update of read-only
        }
    }

    public class SaveAction extends FreemindAction {

        public SaveAction() {
            super(SwingUtils.removeMnemonic(getText("save")), freemind.view.ImageFactory.getInstance().createIcon(
                    getResource("images/filesave.png")), ControllerAdapter.this);
        }

        public void actionPerformed(ActionEvent e) {
            boolean success = save();
            if (success) {
                getFrame().setStatusText(getText("saved")); // perhaps... (PN)
            } else {
                String message = "Saving failed.";
                getFrame().setStatusText(message);
                getController().errorMessage(message);
            }
            getController().setTitle(); // Possible update of read-only
        }

    }

    public class SaveAsAction extends FreemindAction {

        public SaveAsAction() {
            super(getText("save_as"), freemind.view.ImageFactory.getInstance().createIcon(
                    getResource("images/filesaveas.png")), ControllerAdapter.this);
        }

        public void actionPerformed(ActionEvent e) {
            saveAs();
            getController().setTitle(); // Possible update of read-only
        }
    }

    protected class FileOpener implements DropTargetListener {
        private boolean isDragAcceptable(DropTargetDragEvent event) {
            // check if there is at least one File Type in the list
            DataFlavor[] flavors = event.getCurrentDataFlavors();
            for (DataFlavor flavor : flavors) {
                if (flavor.isFlavorJavaFileListType()) {
                    // event.acceptDrag(DnDConstants.ACTION_COPY);
                    return true;
                }
            }
            // event.rejectDrag();
            return false;
        }

        private boolean isDropAcceptable(DropTargetDropEvent event) {
            // check if there is at least one File Type in the list
            DataFlavor[] flavors = event.getCurrentDataFlavors();
            for (DataFlavor flavor : flavors) {
                if (flavor.isFlavorJavaFileListType()) {
                    return true;
                }
            }
            return false;
        }

        public void drop(DropTargetDropEvent dtde) {
            if (!isDropAcceptable(dtde)) {
                dtde.rejectDrop();
                return;
            }
            dtde.acceptDrop(DnDConstants.ACTION_COPY);
            try {
                Object data = dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                if (data == null) {
                    // Shouldn't happen because dragEnter() rejects drags w/out
                    // at least
                    // one javaFileListFlavor. But just in case it does ...
                    dtde.dropComplete(false);
                    return;
                }
                Iterator<File> iterator = ((List<File>) data).iterator();
                while (iterator.hasNext()) {
                    File file = iterator.next();
                    load(file);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        getView(),
                        "Couldn't open dropped file(s). Reason: "
                                + e.getMessage()
                        // getText("file_not_found")
                );
                dtde.dropComplete(false);
                return;
            }
            dtde.dropComplete(true);
        }

        public void dragEnter(DropTargetDragEvent dtde) {
            if (!isDragAcceptable(dtde)) {
                dtde.rejectDrag();
            }
        }

        public void dragOver(DropTargetDragEvent e) {
        }

        public void dragExit(DropTargetEvent e) {
        }

        public void dragScroll(DropTargetDragEvent e) {
        }

        public void dropActionChanged(DropTargetDragEvent e) {
        }
    }

    public Transferable copy(MindMapNode node, boolean saveInvisible) {
        throw new IllegalArgumentException("No copy so far.");
    }

    public Transferable copy() {
        return copy(getView().getSelectionService().getSelectedNodesSortedByY(), false);
    }

    public Transferable copySingle() {

        final ArrayList<MindMapNode> selectedNodes = getView().getSelectionService().getSingleSelectedNodes();
        return copy(selectedNodes, false);
    }

    public Transferable copy(List<MindMapNode> selectedNodes, boolean copyInvisible) {
        try {
            String forNodesFlavor = createForNodesFlavor(selectedNodes,
                    copyInvisible);
            List<String> createForNodeIdsFlavor = createForNodeIdsFlavor(selectedNodes,
                    copyInvisible);

            String plainText = getMap().getAsPlainText(selectedNodes);
            return new MindMapNodesSelection(forNodesFlavor, null, plainText,
                    getMap().getAsRTF(selectedNodes), getMap().getAsHTML(
                    selectedNodes), null, null, createForNodeIdsFlavor);
        } catch (UnsupportedFlavorException | IOException ex) {
            log.error(ex.getLocalizedMessage(), ex);
        }
        return null;
    }

    public String createForNodesFlavor(List<MindMapNode> selectedNodes, boolean copyInvisible)
            throws UnsupportedFlavorException, IOException {
        StringBuilder forNodesFlavor = new StringBuilder();
        boolean firstLoop = true;
        for (MindMapNode tmpNode : selectedNodes) {
            if (firstLoop) {
                firstLoop = false;
            } else {
                forNodesFlavor.append(NODESEPARATOR);
            }

            forNodesFlavor.append(copy(tmpNode, copyInvisible).getTransferData(
                    MindMapNodesSelection.mindMapNodesFlavor));
        }
        return forNodesFlavor.toString();
    }

    public List<String> createForNodeIdsFlavor(List<MindMapNode> selectedNodes, boolean copyInvisible) {
        List<String> forNodesFlavor = new ArrayList<>();

        for (MindMapNode tmpNode : selectedNodes) {
            forNodesFlavor.add(getNodeID(tmpNode));
        }
        return forNodesFlavor;
    }

    /*
     * (non-Javadoc)
     *
     * @see freemind.modes.ModeController#updatePopupMenu(freemind.controller.
     * StructuredMenuHolder)
     */
    public void updatePopupMenu(StructuredMenuHolder holder) {

    }

    /**
     *
     */

    public void shutdownController() {
        setAllActions(false);
        getMapMouseWheelListener().deregister();
    }

    /**
     * This method is called after and before a change of the map module. Use it
     * to perform the actions that cannot be performed at creation time.
     */
    public void startupController() {
        setAllActions(true);
        if (getFrame().getView() != null) {
            FileOpener fileOpener = new FileOpener();
            new DropTarget(getFrame().getView(), fileOpener);
        }
        getMapMouseWheelListener().register(
                new MindMapMouseWheelEventHandler(this));
    }

    public String getLinkShortText(MindMapNode node) {
        return displayNavigationService.getLinkShortText(node);
    }

    public void displayNode(MindMapNode node) {
        displayNavigationService.displayNode(node);
    }

    public void displayNode(MindMapNode node, ArrayList<MindMapNode> nodesUnfoldedByDisplay) {
        displayNavigationService.displayNode(node, nodesUnfoldedByDisplay);
    }

    public void centerNode(MindMapNode node) {
        displayNavigationService.centerNode(node);
    }

    @Override
    public NodeView getNodeView(MindMapNode node) {
        return displayNavigationService.getNodeView(node);
    }

    public void loadURL() {
        String link = getSelected().getLink();
        if (link != null) {
            loadURL(link);
        }
    }

    public Set<MouseWheelEventHandler> getRegisteredMouseWheelEventHandler() {
        return Collections.emptySet();
    }

    public MapModule getMapModule() {
        return getController().getMapModuleManager()
                .getModuleGivenModeController(this);
    }

    public void setToolTip(MindMapNode node, String key, String value) {
        node.setToolTip(key, value);
        nodeRefresh(node);
    }

    /* (non-Javadoc)
     * @see freemind.model.MindMap.MapFeedback#getProperty(java.lang.String)
     */
    @Override
    public String getProperty(String pResourceId) {
        return getController().getProperty(pResourceId);
    }

    /* (non-Javadoc)
     * @see freemind.model.MindMap.MapFeedback#getDefaultFont()
     */
    @Override
    public Font getDefaultFont() {
        return getController().getDefaultFont();
    }

    /* (non-Javadoc)
     * @see freemind.model.MindMap.MapFeedback#getFontThroughMap(java.awt.Font)
     */
    @Override
    public Font getFontThroughMap(Font pFont) {
        return getController().getFontThroughMap(pFont);
    }

    @Override
    public NodeMouseMotionListener getNodeMouseMotionListener() {
        return getController().getNodeMouseMotionListener();
    }

    @Override
    public NodeMotionListener getNodeMotionListener() {
        return getController().getNodeMotionListener();
    }

    @Override
    public NodeKeyListener getNodeKeyListener() {
        return getController().getNodeKeyListener();
    }

    @Override
    public NodeDragListener getNodeDragListener() {
        return getController().getNodeDragListener();
    }

    @Override
    public NodeDropListener getNodeDropListener() {
        return getController().getNodeDropListener();
    }

    @Override
    public MapMouseMotionListener getMapMouseMotionListener() {
        return getController().getMapMouseMotionListener();
    }

    @Override
    public MapMouseWheelListener getMapMouseWheelListener() {
        return getController().getMapMouseWheelListener();
    }


    /**
     *
     */
    @Override
    public NodeAdapter getNodeFromID(String nodeID) {
        NodeAdapter node = (NodeAdapter) getMap().getLinkRegistry().getTargetForId(nodeID);
        if (node == null) {
            throw new IllegalArgumentException("Node belonging to the node id "
                    + nodeID + " not found in map " + getMap().getFile());
        }
        return node;
    }

    @Override
    public String getNodeID(MindMapNode selected) {
        return getMap().getLinkRegistry().registerLinkTarget(selected);
    }

    @Override
    public void setProperty(String pProperty, String pValue) {
        // this method fires a property change event to inform others.
        getController().setProperty(pProperty, pValue);
    }


}
