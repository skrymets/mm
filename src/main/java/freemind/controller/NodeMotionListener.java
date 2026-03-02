package freemind.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * The MouseMotionListener which belongs to every NodeView
 */
public class NodeMotionListener extends MouseAdapter implements
        MouseMotionListener, MouseListener {

    public static abstract class NodeMotionAdapter extends MouseAdapter
            implements MouseMotionListener, MouseListener {

    }

    private NodeMotionAdapter mListener;

    public NodeMotionListener(Controller controller) {
    }

    public void register(NodeMotionAdapter listener) {
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

}
