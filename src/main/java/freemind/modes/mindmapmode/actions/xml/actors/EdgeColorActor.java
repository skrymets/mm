package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.EdgeColorFormatAction;
import freemind.controller.actions.XmlAction;
import freemind.main.ColorUtils;
import freemind.model.EdgeAdapter;
import freemind.model.MindMapNode;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.mindmapmode.MindMapEdgeModel;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

import java.awt.*;
import java.util.Objects;

public class EdgeColorActor extends XmlActorAdapter {

    public EdgeColorActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public void setEdgeColor(MindMapNode node, Color color) {
        EdgeColorFormatAction doAction = createEdgeColorFormatAction(node,
                color);
        EdgeColorFormatAction undoAction = createEdgeColorFormatAction(node,
                ((EdgeAdapter) node.getEdge()).getRealColor());
        execute(new ActionPair(doAction, undoAction));

    }

    public void act(XmlAction action) {
        if (action instanceof EdgeColorFormatAction) {
            EdgeColorFormatAction edgeAction = (EdgeColorFormatAction) action;
            Color color = ColorUtils.xmlToColor(edgeAction.getColor());
            MindMapNode node = getNodeFromID(edgeAction.getNode());
            Color oldColor = ((EdgeAdapter) node.getEdge()).getRealColor();
            if (!Objects.equals(color, oldColor)) {
                ((MindMapEdgeModel) node.getEdge()).setColor(color);
                getExMapFeedback().nodeChanged(node);
            }
        }
    }

    public EdgeColorFormatAction createEdgeColorFormatAction(MindMapNode node,
                                                             Color color) {
        EdgeColorFormatAction edgeAction = new EdgeColorFormatAction();
        edgeAction.setNode(getNodeID(node));
        if (color != null) {
            edgeAction.setColor(ColorUtils.colorToXml(color));
        }
        return edgeAction;
    }

    public Class<EdgeColorFormatAction> getDoActionClass() {
        return EdgeColorFormatAction.class;
    }

}
