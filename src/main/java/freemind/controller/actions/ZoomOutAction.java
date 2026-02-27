package freemind.controller.actions;

import freemind.controller.Controller;
import freemind.controller.services.ZoomService;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ZoomOutAction extends AbstractAction {
    private final Controller controller;

    public ZoomOutAction(Controller controller) {
        super(controller.getResourceString("zoom_out"));
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        float currentZoom = controller.getView().getZoom();
        float[] values = ZoomService.getZoomValues();
        float lastZoom = values[0];
        for (float val : values) {
            if (val >= currentZoom) {
                controller.setZoom(lastZoom);
                return;
            }
            lastZoom = val;
        }
        controller.setZoom(lastZoom);
    }
}
