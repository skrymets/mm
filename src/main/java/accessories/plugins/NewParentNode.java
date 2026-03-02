/*
 * Created on 19.04.2004
 *
 */
package accessories.plugins;

import freemind.main.Tools;
import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.hooks.MindMapNodeHookAdapter;
import freemind.view.mindmapview.MapView;
import lombok.extern.slf4j.Slf4j;

import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <a href="https://sourceforge.net/tracker/?func=detail&atid=307118&aid=881217&">...</a>
 * group_id=7118
 * <p>
 * Initial Comment: The "New Parent Node" action creates a node as a
 * parent of one or more selected nodes. If more than one node is
 * selected, the selected nodes must all have the same parent -- this
 * restriction is imposed to make the action easier to understand and to
 * undo manually, and could potentially be removed when we get automated
 * undo.
 * <p>
 * The root node must not be one of the selected nodes. I find this
 * action useful when I need to add an extra level of grouping in the
 * middle of an existing hierarchy. It is quicker than adding a new node
 * at the same level and then cutting-and-pasting the child nodes. The
 * code simply performs these actions in sequence, after validating the
 * selected nodes.
 */
@Slf4j
public class NewParentNode extends MindMapNodeHookAdapter {

    public NewParentNode() {
        super();
    }

    public void invoke(MindMapNode rootNode) {
        final MapView mapView = getMindMapController().getView();
        // we dont need node.
        MindMapNode focussed = getMindMapController().getSelected();
        List<MindMapNode> selecteds = getMindMapController().getSelecteds();
        MindMapNode selectedNode = focussed;
        List<MindMapNode> selectedNodes = selecteds;

        // bug fix: sort to make independent by user's selection:
        getMindMapController().sortNodesByDepth(selectedNodes);

        if (focussed.isRoot()) {
            if (selecteds.size() == 1) {
                // only root is selected. we try to create a new root:
                List<MindMapNode> children = new ArrayList<>(rootNode.getChildren());
                // copy only root.
                Transferable rootContent = getMindMapController().copySingle();
                // and paste it directly again.
                getMindMapController().paste(rootContent, rootNode);
                List<MindMapNode> childrenNew = new ArrayList<>(rootNode.getChildren());
                /*
                 * look for the new node as the difference between former
                 * children and new children.
                 */
                MindMapNode rootCopy = null;
                boolean found = false;
                for (MindMapNode mindMapNode : childrenNew) {
                    rootCopy = mindMapNode;
                    if (!children.contains(rootCopy)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    log.warn("New node not found in list of all children. Strange...");
                    return;
                }
                // delete root node content:
                getMindMapController().clearNodeContents(rootNode);
                // children list must be able to modify, thus we copy it deeply.
                moveToOtherNode(rootNode, children, rootNode, rootCopy);
                return;
            }
            getMindMapController().getController().errorMessage(
                    getResourceString("cannot_add_parent_to_root"));
            return;
        }

        MindMapNode newNode = moveToNewParent(rootNode, selectedNode,
                selectedNodes);
        if (newNode == null) {
            return;
        }
        // Start editing new node
        mapView.getSelectionService().selectAsTheOnlyOneSelected(mapView.getViewerRegistryService().getNodeView(newNode));
        getMindMapController().getFrame().repaint();
        // edit is not necessary, as the new text can directly entered and
        // editing is starting automatically.
        // getMindMapController().edit(newNode.getViewer(),
        // selectedParent.getViewer(), null, false, false, false);
    }

    private MindMapNode moveToNewParent(MindMapNode rootNode,
                                        MindMapNode selectedNode, List<MindMapNode> selectedNodes) {
        // Create new node in the position of the selectedNode
        MindMapNode selectedParent = selectedNode.getParentNode();
        int childPosition = selectedParent.getChildPosition(selectedNode);
        MindMapNode newNode = getMindMapController().addNewNode(selectedParent,
                childPosition, selectedNode.isLeft());
        return moveToOtherNode(rootNode, selectedNodes, selectedParent, newNode);
    }

    private MindMapNode moveToOtherNode(MindMapNode rootNode,
                                        List<MindMapNode> nodesToBeMoved, MindMapNode selectedParent, MindMapNode newNode) {
        if (nodesToBeMoved.isEmpty()) {
            // nothing to do.
            return newNode;
        }
        // Make sure the selected nodes all have the same parent
        // (this restriction is to simplify the action, and could
        // possibly be removed in the future, when we have undo)
        // Also make sure that none of the selected nodes are the root node
        for (MindMapNode node : nodesToBeMoved) {
            if (node.getParentNode() != selectedParent) {
                getMindMapController().getController().errorMessage(
                        getResourceString("cannot_add_parent_diff_parents"));
                return null;
            }
            if (node == rootNode) {
                getMindMapController().getController().errorMessage(
                        getResourceString("cannot_add_parent_to_root"));
                return null;
            }
        }

        // MindMapNode newNode = getMindMapController().newNode();
        // getMap().insertNodeInto(newNode, selectedParent, childPosition);

        // Move selected nodes to become children of new node
        Transferable copy = getMindMapController().cut(nodesToBeMoved);
        getMindMapController().paste(copy, newNode);
        getMindMapController().select(selectedParent, Collections.singletonList(selectedParent));
//		getMindMapController().obtainFocusForSelected();
        nodeChanged(selectedParent);

        return newNode;
    }

}
