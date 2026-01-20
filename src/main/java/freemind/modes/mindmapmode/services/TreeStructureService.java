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
import freemind.modes.mindmapmode.actions.xml.actors.XmlActorFactory;

import java.util.List;
import java.util.ListIterator;

/**
 * Service for tree structure operations.
 * Handles node creation, deletion, movement, and folding.
 * Extracted from MindMapController to reduce class coupling.
 */
public class TreeStructureService {

    private final XmlActorFactory actorFactory;

    public TreeStructureService(XmlActorFactory actorFactory) {
        this.actorFactory = actorFactory;
    }

    /**
     * Creates a new node as a child of the parent at the specified index.
     */
    public MindMapNode addNewNode(MindMapNode parent, int index, boolean newNodeIsLeft) {
        return actorFactory.getNewChildActor().addNewNode(parent, index, newNodeIsLeft);
    }

    /**
     * Deletes the specified node.
     */
    public void deleteNode(MindMapNode selectedNode) {
        actorFactory.getDeleteChildActor().deleteNode(selectedNode);
    }

    /**
     * Toggles the folded state of the selected nodes.
     */
    public void toggleFolded(ListIterator<MindMapNode> selectedsIterator) {
        actorFactory.getToggleFoldedActor().toggleFolded(selectedsIterator);
    }

    /**
     * Sets the folded state of a node.
     */
    public void setFolded(MindMapNode node, boolean folded) {
        actorFactory.getToggleFoldedActor().setFolded(node, folded);
    }

    /**
     * Moves nodes up or down in the tree.
     */
    public void moveNodes(MindMapNode selected, List<MindMapNode> selecteds, int direction) {
        actorFactory.getNodeUpActor().moveNodes(selected, selecteds, direction);
    }
}
