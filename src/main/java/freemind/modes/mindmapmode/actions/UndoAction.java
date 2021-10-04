/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2004  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
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
 *
 * Created on 20.09.2004
 */
/*$Id: UndoAction.java,v 1.1.2.2.2.4 2006/11/26 10:20:44 dpolivaev Exp $*/
package freemind.modes.mindmapmode.actions;

import freemind.controller.actions.generated.instance.CompoundAction;
import freemind.controller.actions.generated.instance.XmlAction;
import freemind.frok.patches.JIBXGeneratedUtil;
import freemind.main.Tools;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.actions.xml.AbstractXmlAction;
import freemind.modes.mindmapmode.actions.xml.ActionPair;
import lombok.extern.log4j.Log4j2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Vector;

@SuppressWarnings("serial")
@Log4j2
public class UndoAction extends AbstractXmlAction {

    private MindMapController controller;
    private boolean isUndoAction;
    protected Vector<ActionPair> actionPairList = new Vector<>();
    private long timeOfLastAdd = 0;
    private boolean actionFrameStarted = false;
    private static final long TIME_TO_BEGIN_NEW_ACTION = 100;

    public UndoAction(MindMapController controller) {
        this(controller,
                controller.getText("undo"),
                freemind.view.ImageFactory.getInstance().createIcon(controller.getResource("images/undo.png")), controller);
        this.controller = controller;
    }

    protected UndoAction(MindMapController adapter, String text, Icon icon, MindMapController mode) {
        super(text, icon, mode);
        this.controller = adapter;
        setEnabled(false);
        isUndoAction = false;
    }

    public boolean isUndoAction() {
        return isUndoAction;
    }

    protected void xmlActionPerformed(ActionEvent arg0) {
        if (actionPairList.size() > 0) {
            ActionPair pair = (ActionPair) actionPairList.get(0);
            informUndoPartner(pair);
            actionPairList.remove(0);
            undoDoAction(pair);
        }
        if (actionPairList.size() == 0) {
            // disable undo
            this.setEnabled(false);
        }
    }

    protected void informUndoPartner(ActionPair pair) {
        this.controller.redo.add(pair.reverse());
        this.controller.redo.setEnabled(true);
    }

    protected void undoDoAction(ActionPair pair) {
        log.info("Undo, doing: {}", Tools.printXmlAction(pair.getUndoAction()));
        log.info("Redo, would: {}", Tools.printXmlAction(pair.getDoAction()));
        isUndoAction = true;
        this.controller.doTransaction("Undo", new ActionPair(pair.getUndoAction(), pair.getDoAction()));
        isUndoAction = false;
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            super.setEnabled(actionPairList.size() != 0);
        } else {
            super.setEnabled(false);
        }
    }

    public void add(ActionPair pair) {

        XmlAction dcDo = Tools.deepCopy(pair.getDoAction());
        CompoundAction.Choice dcDoChoice = JIBXGeneratedUtil.choiceFromXmlActions(dcDo);
        XmlAction dcUndo = Tools.deepCopy(pair.getUndoAction());
        CompoundAction.Choice dcUndoChoice = JIBXGeneratedUtil.choiceFromXmlActions(dcUndo);

        long currentTime = System.currentTimeMillis();

        if ((actionPairList.size() > 0) && (actionFrameStarted || currentTime - timeOfLastAdd < TIME_TO_BEGIN_NEW_ACTION)) {
            // the actions are gathered in one compound action.
            ActionPair firstPair = (ActionPair) actionPairList.get(0);
            CompoundAction action;
            CompoundAction remedia;
            if (!(firstPair.getDoAction() instanceof CompoundAction) || !(firstPair.getUndoAction() instanceof CompoundAction)) {

                CompoundAction.Choice actionChoice = JIBXGeneratedUtil.choiceFromXmlActions(firstPair.getDoAction());
                action = new CompoundAction();
                action.addChoice(actionChoice);

                CompoundAction.Choice remediaChoice = JIBXGeneratedUtil.choiceFromXmlActions(firstPair.getUndoAction());
                remedia = new CompoundAction();
                remedia.addChoice(remediaChoice);
                actionPairList.remove(0);
                actionPairList.add(0, new ActionPair(action, remedia));
                firstPair = (ActionPair) actionPairList.get(0);
            } else {
                action = (CompoundAction) firstPair.getDoAction();
                remedia = (CompoundAction) firstPair.getUndoAction();
            }

            action.addChoice(dcDoChoice);
            remedia.getChoiceList().add(0, dcUndoChoice);
        } else {
            ActionPair storagePair = new ActionPair(dcDo, dcUndo);
            actionPairList.add(0, storagePair);
            // and cut vector, if bigger than given size:
            int maxEntries = 100;
            try {
                maxEntries = new Integer(controller.getFrame().getProperty("undo_levels"));
            } catch (NumberFormatException e) {
                freemind.main.Resources.getInstance().logException(e);
            }
            while (actionPairList.size() > maxEntries) {
                actionPairList.remove(actionPairList.size() - 1); // remove
                // last elt
            }
        }
        startActionFrame();
        timeOfLastAdd = currentTime;
    }

    private void startActionFrame() {
        if (actionFrameStarted == false && EventQueue.isDispatchThread()) {
            actionFrameStarted = true;
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    actionFrameStarted = false;
                }
            });
        }
    }

    public void clear() {
        actionPairList.clear();
    }

    public void print() {
        log.info("Undo list:");
        int j = 0;
        for (ActionPair pair : actionPairList) {
            log.info("line " + (j++) + " = " + Tools.printXmlAction(pair.getDoAction()));
        }
    }
}
