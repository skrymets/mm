package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.CompoundAction;
import freemind.controller.actions.CutNodeAction;
import freemind.controller.actions.XmlAction;
import freemind.frok.patches.JIBXGeneratedUtil;
import freemind.model.MindMapNode;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.mindmapmode.actions.xml.ActionPair;
import freemind.modes.mindmapmode.actions.xml.actors.PasteActor.NodeCoordinate;

import java.awt.datatransfer.Transferable;
import java.util.List;

public class CutActor extends XmlActorAdapter {

    public CutActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public CutNodeAction getCutNodeAction(MindMapNode node) {
        var cutAction = new CutNodeAction();
        cutAction.setNode(getNodeID(node));
        return cutAction;
    }

    public Transferable cut(List<MindMapNode> nodeList) {
        getExMapFeedback().sortNodesByDepth(nodeList);
        Transferable totalCopy = getExMapFeedback().copy(nodeList, true);
        // Do-action
        var doAction = new CompoundAction();
        // Undo-action
        var undo = new CompoundAction();
        // sort selectedNodes list by depth, in order to guarantee that sons are
        // deleted first:
        for (var node : nodeList) {
            if (node.getParentNode() == null) {
                continue;
            }
            CutNodeAction cutNodeAction = getCutNodeAction(node);

            CompoundAction.Choice doActionChoice = JIBXGeneratedUtil.choiceFromXmlActions(cutNodeAction);
            doAction.addChoice(doActionChoice);

            var coord = new NodeCoordinate(node, node.isLeft());
            Transferable copy = getExMapFeedback().copy(node, true);
            XmlAction pasteNodeAction = getXmlActorFactory().getPasteActor()
                    .getPasteNodeAction(copy, coord, null);
            // The paste actions are reversed because of the strange
            // coordinates.
            CompoundAction.Choice undoActionChoice = JIBXGeneratedUtil.choiceFromXmlActions(pasteNodeAction);
            undo.getChoiceList().add(0, undoActionChoice);

        }
        if (doAction.sizeChoiceList() > 0) {
            execute(new ActionPair(doAction, undo));
        }
        return totalCopy;
    }

    public void act(XmlAction action) {
        CutNodeAction cutAction = (CutNodeAction) action;
        MindMapNode selectedNode = getNodeFromID(cutAction
                .getNode());
        getXmlActorFactory().getDeleteChildActor().deleteWithoutUndo(selectedNode);
    }

    public Class<CutNodeAction> getDoActionClass() {
        return CutNodeAction.class;
    }

}
