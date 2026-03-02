package freemind.controller;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * The MouseListener which belongs to MapView
 */
public class MapMouseWheelListener implements MouseWheelListener {

    private MouseWheelListener mListener;

    public MapMouseWheelListener(Controller controller) {
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        if (mListener != null)
            mListener.mouseWheelMoved(e);
    }

    public void register(MouseWheelListener handler) {
        mListener = handler;
    }

    public void deregister() {
        mListener = null;
    }
}
