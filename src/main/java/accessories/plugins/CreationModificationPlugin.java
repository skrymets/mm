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
 * Created on 10.03.2004
 *
 */
package accessories.plugins;

import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.hooks.PermanentMindMapNodeHookAdapter;
import lombok.extern.log4j.Log4j2;

import java.text.MessageFormat;
import java.util.Iterator;

/**
 * @author foltin
 */
@Log4j2
public class CreationModificationPlugin extends PermanentMindMapNodeHookAdapter {

    private String tooltipFormat;

    public CreationModificationPlugin() {
        super();
    }

    private void setStyle(MindMapNode node) {
        Object[] messageArguments = {
                node.getHistoryInformation().getCreatedAt(),
                node.getHistoryInformation().getLastModifiedAt()};
        if (tooltipFormat == null) {
            tooltipFormat = getResourceString("tooltip_format");
        }
        MessageFormat formatter = new MessageFormat(tooltipFormat);
        String message = formatter.format(messageArguments);
        setToolTip(node, getName(), message);
        log.trace(this + "Tooltip for " + node + " with parent " + node.getParentNode() + " is " + message);
    }

    public void shutdownMapHook() {
        removeToolTipRecursively(getNode());
        super.shutdownMapHook();
    }

    private void removeToolTipRecursively(MindMapNode node) {
        setToolTip(node, getName(), null);
        for (Iterator<MindMapNode> i = node.childrenUnfolded(); i.hasNext(); ) {
            MindMapNode child = i.next();
            removeToolTipRecursively(child);
        }
    }

    public void onUpdateChildrenHook(MindMapNode updatedNode) {
        super.onUpdateChildrenHook(updatedNode);
        setStyleRecursive(updatedNode);
    }

    public void onUpdateNodeHook() {
        super.onUpdateNodeHook();
        setStyle(getNode());
    }

    public void invoke(MindMapNode node) {
        super.invoke(node);
        setStyleRecursive(node);
    }

    private void setStyleRecursive(MindMapNode node) {
        log.trace("setStyle " + node);
        setStyle(node);
        // recurse:
        for (Iterator<MindMapNode> i = node.childrenFolded(); i.hasNext(); ) {
            MindMapNode child = i.next();
            setStyleRecursive(child);
        }
    }

    public void onAddChildren(MindMapNode pAddedChild) {
        setStyleRecursive(pAddedChild);
    }

    public void onNewChild(MindMapNode pNewChildNode) {
        setStyleRecursive(pNewChildNode);
    }

}
