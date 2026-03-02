/*
 * Created on 05.06.2005
 *
 */
package freemind.view.mindmapview;

import freemind.main.PointUtils;

import javax.swing.*;
import java.awt.*;

public class LeftNodeViewLayout extends NodeViewLayoutAdapter {
    static private LeftNodeViewLayout instance = null;

    protected void layout() {
        final int contentHeight = getChildContentHeight(true);
        int childVerticalShift = getChildVerticalShift(true);
        final int childHorizontalShift = getChildHorizontalShift();

        final int x = Math.max(getSpaceAround(), -childHorizontalShift);
        if (getView().isContentVisible()) {
            getContent().setVisible(true);
            final Dimension contentPreferredSize = getContent()
                    .getPreferredSize();
            childVerticalShift += (contentPreferredSize.height - contentHeight) / 2;
            final int y = Math.max(getSpaceAround(), -childVerticalShift);
            getContent().setBounds(x, y, contentPreferredSize.width,
                    contentPreferredSize.height);
        } else {
            getContent().setVisible(false);
            final int y = Math.max(getSpaceAround(), -childVerticalShift);
            getContent().setBounds(x, y, 0, contentHeight);
        }

        placeLeftChildren(childVerticalShift);
    }

    static LeftNodeViewLayout getInstance() {
        if (instance == null)
            instance = new LeftNodeViewLayout();
        return instance;
    }

    public void layoutNodeMotionListenerView(NodeMotionListenerView nodeMotionView) {
        NodeView nodeView = nodeMotionView.getMovedView();
        final JComponent content = nodeView.getContent();
        location.x = content.getWidth();
        location.y = 0;
        PointUtils.convertPointToAncestor(content, location, nodeMotionView.getParent());
        nodeMotionView.setLocation(location);
        nodeMotionView.setSize(LISTENER_VIEW_WIDTH, content.getHeight());
    }

    public Point getMainViewOutPoint(NodeView view, NodeView targetView,
                                     Point destinationPoint) {
        final MainView mainView = view.getMainView();
        return mainView.getLeftPoint();
    }

    public Point getMainViewInPoint(NodeView view) {
        final MainView mainView = view.getMainView();
        return mainView.getRightPoint();
    }

}
