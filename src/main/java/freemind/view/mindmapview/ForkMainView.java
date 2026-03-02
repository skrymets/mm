package freemind.view.mindmapview;

import freemind.main.Tools;
import freemind.main.SwingUtils;
import freemind.model.EdgeAdapter;
import freemind.model.MindMapEdge;
import freemind.model.MindMapNode;

import java.awt.*;
import java.util.Iterator;

class ForkMainView extends MainView {
    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;

        final NodeView nodeView = getNodeView();
        final MindMapNode model = nodeView.getModel();
        if (model == null)
            return;

        Object renderingHint = getNodeView().getMap().getRenderingService().setEdgesRenderingHint(g);
        paintSelected(g);
        paintDragOver(g);

        int edgeWidth = getEdgeWidth();
        Color oldColor = g.getColor();
        g.setStroke(new BasicStroke(edgeWidth));
        // Draw a standard node
        g.setColor(model.getEdge().getColor());
        g.drawLine(0, getHeight() - edgeWidth / 2 - 1, getWidth(), getHeight()
                - edgeWidth / 2 - 1);
        g.setColor(oldColor);
        SwingUtils.restoreAntialiasing(g, renderingHint);
        super.paint(g);
    }

    public int getMainViewWidthWithFoldingMark() {
        int width = getWidth();
        if (getNodeView().getModel().isFolded()) {
            width += getZoomedFoldingSymbolHalfWidth() * 2
                    + getZoomedFoldingSymbolHalfWidth();
        }
        return width;
    }

    public int getMainViewHeightWithFoldingMark() {
        int height = getHeight();
        if (getNodeView().getModel().isFolded()) {
            height += getZoomedFoldingSymbolHalfWidth();
        }
        return height;
    }

    public int getDeltaX() {
        if (getNodeView().getModel().isFolded() && getNodeView().isLeft()) {
            return super.getDeltaX() + getZoomedFoldingSymbolHalfWidth() * 3;
        }
        return super.getDeltaX();
    }

    public String getStyle() {
        return MindMapNode.STYLE_FORK;
    }

    /**
     * Returns the relative position of the Edge
     */
    int getAlignment() {
        return NodeView.ALIGN_BOTTOM;
    }

    Point getLeftPoint() {
        int edgeWidth = getEdgeWidth();
        Point in = new Point(0, getHeight() - edgeWidth / 2 - 1);
        return in;
    }

    protected int getEdgeWidth() {
        MindMapNode nodeModel = getNodeView().getModel();
        MindMapEdge edge = nodeModel.getEdge();
        int edgeWidth = edge.getWidth();
        if (edgeWidth == 0) {
            edgeWidth = 1;
        }
        switch (edge.getStyleAsInt()) {
            case EdgeAdapter.INT_EDGESTYLE_SHARP_BEZIER:
                // intentionally fall through
            case EdgeAdapter.INT_EDGESTYLE_SHARP_LINEAR:
                // here, we take the maximum of width of children:
                edgeWidth = 1;
                for (Iterator<MindMapNode> it = nodeModel.childrenUnfolded(); it.hasNext(); ) {
                    MindMapNode child = it.next();
                    edgeWidth = Math.max(edgeWidth, child.getEdge().getWidth());
                }
        }
        return edgeWidth;
    }

    Point getCenterPoint() {
        Point in = new Point(getWidth() / 2, getHeight() / 2);
        return in;
    }

    Point getRightPoint() {
        int edgeWidth = getEdgeWidth();
        Point in = new Point(getWidth() - 1, getHeight() - edgeWidth / 2 - 1);
        return in;
    }

}
