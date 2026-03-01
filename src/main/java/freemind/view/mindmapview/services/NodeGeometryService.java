package freemind.view.mindmapview.services;

import freemind.main.PointUtils;
import freemind.modes.MindMapCloud;
import freemind.view.mindmapview.CloudView;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.NodeView;

import java.awt.*;
import java.util.LinkedList;

public class NodeGeometryService {

    private final NodeView nodeView;

    public NodeGeometryService(NodeView nodeView) {
        this.nodeView = nodeView;
    }

    public void getCoordinates(LinkedList<Point> inList) {
        getCoordinates(inList, 0, false, 0, 0);
    }

    private void getCoordinates(LinkedList<Point> inList, int additionalDistanceForConvexHull, boolean byChildren, int transX, int transY) {
        if (!nodeView.isVisible()) return;

        if (nodeView.isContentVisible()) {
            MindMapCloud cloud = nodeView.getModel().getCloud();
            if (byChildren && cloud != null) {
                additionalDistanceForConvexHull += CloudView.getAdditionalHeigth(cloud, nodeView) / 5;
            }
            final int x = transX + nodeView.getContent().getX() - nodeView.getMainView().getDeltaX();
            final int y = transY + nodeView.getContent().getY() - nodeView.getMainView().getDeltaY();
            final int width = nodeView.getMainView().getMainViewWidthWithFoldingMark();
            int heightWithFoldingMark = nodeView.getMainView().getMainViewHeightWithFoldingMark();
            final int height = Math.max(heightWithFoldingMark, nodeView.getContent().getHeight());
            inList.addLast(new Point(-additionalDistanceForConvexHull + x, -additionalDistanceForConvexHull + y));
            inList.addLast(new Point(-additionalDistanceForConvexHull + x, additionalDistanceForConvexHull + y + height));
            inList.addLast(new Point(additionalDistanceForConvexHull + x + width, additionalDistanceForConvexHull + y + height));
            inList.addLast(new Point(additionalDistanceForConvexHull + x + width, -additionalDistanceForConvexHull + y));
        }

        for (NodeView child : nodeView.getChildrenViews()) {
            new NodeGeometryService(child).getCoordinates(inList, additionalDistanceForConvexHull, true, transX + child.getX(), transY + child.getY());
        }
    }

    public Point getLinkPoint(Point declination) {
        MapView map = nodeView.getMap();
        int x, y;
        if (declination != null) {
            x = map.getZoomed(declination.x);
            y = map.getZoomed(declination.y);
        } else {
            x = 1;
            y = 0;
        }
        if (nodeView.isLeft()) {
            x = -x;
        }
        Point linkPoint;
        if (y != 0) {
            double ctgRect = Math.abs((double) nodeView.getContent().getWidth() / nodeView.getContent().getHeight());
            double ctgLine = Math.abs((double) x / y);
            int absLinkX, absLinkY;
            if (ctgRect > ctgLine) {
                absLinkX = Math.abs(x * nodeView.getContent().getHeight() / (2 * y));
                absLinkY = nodeView.getContent().getHeight() / 2;
            } else {
                absLinkX = nodeView.getContent().getWidth() / 2;
                absLinkY = Math.abs(y * nodeView.getContent().getWidth() / (2 * x));
            }
            linkPoint = new Point(nodeView.getContent().getWidth() / 2 + (x > 0 ? absLinkX : -absLinkX), nodeView.getContent().getHeight() / 2 + (y > 0 ? absLinkY : -absLinkY));
        } else {
            linkPoint = new Point((x > 0 ? nodeView.getContent().getWidth() : 0), (nodeView.getContent().getHeight() / 2));
        }
        linkPoint.translate(nodeView.getContent().getX(), nodeView.getContent().getY());
        nodeView.convertPointToMap(linkPoint);
        return linkPoint;
    }

    public int getAdditionalCloudHeigth() {
        if (!nodeView.isContentVisible()) {
            return 0;
        }
        MindMapCloud cloud = nodeView.getModel().getCloud();
        if (cloud != null) {
            return CloudView.getAdditionalHeigth(cloud, nodeView);
        }
        return 0;
    }

    public int getShift() {
        return nodeView.getMap().getZoomed(nodeView.getModel().calcShiftY());
    }

    public int getVGap() {
        return nodeView.getMap().getZoomed(nodeView.getModel().getVGap());
    }

    public int getHGap() {
        return nodeView.getMap().getZoomed(nodeView.getModel().getHGap());
    }

    public int getZoomedFoldingSymbolHalfWidth() {
        int preferredFoldingSymbolHalfWidth = (int) ((NodeView.getFoldingSymbolWidth() * nodeView.getMap().getZoom()) / 2);
        return Math.min(preferredFoldingSymbolHalfWidth, nodeView.getHeight() / 2);
    }

    public Point getFoldingMarkPosition() {
        return nodeView.getMainViewOutPoint(nodeView, new Point());
    }

    public Rectangle getInnerBounds() {
        final int space = nodeView.getMap().getZoomed(NodeView.SPACE_AROUND);
        return new Rectangle(space, space, nodeView.getWidth() - 2 * space, nodeView.getHeight() - 2 * space);
    }

    public boolean contains(int x, int y) {
        final int space = nodeView.getMap().getZoomed(NodeView.SPACE_AROUND) - 2 * getZoomedFoldingSymbolHalfWidth();
        return (x >= space) && (x < nodeView.getWidth() - space) && (y >= space) && (y < nodeView.getHeight() - space);
    }

    public Point convertPointToMap(Point p) {
        return PointUtils.convertPointToAncestor(nodeView, p, nodeView.getMap());
    }

    public Point getInPointInMap() {
        return convertPointToMap(nodeView.getMainViewInPoint());
    }
}
