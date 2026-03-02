package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.CompoundAction;
import freemind.controller.actions.FoldAction;
import freemind.controller.actions.XmlAction;
import freemind.frok.patches.JIBXGeneratedUtil;
import freemind.main.FreeMind;

import freemind.model.MindMapNode;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.common.CommonToggleFoldedAction;
import freemind.modes.mindmapmode.actions.xml.ActionPair;
import lombok.extern.slf4j.Slf4j;

import java.util.ListIterator;
import java.util.Objects;

@Slf4j
public class ToggleFoldedActor extends XmlActorAdapter {

    public ToggleFoldedActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public void toggleFolded(ListIterator listIterator) {

        boolean fold = CommonToggleFoldedAction.getFoldingState(CommonToggleFoldedAction.reset(listIterator));
        CompoundAction doAction = createFoldAction(CommonToggleFoldedAction.reset(listIterator), fold, false);
        CompoundAction undoAction = createFoldAction(CommonToggleFoldedAction.reset(listIterator), !fold, true);
        execute(new ActionPair(doAction, undoAction));
    }

    private CompoundAction createFoldAction(ListIterator<MindMapNode> iterator,
                                            boolean fold, boolean undo) {
        CompoundAction comp = new CompoundAction();
        // sort selectedNodes list by depth, in order to guarantee that sons
        // are deleted first:
        for (ListIterator<MindMapNode> it = iterator; it.hasNext(); ) {
            MindMapNode node = it.next();
            FoldAction foldAction = createSingleFoldAction(fold, node, undo);
            if (foldAction != null) {
                CompoundAction.Choice foldActionChoice = JIBXGeneratedUtil.choiceFromXmlActions(foldAction);
                if (!undo) {
                    comp.addChoice(foldActionChoice);
                } else {
                    // reverse the order:
                    comp.getChoiceList().add(0, foldActionChoice);
                }
            }
        }
        log.trace("Compound contains {} elements.", comp.sizeChoiceList());
        return comp;
    }

    /**
     * @return null if node cannot be folded.
     */
    private FoldAction createSingleFoldAction(boolean fold, MindMapNode node,
                                              boolean undo) {
        FoldAction foldAction = null;
        if ((undo && (node.isFolded() == fold)) || (!undo && (node.isFolded() != fold))) {
            if (node.hasChildren() || Objects.equals(getExMapFeedback().getProperty("enable_leaves_folding"), "true")) {
                foldAction = new FoldAction();
                foldAction.setFolded(fold);
                foldAction.setNode(getNodeID(node));
            }
        }
        return foldAction;
    }

    public void act(XmlAction action) {
        if (action instanceof FoldAction) {
            FoldAction foldAction = (FoldAction) action;
            MindMapNode node = getNodeFromID(foldAction
                    .getNode());
            boolean folded = foldAction.isFolded();
            // no root folding, fc, 16.5.2004
            if (node.isRoot() && folded) {
                return;
            }
            if (node.isFolded() != folded) {
                node.setFolded(folded);
                getExMapFeedback().getMap().nodeStructureChanged(node);
            }
            if (getExMapFeedback().getResources().getBoolProperty(
                    FreeMind.RESOURCES_SAVE_FOLDING_STATE)) {
                getExMapFeedback().nodeChanged(node);
            }
        }
    }

    public Class<FoldAction> getDoActionClass() {
        return FoldAction.class;
    }

    public void setFolded(MindMapNode node, boolean folded) {
        FoldAction doAction = createSingleFoldAction(folded, node, false);
        FoldAction undoAction = createSingleFoldAction(!folded, node, true);
        if (doAction == null || undoAction == null) {
            return;
        }
        execute(new ActionPair(doAction, undoAction));
    }

}
