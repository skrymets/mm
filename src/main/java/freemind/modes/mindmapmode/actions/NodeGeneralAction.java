/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006 Joerg Mueller, Daniel Polansky, Christian Foltin, Dimitri Polivaev and others.
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
/*
 * Created on 05.05.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package freemind.modes.mindmapmode.actions;

import freemind.controller.actions.CompoundAction;
import freemind.frok.patches.JIBXGeneratedUtil;
import freemind.main.Tools;
import freemind.main.SwingUtils;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.MindMapMapModel;
import freemind.modes.mindmapmode.MindMapNodeModel;
import freemind.modes.mindmapmode.actions.xml.AbstractXmlAction;
import freemind.modes.mindmapmode.actions.xml.ActionPair;
import freemind.modes.mindmapmode.actions.xml.ActorXml;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
@Slf4j
public class NodeGeneralAction extends AbstractXmlAction {
    protected final MindMapController modeController;

    /**
     * -- SETTER --
     *  The singleNodeOperation to set.
     */
    @Setter
    SingleNodeOperation singleNodeOperation;

    private Class<?> mDoActionClass;

    /**
     * null if you cannot provide a title that is present in the resources. Use
     * the setName method to set your not translateble title after that. give a
     * resource name for the icon.
     */
    protected NodeGeneralAction(MindMapController modeController,
                                final String textID, String iconPath) {
        super(null, iconPath != null ? freemind.view.ImageFactory.getInstance().createIcon(
                modeController.getResource(iconPath)) : null, modeController);
        this.modeController = modeController;
        if (textID != null) {
            this.setName(modeController.getText(textID));
        }

        this.singleNodeOperation = null;
    }

    protected void setName(String name) {
        if (name != null) {
            putValue(Action.NAME, name);
            putValue(Action.SHORT_DESCRIPTION, SwingUtils.removeMnemonic(name));
        }

    }

    public NodeGeneralAction(MindMapController modeController, String textID,
                             String iconPath, SingleNodeOperation singleNodeOperation) {
        this(modeController, textID, iconPath);
        this.singleNodeOperation = singleNodeOperation;
    }

    public NodeGeneralAction(MindMapController modeController, String textID,
                             String iconPath,
                             freemind.modes.mindmapmode.actions.NodeActorXml actor) {
        this(modeController, textID, iconPath);
        addActor(actor);
    }

    /* (non-Javadoc)
     * @see freemind.modes.mindmapmode.actions.xml.AbstractXmlAction#xmlActionPerformed(java.awt.event.ActionEvent)
     */
    public void xmlActionPerformed(ActionEvent e) {
        if (singleNodeOperation != null) {
            for (freemind.model.MindMapNode mindMapNode : modeController.getSelecteds()) {
                MindMapNodeModel selected = (MindMapNodeModel) mindMapNode;
                singleNodeOperation.apply(
                        (MindMapMapModel) this.modeController.getMap(),
                        selected);
            }
        } else {
            // xml action:
            // Do-action
            CompoundAction doAction = new CompoundAction();
            // Undo-action
            CompoundAction undo = new CompoundAction();
            // sort selectedNodes list by depth, in order to guarantee that
            // sons are deleted first:
            for (freemind.model.MindMapNode mindMapNode : modeController.getSelecteds()) {
                MindMapNodeModel selected = (MindMapNodeModel) mindMapNode;
                ActionPair pair = getActionPair(selected);
                if (pair != null) {
                    CompoundAction.Choice doActionChoice = JIBXGeneratedUtil.choiceFromXmlActions(doAction);
                    doAction.addChoice(doActionChoice);
                    //doAction.addChoice(pair.getDoAction());
                    CompoundAction.Choice undoActionChoice = JIBXGeneratedUtil.choiceFromXmlActions(pair.getUndoAction());
                    undo.getChoiceList().add(0, undoActionChoice);
                }
            }
            if (doAction.sizeChoiceList() == 0)
                return;
            modeController.doTransaction((String) getValue(NAME),
                    new ActionPair(doAction, undo));
        }

    }

    /**
     * Override, if you have a different method to get to an actionpair (see EdgeStyleAction).
     */
    protected ActionPair getActionPair(MindMapNodeModel selected) {
        ActionPair pair = null;
        if (mDoActionClass != null) {
            ActorXml actorXml = getMindMapController().getActionRegistry().getActor(mDoActionClass);
            if (actorXml instanceof NodeActorXml) {
                NodeActorXml nodeActorXml = (NodeActorXml) actorXml;
                pair = nodeActorXml.apply(this.modeController.getMap(),
                        selected);
            } else {
                throw new IllegalArgumentException("ActorXml " + actorXml + " is not a NodeActorXml.");
            }
        } else {
            throw new IllegalArgumentException("doActionClass not defined.");
        }
        return pair;
    }

    protected void setDoActionClass(Class<?> pDoActionClass) {
        this.mDoActionClass = pDoActionClass;
    }

}
