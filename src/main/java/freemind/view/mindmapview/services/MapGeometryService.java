package freemind.view.mindmapview.services;

import freemind.main.PointUtils;
import freemind.view.mindmapview.ArrowLinkView;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.NodeView;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MapGeometryService {

    private final MapView mapView;

    public MapGeometryService(MapView mapView) {
        this.mapView = mapView;
    }

    public Point getNodeContentLocation(NodeView nodeView) {
        Point contentXY = new Point(0, 0);
        PointUtils.convertPointToAncestor(nodeView.getContent(), contentXY, mapView);
        return contentXY;
    }

    public Dimension getViewportSize() {
        if (mapView.getParent() instanceof JViewport) {
            JViewport mapViewport = (JViewport) mapView.getParent();
            return mapViewport == null ? null : mapViewport.getSize();
        }
        return null;
    }

    public Point getViewPosition() {
        Point viewPosition = new Point(0, 0);
        if (mapView.getParent() instanceof JViewport) {
            JViewport mapViewport = (JViewport) mapView.getParent();
            viewPosition = mapViewport.getViewPosition();
        }
        return viewPosition;
    }

    public Rectangle getInnerBounds(List<ArrowLinkView> arrowLinkViews) {
        final Rectangle innerBounds = mapView.getRoot().getInnerBounds();
        innerBounds.x += mapView.getRoot().getX();
        innerBounds.y += mapView.getRoot().getY();
        final Rectangle maxBounds = new Rectangle(0, 0, mapView.getWidth(), mapView.getHeight());
        for (ArrowLinkView arrowView : arrowLinkViews) {
            final java.awt.geom.CubicCurve2D arrowLinkCurve = arrowView.arrowLinkCurve;
            if (arrowLinkCurve == null) {
                continue;
            }
            Rectangle arrowViewBigBounds = arrowLinkCurve.getBounds();
            if (!innerBounds.contains(arrowViewBigBounds)) {
                Rectangle arrowViewBounds = freemind.view.mindmapview.PathBBox.getBBox(arrowLinkCurve)
                        .getBounds();
                innerBounds.add(arrowViewBounds);
            }
        }
        return innerBounds.intersection(maxBounds);
    }
}
