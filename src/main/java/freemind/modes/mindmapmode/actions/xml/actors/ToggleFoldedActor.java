/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2014 Christian Foltin, Joerg Mueller, Daniel Polansky, Dimitri Polivaev and others.
 *
 *See COPYING for Details
 *
 *This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.generated.instance.CompoundAction;
import freemind.controller.actions.generated.instance.FoldAction;
import freemind.controller.actions.generated.instance.XmlAction;
import freemind.frok.patches.JIBXGeneratedUtil;
import freemind.main.FreeMind;
import freemind.main.Resources;
import freemind.main.Tools;
import freemind.model.MindMapNode;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.common.CommonToggleFoldedAction;
import freemind.modes.mindmapmode.actions.xml.ActionPair;
import lombok.extern.log4j.Log4j2;

import java.util.ListIterator;

/**
 * @author foltin
 * @date 10.04.2014
 */
@Log4j2
public class ToggleFoldedActor extends XmlActorAdapter {

    /**
     * @param pMapFeedback
     */
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
        log.trace("Compound contains " + comp.sizeChoiceList() + " elements.");
        return comp;
    }

    /**
     * @return null if node cannot be folded.
     */
    private FoldAction createSingleFoldAction(boolean fold, MindMapNode node,
                                              boolean undo) {
        FoldAction foldAction = null;
        if ((undo && (node.isFolded() == fold))
                || (!undo && (node.isFolded() != fold))) {
            if (node.hasChildren()
                    || Tools.safeEquals(
                    getExMapFeedback().getProperty(
                            "enable_leaves_folding"), "true")) {
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
            if (Resources.getInstance().getBoolProperty(
                    FreeMind.RESOURCES_SAVE_FOLDING_STATE)) {
                getExMapFeedback().nodeChanged(node);
            }
        }
    }

    public Class<FoldAction> getDoActionClass() {
        return FoldAction.class;
    }

    /**
     *
     */
    public void setFolded(MindMapNode node, boolean folded) {
        FoldAction doAction = createSingleFoldAction(folded, node, false);
        FoldAction undoAction = createSingleFoldAction(!folded, node, true);
        if (doAction == null || undoAction == null) {
            return;
        }
        execute(new ActionPair(doAction, undoAction));
    }

}
