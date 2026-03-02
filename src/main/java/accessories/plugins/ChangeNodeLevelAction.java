/*
 * Created on 19.02.2006
 *
 */

package accessories.plugins;

import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.hooks.MindMapNodeHookAdapter;

import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ChangeNodeLevelAction extends MindMapNodeHookAdapter {

    public ChangeNodeLevelAction() {
        super();
    }

    public void invoke(MindMapNode rootNode) {
        // we dont need node.
        MindMapNode selectedNode;
        List<MindMapNode> selectedNodes;
        MindMapNode focussed = getMindMapController().getSelected();
        List<MindMapNode> selecteds = getMindMapController().getSelecteds();

        selectedNode = focussed;
        selectedNodes = selecteds;

        // bug fix: sort to make independent by user's selection:
        getMindMapController().sortNodesByDepth(selectedNodes);

        if (selectedNode.isRoot()) {
            getMindMapController().getController().errorMessage(getResourceString("cannot_add_parent_to_root"));
            return;
        }

        boolean upwards = Objects.equals("left", getResourceString("action_type")) != selectedNode.isLeft();
        // Make sure the selected nodes all have the same parent
        // (this restriction is to simplify the action, and could
        // possibly be removed in the future, when we have undo)
        // Also make sure that none of the selected nodes are the root node
        MindMapNode selectedParent = selectedNode.getParentNode();
        for (MindMapNode node : selectedNodes) {
            if (node.getParentNode() != selectedParent) {
                getMindMapController().getController().errorMessage(getResourceString("cannot_add_parent_diff_parents"));
                return;
            }
            if (node == rootNode) {
                getMindMapController().getController().errorMessage(getResourceString("cannot_add_parent_to_root"));
                return;
            }
        }

        // collect node ids:
        String selectedNodeId = selectedNode.getObjectId(getController());
        // WORKAROUND: Make target of local hyperlinks for the case, that ids
        // are not stored persistently.
        getMap().getLinkRegistry().registerLocalHyperlinkId(selectedNodeId);
        List<String> selectedNodesId = new ArrayList<>();
        for (MindMapNode node : selectedNodes) {
            String nodeId = node.getObjectId(getController());
            // WORKAROUND: Make target of local hyperlinks for the case, that
            // ids are not stored persistently.
            getMap().getLinkRegistry().registerLocalHyperlinkId(nodeId);
            selectedNodesId.add(nodeId);
        }

        if (upwards) {
            if (selectedParent.isRoot()) {
                // change side of the items:
                boolean isLeft = selectedNode.isLeft();
                Transferable copy = getMindMapController().cut(selectedNodes);
                getMindMapController().paste(copy, selectedParent, false, !isLeft);
                select(selectedNodeId, selectedNodesId);
                return;
            }
            // determine child pos of parent
            MindMapNode grandParent = selectedParent.getParentNode();
            int parentPosition = grandParent.getChildPosition(selectedParent);
            boolean isLeft = selectedParent.isLeft();
            Transferable copy = getMindMapController().cut(selectedNodes);
            if (parentPosition == grandParent.getChildCount() - 1) {
                getMindMapController().paste(copy, grandParent, false, isLeft);
            } else {
                getMindMapController().paste(copy, (MindMapNode) grandParent.getChildAt(parentPosition + 1), true, isLeft);
            }
            select(selectedNodeId, selectedNodesId);

        } else {
            int ownPosition = selectedParent.getChildPosition(selectedNode);
            // find node above the own nodes:
            MindMapNode directSibling = null;
            for (int i = ownPosition - 1; i >= 0; --i) {
                MindMapNode sibling = (MindMapNode) selectedParent.getChildAt(i);
                if ((!selectedNodes.contains(sibling)) && selectedNode.isLeft() == sibling.isLeft()) {
                    directSibling = sibling;
                    break;
                }
            }
            if (directSibling == null) {
                // start searching for a sibling after the selected block:
                for (int i = ownPosition + 1; i < selectedParent.getChildCount(); ++i) {
                    MindMapNode sibling = (MindMapNode) selectedParent.getChildAt(i);
                    if ((!selectedNodes.contains(sibling)) && selectedNode.isLeft() == sibling.isLeft()) {
                        directSibling = sibling;
                        break;
                    }
                }
            }
            if (directSibling != null) {
                // sibling on the same side found:
                Transferable copy = getMindMapController().cut(selectedNodes);
                getMindMapController().paste(copy, directSibling, false, directSibling.isLeft());
                select(selectedNodeId, selectedNodesId);
            }
        }
        obtainFocusForSelected();
    }

    private void select(String selectedNodeId, List<String> selectedNodesIds) {
        // get new nodes by object id:
        MindMapNode newInstanceOfSelectedNode = getMindMapController().getNodeFromID(selectedNodeId);
        List<MindMapNode> newSelecteds = new LinkedList<>();
        for (String nodeId : selectedNodesIds) {
            newSelecteds.add(getMindMapController().getNodeFromID(nodeId));
        }
        getMindMapController().select(newInstanceOfSelectedNode, newSelecteds);
    }

}
