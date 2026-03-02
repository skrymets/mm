package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.CloudColorXmlAction;
import freemind.controller.actions.XmlAction;
import freemind.main.ColorUtils;
import freemind.model.LineAdapter;
import freemind.model.MindMapNode;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

import java.awt.*;
import java.util.Objects;

public class CloudColorActor extends XmlActorAdapter {

    public CloudColorActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public void setCloudColor(MindMapNode node, Color color) {
        CloudColorXmlAction doAction = createCloudColorXmlAction(node, color);
        CloudColorXmlAction undoAction = createCloudColorXmlAction(node,
                (node.getCloud() == null) ? null : node.getCloud().getColor());
        execute(new ActionPair(doAction, undoAction));
    }

    public CloudColorXmlAction createCloudColorXmlAction(MindMapNode node,
                                                         Color color) {
        CloudColorXmlAction nodeAction = new CloudColorXmlAction();
        nodeAction.setNode(getNodeID(node));
        nodeAction.setColor(ColorUtils.colorToXml(color));
        return nodeAction;
    }

    public void act(XmlAction action) {
        if (action instanceof CloudColorXmlAction nodeColorAction) {
            Color color = ColorUtils.xmlToColor(nodeColorAction.getColor());
            MindMapNode node = getNodeFromID(nodeColorAction
                    .getNode());
            // this is not necessary, as this action is not enabled if there is
            // no cloud.
            if (node.getCloud() == null) {
                getExMapFeedback().getActorFactory().getCloudActor().setCloud(node, true);
            }
            Color selectedColor = null;
            if (node.getCloud() != null) {
                selectedColor = node.getCloud().getColor();
            }
            if (!Objects.equals(color, selectedColor)) {
                ((LineAdapter) node.getCloud()).setColor(color); // null
                getExMapFeedback().nodeChanged(node);
            }
        }
    }

    public Class<CloudColorXmlAction> getDoActionClass() {
        return CloudColorXmlAction.class;
    }

}
