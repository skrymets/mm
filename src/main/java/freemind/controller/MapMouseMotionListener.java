package freemind.controller;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * The MouseListener which belongs to MapView
 */
public class MapMouseMotionListener implements MouseMotionListener,
        MouseListener {

    public interface MapMouseMotionReceiver {
        void mouseDragged(MouseEvent e);

        void mousePressed(MouseEvent e);

        void mouseReleased(MouseEvent e);
    }

    private MapMouseMotionReceiver mReceiver;

    private final Controller c;

    public MapMouseMotionListener(Controller controller) {
        c = controller;
    }

    public void register(MapMouseMotionReceiver receiver) {
        mReceiver = receiver;
    }

    public void deregister() {
        mReceiver = null;
    }

    private void handlePopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            JPopupMenu popup = null;
            // detect collision with an element placed on the root pane of the
            // window.
            java.lang.Object obj = c.getView().detectCollision(e.getPoint());
            if (obj != null) {
                // there is a collision with object obj.
                // call the modecontroller to give a popup menu for this object
                popup = c.getModeController().getPopupForModel(obj);
            }
            if (popup == null) { // no context popup found:
                // normal popup:
                popup = c.getFrame().getFreeMindMenuBar().getMapsPopupMenu();
            }
            popup.show(e.getComponent(), e.getX(), e.getY());
            popup.setVisible(true);
        }
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        if (mReceiver != null) {
            mReceiver.mouseDragged(e);
        }
    }

    public void mouseClicked(MouseEvent e) {
        // to loose the focus in edit
        c.getView().getSelectionService().selectAsTheOnlyOneSelected(c.getView().getSelectionService().getSelected());
        c.obtainFocusForSelected();
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) { // start the move, when the user press the
            // mouse (PN)
            handlePopup(e);
        } else if (mReceiver != null)
            mReceiver.mousePressed(e);
        e.consume();
    }

    public void mouseReleased(MouseEvent e) {
        if (mReceiver != null) {
            mReceiver.mouseReleased(e);
        }
        handlePopup(e);
        e.consume();
        c.getView().setMoveCursor(false); // release the cursor to default
        // (PN)
    }
}
