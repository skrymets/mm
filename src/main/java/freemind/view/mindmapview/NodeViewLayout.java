package freemind.view.mindmapview;

import java.awt.*;

public interface NodeViewLayout extends LayoutManager {
    void layoutNodeMotionListenerView(NodeMotionListenerView view);

    Point getMainViewOutPoint(NodeView view, NodeView targetView, Point destinationPoint);

    Point getMainViewInPoint(NodeView view);

    void layoutNodeFoldingComponent(NodeFoldingComponent pFoldingComponent);
}
