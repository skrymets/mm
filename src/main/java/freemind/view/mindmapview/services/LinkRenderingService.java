package freemind.view.mindmapview.services;

import freemind.model.MindMapLink;
import freemind.modes.MindMapArrowLink;
import freemind.view.mindmapview.ArrowLinkView;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.NodeView;

import java.awt.*;
import java.awt.geom.CubicCurve2D;
import java.util.*;
import java.util.List;

public class LinkRenderingService {

    private final MapView mapView;
    private List<ArrowLinkView> mArrowLinkViews = new ArrayList<>();

    public LinkRenderingService(MapView mapView) {
        this.mapView = mapView;
    }

    public List<ArrowLinkView> getArrowLinkViews() {
        return mArrowLinkViews;
    }

    public void resetArrowLinkViews() {
        mArrowLinkViews = new ArrayList<>();
    }

    public void collectLabels(NodeView source, HashMap<String, NodeView> labels) {
        if (mapView.getModel().getLinkRegistry() == null)
            return;
        String label = mapView.getModel().getLinkRegistry().getLabel(source.getModel());
        if (label != null)
            labels.put(label, source);
        for (NodeView target : source.getChildrenViews()) {
            collectLabels(target, labels);
        }
    }

    public void paintLinks(NodeView source, Graphics2D graphics, HashMap<String, NodeView> labels, HashSet<MindMapLink> linkAlreadyVisited) {
        if (mapView.getModel().getLinkRegistry() == null)
            return;

        if (linkAlreadyVisited == null)
            linkAlreadyVisited = new HashSet<>();

        List<MindMapLink> vec = mapView.getModel().getLinkRegistry()
                .getAllLinks(source.getModel());

        for (MindMapLink ref : vec) {
            if (linkAlreadyVisited.add(ref)) {
                if (ref instanceof MindMapArrowLink) {
                    ArrowLinkView arrowLink = new ArrowLinkView(
                            (MindMapArrowLink) ref,
                            mapView.getViewerRegistryService().getNodeView(ref.getSource()),
                            mapView.getViewerRegistryService().getNodeView(ref.getTarget()));

                    arrowLink.paint(graphics);
                    mArrowLinkViews.add(arrowLink);
                }
            }
        }
        for (NodeView target : source.getChildrenViews()) {
            paintLinks(target, graphics, labels, linkAlreadyVisited);
        }
    }

    public MindMapArrowLink detectCollision(Point p) {
        if (mArrowLinkViews == null)
            return null;

        for (ArrowLinkView arrowView : mArrowLinkViews) {
            if (arrowView.detectCollision(p))
                return arrowView.getModel();
        }

        return null;
    }
}
