package freemind.view.mindmapview.services;

import freemind.model.MindMapNode;
import freemind.view.mindmapview.NodeView;

import javax.swing.event.TreeModelEvent;
import java.awt.*;
import java.util.Objects;

public class NodeTreeSyncService {

    private final NodeView nodeView;

    public NodeTreeSyncService(NodeView nodeView) {
        this.nodeView = nodeView;
    }

    public void treeNodesChanged(TreeModelEvent e) {
        nodeView.update();
    }

    public void treeNodesInserted(TreeModelEvent e) {
        nodeView.addFoldingListener();
        if (nodeView.getModel().isFolded()) {
            return;
        }
        final int[] childIndices = e.getChildIndices();
        for (int index : childIndices) {
            nodeView.insert((MindMapNode) nodeView.getModel().getChildAt(index), index);
        }
        nodeView.revalidate();
    }

    public void treeNodesRemoved(TreeModelEvent e) {
        if (!nodeView.getModel().hasVisibleChilds()) {
            nodeView.removeFoldingListener();
        }
        nodeView.getMap().resetShiftSelectionOrigin();
        if (nodeView.getModel().isFolded()) {
            return;
        }

        final int[] childIndices = e.getChildIndices();
        NodeView preferredChild = nodeView.getPreferredVisibleChild(false);
        boolean preferredChildIsLeft = preferredChild != null && preferredChild.isLeft();
        // Reset to check actual preferred from nodeView
        preferredChildIsLeft = nodeView.getPreferredChild() != null
                && nodeView.getPreferredChild().isLeft();

        for (int i = childIndices.length - 1; i >= 0; i--) {
            final int index = childIndices[i];
            final NodeView node = (NodeView) nodeView.getComponent(index);
            if (node == nodeView.getPreferredChild()) {
                nodeView.setPreferredChild(null);
                for (int j = index + 1; j < nodeView.getComponentCount(); j++) {
                    final Component c = nodeView.getComponent(j);
                    if (!(c instanceof NodeView)) {
                        break;
                    }
                    NodeView candidate = (NodeView) c;
                    if (candidate.isVisible()
                            && node.isLeft() == candidate.isLeft()) {
                        nodeView.setPreferredChild(candidate);
                        break;
                    }
                }
                if (nodeView.getPreferredChild() == null) {
                    for (int j = index - 1; j >= 0; j--) {
                        final Component c = nodeView.getComponent(j);
                        if (!(c instanceof NodeView)) {
                            break;
                        }
                        NodeView candidate = (NodeView) c;
                        if (candidate.isVisible()
                                && node.isLeft() == candidate.isLeft()) {
                            nodeView.setPreferredChild(candidate);
                            break;
                        }
                    }
                }
            }
            node.remove();
        }
        NodeView preferred = nodeView.getPreferredVisibleChild(preferredChildIsLeft);
        nodeView.getMap().selectAsTheOnlyOneSelected(Objects.requireNonNullElse(preferred, nodeView));
        nodeView.revalidate();
    }

    public void treeStructureChanged(TreeModelEvent e) {
        nodeView.getMap().resetShiftSelectionOrigin();
        for (NodeView child : nodeView.getChildrenViews()) {
            child.remove();
        }
        nodeView.insert();
        if (nodeView.getMap().getSelected() == null) {
            nodeView.getMap().selectAsTheOnlyOneSelected(nodeView);
        }
        nodeView.getMap().revalidateSelecteds();
        nodeView.revalidate();
    }
}
