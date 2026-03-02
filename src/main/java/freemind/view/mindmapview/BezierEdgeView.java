package freemind.view.mindmapview;

import java.awt.*;
import java.awt.geom.CubicCurve2D;

/**
 * This class represents a single Edge of a MindMap.
 */
public class BezierEdgeView extends EdgeView {

    final CubicCurve2D.Float graph = new CubicCurve2D.Float();
    private static final int XCTRL = 12;// the distance between endpoint and
    // controlpoint
    private static final int CHILD_XCTRL = 20; // -||- at the child's end

    public BezierEdgeView() {
        super();
    }

    private void update() {

        // YCTRL could be implemented but then we had to check whether target is
        // above or below source.
        int sign = (getTarget().isLeft()) ? -1 : 1;
        int sourceSign = 1;
        if (getSource().isRoot()
                && !VerticalRootNodeViewLayout.getUseCommonOutPointForRootNode()) {
            sourceSign = 0;
        }
        int xctrl = getMap().getZoomed(sourceSign * sign * XCTRL);
        int childXctrl = getMap().getZoomed(-1 * sign * CHILD_XCTRL);

        graph.setCurve(start.x, start.y, start.x + xctrl, start.y, end.x
                + childXctrl, end.y, end.x, end.y);
    }

    protected void paint(Graphics2D g) {
        update();
        final Color color = getColor();
        g.setColor(color);
        final Stroke stroke = getStroke();
        g.setStroke(stroke);
        g.draw(graph);

        if (isTargetEclipsed()) {
            g.setColor(g.getBackground());
            g.setStroke(getEclipsedStroke());
            g.draw(graph);
            g.setStroke(stroke);
            g.setColor(color);
        }
    }

    public Color getColor() {
        return getModel().getColor();
    }
}
