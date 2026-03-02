package freemind.view.mindmapview;

import freemind.main.Tools;
import freemind.main.SwingUtils;
import freemind.model.MindMapNode;

import java.awt.*;

class BubbleMainView extends MainView {
    final static Stroke DEF_STROKE = new BasicStroke();

    public Dimension getPreferredSize() {
        Dimension prefSize = super.getPreferredSize();
        prefSize.width += getNodeView().getMap().getZoomed(5);
        return prefSize;
    }

    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        final NodeView nodeView = getNodeView();
        final MindMapNode model = nodeView.getModel();
        if (model == null)
            return;

        Object renderingHint = getNodeView().getMap().getRenderingService().setEdgesRenderingHint(g);
        paintSelected(g);
        paintDragOver(g);

        // change to bold stroke
        // g.setStroke(BOLD_STROKE); // Changed by Daniel

        // Draw a standard node
        g.setColor(model.getEdge().getColor());
        // g.drawOval(0,0,size.width-1,size.height-1); // Changed by Daniel

        // return to std stroke
        g.setStroke(DEF_STROKE);
        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
        SwingUtils.restoreAntialiasing(g, renderingHint);

        super.paint(g);
    }

    public void paintSelected(Graphics2D graphics) {
        super.paintSelected(graphics);
        if (getNodeView().useSelectionColors()) {
            graphics.setColor(MapView.standardSelectColor);
            graphics.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10,
                    10);
        }
    }

    protected void paintBackground(Graphics2D graphics, Color color) {
        graphics.setColor(color);
        graphics.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
    }

    Point getLeftPoint() {
        Point in = new Point(0, getHeight() / 2);
        return in;
    }

    Point getCenterPoint() {
        Point in = getLeftPoint();
        in.x = getWidth() / 2;
        return in;
    }

    Point getRightPoint() {
        Point in = getLeftPoint();
        in.x = getWidth() - 1;
        return in;
    }

    public int getMainViewWidthWithFoldingMark() {
        int width = getWidth();
        int dW = getZoomedFoldingSymbolHalfWidth() * 2;
        if (getNodeView().getModel().isFolded()) {
            width += dW;
        }
        return width + dW;
    }

    public int getDeltaX() {
        if (getNodeView().getModel().isFolded() && getNodeView().isLeft()) {
            return super.getDeltaX() + getZoomedFoldingSymbolHalfWidth() * 2;
        }
        return super.getDeltaX();
    }

    public String getStyle() {
        return MindMapNode.STYLE_BUBBLE;
    }

    /**
     * Returns the relative position of the Edge
     */
    int getAlignment() {
        return NodeView.ALIGN_CENTER;
    }

    public int getTextWidth() {
        return super.getTextWidth() + getNodeView().getMap().getZoomed(5);
    }

    public int getTextX() {
        return super.getTextX() + getNodeView().getMap().getZoomed(2);
    }

}
