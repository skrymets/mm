package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.EdgeWidthFormatAction;
import freemind.controller.actions.XmlAction;
import freemind.model.EdgeAdapter;
import freemind.model.MindMapNode;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

public class EdgeWidthActor extends XmlActorAdapter {

    public EdgeWidthActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public Class<EdgeWidthFormatAction> getDoActionClass() {
        return EdgeWidthFormatAction.class;
    }

    public void setEdgeWidth(MindMapNode node, int width) {
        if (width == getWidth(node)) {
            return;
        }
        execute(getActionPair(node, width));

    }

    public ActionPair getActionPair(MindMapNode selected, int width) {
        EdgeWidthFormatAction styleAction = createEdgeWidthFormatAction(
                selected, width);
        EdgeWidthFormatAction undoStyleAction = createEdgeWidthFormatAction(
                selected, getWidth(selected));
        return new ActionPair(styleAction, undoStyleAction);
    }

    public int getWidth(MindMapNode selected) {
        return selected.getEdge().getRealWidth();
    }

    private EdgeWidthFormatAction createEdgeWidthFormatAction(
            MindMapNode selected, int width) {
        EdgeWidthFormatAction edgeWidthAction = new EdgeWidthFormatAction();
        edgeWidthAction.setNode(getNodeID(selected));
        edgeWidthAction.setWidth(width);
        return edgeWidthAction;
    }

    public void act(XmlAction action) {
        if (action instanceof EdgeWidthFormatAction) {
            EdgeWidthFormatAction edgeWithAction = (EdgeWidthFormatAction) action;
            MindMapNode node = getNodeFromID(edgeWithAction.getNode());
            int width = edgeWithAction.getWidth();
            EdgeAdapter edge = (EdgeAdapter) node.getEdge();
            if (edge.getRealWidth() != width) {
                edge.setWidth(width);
                getExMapFeedback().nodeChanged(node);
            }
        }
    }

}
