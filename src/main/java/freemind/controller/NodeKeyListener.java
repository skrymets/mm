package freemind.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * The KeyListener which belongs to the node and cares for Events like C-D
 * (Delete Node). It forwards the requests to NodeController.
 */
public class NodeKeyListener implements KeyListener {

    private KeyListener mListener;

    public NodeKeyListener(Controller controller) {
    }

    public void register(KeyListener listener) {
        this.mListener = listener;

    }

    public void deregister() {
        mListener = null;
    }

    public void keyPressed(KeyEvent e) {
        if (mListener != null)
            mListener.keyPressed(e);
    }

    public void keyReleased(KeyEvent e) {
        if (mListener != null)
            mListener.keyReleased(e);
    }

    public void keyTyped(KeyEvent e) {
        if (mListener != null)
            mListener.keyTyped(e);
    }

}
