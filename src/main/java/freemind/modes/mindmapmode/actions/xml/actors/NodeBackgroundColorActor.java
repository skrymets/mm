package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.NodeBackgroundColorFormatAction;
import freemind.controller.actions.XmlAction;
import freemind.main.ColorUtils;
import freemind.model.MindMapNode;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

import java.awt.*;
import java.util.Objects;

public class NodeBackgroundColorActor extends XmlActorAdapter {

    public NodeBackgroundColorActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public void setNodeBackgroundColor(MindMapNode node, Color color) {
        NodeBackgroundColorFormatAction doAction = createNodeBackgroundColorFormatAction(
                node, color);
        NodeBackgroundColorFormatAction undoAction = createNodeBackgroundColorFormatAction(
                node, node.getBackgroundColor());
        execute(new ActionPair(doAction, undoAction));
    }

    public NodeBackgroundColorFormatAction createNodeBackgroundColorFormatAction(
            MindMapNode node, Color color) {
        NodeBackgroundColorFormatAction nodeAction = new NodeBackgroundColorFormatAction();
        nodeAction.setNode(getNodeID(node));
        nodeAction.setColor(ColorUtils.colorToXml(color));
        return nodeAction;
    }

    public void act(XmlAction action) {
        if (action instanceof NodeBackgroundColorFormatAction) {
            NodeBackgroundColorFormatAction nodeColorAction = (NodeBackgroundColorFormatAction) action;
            Color color = ColorUtils.xmlToColor(nodeColorAction.getColor());
            MindMapNode node = getNodeFromID(nodeColorAction.getNode());
            Color oldColor = node.getBackgroundColor();
            if (!Objects.equals(color, oldColor)) {
                node.setBackgroundColor(color); // null
                getExMapFeedback().nodeChanged(node);
            }
        }
    }

    public Class<NodeBackgroundColorFormatAction> getDoActionClass() {
        return NodeBackgroundColorFormatAction.class;
    }

}
