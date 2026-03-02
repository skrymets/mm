package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.AddCloudXmlAction;
import freemind.controller.actions.XmlAction;
import freemind.main.ColorUtils;
import freemind.model.MindMap;
import freemind.model.MindMapNode;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.mindmapmode.MindMapCloudModel;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

import java.awt.*;

public class CloudActor extends NodeXmlActorAdapter {

    public CloudActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public Class<AddCloudXmlAction> getDoActionClass() {
        return AddCloudXmlAction.class;
    }

    public ActionPair apply(MindMap model, MindMapNode selected) {
        ActionPair pair = getActionPair(selected, selected.getCloud() == null);
        return pair;
    }

    public void setCloud(MindMapNode node, boolean enable) {
        execute(getActionPair(node, enable));

    }

    private ActionPair getActionPair(MindMapNode selected, boolean enable) {
        AddCloudXmlAction cloudAction = createAddCloudXmlAction(selected,
                enable, null);
        AddCloudXmlAction undocloudAction = null;
        if (selected.getCloud() != null) {
            undocloudAction = createAddCloudXmlAction(selected, true, selected
                    .getCloud().getColor());
        } else {
            undocloudAction = createAddCloudXmlAction(selected, false, null);

        }
        return new ActionPair(cloudAction, undocloudAction);
    }

    private AddCloudXmlAction createAddCloudXmlAction(MindMapNode selected,
                                                      boolean enable, Color color) {
        AddCloudXmlAction nodecloudAction = new AddCloudXmlAction();
        nodecloudAction.setNode(getNodeID(selected));
        nodecloudAction.setEnabled(enable);
        nodecloudAction.setColor(ColorUtils.colorToXml(color));
        return nodecloudAction;
    }

    public void act(XmlAction action) {
        if (action instanceof AddCloudXmlAction) {
            AddCloudXmlAction nodecloudAction = (AddCloudXmlAction) action;
            MindMapNode node = getNodeFromID(nodecloudAction.getNode());
            if ((node.getCloud() == null) == nodecloudAction.isEnabled()) {
                if (nodecloudAction.isEnabled()) {
                    if (node.isRoot()) {
                        return;
                    }
                    MindMapCloudModel cloudModel = new MindMapCloudModel(node,
                            getExMapFeedback());
                    node.setCloud(cloudModel);
                    if (nodecloudAction.getColor() != null) {
                        Color color = ColorUtils.xmlToColor(nodecloudAction
                                .getColor());
                        cloudModel.setColor(color);
                    }
                } else {
                    node.setCloud(null);
                }
                getExMapFeedback().nodeChanged(node);
            }
        }
    }

}
