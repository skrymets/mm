package freemind.view.mindmapview.services;

import freemind.main.PointUtils;
import freemind.main.SwingUtils;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.NodeView;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;

public class MapRenderingService {

    private final MapView mapView;
    private final LinkRenderingService linkRenderingService;

    private static boolean antialiasEdges = false;
    private static boolean antialiasAll = false;
    private static Stroke standardSelectionStroke;

    int paintingTime;
    int paintingAmount;

    public MapRenderingService(MapView mapView, LinkRenderingService linkRenderingService) {
        this.mapView = mapView;
        this.linkRenderingService = linkRenderingService;
    }

    public static void setAntialiasEdges(boolean value) {
        antialiasEdges = value;
    }

    public static void setAntialiasAll(boolean value) {
        antialiasAll = value;
    }

    public static boolean getAntialiasEdges() {
        return antialiasEdges;
    }

    public static boolean getAntialiasAll() {
        return antialiasAll;
    }

    public Object setEdgesRenderingHint(Graphics2D g) {
        Object renderingHint = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                (getAntialiasEdges())
                        ? RenderingHints.VALUE_ANTIALIAS_ON
                        : RenderingHints.VALUE_ANTIALIAS_OFF);
        return renderingHint;
    }

    public void setTextRenderingHint(Graphics2D g) {
        g.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                (getAntialiasAll())
                        ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON
                        : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                (getAntialiasAll())
                        ? RenderingHints.VALUE_ANTIALIAS_ON
                        : RenderingHints.VALUE_ANTIALIAS_OFF
        );
    }

    public void paintSelecteds(Graphics2D g) {
        if (!MapView.standardDrawRectangleForSelection || mapView.isCurrentlyPrinting()) {
            return;
        }
        final Color c = g.getColor();
        final Stroke s = g.getStroke();
        g.setColor(MapView.standardSelectRectangleColor);
        if (standardSelectionStroke == null) {
            standardSelectionStroke = new BasicStroke(2.0f);
        }
        g.setStroke(standardSelectionStroke);
        Object renderingHint = setEdgesRenderingHint(g);
        final Iterator<NodeView> i = mapView.getSelecteds().iterator();
        while (i.hasNext()) {
            NodeView selected = i.next();
            paintSelected(g, selected);
        }
        SwingUtils.restoreAntialiasing(g, renderingHint);
        g.setColor(c);
        g.setStroke(s);
    }

    private void paintSelected(Graphics2D g, NodeView selected) {
        final int arcWidth = 4;
        final JComponent content = selected.getContent();
        Point contentLocation = new Point();
        PointUtils.convertPointToAncestor(content, contentLocation, mapView);
        g.drawRoundRect(contentLocation.x - arcWidth, contentLocation.y
                        - arcWidth, content.getWidth() + 2 * arcWidth,
                content.getHeight() + 2 * arcWidth, 15, 15);
    }

    public int getPaintingTime() {
        return paintingTime;
    }

    public int getPaintingAmount() {
        return paintingAmount;
    }

    public void recordPaintTime(long localTime) {
        paintingAmount++;
        paintingTime += localTime;
    }
}
