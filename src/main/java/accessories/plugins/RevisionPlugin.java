/*
 * Created on 5.06.2004
 *
 */
package accessories.plugins;

import freemind.controller.actions.EditNodeAction;
import freemind.controller.actions.XmlAction;
import freemind.model.MindMapNode;
import freemind.model.NodeAdapter;
import freemind.modes.mindmapmode.actions.xml.ActionHandler;
import freemind.modes.mindmapmode.hooks.PermanentMindMapNodeHookAdapter;

import java.awt.*;

public class RevisionPlugin extends PermanentMindMapNodeHookAdapter implements
        ActionHandler {

    static boolean alreadyUsed = false;

    private Color color;

    public RevisionPlugin() {
        super();
    }

    public void invoke(MindMapNode node) {
        super.invoke(node);
        if (alreadyUsed == false) {
            color = Color.YELLOW;
            // new register:
            getMindMapController().getActionRegistry().registerHandler(this);
            alreadyUsed = true;
        }
    }

    public void shutdownMapHook() {
        getMindMapController().getActionRegistry().deregisterHandler(this);
        super.shutdownMapHook();
    }

    public void executeAction(XmlAction action) {
        if (action instanceof EditNodeAction) {
            // there is an edit action.
            EditNodeAction editAction = (EditNodeAction) action;
            NodeAdapter node = getMindMapController().getNodeFromID(
                    editAction.getNode());
            node.setBackgroundColor(color);
            nodeChanged(node);
        }
    }

    public void startTransaction(String name) {
    }

    public void endTransaction(String name) {
    }

}
