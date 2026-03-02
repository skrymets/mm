package freemind.view.mindmapview;

import java.awt.*;

/**
 * This class represents a sharp Edge of a MindMap.
 */
public class SharpLinearEdgeView extends EdgeView {

    public SharpLinearEdgeView() {
        super();
    }

    protected void paint(Graphics2D g) {
        g.setColor(getColor());
        g.setPaint(getColor());
        g.setStroke(DEF_STROKE);
        int w = getMap().getZoomed(getWidth() / 2 + 1);
        int[] xs = {start.x, end.x, start.x};
        int[] ys = {start.y + w, end.y, start.y - w};
        // g.drawPolygon(xs,ys,3);
        g.fillPolygon(xs, ys, 3);
    }

    public Color getColor() {
        return getModel().getColor();
    }
}
