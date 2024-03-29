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

import freemind.controller.actions.generated.instance.AddLinkXmlAction;
import freemind.controller.actions.generated.instance.XmlAction;
import freemind.model.MindMapNode;
import freemind.model.NodeAdapter;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

/**
 * @author foltin
 * @date 10.04.2014
 */
public class SetLinkActor extends XmlActorAdapter {

	/**
	 * @param pMapFeedback
	 */
	public SetLinkActor(ExtendedMapFeedback pMapFeedback) {
		super(pMapFeedback);
	}

	public void setLink(MindMapNode node, String link) {
		execute(getActionPair(node, link));
	}

	public void act(XmlAction action) {
		if (action instanceof AddLinkXmlAction) {
			AddLinkXmlAction linkAction = (AddLinkXmlAction) action;
			NodeAdapter node = getNodeFromID(linkAction.getNode());
			node.setLink(linkAction.getDestination());
			getExMapFeedback().nodeChanged(node);
		}
	}

	public Class<AddLinkXmlAction> getDoActionClass() {
		return AddLinkXmlAction.class;
	}

	private ActionPair getActionPair(MindMapNode node, String link) {
		return new ActionPair(createAddLinkXmlAction(node, link),
				createAddLinkXmlAction(node, node.getLink()));
	}

	private AddLinkXmlAction createAddLinkXmlAction(MindMapNode node,
			String link) {
		AddLinkXmlAction action = new AddLinkXmlAction();
		action.setNode(getNodeID(node));
		action.setDestination(link);
		return action;
	}
}
