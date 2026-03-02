package freemind.controller.printpreview;

import javax.swing.*;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
class ZoomAction extends AbstractAction {
    public ZoomAction(Preview preview, double zoomStep) {
        super();
        this.preview = preview;
        this.zoomStep = zoomStep;
    }

    public void actionPerformed(ActionEvent e) {
        preview.changeZoom(zoomStep);
        preview.repaint();
    }

    protected final Preview preview;
    protected final double zoomStep;
}
