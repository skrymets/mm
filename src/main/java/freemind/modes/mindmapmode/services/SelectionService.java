/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006 Joerg Mueller, Daniel Polansky, Christian Foltin and others.
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
package freemind.modes.mindmapmode.services;

import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import freemind.view.mindmapview.MainView;
import freemind.view.mindmapview.NodeView;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SystemUtils;

import java.awt.event.MouseEvent;

/**
 * Service for mouse-driven selection operations on mind map nodes.
 * Extracted from MindMapController to reduce class size.
 */
@Slf4j
public class SelectionService {

    private final MindMapController controller;

    public SelectionService(MindMapController controller) {
        this.controller = controller;
    }

    /**
     * Handles a plain (single) click on a node. If the click is in the follow-link
     * region, loads the URL; otherwise toggles folding or starts editing for leaf nodes.
     */
    public void plainClick(MouseEvent e) {
        if (controller.getSelecteds().size() != 1) {
            return;
        }
        final MainView component = (MainView) e.getComponent();
        if (component.isInFollowLinkRegion(e.getX())) {
            controller.loadURL();
        } else {
            MindMapNode node = component.getNodeView().getModel();
            if (!node.hasChildren()) {
                doubleClick(e);
                return;
            }
            controller.toggleFolded();
        }
    }

    /**
     * Handles a double-click on a node, starting inline editing if conditions are met.
     */
    public void doubleClick(MouseEvent e) {
        if (controller.getSelecteds().size() != 1) {
            return;
        }
        MindMapNode node = ((MainView) e.getComponent()).getNodeView().getModel();
        if (!e.isAltDown() && !e.isControlDown() && !e.isShiftDown()
                && !e.isPopupTrigger() && e.getButton() == MouseEvent.BUTTON1
                && (node.getLink() == null)) {
            controller.edit(null, false, false);
        }
    }

    /**
     * Extends the current selection based on modifier keys (Ctrl for toggle,
     * Shift for continuous range, Alt for branch selection).
     *
     * @return true if the selection was changed
     */
    public boolean extendSelection(MouseEvent e) {
        NodeView newlySelectedNodeView = ((MainView) e.getComponent()).getNodeView();
        boolean extend = e.isControlDown();
        if (SystemUtils.IS_OS_MAC) {
            extend |= e.isMetaDown();
        }
        boolean range = e.isShiftDown();
        boolean branch = e.isAltGraphDown() || e.isAltDown();
        boolean retValue = false;

        if (extend || range || branch || !controller.getView().getSelectionService().isSelected(newlySelectedNodeView)) {
            if (!range) {
                if (extend) {
                    controller.getView().getSelectionService().toggleSelected(newlySelectedNodeView);
                } else {
                    controller.select(newlySelectedNodeView);
                }
                retValue = true;
            } else {
                retValue = controller.getView().getSelectionService().selectContinuous(newlySelectedNodeView);
            }
            if (branch) {
                controller.getView().getSelectionService().selectBranch(newlySelectedNodeView, extend);
                retValue = true;
            }
        }

        if (retValue) {
            e.consume();

            String link = newlySelectedNodeView.getModel().getLink();
            if (link != null) {
                controller.getController().getFrame().setStatusText(link);
            }
        }
        log.trace("MouseEvent: extend:{}, range:{}, branch:{}, event:{}, retValue:{}",
                extend, range, branch, e, retValue);
        controller.obtainFocusForSelected();
        return retValue;
    }
}
