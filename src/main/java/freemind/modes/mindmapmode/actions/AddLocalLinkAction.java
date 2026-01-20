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
 * Created on 07.10.2004
 */

package freemind.modes.mindmapmode.actions;

import freemind.main.Tools;
import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * @author foltin
 */
@Slf4j
public class AddLocalLinkAction extends MindmapAction {

    private final MindMapController modeController;

    public AddLocalLinkAction(MindMapController modeController) {
        super("paste_as_local_link", "images/stock_right.png", modeController);
        this.modeController = modeController;
    }

    public void actionPerformed(ActionEvent e) {
        MindMapNode source = modeController.getSelected();
        List<MindMapNode> nodesFromClipboard = Tools.getMindMapNodesFromClipboard(modeController);
        if (isNotEmpty(nodesFromClipboard)) {
            modeController.getController().errorMessage(modeController.getText("no_copied_nodes"));
            return;
        }
        boolean first = true;
        for (MindMapNode destination : nodesFromClipboard) {
            if (!first) {
                log.warn("Can't link the node '{}' to more than one destination. Only the last is used.", source);
            }
            if (source != destination) {
                modeController.setLink(source, "#" + modeController.getNodeID(destination));
            } else {
                // hmm, give an error?
                log.warn("Can't link the node '{}' onto itself. Skipped.", source);
            }
            first = false;
        }
    }

    @Override
    public boolean isEnabled(JMenuItem pItem, Action pAction) {
        return super.isEnabled(pItem, pAction)
                && (modeController != null)
                && Tools.getMindMapNodesFromClipboard(modeController).size() == 1;
    }

}
