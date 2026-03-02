package freemind.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * The MouseMotionListener which belongs to every NodeView
 */
public class NodeMouseMotionListener implements MouseMotionListener,
        MouseListener {

    public interface NodeMouseMotionObserver extends
            MouseMotionListener, MouseListener {

        void updateSelectionMethod();

    }

    private NodeMouseMotionObserver mListener;

    public NodeMouseMotionListener(Controller controller) {
    }

    public void register(NodeMouseMotionObserver listener) {
        this.mListener = listener;

    }

    public void deregister() {
        mListener = null;
    }

    public void mouseClicked(MouseEvent e) {
        if (mListener != null)
            mListener.mouseClicked(e);
    }

    public void mouseDragged(MouseEvent e) {
        if (mListener != null)
            mListener.mouseDragged(e);
    }

    public void mouseEntered(MouseEvent e) {
        if (mListener != null)
            mListener.mouseEntered(e);
    }

    public void mouseExited(MouseEvent e) {
        if (mListener != null)
            mListener.mouseExited(e);
    }

    public void mouseMoved(MouseEvent e) {
        if (mListener != null)
            mListener.mouseMoved(e);
    }

    public void mousePressed(MouseEvent e) {
        if (mListener != null)
            mListener.mousePressed(e);
    }

    public void mouseReleased(MouseEvent e) {
        if (mListener != null)
            mListener.mouseReleased(e);
    }

    public void updateSelectionMethod() {
        if (mListener != null)
            mListener.updateSelectionMethod();
    }

}
