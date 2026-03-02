package freemind.modes.mindmapmode.listeners;

import freemind.controller.MapMouseMotionListener.MapMouseMotionReceiver;
import freemind.modes.MindMapArrowLink;
import freemind.modes.mindmapmode.MindMapController;
import freemind.view.mindmapview.MapView;

import java.awt.*;
import java.awt.event.MouseEvent;

public class MindMapMouseMotionManager implements MapMouseMotionReceiver {

    int originX = -1;

    int originY = -1;

    MindMapArrowLink draggedLink = null;

    private Point draggedLinkOldStartPoint;

    private Point draggedLinkOldEndPoint;

    private final MindMapController mController;

    // |= oldX >=0 iff we are in the drag

    public MindMapMouseMotionManager(MindMapController controller) {
        super();
        this.mController = controller;

    }

    public void mouseDragged(MouseEvent e) {
        MapView mapView = (MapView) e.getComponent();
        // Always try to get mouse to the original position in the Map.
        if (originX >= 0) {
            if (draggedLink != null) {
                int deltaX = (int) ((e.getX() - originX) / mController
                        .getView().getZoom());
                int deltaY = (int) ((e.getY() - originY) / mController
                        .getView().getZoom());
                draggedLink.changeInclination(mapView, originX, originY,
                        deltaX, deltaY);
                originX = e.getX();
                originY = e.getY();
                mController.getView().repaint();
            } else {
                mapView.getScrollService().scrollBy(originX - e.getX(), originY - e.getY());
            }
        }
    }

    public void mousePressed(MouseEvent e) {
        if (!mController.isBlocked() && e.getButton() == MouseEvent.BUTTON1) {
            mController.getView().setMoveCursor(true);
            originX = e.getX();
            originY = e.getY();
            draggedLink = mController.getView().detectCollision(
                    new Point(originX, originY));
            if (draggedLink != null) {
                draggedLinkOldStartPoint = draggedLink.getStartInclination();
                draggedLinkOldEndPoint = draggedLink.getEndInclination();
                draggedLink.showControlPoints(true);
                mController.getView().repaint();
            }

        }
    }

    public void mouseReleased(MouseEvent e) {
        originX = -1;
        originY = -1;
        if (draggedLink != null) {
            draggedLink.showControlPoints(false);
            // make action undoable.

            Point draggedLinkNewStartPoint = draggedLink.getStartInclination();
            Point draggedLinkNewEndPoint = draggedLink.getEndInclination();
            // restore old positions.
            draggedLink.setStartInclination(draggedLinkOldStartPoint);
            draggedLink.setEndInclination(draggedLinkOldEndPoint);
            // and change to the new again.
            mController.setArrowLinkEndPoints(draggedLink,
                    draggedLinkNewStartPoint, draggedLinkNewEndPoint);
            mController.getView().repaint();
            draggedLink = null;
        }

    }

}
