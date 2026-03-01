package freemind.view.mindmapview.services;

import freemind.main.PointUtils;
import freemind.main.SwingUtils;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.NodeView;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
public class ScrollService {

    private final MapView mapView;
    private NodeView nodeToBeVisible = null;
    private int extraWidth;
    private Point rootContentLocation;
    private final Timer mCenterNodeTimer;

    public ScrollService(MapView mapView) {
        this.mapView = mapView;
        this.mCenterNodeTimer = new Timer();
        this.rootContentLocation = new Point();
    }

    public void centerNode(final NodeView node) {
        SwingUtils.waitForEventQueue();
        if (!mapView.isValid()) {
            mCenterNodeTimer.schedule(new CheckLaterForCenterNodeTask(node), 100);
            return;
        }

        Dimension d = getViewportSize();
        JComponent content = node.getContent();
        Rectangle rect = new Rectangle(
                content.getWidth() / 2 - d.width / 2,
                content.getHeight() / 2 - d.height / 2,
                d.width,
                d.height
        );
        log.trace("Scroll to {}, {}", rect, mapView.getPreferredSize());
        content.scrollRectToVisible(rect);
    }

    public void scrollNodeToVisible(NodeView node) {
        scrollNodeToVisible(node, 0);
    }

    public void scrollNodeToVisible(NodeView node, int extraWidth) {
        if (!mapView.isValid()) {
            nodeToBeVisible = node;
            this.extraWidth = extraWidth;
            return;
        }
        final int HORIZ_SPACE = 10;
        final int HORIZ_SPACE2 = 20;
        final int VERT_SPACE = 5;
        final int VERT_SPACE2 = 10;

        final JComponent nodeContent = node.getContent();
        int width = nodeContent.getWidth();
        if (extraWidth < 0) {
            width -= extraWidth;
            nodeContent.scrollRectToVisible(new Rectangle(
                    -HORIZ_SPACE + extraWidth,
                    -VERT_SPACE,
                    width + HORIZ_SPACE2,
                    nodeContent.getHeight() + VERT_SPACE2
            ));
        } else {
            width += extraWidth;
            nodeContent.scrollRectToVisible(new Rectangle(
                    -HORIZ_SPACE,
                    -VERT_SPACE,
                    width + HORIZ_SPACE2,
                    nodeContent.getHeight() + VERT_SPACE2
            ));
        }
    }

    public void scrollBy(int x, int y) {
        Point currentPoint = getViewPosition();
        currentPoint.translate(x, y);
        setViewLocation(currentPoint.x, currentPoint.y);
    }

    public void setViewLocation(int x, int y) {
        Point currentPoint = new Point(x, y);
        if (currentPoint.getX() < 0) {
            currentPoint.setLocation(0, currentPoint.getY());
        }
        if (currentPoint.getY() < 0) {
            currentPoint.setLocation(currentPoint.getX(), 0);
        }
        Dimension viewportSize = getViewportSize();
        if (viewportSize == null) {
            return;
        }
        Dimension size = mapView.getSize();
        double maxX = size.getWidth() - viewportSize.getWidth();
        double maxY = size.getHeight() - viewportSize.getHeight();
        if (currentPoint.getX() > maxX) {
            currentPoint.setLocation(maxX, currentPoint.getY());
        }
        if (currentPoint.getY() > maxY) {
            currentPoint.setLocation(currentPoint.getX(), maxY);
        }
        setViewPosition(currentPoint);
    }

    public void setViewPosition(Point currentPoint) {
        if (mapView.getParent() instanceof JViewport) {
            JViewport mapViewport = (JViewport) mapView.getParent();
            mapViewport.setViewPosition(currentPoint);
        }
    }

    public void setViewPositionAfterValidate() {
        Point viewPosition = getViewPosition();
        Point oldRootContentLocation = rootContentLocation;
        final NodeView root = mapView.getRoot();
        Point newRootContentLocation = root.getContent().getLocation();
        PointUtils.convertPointToAncestor(mapView.getRoot(), newRootContentLocation, mapView.getParent());

        final int deltaX = newRootContentLocation.x - oldRootContentLocation.x;
        final int deltaY = newRootContentLocation.y - oldRootContentLocation.y;
        if (deltaX != 0 || deltaY != 0) {
            viewPosition.x += deltaX;
            viewPosition.y += deltaY;
            final int scrollMode = getScrollMode();
            setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
            setViewPosition(viewPosition);
            setScrollMode(scrollMode);
        } else {
            mapView.repaint();
        }
        if (nodeToBeVisible != null) {
            final int scrollMode = getScrollMode();
            setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
            scrollNodeToVisible(nodeToBeVisible, extraWidth);
            setScrollMode(scrollMode);
            nodeToBeVisible = null;
        }
    }

    public NodeView getNodeToBeVisible() {
        return nodeToBeVisible;
    }

    public void setNodeToBeVisible(NodeView node) {
        this.nodeToBeVisible = node;
    }

    public Point getRootContentLocation() {
        return rootContentLocation;
    }

    public void setRootContentLocation(Point location) {
        this.rootContentLocation = location;
    }

    private Dimension getViewportSize() {
        if (mapView.getParent() instanceof JViewport) {
            JViewport mapViewport = (JViewport) mapView.getParent();
            return mapViewport == null ? null : mapViewport.getSize();
        }
        return null;
    }

    private Point getViewPosition() {
        Point viewPosition = new Point(0, 0);
        if (mapView.getParent() instanceof JViewport) {
            JViewport mapViewport = (JViewport) mapView.getParent();
            viewPosition = mapViewport.getViewPosition();
        }
        return viewPosition;
    }

    private void setScrollMode(int mode) {
        if (mapView.getParent() instanceof JViewport) {
            JViewport mapViewport = (JViewport) mapView.getParent();
            mapViewport.setScrollMode(mode);
        }
    }

    private int getScrollMode() {
        if (mapView.getParent() instanceof JViewport) {
            JViewport mapViewport = (JViewport) mapView.getParent();
            return mapViewport.getScrollMode();
        }
        return 0;
    }

    private class CheckLaterForCenterNodeTask extends TimerTask {
        final NodeView mNode;

        public CheckLaterForCenterNodeTask(NodeView pNode) {
            super();
            mNode = pNode;
        }

        public void run() {
            centerNode(mNode);
        }
    }
}
