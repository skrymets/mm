package freemind.view.mindmapview.services;

import freemind.main.Pair;
import freemind.main.PointUtils;
import freemind.model.MindMapNode;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.NodeView;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.*;
import java.util.List;

@Slf4j
public class MapSelectionService {

    private final MapView mapView;
    private final MapPrintingService printingService;
    private final Selected selected = new Selected();
    private NodeView shiftSelectionOrigin = null;
    private boolean selectedsValid = true;

    public MapSelectionService(MapView mapView, MapPrintingService printingService) {
        this.mapView = mapView;
        this.printingService = printingService;
    }

    public void select(NodeView node) {
        if (node == null) {
            log.warn("Select with null NodeView called!");
            return;
        }
        mapView.scrollNodeToVisible(node);
        selectAsTheOnlyOneSelected(node);
        mapView.setSiblingMaxLevel(node.getModel().getNodeLevel());
    }

    public void selectAsTheOnlyOneSelected(NodeView newSelected) {
        log.trace("selectAsTheOnlyOneSelected");
        LinkedList<NodeView> oldSelecteds = getSelecteds();
        selected.clear();
        selected.add(newSelected);

        if (newSelected.getModel().getParentNode() != null) {
            ((NodeView) newSelected.getParent()).setPreferredChild(newSelected);
        }

        mapView.scrollNodeToVisible(newSelected);
        newSelected.repaintSelected();

        for (NodeView oldSelected : oldSelecteds) {
            if (oldSelected != null) {
                oldSelected.repaintSelected();
            }
        }
    }

    public void toggleSelected(NodeView newSelected) {
        log.trace("toggleSelected");
        NodeView oldSelected = getSelected();
        if (isSelected(newSelected)) {
            if (selected.size() > 1) {
                selected.remove(newSelected);
                oldSelected = newSelected;
            }
        } else {
            selected.add(newSelected);
        }
        getSelected().repaintSelected();
        if (oldSelected != null)
            oldSelected.repaintSelected();
    }

    public void makeTheSelected(NodeView newSelected) {
        log.trace("makeTheSelected");
        if (isSelected(newSelected)) {
            selected.moveToFirst(newSelected);
        } else {
            selected.add(newSelected);
        }
        getSelected().repaintSelected();
    }

    public void deselect(NodeView newSelected) {
        if (isSelected(newSelected)) {
            selected.remove(newSelected);
            newSelected.repaintSelected();
        }
    }

    public void selectBranch(NodeView newlySelectedNodeView, boolean extend) {
        if (!extend) {
            selectAsTheOnlyOneSelected(newlySelectedNodeView);
        } else if (!isSelected(newlySelectedNodeView) && newlySelectedNodeView.isContentVisible()) {
            toggleSelected(newlySelectedNodeView);
        }
        for (NodeView target : newlySelectedNodeView.getChildrenViews()) {
            selectBranch(target, true);
        }
    }

    public boolean selectContinuous(NodeView newSelected) {
        NodeView oldSelected = null;
        LinkedList<NodeView> selList = getSelecteds();
        ListIterator<NodeView> j = selList.listIterator();
        while (j.hasNext()) {
            NodeView selectedNode = j.next();
            if (selectedNode != newSelected && newSelected.isSiblingOf(selectedNode)) {
                oldSelected = selectedNode;
                break;
            }
        }
        if (oldSelected == null) {
            if (!isSelected(newSelected) && newSelected.isContentVisible()) {
                toggleSelected(newSelected);
                return true;
            }
            return false;
        }
        boolean oldPositionLeft = oldSelected.isLeft();
        boolean newPositionLeft = newSelected.isLeft();
        ListIterator<NodeView> i = newSelected.getSiblingViews().listIterator();
        while (i.hasNext()) {
            NodeView nodeView = i.next();
            if (nodeView == oldSelected) {
                break;
            }
        }
        ListIterator<NodeView> i_backup = i;
        while (i.hasNext()) {
            NodeView nodeView = i.next();
            if ((nodeView.isLeft() == oldPositionLeft || nodeView.isLeft() == newPositionLeft)) {
                if (isSelected(nodeView))
                    deselect(nodeView);
                else
                    break;
            }
        }
        i = i_backup;
        if (i.hasPrevious()) {
            i.previous();
            while (i.hasPrevious()) {
                NodeView nodeView = i.previous();
                if (nodeView.isLeft() == oldPositionLeft
                        || nodeView.isLeft() == newPositionLeft) {
                    if (isSelected(nodeView))
                        deselect(nodeView);
                    else
                        break;
                }
            }
        }
        i = newSelected.getSiblingViews().listIterator();
        i = newSelected.getSiblingViews().listIterator();
        while (i.hasNext()) {
            NodeView nodeView = i.next();
            if (nodeView == newSelected || nodeView == oldSelected) {
                if (!isSelected(nodeView) && nodeView.isContentVisible())
                    toggleSelected(nodeView);
                break;
            }
        }
        while (i.hasNext()) {
            NodeView nodeView = i.next();
            if ((nodeView.isLeft() == oldPositionLeft || nodeView.isLeft() == newPositionLeft)
                    && !isSelected(nodeView) && nodeView.isContentVisible())
                toggleSelected(nodeView);
            if (nodeView == newSelected || nodeView == oldSelected) {
                break;
            }
        }
        toggleSelected(oldSelected);
        toggleSelected(oldSelected);
        return true;
    }

