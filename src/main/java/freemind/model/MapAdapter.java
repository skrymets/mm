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


package freemind.model;

import freemind.main.*;
import freemind.modes.MapFeedback;
import freemind.modes.MindMapLinkRegistry;
import freemind.modes.XMLElementAdapter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
@Slf4j
public abstract class MapAdapter extends DefaultTreeModel implements MindMap {
    public static final String MAP_INITIAL_START = "<map version=\"";
    public static final String FREEMIND_VERSION_UPDATER_XSLT = "freemind/modes/mindmapmode/freemind_version_updater.xslt";
    /**
     * The current version and all other version that don't need XML update for
     * sure.
     */
    public static final String[] EXPECTED_START_STRINGS = {
            MAP_INITIAL_START + FreeMind.XML_VERSION + "\"",
            MAP_INITIAL_START + "0.7.1\""};

    private static final int INTERVAL_BETWEEN_FILE_MODIFICATION_TIME_CHECKS = 5000;
    /**
     * denotes the amount of changes since the last save. The initial value is
     * zero, such that new models are not to be saved.
     */
    protected int changesPerformedSinceLastSave = 0;
    @Setter
    @Getter
    protected boolean readOnly = true;
    /**
     * -- GETTER --
     *  Change this to always return null if your model doesn't support files.
     */
    @Setter
    @Getter
    private File file;
    private long mFileTime = 0;
    @Setter
    @Getter
    private Filter filter = null;
    private final HashSet<MapSourceChangedObserver> mMapSourceChangedObserverSet = new HashSet<>();
    private final Timer mTimerForFileChangeObservation;
    protected final MapFeedback mMapFeedback;

    public MapAdapter(MapFeedback mapFeedback) {
        super(null);
        this.mMapFeedback = mapFeedback;
        filter = NoFilter.INSTANCE;
        mTimerForFileChangeObservation = new Timer();
        mTimerForFileChangeObservation.schedule(
                new FileChangeInspectorTimerTask(),
                INTERVAL_BETWEEN_FILE_MODIFICATION_TIME_CHECKS,
                INTERVAL_BETWEEN_FILE_MODIFICATION_TIME_CHECKS);
    }

    protected class FileChangeInspectorTimerTask extends TimerTask {

        public void run() {
            boolean shouldFire = false;
            long lastModified = 0;
            // minimal synchronized block:
            synchronized (MapAdapter.this) {
                lastModified = getFileTime();
                if (lastModified > mFileTime) {
                    shouldFire = true;
                    // don't change the file time here. Only, after the user has been asked.
                }
            }
            if (shouldFire) {
                for (MapSourceChangedObserver observer : mMapSourceChangedObserverSet) {
                    log.info("File {} changed on disk as it was last modified at {}", getFile(), new Date(lastModified));
                    try {
                        boolean changeAccepted = observer.mapSourceChanged(MapAdapter.this);
                        if (!changeAccepted) {
                            // this is a trick: at the next save/load the correct value is set again.
                            mFileTime = Long.MAX_VALUE;
                        } else {
                            mFileTime = lastModified;
                        }
                    } catch (Exception e) {
                        log.error(e.getLocalizedMessage(), e);
                    }
                }
            }
        }
    }

    /**
     * Instantiations of this class must call this, when a map was loaded or
     * saved.
     */
    public void setFileTime() {
        mFileTime = getFileTime();
    }

    private long getFileTime() {
        long lastModified;
        File fileName;
        fileName = getFile();
        if (fileName != null) {
            lastModified = fileName.lastModified();
        } else {
            lastModified = 0;
        }
        return lastModified;
    }

    /**
     * Attempts to lock the map using semaphore file.
     *
     * @return If the map is locked, return the name of the locking user, return
     * null otherwise.
     */
    public String tryToLock(File file) throws Exception {
        return null;
    }

    public void destroy() {
        cancelFileChangeObservationTimer();
        // Do all the necessary destructions in your model,
        // e.g. remove file locks.
        // and remove all hooks:
        removeNodes(getRootNode());
    }

