package freemind.modes.mindmapmode;

import freemind.main.FreeMindXml;
import freemind.model.MindMapNode;
import freemind.modes.ArrowLinkAdapter;
import freemind.modes.MapFeedback;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.NodeView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.*;

public class MindMapArrowLinkModel extends ArrowLinkAdapter {

    public MindMapArrowLinkModel(MindMapNode source, MindMapNode target,
                                 MapFeedback pMapFeedback) {
        super(source, target, pMapFeedback);
    }

    /* maybe this method is wrong here, but ... */
    public Object clone() {
        return super.clone();
    }

    public String toString() {
        Document doc = FreeMindXml.newDocument();
        Element saved = save(doc);
        return "Source=" + getSource() + ", target=" + getTarget() + ", "
                + FreeMindXml.toString(saved);
    }

    public void changeInclination(MapView map, int originX, int originY,
                                  int deltaX, int deltaY) {
        double distSqToTarget = 0;
        double distSqToSource = 0;
        NodeView targetView = map.getViewerRegistryService().getNodeView(getTarget());
        NodeView sourceView = map.getViewerRegistryService().getNodeView(getSource());
        if (targetView != null && sourceView != null) {
            Point targetLinkPoint = targetView
                    .getLinkPoint(getEndInclination());
            Point sourceLinkPoint = sourceView
                    .getLinkPoint(getStartInclination());
            distSqToTarget = targetLinkPoint.distanceSq(originX, originY);
            distSqToSource = sourceLinkPoint.distanceSq(originX, originY);
        }
        if ((targetView == null || sourceView != null)
                && distSqToSource < distSqToTarget * 2.25) {
            Point changedInclination = getStartInclination();
            changeInclination(deltaX, deltaY, sourceView, changedInclination);
            setStartInclination(changedInclination);
        }

        if ((sourceView == null || targetView != null)
                && distSqToTarget < distSqToSource * 2.25) {
            Point changedInclination = getEndInclination();
            changeInclination(deltaX, deltaY, targetView, changedInclination);
            setEndInclination(changedInclination);
        }

    }

    private void changeInclination(int deltaX, int deltaY,
                                   NodeView linkedNodeView, Point changedInclination) {
        if (linkedNodeView.isLeft()) {
            deltaX = -deltaX;
        }
        changedInclination.translate(deltaX, deltaY);
        if (changedInclination.x != 0
                && Math.abs((double) changedInclination.y
                / changedInclination.x) < 0.015) {
            changedInclination.y = 0;
        }
        double k = changedInclination.distance(0, 0);
        if (k < 10) {
            if (k > 0) {
                changedInclination.x = (int) (changedInclination.x * 10 / k);
                changedInclination.y = (int) (changedInclination.y * 10 / k);
            } else {
                changedInclination.x = 10;
            }
        }
    }

}