    public NodeView getSelected() {
        if (selected.size() > 0)
            return selected.get(0);
        else
            return null;
    }

    private NodeView getSelected(int i) {
        return selected.get(i);
    }

    public LinkedList<NodeView> getSelecteds() {
        LinkedList<NodeView> result = new LinkedList<>();
        for (int i = 0; i < selected.size(); i++) {
            result.add(getSelected(i));
        }
        return result;
    }

    public ArrayList<MindMapNode> getSelectedNodesSortedByY() {
        final HashSet<MindMapNode> selectedNodesSet = new HashSet<>();
        for (int i = 0; i < selected.size(); i++) {
            selectedNodesSet.add(getSelected(i).getModel());
        }
        LinkedList<Pair> pointNodePairs = new LinkedList<>();

        Point point = new Point();
        iteration:
        for (int i = 0; i < selected.size(); i++) {
            final NodeView view = getSelected(i);
            final MindMapNode node = view.getModel();
            for (MindMapNode parent = node.getParentNode(); parent != null; parent = parent.getParentNode()) {
                if (selectedNodesSet.contains(parent)) {
                    continue iteration;
                }
            }
            view.getContent().getLocation(point);
            PointUtils.convertPointToAncestor(view, point, mapView);
            pointNodePairs.add(new Pair(Integer.valueOf(point.y), node));
        }
        pointNodePairs.sort((pair0, pair1) -> {
            Integer int0 = (Integer) pair0.getFirst();
            Integer int1 = (Integer) pair1.getFirst();
            return int0.compareTo(int1);
        });

        ArrayList<MindMapNode> selectedNodes = new ArrayList<>();
        for (Pair pointNodePair : pointNodePairs) {
            selectedNodes.add((MindMapNode) pointNodePair.getSecond());
        }
        return selectedNodes;
    }

    public ArrayList<MindMapNode> getSingleSelectedNodes() {
        ArrayList<MindMapNode> selectedNodes = new ArrayList<>(selected.size());
        for (int i = selected.size() - 1; i >= 0; i--) {
            selectedNodes.add(getSelected(i).getModel().shallowCopy());
        }
        return selectedNodes;
    }

    public boolean isSelected(NodeView n) {
        if (printingService.isCurrentlyPrinting())
            return false;
        return selected.contains(n);
    }

    public void resetShiftSelectionOrigin() {
        shiftSelectionOrigin = null;
    }

    public NodeView getShiftSelectionOrigin() {
        return shiftSelectionOrigin;
    }

    public void setShiftSelectionOrigin(NodeView origin) {
        this.shiftSelectionOrigin = origin;
    }

    public void revalidateSelecteds() {
        selectedsValid = false;
    }

    public void validateSelecteds() {
        if (selectedsValid) {
            return;
        }
        selectedsValid = true;
        log.trace("validateSelecteds");
        ArrayList<NodeView> selectedNodes = new ArrayList<>();
        for (NodeView nodeView : getSelecteds()) {
            if (nodeView != null) {
                selectedNodes.add(nodeView);
            }
        }
        selected.clear();
        for (NodeView oldNodeView : selectedNodes) {
            if (oldNodeView.isContentVisible()) {
                NodeView newNodeView = mapView.getNodeView(oldNodeView.getModel());
                if (newNodeView != null) {
                    selected.add(newNodeView);
                }
            }
        }
    }

    // Inner class managing the selection list
    private class Selected {
        private final List<NodeView> mySelected = new ArrayList<>();

        public void clear() {
            if (size() > 0) {
                removeFocusForHooks(get(0));
            }
            mySelected.clear();
            List<NodeView> selectedCopy = new ArrayList<>(mySelected);
            for (NodeView view : selectedCopy) {
                changeSelection(view, false);
            }
            log.trace("Cleared selected.");
        }

        private void changeSelection(NodeView pNode, boolean pIsSelected) {
            if (pNode.getModel() == null)
                return;
            mapView.getViewFeedback().changeSelection(pNode, pIsSelected);
        }

        public int size() {
            return mySelected.size();
        }

        public void remove(NodeView node) {
            if (mySelected.indexOf(node) == 0) {
                removeFocusForHooks(node);
            }
            mySelected.remove(node);
            changeSelection(node, false);
            log.trace("Removed focused {}", node);
        }

        public void add(NodeView node) {
            if (size() > 0) {
                removeFocusForHooks(get(0));
            }
            mySelected.add(0, node);
            addFocusForHooks(node);
            changeSelection(node, true);
            log.trace("Added focused {}\nAll={}", node, mySelected);
        }

        private void removeFocusForHooks(NodeView node) {
            if (node.getModel() == null)
                return;
            mapView.getViewFeedback().onLostFocusNode(node);
        }

        private void addFocusForHooks(NodeView node) {
            mapView.getViewFeedback().onFocusNode(node);
        }

        public NodeView get(int i) {
            return mySelected.get(i);
        }

        public boolean contains(NodeView node) {
            return mySelected.contains(node);
        }

        public void moveToFirst(NodeView newSelected) {
            if (contains(newSelected)) {
                int pos = mySelected.indexOf(newSelected);
                if (pos > 0) {
                    if (size() > 0) {
                        removeFocusForHooks(get(0));
                    }
                    mySelected.remove(newSelected);
                    mySelected.add(0, newSelected);
                }
            } else {
                add(newSelected);
            }
            addFocusForHooks(newSelected);
            log.trace("MovedToFront selected {}\nAll={}", newSelected, mySelected);
        }
    }
}