    protected void cancelFileChangeObservationTimer() {
        mTimerForFileChangeObservation.cancel();
    }

    // (PN)
    // public void close() {
    // }

    /**
     *
     */
    private void removeNodes(MindMapNode node) {
        node.removeAllHooks();
        mMapFeedback.fireNodePreDeleteEvent(node);
        // and all children:
        for (Iterator<MindMapNode> i = node.childrenUnfolded(); i.hasNext(); ) {
            MindMapNode child = i.next();
            removeNodes(child);
        }
    }

    @Override
    public void removeNodeFromParent(MindMapNode pNode) {
        super.removeNodeFromParent(pNode);
    }

    public boolean isSaved() {
        return (changesPerformedSinceLastSave == 0);
    }

    /**
     * Counts the number of actions performed.
     *
     * @param saved true if the file was saved recently. False otherwise.
     */
    public boolean setSaved(boolean saved) {
        boolean setTitle = false;
        if (saved) {
            changesPerformedSinceLastSave = 0;
            setTitle = true;
        } else {
            if (changesPerformedSinceLastSave == 0) {
                setTitle = true;
            }
            ++changesPerformedSinceLastSave;
        }
        return setTitle;
    }

    protected int getNumberOfChangesSinceLastSave() {
        return changesPerformedSinceLastSave;
    }

    public MindMapNode getRootNode() {
        return (MindMapNode) getRoot();
    }

    public void setRoot(MindMapNode root) {
        super.setRoot(root);
    }

    /**
     * @param newRoot one of the nodes, that is now root. The others are grouped
     *                around.
     */
    public void changeRoot(MindMapNode newRoot) {
        if (newRoot == getRootNode()) {
            return;
        }
        boolean left = newRoot.isLeft();
        MindMapNode node = newRoot;
        // collect parents (as we remove them from their parents...)
        List<MindMapNode> parents = new ArrayList<>();
        while (node.getParentNode() != null) {
            MindMapNode parent = node.getParentNode();
            parents.add(0, node);
            node = parent;
        }
        // bind all parents to a new chain:
        for (MindMapNode mindMapNode : parents) {
            node = mindMapNode;
            MindMapNode parent = node.getParentNode();
            // remove parent
            node.removeFromParent();
            // special treatment for left/right
            if (node == newRoot) {
                for (MindMapNode child : node.getChildren()) {
                    child.setLeft(left);
                }
                parent.setLeft(!left);
            }
            // and put it as a child
            node.insert(parent, node.getChildCount());
        }
        // and set root
        setRoot(newRoot);
    }

    /**
     * Return URL of the map (whether as local file or a web location)
     */
    public URL getURL() throws MalformedURLException {
        return getFile() != null ? Tools.fileToUrl(getFile()) : null;
    }

    protected String getText(String textId) {
        return getMapFeedback().getResourceString(textId);
    }

    //
    // Node editing
    //

    public String getAsPlainText(List<MindMapNode> mindMapNodes) {
        return "";
    }

    public String getAsRTF(List<MindMapNode> mindMapNodes) {
        return "";
    }

    public String getAsHTML(List<MindMapNode> mindMapNodes) {
        return null;
    }

    public String getRestorable() {
        return null;
    }

    public MindMapLinkRegistry getLinkRegistry() {
        return null;
    }

    /* (non-Javadoc)
     * @see javax.swing.tree.DefaultTreeModel#insertNodeInto(javax.swing.tree.MutableTreeNode, javax.swing.tree.MutableTreeNode, int)
     */
    @Override
    public void insertNodeInto(MindMapNode pNewChild,
                               MindMapNode pParent, int pIndex) {
        super.insertNodeInto(pNewChild, pParent, pIndex);
        // call hooks
        mMapFeedback.fireRecursiveNodeCreateEvent(pNewChild);

    }

    /**
     * This method should not be called directly!
     */
    public void nodeChanged(TreeNode node) {
        mMapFeedback.nodeChanged((MindMapNode) node);
    }

