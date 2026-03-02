/*
 * Created on 12.03.2004
 *
 */
package accessories.plugins;

import freemind.extensions.ModeControllerHookAdapter;
import freemind.view.mindmapview.MapView;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;

@Slf4j
public class FitToPage extends ModeControllerHookAdapter {

    private MapView view;

    public FitToPage() {
        super();
    }

    public void startupMapHook() {
        super.startupMapHook();
        view = getController().getView();
        if (view == null)
            return;

        zoom();
        EventQueue.invokeLater(this::scroll);
    }

    private int shift(int coord1, int size1, int coord2, int size2) {
        return coord1 - coord2 + (size1 - size2) / 2;
    }

    private void scroll() {
        Rectangle rect = view.getInnerBounds();
        Rectangle viewer = view.getVisibleRect();
        view.getScrollService().scrollBy(shift(rect.x, rect.width, viewer.x, viewer.width), shift(rect.y, rect.height, viewer.y, viewer.height));
    }

    private void zoom() {
        Rectangle rect = view.getInnerBounds();
        // calculate the zoom:
        double oldZoom = getController().getView().getZoom();
        JViewport viewPort = (JViewport) view.getParent();
        Dimension viewer = viewPort.getExtentSize();
        log.info("Found viewer rect={}/{}, {}/{}", viewer.height, rect.height, viewer.width, rect.width);
        double newZoom = viewer.width * oldZoom / (rect.width + 0.0);
        double heightZoom = viewer.height * oldZoom / (rect.height + 0.0);
        if (heightZoom < newZoom) {
            newZoom = heightZoom;
        }
        log.info("Calculated new zoom {}", newZoom);
        getController().getController().setZoom((float) (newZoom));
    }

}
