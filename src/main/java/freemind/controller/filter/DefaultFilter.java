package freemind.controller.filter;

import freemind.controller.Controller;
import freemind.controller.filter.condition.Condition;
import freemind.model.Filter;
import freemind.model.FilterConstants;
import freemind.model.FilterContext;
import freemind.model.MindMapNode;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.NodeView;

import java.util.LinkedList;
import java.util.ListIterator;

public class DefaultFilter implements Filter {

    private Condition condition = null;
    private int options = 0;

    public DefaultFilter(Condition condition, boolean areAncestorsShown, boolean areDescendantsShown) {
        super();

        this.condition = condition;
        this.options = FilterConstants.FILTER_INITIAL_VALUE | FilterConstants.FILTER_SHOW_MATCHED;

        if (areAncestorsShown) {
            options += FilterConstants.FILTER_SHOW_ANCESTOR;
        }

        options += FilterConstants.FILTER_SHOW_ECLIPSED;
        if (areDescendantsShown) {
            options += FilterConstants.FILTER_SHOW_DESCENDANT;
        }
    }

    public void applyFilter(FilterContext context) {
        if (condition == null) {
            return;
        }
        Controller controller = (Controller) context;

        try {
            context.setWaitingCursor(true);
            MindMapNode root = context.getRootNode();
            MapView mapView = controller.getView();

            resetFilter(root);
            if (filterChildren(root, controller, condition.checkNode(controller, root), false)) {
                addFilterResult(root, FilterConstants.FILTER_SHOW_ANCESTOR);
            }
            selectVisibleNode(mapView);
        } finally {
            context.setWaitingCursor(false);
        }
    }

    static public void selectVisibleNode(MapView mapView) {
        LinkedList<NodeView> selectedNodes = mapView.getSelectionService().getSelecteds();
        final int lastSelectedIndex = selectedNodes.size() - 1;
        if (lastSelectedIndex == -1) {
            return;
        }

        ListIterator<NodeView> iterator = selectedNodes.listIterator(lastSelectedIndex);
        while (iterator.hasPrevious()) {
            NodeView previous = iterator.previous();
            if (!previous.getModel().isVisible()) {
                mapView.getSelectionService().toggleSelected(previous);
            }
        }

        NodeView selected = mapView.getSelectionService().getSelected();
        if (!selected.getModel().isVisible()) {
            selected = getNearestVisibleParent(selected);
            mapView.getSelectionService().selectAsTheOnlyOneSelected(selected);
        }
        mapView.setSiblingMaxLevel(selected.getModel().getNodeLevel());
    }

    static private NodeView getNearestVisibleParent(NodeView selectedNode) {
        return selectedNode.getModel().isVisible()
                ? selectedNode
                : getNearestVisibleParent(selectedNode.getParentView());
    }

    private boolean filterChildren(MindMapNode parent, Controller c, boolean isAncestorSelected, boolean isAncestorEclipsed) {
        ListIterator<MindMapNode> iterator = parent.childrenUnfolded();
        boolean isDescendantSelected = false;
        while (iterator.hasNext()) {
            MindMapNode node = iterator.next();
            isDescendantSelected = applyFilter(node, c, isAncestorSelected, isAncestorEclipsed, isDescendantSelected);
        }
        return isDescendantSelected;
    }

    private boolean applyFilter(MindMapNode node,
                                Controller c,
                                boolean isAncestorSelected,
                                boolean isAncestorEclipsed,
                                boolean isDescendantSelected) {
        resetFilter(node);
        if (isAncestorSelected) {
            addFilterResult(node, FilterConstants.FILTER_SHOW_DESCENDANT);
        }

        boolean conditionSatisfied = condition.checkNode(c, node);
        if (conditionSatisfied) {
            isDescendantSelected = true;
            addFilterResult(node, FilterConstants.FILTER_SHOW_MATCHED);
        } else {
            addFilterResult(node, FilterConstants.FILTER_SHOW_HIDDEN);
        }

        if (isAncestorEclipsed) {
            addFilterResult(node, FilterConstants.FILTER_SHOW_ECLIPSED);
        }

        if (filterChildren(node,
                c,
                conditionSatisfied || isAncestorSelected,
                !conditionSatisfied || isAncestorEclipsed)) {
            addFilterResult(node, FilterConstants.FILTER_SHOW_ANCESTOR);
            isDescendantSelected = true;
        }
        return isDescendantSelected;
    }

    public boolean isVisible(MindMapNode node) {
        if (condition == null)
            return true;
        int filterResult = node.getFilterInfo().get();
        if ((options & FilterConstants.FILTER_SHOW_ANCESTOR) != 0) {
            if ((options & filterResult & ~FilterConstants.FILTER_SHOW_ECLIPSED) != 0) {
                return true;
            }
        } else if ((options & FilterConstants.FILTER_SHOW_ECLIPSED) >= (filterResult & FilterConstants.FILTER_SHOW_ECLIPSED)) {
            if ((options & filterResult & ~FilterConstants.FILTER_SHOW_ECLIPSED) != 0) {
                return true;
            }
        }
        return false;
    }

    static public void resetFilter(MindMapNode node) {
        node.getFilterInfo().reset();
    }

    static void addFilterResult(MindMapNode node, int flag) {
        node.getFilterInfo().add(flag);
    }

    public boolean areMatchedShown() {
        return true;
    }

    public boolean areHiddenShown() {
        return false;
    }

    public boolean areAncestorsShown() {
        return 0 != (options & FilterConstants.FILTER_SHOW_ANCESTOR);
    }

    public boolean areDescendantsShown() {
        return 0 != (options & FilterConstants.FILTER_SHOW_DESCENDANT);
    }

    public boolean areEclipsedShown() {
        return true;
    }

    public Object getCondition() {
        return condition;
    }
}
