package freemind.view.mindmapview.services;

import freemind.main.PointUtils;
import freemind.main.SwingUtils;
import freemind.view.mindmapview.*;

import java.awt.*;

public class NodeRenderingService {

    private final NodeView nodeView;

    public NodeRenderingService(NodeView nodeView) {
        this.nodeView = nodeView;
    }

    public void paintCloudsAndEdges(Graphics2D g) {
        Object renderingHint = nodeView.getMap().setEdgesRenderingHint(g);
        for (int i = 0; i < nodeView.getComponentCount(); i++) {
            final Component component = nodeView.getComponent(i);
            if (!(component instanceof NodeView)) {
                continue;
            }
            NodeView childView = (NodeView) component;
            if (childView.isContentVisible()) {
                Point p = new Point();
                PointUtils.convertPointToAncestor(childView, p, nodeView);
                g.translate(p.x, p.y);
                paintCloud(g, childView);
                g.translate(-p.x, -p.y);
                EdgeView edge = NodeViewFactory.getInstance().getEdge(childView);
                edge.paint(childView, g);
            } else {
                new NodeRenderingService(childView).paintCloudsAndEdges(g);
            }
        }
        SwingUtils.restoreAntialiasing(g, renderingHint);
    }

    public void paintCloud(Graphics g) {
        paintCloud(g, nodeView);
    }

    private void paintCloud(Graphics g, NodeView view) {
        if (view.isContentVisible() && view.getModel().getCloud() != null) {
            CloudView cloud = new CloudView(view.getModel().getCloud(), view);
            cloud.paint(g);
        }
    }
}
