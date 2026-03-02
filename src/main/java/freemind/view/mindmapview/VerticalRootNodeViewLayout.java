/*
 * Created on 05.06.2005
 *
 */
package freemind.view.mindmapview;

import freemind.main.PointUtils;
import freemind.main.Resources;

import java.awt.*;

/**
 * Root layout.
 */
public class VerticalRootNodeViewLayout extends NodeViewLayoutAdapter {
    private static final String USE_COMMON_OUT_POINT_FOR_ROOT_NODE_STRING = "use_common_out_point_for_root_node";
    private static Boolean useCommonOutPointForRootNode;

    static boolean getUseCommonOutPointForRootNode() {
        if (useCommonOutPointForRootNode == null) {
            useCommonOutPointForRootNode = Resources.get()
                    .getBoolProperty(USE_COMMON_OUT_POINT_FOR_ROOT_NODE_STRING);
        }
        return useCommonOutPointForRootNode;
    }

    static private VerticalRootNodeViewLayout instance = null;

    protected void layout() {
        final int rightContentHeight = getChildContentHeight(false);
        int rightChildVerticalShift = getChildVerticalShift(false);
        final int leftContentHeight = getChildContentHeight(true);
        int leftChildVerticalShift = getChildVerticalShift(true);
        final int childHorizontalShift = getChildHorizontalShift();
        final int contentHeight = Math.max(rightContentHeight,
                leftContentHeight);
        final int x = Math.max(getSpaceAround(), -childHorizontalShift);
        if (getView().isContentVisible()) {
            getContent().setVisible(true);
            final Dimension contentPreferredSize = getContent()
                    .getPreferredSize();
            rightChildVerticalShift += (contentPreferredSize.height - rightContentHeight) / 2;
            leftChildVerticalShift += (contentPreferredSize.height - leftContentHeight) / 2;
            final int childVerticalShift = Math.min(rightChildVerticalShift,
                    leftChildVerticalShift);
            final int y = Math.max(getSpaceAround(), -childVerticalShift);
            getContent().setBounds(x, y, contentPreferredSize.width,
                    contentPreferredSize.height);
        } else {
            getContent().setVisible(false);
            int childVerticalShift = Math.min(rightChildVerticalShift,
                    leftChildVerticalShift);
            final int y = Math.max(getSpaceAround(), -childVerticalShift);
            getContent().setBounds(x, y, 0, contentHeight);
        }

        placeLeftChildren(leftChildVerticalShift);
        int width1 = getView().getWidth();
        int height1 = getView().getHeight();
        placeRightChildren(rightChildVerticalShift);
        int width2 = getView().getWidth();
        int height2 = getView().getHeight();
        getView().setSize(Math.max(width1, width2), Math.max(height1, height2));

    }

    static VerticalRootNodeViewLayout getInstance() {
        if (instance == null)
            instance = new VerticalRootNodeViewLayout();
        return instance;
    }

    public void layoutNodeMotionListenerView(NodeMotionListenerView view) {
        // there is no move handle at root.
    }

    public Point getMainViewOutPoint(NodeView view, NodeView targetView,
                                     Point destinationPoint) {
        final MainView mainView = view.getMainView();
        if (getUseCommonOutPointForRootNode()) {
            if (targetView.isLeft()) {
                return mainView.getLeftPoint();
            } else {
                return mainView.getRightPoint();
            }
        }
        final Point p = new Point(destinationPoint);
        PointUtils.convertPointFromAncestor(view, p, mainView);
        double nWidth = mainView.getWidth() / 2f;
        double nHeight = mainView.getHeight() / 2f;
        final Point centerPoint = new Point((int) nWidth, (int) nHeight);
        // assume, that destinationPoint is on the right:
        double angle = Math.atan((p.y - centerPoint.y + 0f)
                / (p.x - centerPoint.x + 0f));
        if (p.x < centerPoint.x) {
            angle += Math.PI;
        }
        // now determine point on ellipsis corresponding to that angle:
        final Point out = new Point(centerPoint.x
                + (int) (Math.cos(angle) * nWidth), centerPoint.y
                + (int) (Math.sin(angle) * nHeight));
        return out;
    }

    public Point getMainViewInPoint(NodeView view) {
        final Point centerPoint = view.getMainView().getCenterPoint();
        return centerPoint;
    }

}
