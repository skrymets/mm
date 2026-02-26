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

import freemind.controller.actions.generated.instance.MoveNodesAction;
import freemind.controller.actions.generated.instance.NodeListMember;
import freemind.controller.actions.generated.instance.XmlAction;
import freemind.model.MindMap;
import freemind.model.MindMapNode;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.mindmapmode.actions.xml.ActionPair;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

/**
 * @author foltin
 * {@code @date} 08.04.2014
 */
@Slf4j
public class NodeUpActor extends XmlActorAdapter {

    public NodeUpActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public void moveNodes(MindMapNode selected, List<MindMapNode> selecteds, int direction) {
        MoveNodesAction doAction = createMoveNodesAction(selected, selecteds, direction);
        MoveNodesAction undoAction = createMoveNodesAction(selected, selecteds, -direction);
        execute(new ActionPair(doAction, undoAction));
    }

    private void _moveNodes(MindMapNode selected, List<MindMapNode> selecteds, int direction) {
        Comparator<Integer> comparator = (direction == -1) ? null : (i1, i2) -> i2 - i1;
        if (!selected.isRoot()) {
            MindMapNode parent = selected.getParentNode();
            // multiple move:
            List<MindMapNode> sortedChildren = getSortedSiblings(parent);
            TreeSet<Integer> range = new TreeSet<>(comparator);
            for (MindMapNode node : selecteds) {
                if (node.getParent() != parent) {
                    log.warn("Not all selected nodes (here: {}) have the same parent {}.", node.getText(), parent.getText());
                    return;
                }
                range.add(Integer.valueOf(sortedChildren.indexOf(node)));
            }
            // test range for adjacent nodes:
            Integer last = range.iterator().next();
            for (Integer newInt : range) {
                if (Math.abs(newInt.intValue() - last.intValue()) > 1) {
                    log.warn("Not adjacent nodes. Skipped. ");
                    return;
                }
                last = newInt;
            }
            for (Integer position : range) {
                // from above:
                MindMapNode node = sortedChildren.get(position.intValue());
                moveNodeTo(node, parent, direction);
            }
        }
    }

    /**
     * The direction is used if side left and right are present. then the next
     * suitable place on the same side# is searched. if there is no such place,
     * then the side is changed.
     *
     * @return returns the new index.
     */
    private int moveNodeTo(MindMapNode newChild, MindMapNode parent, int direction) {
        MindMap model = getExMapFeedback().getMap();
        int index = model.getIndexOfChild(parent, newChild);
        int newIndex = index;
        int maxIndex = parent.getChildCount();
        List<MindMapNode> sortedNodesIndices = getSortedSiblings(parent);
        int newPositionInVector = sortedNodesIndices.indexOf(newChild) + direction;
        if (newPositionInVector < 0) {
            newPositionInVector = maxIndex - 1;
        }
        if (newPositionInVector >= maxIndex) {
            newPositionInVector = 0;
        }
        MindMapNode destinationNode = sortedNodesIndices.get(newPositionInVector);
        newIndex = model.getIndexOfChild(parent, destinationNode);
        getExMapFeedback().removeNodeFromParent(newChild);
        getExMapFeedback().insertNodeInto(newChild, parent, newIndex);
        getExMapFeedback().nodeChanged(newChild);
        return newIndex;
    }

    /**
     * Sorts nodes by their left/right status. The left are first.
     */
    private List<MindMapNode> getSortedSiblings(MindMapNode node) {
        List<MindMapNode> nodes = new ArrayList<>();
        for (Iterator<MindMapNode> i = node.childrenUnfolded(); i.hasNext(); ) {
            nodes.add(i.next());
        }
        nodes.sort((n1, n2) -> {
            int b1 = n1.isLeft() ? 0 : 1;
            int b2 = n2.isLeft() ? 0 : 1;
            return b1 - b2;
        });
        // logger.trace("Sorted nodes "+ nodes);
        return nodes;
    }

    public void act(XmlAction action) {
        if (action instanceof MoveNodesAction) {
            MoveNodesAction moveAction = (MoveNodesAction) action;
            MindMapNode selected = getNodeFromID(moveAction
                    .getNode());
            List<MindMapNode> selecteds = new ArrayList<>();
            for (NodeListMember node : moveAction.getNodeListMemberList()) {
                selecteds.add(getNodeFromID(node.getNode()));
            }
            _moveNodes(selected, selecteds, moveAction.getDirection());
        }
    }

    public Class<MoveNodesAction> getDoActionClass() {
        return MoveNodesAction.class;
    }

    private MoveNodesAction createMoveNodesAction(MindMapNode selected,
                                                  List<MindMapNode> selecteds, int direction) {
        MoveNodesAction moveAction = new MoveNodesAction();
        moveAction.setDirection(direction);
        moveAction.setNode(getNodeID(selected));
        // selectedNodes list
        for (MindMapNode node : selecteds) {

            NodeListMember nodeListMember = new NodeListMember();
            nodeListMember.setNode(getNodeID(node));
            moveAction.addNodeListMember(nodeListMember);
        }
        return moveAction;

    }

}
