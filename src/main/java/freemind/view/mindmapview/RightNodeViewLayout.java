/*
 * Created on 05.06.2005
 *
 */
package freemind.view.mindmapview;

import freemind.main.PointUtils;

import javax.swing.*;
import java.awt.*;

public class RightNodeViewLayout extends NodeViewLayoutAdapter {
    static private RightNodeViewLayout instance = null;

    protected void layout() {
        final int contentHeight = getChildContentHeight(false);
        int childVerticalShift = getChildVerticalShift(false);
        final int childHorizontalShift = getChildHorizontalShift();

        if (getView().isContentVisible()) {
            getContent().setVisible(true);
            final Dimension contentPreferredSize = getContent()
                    .getPreferredSize();
            final int x = Math.max(getSpaceAround(),
                    -contentPreferredSize.width - childHorizontalShift);
            childVerticalShift += (contentPreferredSize.height - contentHeight) / 2;
            final int y = Math.max(getSpaceAround(), -childVerticalShift);
            getContent().setBounds(x, y, contentPreferredSize.width,
                    contentPreferredSize.height);
        } else {
            getContent().setVisible(false);
            final int x = Math.max(getSpaceAround(), -childHorizontalShift);
            final int y = Math.max(getSpaceAround(), -childVerticalShift);
            getContent().setBounds(x, y, 0, contentHeight);
        }

        placeRightChildren(childVerticalShift);
    }

    static RightNodeViewLayout getInstance() {
        if (instance == null)
            instance = new RightNodeViewLayout();
        return instance;
    }

    public void layoutNodeMotionListenerView(NodeMotionListenerView nodeMotionView) {
        NodeView nodeView = nodeMotionView.getMovedView();
        final JComponent content = nodeView.getContent();
        location.x = -LISTENER_VIEW_WIDTH;
        location.y = 0;
        PointUtils.convertPointToAncestor(content, location, nodeMotionView.getParent());
        nodeMotionView.setLocation(location);
        nodeMotionView.setSize(LISTENER_VIEW_WIDTH, content.getHeight());
    }

    public Point getMainViewOutPoint(NodeView view, NodeView targetView,
                                     Point destinationPoint) {
        final MainView mainView = view.getMainView();
        return mainView.getRightPoint();
    }

    public Point getMainViewInPoint(NodeView view) {
        final MainView mainView = view.getMainView();
        return mainView.getLeftPoint();
    }

}
