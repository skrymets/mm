package freemind.view.mindmapview.services;

import freemind.main.PointUtils;
import freemind.view.mindmapview.NodeView;

import java.awt.*;
import java.util.LinkedList;

public class NodeNavigationService {

    private final NodeView nodeView;
    private NodeView preferredChild;

    public NodeNavigationService(NodeView nodeView) {
        this.nodeView = nodeView;
    }

    public NodeView getNextVisibleSibling() {
        NodeView sibling;
        NodeView lastSibling = nodeView;
        for (sibling = nodeView; !sibling.getModel().isRoot(); sibling = sibling
                .getParentView()) {
            lastSibling = sibling;
            sibling = sibling.getNextSiblingSingle();
            if (sibling != lastSibling) {
                break;
            }
        }
        while (sibling.getModel().getNodeLevel() < sibling.getMap()
                .getSiblingMaxLevel()) {
            NodeView first = sibling.getFirst(sibling.isRoot() ? lastSibling
                    : null, nodeView.isLeft(), !nodeView.isLeft());
            if (first == null) {
                break;
            }
            sibling = first;
        }
        if (sibling.isRoot()) {
            return nodeView;
        }
        return sibling;
    }

    public NodeView getPreviousVisibleSibling() {
        NodeView sibling;
        NodeView previousSibling = nodeView;
        for (sibling = nodeView; !sibling.getModel().isRoot(); sibling = sibling
                .getParentView()) {
            previousSibling = sibling;
            sibling = sibling.getPreviousSiblingSingle();
            if (sibling != previousSibling) {
                break;
            }
        }
        while (sibling.getModel().getNodeLevel() < sibling.getMap()
                .getSiblingMaxLevel()) {
            NodeView last = sibling.getLast(sibling.isRoot() ? previousSibling
                    : null, nodeView.isLeft(), !nodeView.isLeft());
            if (last == null) {
                break;
            }
            sibling = last;
        }
        if (sibling.isRoot()) {
            return nodeView;
        }
        return sibling;
    }

    public NodeView getNextPage() {
        if (nodeView.getModel().isRoot()) {
            return nodeView;
        }
        int y0 = nodeView.getInPointInMap().y + nodeView.getMap().getGeometryService().getViewportSize().height;
        NodeView sibling = getNextVisibleSibling();
        if (sibling == nodeView) {
            return nodeView;
        }
        NodeView nextSibling = new NodeNavigationService(sibling).getNextVisibleSibling();
        while (nextSibling != sibling
                && sibling.getParentView() == nextSibling.getParentView()) {
            if (nextSibling.getInPointInMap().y >= y0) {
                break;
            }
            sibling = nextSibling;
            nextSibling = new NodeNavigationService(nextSibling).getNextVisibleSibling();
        }
        return sibling;
    }

    public NodeView getPreviousPage() {
        if (nodeView.getModel().isRoot()) {
            return nodeView;
        }
        int y0 = nodeView.getInPointInMap().y - nodeView.getMap().getGeometryService().getViewportSize().height;
        NodeView sibling = getPreviousVisibleSibling();
        if (sibling == nodeView) {
            return nodeView;
        }
        NodeView previousSibling = new NodeNavigationService(sibling).getPreviousVisibleSibling();
        while (previousSibling != sibling
                && sibling.getParentView() == previousSibling.getParentView()) {
            if (previousSibling.getInPointInMap().y <= y0) {
                break;
            }
            sibling = previousSibling;
            previousSibling = new NodeNavigationService(previousSibling).getPreviousVisibleSibling();
        }
        return sibling;
    }

    public NodeView getPreferredVisibleChild(boolean left) {
        if (preferredChild != null && (left == preferredChild.isLeft())
                && preferredChild.getParent() == nodeView) {
            if (preferredChild.isContentVisible()) {
                return preferredChild;
            } else {
                NodeView newSelected = new NodeNavigationService(preferredChild)
                        .getPreferredVisibleChild(left);
                if (newSelected != null) {
                    return newSelected;
                }
            }
        }
        if (!nodeView.getModel().isLeaf()) {
            int yGap = Integer.MAX_VALUE;
            final NodeView baseComponent;
            if (nodeView.isContentVisible()) {
                baseComponent = nodeView;
            } else {
                baseComponent = nodeView.getVisibleParentView();
            }
            int ownY = baseComponent.getMainView().getY()
                    + baseComponent.getMainView().getHeight() / 2;
            NodeView newSelected = null;
            for (int i = 0; i < nodeView.getComponentCount(); i++) {
                Component c = nodeView.getComponent(i);
                if (!(c instanceof NodeView)) {
                    continue;
                }
                NodeView childView = (NodeView) c;
                if (!(childView.isLeft() == left)) {
                    continue;
                }
                if (!childView.isContentVisible()) {
                    childView = new NodeNavigationService(childView).getPreferredVisibleChild(left);
                    if (childView == null) {
                        continue;
                    }
                }
                Point childPoint = new Point(0, childView.getMainView()
                        .getHeight() / 2);
                PointUtils.convertPointToAncestor(childView.getMainView(),
                        childPoint, baseComponent);
                final int gapToChild = Math.abs(childPoint.y - ownY);
                if (gapToChild < yGap) {
                    newSelected = childView;
                    preferredChild = (NodeView) c;
                    yGap = gapToChild;
                } else {
                    break;
                }
            }
            return newSelected;
        }
        return null;
    }

    public void setPreferredChild(NodeView view) {
        this.preferredChild = view;
        final Container parent = nodeView.getParent();
        if (view == null) {
            // nothing
        } else if (parent instanceof NodeView) {
            ((NodeView) parent).setPreferredChild(nodeView);
        }
    }

    public NodeView getPreferredChild() {
        return preferredChild;
    }

    public NodeView getNextSiblingSingle() {
        return nodeView.getNextSiblingSingle();
    }

    public NodeView getPreviousSiblingSingle() {
        return nodeView.getPreviousSiblingSingle();
    }

    public NodeView getFirst(Component startAfter, boolean leftOnly, boolean rightOnly) {
        return nodeView.getFirst(startAfter, leftOnly, rightOnly);
    }

    public NodeView getLast(Component startBefore, boolean leftOnly, boolean rightOnly) {
        return nodeView.getLast(startBefore, leftOnly, rightOnly);
    }

    public LinkedList<NodeView> getLeft(boolean onlyVisible) {
        return nodeView.getLeft(onlyVisible);
    }

    public LinkedList<NodeView> getRight(boolean onlyVisible) {
        return nodeView.getRight(onlyVisible);
    }
}
