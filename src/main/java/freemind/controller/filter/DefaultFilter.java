/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
 *
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
/*
 * Created on 07.05.2005
 *
 */
package freemind.controller.filter;

import freemind.controller.Controller;
import freemind.controller.filter.condition.Condition;
import freemind.model.MindMap;
import freemind.model.MindMapNode;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.NodeView;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * @author dimitri 07.05.2005
 */
public class DefaultFilter implements Filter {

    private Condition condition = null;
    private int options = 0;

    /**
     *
     */
    public DefaultFilter(Condition condition, boolean areAnchestorsShown, boolean areDescendantsShown) {
        super();

        this.condition = condition;
        this.options = FILTER_INITIAL_VALUE | FILTER_SHOW_MATCHED;
        if (areAnchestorsShown) {
            options += FILTER_SHOW_ANCESTOR;
        }

        options += FILTER_SHOW_ECLIPSED;
        if (areDescendantsShown) {
            options += FILTER_SHOW_DESCENDANT;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * freemind.controller.filter.Filter#applyFilter(freemind.model.MindMap)
     */
    public void applyFilter(Controller controller) {
        if (condition == null) {
            return;
        }

        try {
            controller.getFrame().setWaitingCursor(true);
            MindMap map = controller.getModel();
            MapView mapView = controller.getView();
            MindMapNode root = map.getRootNode();

            resetFilter(root);
            if (filterChildren(root, controller, condition.checkNode(controller, root), false)) {
                addFilterResult(root, FILTER_SHOW_ANCESTOR);
            }
            selectVisibleNode(mapView);
        } finally {
            controller.getFrame().setWaitingCursor(false);
        }
    }

    static public void selectVisibleNode(MapView mapView) {
        LinkedList<NodeView> selectedNodes = mapView.getSelecteds();
        final int lastSelectedIndex = selectedNodes.size() - 1;
        if (lastSelectedIndex == -1) {
            return;
        }

        ListIterator<NodeView> iterator = selectedNodes.listIterator(lastSelectedIndex);
        while (iterator.hasPrevious()) {
            NodeView previous = (NodeView) iterator.previous();
            if (!previous.getModel().isVisible()) {
                mapView.toggleSelected(previous);
            }
        }

        NodeView selected = mapView.getSelected();
        if (!selected.getModel().isVisible()) {
            selected = getNearestVisibleParent(selected);
            mapView.selectAsTheOnlyOneSelected(selected);
        }
        mapView.setSiblingMaxLevel(selected.getModel().getNodeLevel());
    }

    static private NodeView getNearestVisibleParent(NodeView selectedNode) {
        return selectedNode.getModel().isVisible()
                ? selectedNode
                : getNearestVisibleParent(selectedNode.getParentView());
    }

    /**
     * @param c TODO
     */
    private boolean filterChildren(MindMapNode parent, Controller c, boolean isAncestorSelected, boolean isAncestorEclipsed) {
        ListIterator<MindMapNode> iterator = parent.childrenUnfolded();
        boolean isDescendantSelected = false;
        while (iterator.hasNext()) {
            MindMapNode node = (MindMapNode) iterator.next();
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
            addFilterResult(node, FILTER_SHOW_DESCENDANT);
        }

        boolean conditionSatisfied = condition.checkNode(c, node);
        if (conditionSatisfied) {
            isDescendantSelected = true;
            addFilterResult(node, FILTER_SHOW_MATCHED);
        } else {
            addFilterResult(node, FILTER_SHOW_HIDDEN);
        }

        if (isAncestorEclipsed) {
            addFilterResult(node, FILTER_SHOW_ECLIPSED);
        }

        if (filterChildren(node,
                c,
                conditionSatisfied || isAncestorSelected,
                !conditionSatisfied || isAncestorEclipsed)) {
            addFilterResult(node, FILTER_SHOW_ANCESTOR);
            isDescendantSelected = true;
        }
        return isDescendantSelected;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * freemind.controller.filter.Filter#isVisible(freemind.model.MindMapNode)
     */
    public boolean isVisible(MindMapNode node) {
        if (condition == null)
            return true;
        int filterResult = node.getFilterInfo().get();
        if ((options & FILTER_SHOW_ANCESTOR) != 0) {
            if ((options & filterResult & ~FILTER_SHOW_ECLIPSED) != 0) {
                return true;
            }
        } else if ((options & FILTER_SHOW_ECLIPSED) >= (filterResult & FILTER_SHOW_ECLIPSED)) {
            if ((options & filterResult & ~FILTER_SHOW_ECLIPSED) != 0) {
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

    /*
     * (non-Javadoc)
     *
     * @see freemind.controller.filter.Filter#areMatchedShown()
     */
    public boolean areMatchedShown() {
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see freemind.controller.filter.Filter#areHiddenShown()
     */
    public boolean areHiddenShown() {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see freemind.controller.filter.Filter#areAncestorsShown()
     */
    public boolean areAncestorsShown() {
        return 0 != (options & FILTER_SHOW_ANCESTOR);
    }

    /*
     * (non-Javadoc)
     *
     * @see freemind.controller.filter.Filter#areDescendantsShown()
     */
    public boolean areDescendantsShown() {
        return 0 != (options & FILTER_SHOW_DESCENDANT);
    }

    /*
     * (non-Javadoc)
     *
     * @see freemind.controller.filter.Filter#areEclipsedShown()
     */
    public boolean areEclipsedShown() {
        return true;
    }

    public Object getCondition() {
        return condition;
    }
}
