package freemind.view.mindmapview.services;

import freemind.main.PointUtils;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.NodeView;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.KeyEvent;

@Slf4j
public class NavigationService {

    private final MapView mapView;
    private final MapSelectionService selectionService;
    private final ScrollService scrollService;

    public NavigationService(MapView mapView, MapSelectionService selectionService, ScrollService scrollService) {
        this.mapView = mapView;
        this.selectionService = selectionService;
        this.scrollService = scrollService;
    }

    public void move(KeyEvent e) {
        NodeView newSelected = getVisibleNeighbour(e.getKeyCode());
        log.trace("New selected: {}", newSelected);
        if (newSelected != null) {
            if (!(newSelected == selectionService.getSelected())) {
                extendSelectionWithKeyMove(newSelected, e);
                scrollService.scrollNodeToVisible(newSelected);
            }
            e.consume();
        }
    }

    public void moveToRoot() {
        selectionService.selectAsTheOnlyOneSelected(mapView.getRoot());
        scrollService.centerNode(mapView.getRoot());
    }

    private NodeView getVisibleNeighbour(int directionCode) {
        NodeView oldSelected = selectionService.getSelected();
        log.trace("Old selected: {}", oldSelected);
        NodeView newSelected = null;

        switch (directionCode) {
            case KeyEvent.VK_LEFT:
                newSelected = getVisibleLeft(oldSelected);
                if (newSelected != null) {
                    mapView.setSiblingMaxLevel(newSelected.getModel().getNodeLevel());
                }
                return newSelected;

            case KeyEvent.VK_RIGHT:
                newSelected = getVisibleRight(oldSelected);
                if (newSelected != null) {
                    mapView.setSiblingMaxLevel(newSelected.getModel().getNodeLevel());
                }
                return newSelected;

            case KeyEvent.VK_UP:
                newSelected = oldSelected.getPreviousVisibleSibling();
                break;

            case KeyEvent.VK_DOWN:
                newSelected = oldSelected.getNextVisibleSibling();
                break;

            case KeyEvent.VK_PAGE_UP:
                newSelected = oldSelected.getPreviousPage();
                break;

            case KeyEvent.VK_PAGE_DOWN:
                newSelected = oldSelected.getNextPage();
                break;
        }
        return newSelected != oldSelected ? newSelected : null;
    }

    private NodeView getVisibleLeft(NodeView oldSelected) {
        NodeView newSelected = oldSelected;
        if (oldSelected.getModel().isRoot()) {
            newSelected = oldSelected.getPreferredVisibleChild(true);
        } else if (!oldSelected.isLeft()) {
            newSelected = oldSelected.getVisibleParentView();
        } else {
            if (oldSelected.getModel().isFolded()) {
                mapView.getViewFeedback().setFolded(oldSelected.getModel(), false);
                return oldSelected;
            }
            newSelected = oldSelected.getPreferredVisibleChild(true);
            while (newSelected != null && !newSelected.isContentVisible()) {
                newSelected = newSelected.getPreferredVisibleChild(true);
            }
        }
        return newSelected;
    }

    private NodeView getVisibleRight(NodeView oldSelected) {
        NodeView newSelected = oldSelected;
        if (oldSelected.getModel().isRoot()) {
            newSelected = oldSelected.getPreferredVisibleChild(false);
        } else if (oldSelected.isLeft()) {
            newSelected = oldSelected.getVisibleParentView();
        } else {
            if (oldSelected.getModel().isFolded()) {
                mapView.getViewFeedback().setFolded(oldSelected.getModel(), false);
                return oldSelected;
            }
            newSelected = oldSelected.getPreferredVisibleChild(false);
            while (newSelected != null && !newSelected.isContentVisible()) {
                newSelected = newSelected.getPreferredVisibleChild(false);
            }
        }
        return newSelected;
    }

    private void extendSelectionWithKeyMove(NodeView newlySelectedNodeView, KeyEvent e) {
        if (e.isShiftDown()) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT
                    || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                selectionService.setShiftSelectionOrigin(null);
                NodeView toBeNewSelected = newlySelectedNodeView
                        .isParentOf(selectionService.getSelected()) ? newlySelectedNodeView
                        : selectionService.getSelected();

                selectionService.selectBranch(toBeNewSelected, false);
                selectionService.makeTheSelected(toBeNewSelected);
                return;
            }

            if (selectionService.getShiftSelectionOrigin() == null) {
                selectionService.setShiftSelectionOrigin(selectionService.getSelected());
            }

            final int newY = getMainViewY(newlySelectedNodeView);
            final int selectionOriginY = getMainViewY(selectionService.getShiftSelectionOrigin());
            int deltaY = newY - selectionOriginY;
            NodeView currentSelected = selectionService.getSelected();

            if (e.getKeyCode() == KeyEvent.VK_PAGE_UP) {
                for (; ; ) {
                    final int currentSelectedY = getMainViewY(currentSelected);
                    if (currentSelectedY > selectionOriginY)
                        selectionService.deselect(currentSelected);
                    else
                        selectionService.makeTheSelected(currentSelected);
                    if (currentSelectedY <= newY)
                        break;
                    currentSelected = currentSelected.getPreviousVisibleSibling();
                }
                return;
            }

            if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
                for (; ; ) {
                    final int currentSelectedY = getMainViewY(currentSelected);
                    if (currentSelectedY < selectionOriginY)
                        selectionService.deselect(currentSelected);
                    else
                        selectionService.makeTheSelected(currentSelected);
                    if (currentSelectedY >= newY)
                        break;
                    currentSelected = currentSelected.getNextVisibleSibling();
                }
                return;
            }

            boolean enlargingMove = (deltaY > 0)
                    && (e.getKeyCode() == KeyEvent.VK_DOWN) || (deltaY < 0)
                    && (e.getKeyCode() == KeyEvent.VK_UP);

            if (enlargingMove) {
                selectionService.toggleSelected(newlySelectedNodeView);
            } else {
                selectionService.toggleSelected(selectionService.getSelected());
                selectionService.makeTheSelected(newlySelectedNodeView);
            }
        } else {
            selectionService.setShiftSelectionOrigin(null);
            selectionService.selectAsTheOnlyOneSelected(newlySelectedNodeView);
        }
    }

    public int getMainViewY(NodeView node) {
        Point newSelectedLocation = new Point();
        PointUtils.convertPointToAncestor(node.getMainView(), newSelectedLocation, mapView);
        return newSelectedLocation.y;
    }
}
