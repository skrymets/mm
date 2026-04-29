package accessories.plugins;

import freemind.controller.actions.*;
import freemind.controller.actions.xml.operations.*;
import freemind.extensions.HookRegistration;
import freemind.frok.patches.JIBXGeneratedUtil;
import freemind.main.XmlMarshallingTools;
import freemind.model.MindMap;
import freemind.modes.ModeController;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.actions.xml.ActionFilter;
import freemind.modes.mindmapmode.actions.xml.ActionHandler;
import freemind.modes.mindmapmode.actions.xml.ActionPair;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;

/**
 * This plugin formats new nodes using the formats given to former nodes.
 */
@Slf4j
public class FormatNewNodes implements ActionHandler, ActionFilter, HookRegistration {

    private final MindMapController controller;

    private final HashMap<String, XmlAction> formatActions;

    public FormatNewNodes(ModeController controller, MindMap map) {
        this.controller = (MindMapController) controller;
        this.formatActions = new HashMap<>();
    }

    public void register() {
        controller.getActionRegistry().registerHandler(this);
        controller.getActionRegistry().registerFilter(this);

    }

    public void deRegister() {
        controller.getActionRegistry().deregisterHandler(this);
        controller.getActionRegistry().deregisterFilter(this);
    }

    public void executeAction(XmlAction action) {
        // detect format changes:
        detectFormatChanges(action);
    }

    private void detectFormatChanges(XmlAction doAction) {

        if (doAction instanceof CompoundAction compAction) {

            List<XmlAction> xmlActions = JIBXGeneratedUtil.listXmlActions(compAction);

            for (var childAction : xmlActions) {
                detectFormatChanges(childAction);
            }
        } else if (doAction instanceof FormatNodeAction) {
            formatActions.put(doAction.getClass().getName(), doAction);
        }

    }

    public void startTransaction(String name) {
    }

    public void endTransaction(String name) {
    }

    public ActionPair filterAction(ActionPair pair) {
        if (pair.getDoAction() instanceof NewNodeAction newNodeAction) {
            // add to a compound the newNodeAction and the other formats we
            // have:
            var compound = new CompoundAction();
            var choice = new CompoundAction.Choice();
            choice.setNewNodeAction(newNodeAction);
            compound.addChoice(choice);

            for (var formatAction : formatActions.values()) {
                // deep copy:
                NodeAction copiedFormatAction = (NodeAction) XmlMarshallingTools.deepCopy(formatAction);
                copiedFormatAction.setNode(newNodeAction.getNewId());
                CompoundAction.Choice copiedFormatActionChoice = JIBXGeneratedUtil.choiceFromXmlActions(copiedFormatAction);
                compound.addChoice(copiedFormatActionChoice);
            }
            var newPair = new ActionPair(compound, pair.getUndoAction());
            return newPair;
        }
        return pair;
    }

}
