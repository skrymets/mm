package freemind.view.mindmapview;

import lombok.extern.slf4j.Slf4j;

import java.awt.*;

/**
 * This class will Layout the Nodes and Edges of an MapView.
 */
@Slf4j
public class MindMapLayout implements LayoutManager {

    final static int BORDER = 30;// width of the border around the map.
    // minimal width for input field of leaf or folded node (PN)
    // the MINIMAL_LEAF_WIDTH is reserved by calculation of the map width
    public final static int MINIMAL_LEAF_WIDTH = 150;

    public MindMapLayout() {
    }

    public void addLayoutComponent(String name, Component comp) {
    }

    public void removeLayoutComponent(Component comp) {
    }

    public void layoutContainer(Container c) {
        final MapView mapView = (MapView) c;
        final int calcXBorderSize = calcXBorderSize(mapView);
        final int calcYBorderSize = calcYBorderSize(mapView);
        getRoot(mapView).validate();
        getRoot(mapView).setLocation(calcXBorderSize, calcYBorderSize);
        mapView.setSize(calcXBorderSize * 2 + getRoot(mapView).getWidth(),
                calcYBorderSize * 2 + getRoot(mapView).getHeight());
        final int componentCount = mapView.getComponentCount();
        for (int i = 0; i < componentCount; i++) {
            final Component component = mapView.getComponent(i);
            if (!component.isValid()) {
                component.validate();
            }
        }
    }

    //
    // Absolute positioning
    //

    //
    // Get Methods
    //

    private NodeView getRoot(Container c) {
        return ((MapView) c).getRoot();
    }

    // This is actually never used.
    public Dimension minimumLayoutSize(Container parent) {
        return new Dimension(200, 200);
    } // For testing Purposes

    public Dimension preferredLayoutSize(Container c) {
        final MapView mapView = (MapView) c;
        final Dimension preferredSize = mapView.getRoot().getPreferredSize();
        return new Dimension(
                2 * calcXBorderSize(mapView) + preferredSize.width, 2
                * calcYBorderSize(mapView) + preferredSize.height);
    }

    private int calcYBorderSize(MapView map) {
        int yBorderSize;
        final int minBorderHeight = map.getZoomed(MindMapLayout.BORDER);
        Dimension visibleSize = map.getGeometryService().getViewportSize();
        if (visibleSize != null) {
            yBorderSize = Math.max(visibleSize.height, minBorderHeight);
        } else {
            yBorderSize = minBorderHeight;
        }
        return yBorderSize;
    }

    private int calcXBorderSize(MapView map) {
        int xBorderSize;
        Dimension visibleSize = map.getGeometryService().getViewportSize();
        final int minBorderWidth = map.getZoomed(MindMapLayout.BORDER
                + MindMapLayout.MINIMAL_LEAF_WIDTH);
        if (visibleSize != null) {
            xBorderSize = Math.max(visibleSize.width, minBorderWidth);
        } else {
            xBorderSize = minBorderWidth;

        }
        return xBorderSize;
    }

}// class MindMapLayout
