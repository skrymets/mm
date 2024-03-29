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

import freemind.controller.actions.generated.instance.AddIconAction;
import freemind.controller.actions.generated.instance.RemoveIconXmlAction;
import freemind.controller.actions.generated.instance.XmlAction;
import freemind.model.MindMap;
import freemind.model.MindMapNode;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.MindIcon;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

import java.util.List;

/**
 * @author foltin
 * @date 25.03.2014
 */
public class RemoveIconActor extends NodeXmlActorAdapter {

	/**
	 * @param pMapFeedback
	 */
	public RemoveIconActor(ExtendedMapFeedback pMapFeedback) {
		super(pMapFeedback);
	}

	public Class<RemoveIconXmlAction> getDoActionClass() {
		return RemoveIconXmlAction.class;
	}
	
	public RemoveIconXmlAction createRemoveIconXmlAction(MindMapNode node,
			int iconPosition) {
		RemoveIconXmlAction action = new RemoveIconXmlAction();
		action.setNode(getNodeID(node));
		action.setIconPosition(iconPosition);
		return action;
	}

	private ActionPair apply(MindMapNode selected) {
		List<MindIcon> icons = selected.getIcons();
		if (icons.size() == 0)
			return null;
		AddIconAction undoAction = getXmlActorFactory().getAddIconActor().createAddIconAction(selected,
				(MindIcon) icons.get(icons.size() - 1), MindIcon.LAST);
		return new ActionPair(
				createRemoveIconXmlAction(selected, MindIcon.LAST), undoAction);
	}
	public int removeLastIcon(MindMapNode node) {
		execute(apply(node));
		return node.getIcons().size();
	}

	/**
    *
    */

	public void act(XmlAction action) {
		if (action instanceof RemoveIconXmlAction) {
			RemoveIconXmlAction removeAction = (RemoveIconXmlAction) action;
			MindMapNode node = getNodeFromID(removeAction
					.getNode());
			int position = removeAction.getIconPosition();
			node.removeIcon(position);
			getExMapFeedback().nodeChanged(node);
		}
	}

	/* (non-Javadoc)
	 * @see freemind.modes.mindmapmode.actions.NodeActorXml#apply(freemind.model.MindMap, freemind.model.MindMapNode)
	 */
	@Override
	public ActionPair apply(MindMap pModel, MindMapNode pSelected) {
		return apply(pSelected);
	}

}
