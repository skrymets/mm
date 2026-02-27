package freemind.controller.actions;

import freemind.controller.Controller;
import freemind.controller.services.ZoomService;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ZoomInAction extends AbstractAction {
    private final Controller controller;

    public ZoomInAction(Controller controller) {
        super(controller.getResourceString("zoom_in"));
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        float currentZoom = controller.getView().getZoom();
        float[] values = ZoomService.getZoomValues();
        for (float val : values) {
            if (val > currentZoom) {
                controller.setZoom(val);
                return;
            }
        }
        controller.setZoom(values[values.length - 1]);
    }
}
