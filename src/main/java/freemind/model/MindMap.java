package freemind.model;

import freemind.main.Tools;
import freemind.modes.*;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public interface MindMap extends TreeModel {

    MindMapNode getRootNode();

    void nodeChanged(TreeNode node);

    void nodeRefresh(TreeNode node);

    MapFeedback getMapFeedback();

    String getAsPlainText(List<MindMapNode> mindMapNodes);

    String getAsRTF(List<MindMapNode> mindMapNodes);

    String getAsHTML(List<MindMapNode> mindMapNodes);

    /**
     * Returns the file name of the map edited or null if not possible.
     */
    File getFile();

    //
    // Abstract methods that _must_ be implemented.
    //

    boolean save(File file) throws IOException;

    // see ModeController.
    // public void load(URL file) throws FileNotFoundException, IOException,
    // XMLParseException, URISyntaxException;

    /**
     * Return URL of the map (whether as local file or a web location)
     */
    URL getURL() throws MalformedURLException;

    /**
     * writes the content of the map to a writer.
     *
     */
    void getXml(Writer fileout) throws IOException;

    /**
     * writes the content of the map to a writer.
     *
     */
    void getFilteredXml(Writer fileout) throws IOException;

    /**
     * Returns a string that may be given to the modes restore() to get this map
     * again. The Mode must take care that two different maps don't give the
     * same restoreable key.
     */
    String getRestorable();

    TreeNode[] getPathToRoot(TreeNode node);

    /**
     * @return returns the link registry associated with this mode, or null, if
     * no registry is present.
     */
    MindMapLinkRegistry getLinkRegistry();

    /**
     * Destroy everything you have created upon opening.
     */
    void destroy();

    boolean isReadOnly();

    void setReadOnly(boolean pIsReadOnly);

    /**
     * @return true if map is clean (saved), false if it is dirty.
     */
    boolean isSaved();

    Filter getFilter();

    void setFilter(Filter inactiveFilter);

    void nodeStructureChanged(TreeNode node);

    /**
     * Use this method to make the map dirty/clean.
     *
     * @return true, if the map state has changed (and thus the title must be
     * changed).
     */
    boolean setSaved(boolean isSaved);

    /**
     * When the map source is changed (eg. on disk, there is a newer version
     * edited from somebody else), this observer can be used to notice this.
     */
    interface MapSourceChangedObserver {
        /**
         * @return true, if the map was reloaded, false otherwise. This means,
         * that if the method returns true, then the next change on disk
         * is reported as well. If it returns false, the next changes
         * will be ignored until the map is saved.
         */
        boolean mapSourceChanged(MindMap pMap) throws Exception;
    }

    interface AskUserBeforeUpdateCallback {
        /**
         * @return true, if the map should be updated.
         */
        boolean askUserForUpdate();
    }

    void registerMapSourceChangedObserver(
            MapSourceChangedObserver pMapSourceChangedObserver,
            long pGetEventIfChangedAfterThisTimeInMillies);

    /**
     * @return the last saving time to be stored (see
     * {@link MindMap#registerMapSourceChangedObserver(MapSourceChangedObserver, long)}
     * )
     */
    long deregisterMapSourceChangedObserver(
            MapSourceChangedObserver pMapSourceChangedObserver);

    /**
     * @param newRoot one of the nodes, that is now root. The others are grouped
     *                around.
     */
    void changeRoot(MindMapNode newRoot);

    /**
     * @return a list of all icons present in the mindmap. Convenience method
     * for filters.
     */
    SortedListModel getIcons();

    NodeAdapter createNodeAdapter(MindMap pMap, String nodeClass);

    EdgeAdapter createEdgeAdapter(NodeAdapter node);

    CloudAdapter createCloudAdapter(NodeAdapter node);

    ArrowLinkAdapter createArrowLinkAdapter(NodeAdapter source, NodeAdapter target);

    ArrowLinkTarget createArrowLinkTarget(NodeAdapter source, NodeAdapter target);

    MindMapNode loadTree(Tools.ReaderCreator pReaderCreator, AskUserBeforeUpdateCallback pAskUserBeforeUpdateCallback) throws IOException;

    MindMapNode createNodeTreeFromXml(Reader pReader, HashMap<String, NodeAdapter> pIDToTarget) throws IOException;

    NodeAdapter createEncryptedNode(String additionalInfo);

    void insertNodeInto(MindMapNode pNode, MindMapNode pParentNode, int pIndex);

    void removeNodeFromParent(MindMapNode node);

}
