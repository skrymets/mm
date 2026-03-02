package freemind.modes.common.listeners;

import freemind.main.FreeMind;
import freemind.main.Resources;
import freemind.modes.ControllerAdapter;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.ViewFeedback.MouseWheelEventHandler;
import freemind.view.mindmapview.ZoomAnimator;
import lombok.extern.slf4j.Slf4j;

import java.awt.event.InputEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Set;

@Slf4j
public class MindMapMouseWheelEventHandler implements MouseWheelListener {

    private static int SCROLL_SKIPS = 8;
    private static final int HORIZONTAL_SCROLL_MASK = InputEvent.SHIFT_MASK
            | InputEvent.BUTTON1_MASK | InputEvent.BUTTON2_MASK
            | InputEvent.BUTTON3_MASK;
    private static final int ZOOM_MASK = InputEvent.CTRL_MASK;
    // |= oldX >=0 iff we are in the drag

    private final ControllerAdapter mController;
    private final ZoomAnimator zoomAnimator = new ZoomAnimator();

    public MindMapMouseWheelEventHandler(ControllerAdapter controller) {
        super();
        mController = controller;

        Resources.addPropertyChangeListener((propertyName, newValue, oldValue) -> {
            if (propertyName.equals(FreeMind.RESOURCES_WHEEL_VELOCITY)) {
                SCROLL_SKIPS = Integer.parseInt(newValue);
            }
        });
        SCROLL_SKIPS = controller.getFrame().getIntProperty(FreeMind.RESOURCES_WHEEL_VELOCITY, 8);
        log.info("Setting SCROLL_SKIPS to {}", SCROLL_SKIPS);
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        if (mController.isBlocked()) {
            return; // block the scroll during edit (PN)
        }
        Set<MouseWheelEventHandler> registeredMouseWheelEventHandler = mController.getRegisteredMouseWheelEventHandler();
        for (MouseWheelEventHandler handler : registeredMouseWheelEventHandler) {
            boolean result = handler.handleMouseWheelEvent(e);
            if (result) {
                // event was consumed:
                return;
            }
        }

        if ((e.getModifiers() & ZOOM_MASK) != 0) {
            // fc, 18.11.2003: when control pressed, then the zoom is changed.
            float newZoomFactor = 1f + Math.abs((float) e.getWheelRotation()) / 10f;
            if (e.getWheelRotation() < 0)
                newZoomFactor = 1 / newZoomFactor;
            final float oldZoom = ((MapView) e.getComponent()).getZoom();
            float newZoom = oldZoom / newZoomFactor;
            // round the value due to possible rounding problems.
            newZoom = (float) Math.rint(newZoom * 1000f) / 1000f;
            newZoom = Math.max(1f / 32f, newZoom);
            newZoom = Math.min(32f, newZoom);
            if (newZoom != oldZoom) {
                zoomAnimator.animateZoom(oldZoom, newZoom,
                    z -> mController.getController().setZoom(z));
            }
            // end zoomchange
        } else if ((e.getModifiers() & HORIZONTAL_SCROLL_MASK) != 0) {
            ((MapView) e.getComponent()).getScrollService().scrollBy(
                    SCROLL_SKIPS * e.getWheelRotation(), 0);
        } else {
            ((MapView) e.getComponent()).getScrollService().scrollBy(0,
                    SCROLL_SKIPS * e.getWheelRotation());
        }
    }

}
