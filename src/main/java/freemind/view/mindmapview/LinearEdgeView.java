package freemind.view.mindmapview;

import java.awt.*;

/**
 * This class represents a single Edge of a MindMap.
 */
public class LinearEdgeView extends EdgeView {

    public LinearEdgeView() {
        super();
    }

    protected void paint(Graphics2D g) {
        final Color color = getColor();
        g.setColor(color);
        final Stroke stroke = getStroke();
        g.setStroke(stroke);
        int w = getWidth();
        if (w <= 1) {
            g.drawLine(start.x, start.y, end.x, end.y);
            if (isTargetEclipsed()) {
                g.setColor(g.getBackground());
                g.setStroke(getEclipsedStroke());
                g.drawLine(start.x, start.y, end.x, end.y);
                g.setColor(color);
                g.setStroke(stroke);
            }
        } else {
            // a little horizontal part because of line cap
            int dx = w / 3 + 1;
            if (getTarget().isLeft())
                dx = -dx;
            int[] xs = {start.x, start.x + dx, end.x - dx, end.x};
            int[] ys = {start.y, start.y, end.y, end.y};
            g.drawPolyline(xs, ys, 4);
            if (isTargetEclipsed()) {
                g.setColor(g.getBackground());
                g.setStroke(getEclipsedStroke());
                g.drawPolyline(xs, ys, 4);
                g.setColor(color);
                g.setStroke(stroke);
            }
        }
    }

    public Color getColor() {
        return getModel().getColor();
    }
}
