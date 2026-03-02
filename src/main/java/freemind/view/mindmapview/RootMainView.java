package freemind.view.mindmapview;

import freemind.main.FreeMind;
import freemind.main.SwingUtils;

import java.awt.*;

class RootMainView extends MainView {

    public Dimension getPreferredSize() {
        Dimension prefSize = super.getPreferredSize();
        prefSize.width *= 1.1;
        prefSize.height *= 2;
        return prefSize;
    }

    public void paint(Graphics graphics) {
        Graphics2D g2d = (Graphics2D) graphics;

        if (getNodeView().getModel() == null)
            return;

        Object renderingHint = getNodeView().getMap().getRenderingService().setEdgesRenderingHint(g2d);
        paintSelected(g2d);
        paintDragOver(g2d);

        // Draw a root node
        g2d.setColor(Color.gray);
        g2d.setStroke(new BasicStroke(1.0f));
        g2d.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
        SwingUtils.restoreAntialiasing(g2d, renderingHint);
        super.paint(g2d);
    }

    public void paintDragOver(Graphics2D graphics) {
        final int draggedOver = getDraggedOver();
        if (draggedOver == NodeView.DRAGGED_OVER_SON) {
            graphics.setPaint(new GradientPaint((float) getWidth() / 4, 0,
                    getNodeView().getMap().getBackground(), (float) (getWidth() * 3) / 4,
                    0, NodeView.dragColor));
            graphics.fillRect(getWidth() / 4, 0, getWidth() - 1,
                    getHeight() - 1);
        } else if (draggedOver == NodeView.DRAGGED_OVER_SON_LEFT) {
            graphics.setPaint(new GradientPaint((float) (getWidth() * 3) / 4, 0,
                    getNodeView().getMap().getBackground(), (float) getWidth() / 4, 0,
                    NodeView.dragColor));
            graphics.fillRect(0, 0, getWidth() * 3 / 4, getHeight() - 1);
        }
    }

    public void paintSelected(Graphics2D graphics) {
        if (getNodeView().useSelectionColors()) {
            paintBackground(graphics, getNodeView().getSelectedColor());
        } else {
            paintBackground(graphics, getNodeView().getTextBackground());
        }
    }

    protected void paintBackground(Graphics2D graphics, Color color) {
        graphics.setColor(color);
        graphics.fillOval(1, 1, getWidth() - 2, getHeight() - 2);
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

    public void setDraggedOver(Point p) {
        setDraggedOver((dropPosition(p.getX())) ? NodeView.DRAGGED_OVER_SON_LEFT
                : NodeView.DRAGGED_OVER_SON);
    }

    public String getStyle() {
        return getNodeView().getMap().getViewFeedback().getResources().getProperty(
                FreeMind.RESOURCES_ROOT_NODE_STYLE);
    }

    /**
     * Returns the relative position of the Edge
     */
    int getAlignment() {
        return NodeView.ALIGN_CENTER;
    }

    public int getTextWidth() {
        return super.getTextWidth() - getWidth() / 10;
    }

    public int getTextX() {
        return getIconWidth() + getWidth() / 20;
    }

    public boolean dropAsSibling(double xCoord) {
        return false;
    }

    /**
     * @return true if should be on the left, false otherwise.
     */
    public boolean dropPosition(double xCoord) {
        return xCoord < (double) (getSize().width * 1) / 2;
    }

}
