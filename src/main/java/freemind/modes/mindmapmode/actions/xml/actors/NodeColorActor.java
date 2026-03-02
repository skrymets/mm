package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.NodeColorFormatAction;
import freemind.controller.actions.XmlAction;
import freemind.main.ColorUtils;
import freemind.model.MindMapNode;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

import java.awt.*;
import java.util.Objects;

public class NodeColorActor extends XmlActorAdapter {

    public NodeColorActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public void setNodeColor(MindMapNode node, Color color) {
        if (Objects.equals(color, node.getColor())) {
            return;
        }
        NodeColorFormatAction doAction = createNodeColorFormatAction(node, color);
        NodeColorFormatAction undoAction = createNodeColorFormatAction(node, node.getColor());
        execute(new ActionPair(doAction, undoAction));
    }

    public NodeColorFormatAction createNodeColorFormatAction(MindMapNode node,
                                                             Color color) {
        NodeColorFormatAction nodeAction = new NodeColorFormatAction();
        nodeAction.setNode(getNodeID(node));
        nodeAction.setColor(ColorUtils.colorToXml(color));
        return nodeAction;
    }

    public void act(XmlAction action) {
        if (action instanceof NodeColorFormatAction) {
            NodeColorFormatAction nodeColorAction = (NodeColorFormatAction) action;
            Color color = ColorUtils.xmlToColor(nodeColorAction.getColor());
            MindMapNode node = getNodeFromID(nodeColorAction
                    .getNode());
            Color oldColor = node.getColor();
            if (!Objects.equals(color, oldColor)) {
                node.setColor(color); // null
                getExMapFeedback().nodeChanged(node);
            }
        }
    }

    public Class<NodeColorFormatAction> getDoActionClass() {
        return NodeColorFormatAction.class;
    }

}
