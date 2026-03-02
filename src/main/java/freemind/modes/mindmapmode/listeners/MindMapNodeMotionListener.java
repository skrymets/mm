package freemind.modes.mindmapmode.listeners;

import freemind.controller.NodeMotionListener.NodeMotionAdapter;
import freemind.main.PointUtils;
import freemind.model.MindMapNode;
import freemind.model.NodeAdapter;
import freemind.modes.mindmapmode.MindMapController;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.NodeMotionListenerView;
import freemind.view.mindmapview.NodeView;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

/**
 * The MouseMotionListener which belongs to every NodeView
 */
@Slf4j
public class MindMapNodeMotionListener extends NodeMotionAdapter {

    private final MindMapController c;

    public MindMapNodeMotionListener(MindMapController controller) {
        c = controller;
    }

    private Point dragStartingPoint = null;
    private int originalParentVGap;
    private int originalHGap;
    private int originalShiftY;

    /**
     * Invoked when a mouse button is pressed on a component and then dragged.
     */
    public void mouseDragged(MouseEvent e) {
        log.trace("Event: mouseDragged");
        if ((e.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK) == (InputEvent.BUTTON1_DOWN_MASK)) {
            final NodeMotionListenerView motionListenerView = (NodeMotionListenerView) e
                    .getSource();
            final NodeView nodeView = getNodeView(e);
            final MapView mapView = nodeView.getMap();
            MindMapNode node = nodeView.getModel();
            Point point = e.getPoint();
            PointUtils.convertPointToAncestor(motionListenerView, point,
                    JScrollPane.class);
            if (!isActive()) {
                setDragStartingPoint(point, node);
            } else {
                Point dragNextPoint = point;
                if ((e.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) == 0) {
                    int nodeShiftY = getNodeShiftY(dragNextPoint, node,
                            dragStartingPoint);
                    int hGap = getHGap(dragNextPoint, node, dragStartingPoint);
                    node.setShiftY(nodeShiftY);
                    node.setHGap(hGap);
                } else {
                    MindMapNode parentNode = nodeView.getVisibleParentView()
                            .getModel();
                    parentNode
                            .setVGap(getVGap(dragNextPoint, dragStartingPoint));
                    c.getModeController().nodeRefresh(parentNode);
                }
                dragStartingPoint = dragNextPoint;
                c.getModeController().nodeRefresh(node);
            }
            Point mapPoint = e.getPoint();
            PointUtils.convertPointToAncestor(motionListenerView, mapPoint, mapView);
            boolean isEventPointVisible = mapView.getVisibleRect().contains(
                    mapPoint);
            if (!isEventPointVisible) {
                Rectangle r = new Rectangle(mapPoint);
                Rectangle bounds = mapView.getBounds();
                mapView.scrollRectToVisible(r);
                Rectangle bounds2 = mapView.getBounds();
                int diffx = bounds2.x - bounds.x;
                int diffy = bounds2.y - bounds.y;
                try {
                    mapPoint.translate(diffx, diffy);
                    // here, there are strange cases, when the mouse moves away.
                    // Workaround.
                    if (mapView.getVisibleRect().contains(mapPoint)) {
                        (new Robot()).mouseMove(e.getXOnScreen() + diffx,
                                e.getYOnScreen() + diffy);
                    }
                } catch (AWTException e1) {
                    log.error(e1.getLocalizedMessage(), e1);
                }
                dragStartingPoint.x += ((node.getHGap() < 0) ? 2 : 1) * diffx;
                dragStartingPoint.y += ((node.getShiftY() < 0) ? 2 : 1) * diffy;
            }
        }
    }

    private int getVGap(Point dragNextPoint, Point dragStartingPoint) {
        int oldVGap = originalParentVGap;
        int vGapChange = (int) ((dragNextPoint.y - dragStartingPoint.y) / c
                .getView().getZoom());
        oldVGap = Math.max(0, oldVGap - vGapChange);
        return oldVGap;
    }

    private int getHGap(Point dragNextPoint, MindMapNode node,
                        Point dragStartingPoint) {
        int oldHGap = node.getHGap();
        int hGapChange = (int) ((dragNextPoint.x - dragStartingPoint.x) / c
                .getView().getZoom());
        if (node.isLeft())
            hGapChange = -hGapChange;
        oldHGap += +hGapChange;
        return oldHGap;
    }

    private int getNodeShiftY(Point dragNextPoint, MindMapNode pNode,
                              Point dragStartingPoint) {
        int shiftY = pNode.getShiftY();
        int shiftYChange = (int) ((dragNextPoint.y - dragStartingPoint.y) / c
                .getView().getZoom());
        shiftY += shiftYChange;
        return shiftY;
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == 1 && e.getClickCount() == 2) {
            if (e.getModifiersEx() == 0) {
                NodeView nodeV = getNodeView(e);
                MindMapNode node = nodeV.getModel();
                c.moveNodePosition(node, node.getVGap(), NodeAdapter.HGAP, 0);
                return;
            }
            if (e.getModifiersEx() == InputEvent.CTRL_DOWN_MASK) {
                NodeView nodeV = getNodeView(e);
                MindMapNode node = nodeV.getModel();
                c.moveNodePosition(node, NodeAdapter.VGAP, node.getHGap(),
                        node.getShiftY());
            }
        }
    }

    private NodeView getNodeView(MouseEvent e) {
        return ((NodeMotionListenerView) e.getSource()).getMovedView();
    }

    public void mouseEntered(MouseEvent e) {
        log.trace("Event: mouseEntered");
        if (!JOptionPane.getFrameForComponent(e.getComponent()).isFocused())
            return;
        if (!isActive()) {
            NodeMotionListenerView v = (NodeMotionListenerView) e.getSource();
            v.setMouseEntered();
        }
    }

    public void mouseExited(MouseEvent e) {
        log.trace("Event: mouseExited");
        if (!isActive()) {
            NodeMotionListenerView v = (NodeMotionListenerView) e.getSource();
            v.setMouseExited();
        }
    }

    private void stopDrag() {
        setDragStartingPoint(null, null);
    }

    public void mouseReleased(MouseEvent e) {
        log.trace("Event: mouseReleased");
        NodeMotionListenerView v = (NodeMotionListenerView) e.getSource();
        if (!v.contains(e.getX(), e.getY()))
            v.setMouseExited();
        if (!isActive())
            return;
        NodeView nodeV = getNodeView(e);
        Point point = e.getPoint();
        PointUtils.convertPointToAncestor(nodeV, point, JScrollPane.class);
        // move node to end position.
        MindMapNode node = nodeV.getModel();
        MindMapNode parentNode = nodeV.getModel().getParentNode();
        final int parentVGap = parentNode.getVGap();
        final int hgap = node.getHGap();
        final int shiftY = node.getShiftY();
        resetPositions(node);
        c.moveNodePosition(node, parentVGap, hgap, shiftY);
        stopDrag();
    }

    private void resetPositions(MindMapNode node) {
        node.getParentNode().setVGap(originalParentVGap);
        node.setHGap(originalHGap);
        node.setShiftY(originalShiftY);
    }

    public boolean isActive() {
        return getDragStartingPoint() != null;
    }

    void setDragStartingPoint(Point point, MindMapNode node) {
        dragStartingPoint = point;
        if (point != null) {
            originalParentVGap = node.getParentNode().getVGap();
            originalHGap = node.getHGap();
            originalShiftY = node.getShiftY();
        } else {
            originalParentVGap = originalHGap = originalShiftY = 0;
        }
    }

    Point getDragStartingPoint() {
        return dragStartingPoint;
    }

}