    public void nodeRefresh(TreeNode node) {
        mMapFeedback.nodeRefresh((MindMapNode) node);
    }

    /**
     * Invoke this method if you've totally changed the children of node and its
     * childrens children... This will post a treeStructureChanged event.
     */
    public void nodeChangedInternal(TreeNode node) {
        if (node != null) {
            fireTreeNodesChanged(this, getPathToRoot(node), null, null);
        }
    }

    /**
     * Notifies all listeners that have registered interest for notification on
     * this event type. The event instance is lazily created using the
     * parameters passed into the fire method.
     *
     * @param source       the node being changed
     * @param path         the path to the root node
     * @param childIndices the indices of the changed elements
     * @param children     the changed elements
     * @see EventListenerList
     */
    protected void fireTreeNodesInserted(Object source, Object[] path,
                                         int[] childIndices, Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        e = fireTreeNodesInserted(source, path, childIndices, children,
                listeners, e);
        MindMapNode node = (MindMapNode) path[path.length - 1];
        fireTreeNodesInserted(source, path, childIndices, children, node
                .getListeners().getListenerList(), e);
    }

    private TreeModelEvent fireTreeNodesInserted(Object source, Object[] path,
                                                 int[] childIndices, Object[] children, Object[] listeners,
                                                 TreeModelEvent e) {
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path, childIndices, children);
                ((TreeModelListener) listeners[i + 1]).treeNodesInserted(e);
            }
        }
        return e;
    }

    protected void fireTreeNodesRemoved(Object source, Object[] path,
                                        int[] childIndices, Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        e = fireTreeNodesRemoved(source, path, childIndices, children,
                listeners, e);
        MindMapNode node = (MindMapNode) path[path.length - 1];
        fireTreeNodesRemoved(source, path, childIndices, children, node
                .getListeners().getListenerList(), e);
    }

    private TreeModelEvent fireTreeNodesRemoved(Object source, Object[] path,
                                                int[] childIndices, Object[] children, Object[] listeners,
                                                TreeModelEvent e) {
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path, childIndices, children);
                ((TreeModelListener) listeners[i + 1]).treeNodesRemoved(e);
            }
        }
        return e;
    }

    protected void fireTreeStructureChanged(Object source, Object[] path,
                                            int[] childIndices, Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        e = fireTreeStructureChanged(source, path, childIndices, children,
                listeners, e);
        MindMapNode node = (MindMapNode) path[path.length - 1];
        fireTreeStructureChanged(source, path, childIndices, children, node
                .getListeners().getListenerList(), e);
    }

    private TreeModelEvent fireTreeStructureChanged(Object source,
                                                    Object[] path, int[] childIndices, Object[] children,
                                                    Object[] listeners, TreeModelEvent e) {
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path, childIndices, children);
                ((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
            }
        }
        return e;
    }

    protected void fireTreeNodesChanged(Object source, Object[] path,
                                        int[] childIndices, Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        e = fireTreeNodesChanged(source, path, childIndices, children,
                listeners, e);
        MindMapNode node = (MindMapNode) path[path.length - 1];
        fireTreeNodesChanged(source, path, childIndices, children, node
                .getListeners().getListenerList(), e);
    }

    private TreeModelEvent fireTreeNodesChanged(Object source, Object[] path,
                                                int[] childIndices, Object[] children, Object[] listeners,
                                                TreeModelEvent e) {
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null)
                    e = new TreeModelEvent(source, path, childIndices, children);
                ((TreeModelListener) listeners[i + 1]).treeNodesChanged(e);
            }
        }
        return e;
    }

    public void registerMapSourceChangedObserver(
            MapSourceChangedObserver pMapSourceChangedObserver,
            long pGetEventIfChangedAfterThisTimeInMillies) {
        if (pGetEventIfChangedAfterThisTimeInMillies != 0
                && mFileTime > pGetEventIfChangedAfterThisTimeInMillies) {
            try {
                // Issue event here.
                pMapSourceChangedObserver.mapSourceChanged(this);
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
        mMapSourceChangedObserverSet.add(pMapSourceChangedObserver);
    }

    public long deregisterMapSourceChangedObserver(
            MapSourceChangedObserver pMapSourceChangedObserver) {
        mMapSourceChangedObserverSet.remove(pMapSourceChangedObserver);
        return mFileTime;
    }

    public MapFeedback getMapFeedback() {
        return mMapFeedback;
    }

    /**
     *
     */
    public SortedMapListModel getIcons() {
        SortedMapListModel mapIcons;
        mapIcons = new SortedMapListModel();
        addIcons(mapIcons, getRootNode());
        return mapIcons;
    }

    /**
     */
    private void addIcons(SortedMapListModel pMapIcons, MindMapNode pNode) {
        pMapIcons.addAll(pNode.getIcons());
        ListIterator<MindMapNode> iterator = pNode.childrenUnfolded();
        while (iterator.hasNext()) {
            MindMapNode node = iterator.next();
            addIcons(pMapIcons, node);
        }
    }

    /**
     * Given a valid Xml parameterization of a node (tree), this method returns
     * freshly created nodes.
     */
    @Override
    public MindMapNode createNodeTreeFromXml(Reader pReader, HashMap<String, NodeAdapter> pIDToTarget) throws IOException {
        Document doc = FreeMindXml.parse(pReader);
        XMLElementAdapter xmlAdapter = new XMLElementAdapter(mMapFeedback, new ArrayList<>(), pIDToTarget);
        xmlAdapter.buildFromDom(doc.getDocumentElement());
        xmlAdapter.processUnfinishedLinks(getLinkRegistry());
        MindMapNode node = xmlAdapter.getMapChild();
        return node;
    }

    public static final DontAskUserBeforeUpdateAdapter sDontAskInstance = new DontAskUserBeforeUpdateAdapter();

    public static class DontAskUserBeforeUpdateAdapter implements AskUserBeforeUpdateCallback {
        @Override
        public boolean askUserForUpdate() {
            return true;
        }

    }

    @Override
    public MindMapNode loadTree(Tools.ReaderCreator pReaderCreator,
                                AskUserBeforeUpdateCallback pAskUserBeforeUpdateCallback) throws IOException {
        int versionInfoLength;
        versionInfoLength = EXPECTED_START_STRINGS[0].length();
        // reading the start of the file:
        StringBuilder buffer = Tools.readFileStart(pReaderCreator.createReader(),
                versionInfoLength);
        // the resulting file is accessed by the reader:
        Reader reader = null;
        for (String expectedStartString : EXPECTED_START_STRINGS) {
            versionInfoLength = expectedStartString.length();
            String mapStart = "";
            if (buffer.length() >= versionInfoLength) {
                mapStart = buffer.substring(0, versionInfoLength);
            }
            if (mapStart.startsWith(expectedStartString)) {
                // actual version:
                reader = Tools.getActualReader(pReaderCreator.createReader());
                break;
            }
        }
        if (reader == null) {
            if (!SwingUtils.isHeadless()) {
                boolean showResult = pAskUserBeforeUpdateCallback.askUserForUpdate();
                if (!showResult) {
                    throw new IllegalArgumentException("We should not open the reader " + pReaderCreator);
                }
            }
            reader = Tools.getUpdateReader(pReaderCreator.createReader(), FREEMIND_VERSION_UPDATER_XSLT, mMapFeedback.getResources());
        }
        try {
            HashMap<String, NodeAdapter> IDToTarget = new HashMap<>();
            return createNodeTreeFromXml(reader, IDToTarget);
        } catch (Exception ex) {
            String errorMessage = "Error while parsing file:" + ex;
            System.err.println(errorMessage);
            log.error(ex.getLocalizedMessage(), ex);
            NodeAdapter result = createNodeAdapter(this, null);
            result.setText(errorMessage);
            return result;
        } finally {
            reader.close();
        }
    }


}
