package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.NodeStyleFormatAction;
import freemind.controller.actions.XmlAction;
import freemind.model.MindMapNode;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

import java.util.Objects;

public class NodeStyleActor extends XmlActorAdapter {

    public NodeStyleActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public Class<NodeStyleFormatAction> getDoActionClass() {
        return NodeStyleFormatAction.class;
    }

    public void setStyle(MindMapNode node, String style) {
        if (style == null) {
            execute(getActionPair(node, null));
            return;
        }
        for (int i = 0; i < MindMapNode.NODE_STYLES.length; i++) {
            String dstyle = MindMapNode.NODE_STYLES[i];
            if (Objects.equals(style, dstyle)) {
                execute(getActionPair(node, style));
                return;
            }
        }
        throw new IllegalArgumentException("Unknown style " + style);
    }

    public ActionPair getActionPair(MindMapNode targetNode, String style) {
        NodeStyleFormatAction styleAction = createNodeStyleFormatAction(
                targetNode, style);
        NodeStyleFormatAction undoStyleAction = createNodeStyleFormatAction(
                targetNode, targetNode.getStyle());
        return new ActionPair(styleAction, undoStyleAction);
    }

    private NodeStyleFormatAction createNodeStyleFormatAction(
            MindMapNode selected, String style) {
        NodeStyleFormatAction nodeStyleAction = new NodeStyleFormatAction();
        nodeStyleAction.setNode(getNodeID(selected));
        nodeStyleAction.setStyle(style);
        return nodeStyleAction;
    }

    public void act(XmlAction action) {
        if (action instanceof NodeStyleFormatAction) {
            NodeStyleFormatAction nodeStyleAction = (NodeStyleFormatAction) action;
            MindMapNode node = getNodeFromID(nodeStyleAction.getNode());
            String style = nodeStyleAction.getStyle();
            if (!Objects.equals(node.hasStyle() ? node.getBareStyle() : null,
                    style)) {
                // logger.info("Setting style of " + node + " to "+ style +
                // " and was " + node.getStyle());
                node.setStyle(style);
                getExMapFeedback().nodeStyleChanged(node);
            }
        }
    }

}
