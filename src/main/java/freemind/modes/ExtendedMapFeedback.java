package freemind.modes;

import freemind.extensions.HookFactory;
import freemind.model.MindMap;
import freemind.model.MindMapNode;
import freemind.model.NodeAdapter;
import freemind.modes.mindmapmode.actions.MindMapActions;
import freemind.modes.mindmapmode.actions.xml.ActionPair;
import freemind.modes.mindmapmode.actions.xml.ActionRegistry;
import freemind.modes.mindmapmode.actions.xml.actors.XmlActorFactory;

import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.List;

/**
 * MapFeedback extended by the xml based node change management.
 */
public interface ExtendedMapFeedback extends MapFeedback, MindMapActions {
    /**
     * @return the action factory that contains the actors definitions.
     */
    ActionRegistry getActionRegistry();

    boolean doTransaction(String pName, ActionPair pPair);

    /**
     * Given a node identifier, this method returns the corresponding node.
     *
     * @throws IllegalArgumentException if the id is unknown.
     */
    NodeAdapter getNodeFromID(String nodeID);

    /**
     * Calling this method the map-unique identifier of the node is returned
     * (and created before, if not present)
     */
    String getNodeID(MindMapNode selected);

    MindMapNode getSelected();

    void select(MindMapNode pFocussed, List<MindMapNode> pSelecteds);

    void insertNodeInto(MindMapNode pNewNode, MindMapNode pParent, int pIndex);

    /**
     * @param pUserObject is the string/html of the new node
     * @return the new node.
     */
    MindMapNode newNode(Object pUserObject, MindMap pMap);

    void removeNodeFromParent(MindMapNode pSelectedNode);

    /**
     * @return the factory used to create all xml actors.
     */
    XmlActorFactory getActorFactory();

    Transferable copy(MindMapNode node, boolean saveInvisible);

    /**
     * @return a MindMapNode list as Transferable (special FM flavor).
     */
    Transferable copy(List<MindMapNode> pNodeList, boolean pSaveInvisible);

    void setWaitingCursor(boolean waiting);

    void nodeStyleChanged(MindMapNode pNode);

    HookFactory getHookFactory();

    /**
     * @param pFile loads a file into a new map.
     */
    MapFeedback load(File pFile);

    /**
     * Closes the actual map.
     *
     * @param pForce true= without save.
     */
    void close(boolean pForce);

}
