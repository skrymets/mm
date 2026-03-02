package freemind.modes.common.listeners;

import freemind.controller.MapMouseMotionListener.MapMouseMotionReceiver;
import freemind.modes.ModeController;
import freemind.view.mindmapview.MapView;

import java.awt.*;
import java.awt.event.MouseEvent;

public class CommonMouseMotionManager implements MapMouseMotionReceiver {

    int originX = -1;

    int originY = -1;

    private final ModeController mController;

    // |= oldX >=0 iff we are in the drag

    public CommonMouseMotionManager(ModeController controller) {
        super();
        this.mController = controller;

    }

    public void mouseDragged(MouseEvent e) {
        Rectangle r = new Rectangle(e.getX(), e.getY(), 1, 1);
        MapView mapView = (MapView) e.getComponent();
        boolean isEventPointVisible = mapView.getVisibleRect().contains(r);
        if (!isEventPointVisible) {
            mapView.scrollRectToVisible(r);
        }
        // Always try to get mouse to the original position in the Map.
        if (originX >= 0 && isEventPointVisible) {
            ((MapView) e.getComponent()).getScrollService().scrollBy(originX - e.getX(), originY
                    - e.getY());
        }
    }

    public void mousePressed(MouseEvent e) {
        if (!mController.isBlocked() && e.getButton() == MouseEvent.BUTTON1) {
            mController.getView().setMoveCursor(true);
            originX = e.getX();
            originY = e.getY();

        }
    }

    public void mouseReleased(MouseEvent e) {
        originX = -1;
        originY = -1;

    }

}
