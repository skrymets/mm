package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.EdgeStyleFormatAction;
import freemind.controller.actions.XmlAction;
import freemind.model.EdgeAdapter;
import freemind.model.MindMapEdge;
import freemind.model.MindMapNode;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

import java.util.Objects;

public class EdgeStyleActor extends XmlActorAdapter {

    public EdgeStyleActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public Class<EdgeStyleFormatAction> getDoActionClass() {
        return EdgeStyleFormatAction.class;
    }

    public void setEdgeStyle(MindMapNode node, String style) {
        if (Objects.equals(style, getStyle(node))) {
            return;
        }
        if (style != null) {
            boolean found = false;
            // check style:
            for (int i = 0; i < EdgeAdapter.EDGESTYLES.length; i++) {
                String possibleStyle = EdgeAdapter.EDGESTYLES[i];
                if (Objects.equals(style, possibleStyle)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new IllegalArgumentException("Style " + style
                        + " is not known");
            }
        }
        execute(getActionPair(node, style));
    }

    public ActionPair getActionPair(MindMapNode selected, String style) {
        EdgeStyleFormatAction styleAction = createNodeStyleFormatAction(
                selected, style);
        EdgeStyleFormatAction undoStyleAction = createNodeStyleFormatAction(
                selected, getStyle(selected));
        return new ActionPair(styleAction, undoStyleAction);
    }

    public String getStyle(MindMapNode selected) {
        String oldStyle = selected.getEdge().getStyle();
        if (!selected.getEdge().hasStyle()) {
            oldStyle = null;
        }
        return oldStyle;
    }

    private EdgeStyleFormatAction createNodeStyleFormatAction(
            MindMapNode selected, String style) {
        EdgeStyleFormatAction edgeStyleAction = new EdgeStyleFormatAction();
        edgeStyleAction.setNode(getNodeID(selected));
        edgeStyleAction.setStyle(style);
        return edgeStyleAction;
    }

    public void act(XmlAction action) {
        if (action instanceof EdgeStyleFormatAction) {
            EdgeStyleFormatAction edgeStyleAction = (EdgeStyleFormatAction) action;
            MindMapNode node = getNodeFromID(edgeStyleAction.getNode());
            String newStyle = edgeStyleAction.getStyle();
            MindMapEdge edge = node.getEdge();
            if (!Objects.equals(edge.hasStyle() ? edge.getStyle() : null,
                    newStyle)) {
                ((EdgeAdapter) edge).setStyle(newStyle);
                getExMapFeedback().nodeChanged(node);
            }
        }
    }

}
