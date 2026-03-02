package freemind.view.mindmapview;

import freemind.main.HtmlTools;
import freemind.main.PointUtils;
import freemind.model.MindMapNode;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Base class for all node views.
 */
@Slf4j
public abstract class MainView extends JLabel {
    static final Dimension minimumSize = new Dimension(0, 0);
    static final Dimension maximumSize = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);

    private static final int MIN_HOR_NODE_SIZE = 10;

    int getZoomedFoldingSymbolHalfWidth() {
        return getNodeView().getZoomedFoldingSymbolHalfWidth();
    }

    MainView() {
        isPainting = false;
        setAlignmentX(NodeView.CENTER_ALIGNMENT);
        setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
    }

    public Dimension getMinimumSize() {
        return minimumSize;
    }

    public Dimension getMaximumSize() {
        return maximumSize;
    }

    private boolean isPainting;

    public NodeView getNodeView() {
        return (NodeView) SwingUtilities.getAncestorOfClass(NodeView.class,
                this);
    }

    public Dimension getPreferredSize() {
        final String text = getText();
        boolean isEmpty = text.isEmpty()
                || (HtmlTools.isHtmlNode(text) && text.indexOf("<img") < 0 && HtmlTools
                .htmlToPlain(text).isEmpty());
        if (isEmpty) {
            setText("!");
        }
        Dimension prefSize = super.getPreferredSize();
        final float zoom = getNodeView().getMap().getZoom();
        if (zoom != 1F) {
            prefSize.width = (int) (0.99 + prefSize.width * zoom);
            prefSize.height = (int) (0.99 + prefSize.height * zoom);
        }

        if (isCurrentlyPrinting() && MapView.NEED_PREF_SIZE_BUG_FIX) {
            prefSize.width += getNodeView().getMap().getZoomed(10);
        }
        prefSize.width = Math.max(
                getNodeView().getMap().getZoomed(MIN_HOR_NODE_SIZE),
                prefSize.width);
        if (isEmpty) {
            setText("");
        }
        prefSize.width += getNodeView().getMap().getZoomed(12);
        prefSize.height += getNodeView().getMap().getZoomed(4);
        // /*@@@@@@@@@@@@@@*/
        // prefSize.width = 150;
        // prefSize.height = 20;
        return prefSize;
    }

    public void paint(Graphics g) {
        float zoom = getZoom();
        if (zoom != 1F) {
            // Dimitry: Workaround because Swing do not use fractional metrics
            // for laying JLabels out
            final Graphics2D g2 = (Graphics2D) g;
            zoom *= ZOOM_CORRECTION_FACTOR;
            final AffineTransform transform = g2.getTransform();
            g2.scale(zoom, zoom);
            isPainting = true;
            super.paint(g);
            isPainting = false;
            g2.setTransform(transform);
        } else {
            super.paint(g);
        }
    }

    protected boolean isCurrentlyPrinting() {
        return getNodeView().getMap().isCurrentlyPrinting();
    }

    private float getZoom() {
        float zoom = getNodeView().getMap().getZoom();
        return zoom;
    }

    public void paintSelected(Graphics2D graphics) {
        if (getNodeView().useSelectionColors()) {
            paintBackground(graphics, getNodeView().getSelectedColor());
        } else {
            final Color backgroundColor = getNodeView().getModel()
                    .getBackgroundColor();
            if (backgroundColor != null) {
                paintBackground(graphics, backgroundColor);
            }
        }
    }

    protected void paintBackground(Graphics2D graphics, Color color) {
        graphics.setColor(color);
        graphics.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
    }

    public void paintDragOver(Graphics2D graphics) {
        if (isDraggedOver == NodeView.DRAGGED_OVER_SON) {
            if (getNodeView().isLeft()) {
                graphics.setPaint(new GradientPaint((float) (getWidth() * 3) / 4, 0,
                        getNodeView().getMap().getBackground(), (float) getWidth() / 4,
                        0, NodeView.dragColor));
                graphics.fillRect(0, 0, getWidth() * 3 / 4, getHeight() - 1);
            } else {
                graphics.setPaint(new GradientPaint((float) getWidth() / 4, 0,
                        getNodeView().getMap().getBackground(),
                        (float) (getWidth() * 3) / 4, 0, NodeView.dragColor));
                graphics.fillRect(getWidth() / 4, 0, getWidth() - 1,
                        getHeight() - 1);
            }
        }

        if (isDraggedOver == NodeView.DRAGGED_OVER_SIBLING) {
            graphics.setPaint(new GradientPaint(0, (float) (getHeight() * 3) / 5,
                    getNodeView().getMap().getBackground(), 0, (float) getHeight() / 5,
                    NodeView.dragColor));
            graphics.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
    }

    public int getHeight() {
        if (isPainting) {
            final float zoom = getZoom();
            if (zoom != 1F) {
                return (int) (super.getHeight() / zoom);
            }
        }
        return super.getHeight();
    }

    public int getWidth() {
        if (isPainting) {
            final float zoom = getZoom();
            if (zoom != 1F) {
                return (int) (0.99f + super.getWidth() / zoom);
            }
        }
        return super.getWidth();
    }

    abstract Point getCenterPoint();

    abstract Point getLeftPoint();

    abstract Point getRightPoint();

    /**
     * get x coordinate including folding symbol
     */
    public int getDeltaX() {
        return 0;
    }

    /**
     * get y coordinate including folding symbol
     */
    public int getDeltaY() {
        return 0;
    }

    /**
     * get height including folding symbol
     */
    public int getMainViewHeightWithFoldingMark() {
        return getHeight();
    }

    /**
     * get width including folding symbol
     */
    public int getMainViewWidthWithFoldingMark() {
        return getWidth();
    }

    protected void convertPointToMap(Point p) {
        PointUtils.convertPointToAncestor(this, p, getNodeView().getMap());
    }

    protected void convertPointFromMap(Point p) {
        PointUtils.convertPointFromAncestor(getNodeView().getMap(), p, this);
    }

    protected int isDraggedOver = NodeView.DRAGGED_OVER_NO;
    public static final float ZOOM_CORRECTION_FACTOR = 1.0F;// former value, but
    // not very
    // understandable,
    // was: 0.97F;

    public void setDraggedOver(int draggedOver) {
        isDraggedOver = draggedOver;
    }

    public void setDraggedOver(Point p) {
        setDraggedOver((dropAsSibling(p.getX())) ? NodeView.DRAGGED_OVER_SIBLING
                : NodeView.DRAGGED_OVER_SON);
    }

    public int getDraggedOver() {
        return isDraggedOver;
    }

    public boolean dropAsSibling(double xCoord) {
        return isInVerticalRegion(xCoord, 1. / 3);
    }

    /**
     * @return true if should be on the left, false otherwise.
     */
    public boolean dropPosition(double xCoord) {
        /* here it is the same as me. */
        return getNodeView().isLeft();
    }

    /**
     * Determines whether or not the xCoord is in the part p of the node: if
     * node is on the left: part [1-p,1] if node is on the right: part[ 0,p] of
     * the total width.
     */
    public boolean isInVerticalRegion(double xCoord, double p) {
        return getNodeView().isLeft() ? xCoord > getSize().width * (1.0 - p)
                : xCoord < getSize().width * p;
    }

    public abstract String getStyle();

    abstract int getAlignment();

    public int getTextWidth() {
        return getWidth() - getIconWidth();
    }

    public int getTextX() {
        int gap = (getWidth() - getPreferredSize().width) / 2;
        final boolean isLeft = getNodeView().isLeft();
        if (isLeft) {
            gap = -gap;
        }
        return gap + (isLeft && !getNodeView().isRoot() ? 0 : getIconWidth());
    }

    public int getIconWidth() {
        final Icon icon = getIcon();
        if (icon == null) {
            return 0;
        }
        return getNodeView().getMap().getZoomed(icon.getIconWidth());
    }

    public boolean isInFollowLinkRegion(double xCoord) {
        final MindMapNode model = getNodeView().getModel();
        return model.getLink() != null
                && (model.isRoot() || !model.hasChildren() || isInVerticalRegion(
                xCoord, 1. / 2));
    }

    /**
     * @return true if a link is to be displayed and the cursor is the hand now.
     */
    public boolean updateCursor(double xCoord) {
        boolean followLink = isInFollowLinkRegion(xCoord);
        int requiredCursor = followLink ? Cursor.HAND_CURSOR
                : Cursor.DEFAULT_CURSOR;
        if (getCursor().getType() != requiredCursor) {
            setCursor(requiredCursor != Cursor.DEFAULT_CURSOR ? new Cursor(
                    requiredCursor) : null);
        }
        return followLink;
    }

}
