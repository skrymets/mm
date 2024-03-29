/*
 * FreeMind - A Program for creating and viewing Mindmaps Copyright (C)
 * 2000-2004 Joerg Mueller, Daniel Polansky, Christian Foltin and others.
 * 
 * See COPYING for Details
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * Created on 05.10.2004
 */


package freemind.modes.mindmapmode.actions;

import freemind.controller.MenuItemSelectedListener;
import freemind.model.EdgeAdapter;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.MindMapNodeModel;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

import javax.swing.*;

@SuppressWarnings("serial")
public class EdgeWidthAction extends NodeGeneralAction implements MenuItemSelectedListener {
	private int mWidth;

	public EdgeWidthAction(MindMapController controller, int width) {
		super(controller, null, null);
		this.mWidth = width;
		setName(getWidthTitle(controller, width));
	}

	/* (non-Javadoc)
	 * @see freemind.modes.mindmapmode.actions.NodeGeneralAction#getActionPair(freemind.modes.mindmapmode.MindMapNodeModel)
	 */
	@Override
	protected ActionPair getActionPair(MindMapNodeModel pSelected) {
		return getMindMapController().getActorFactory().getEdgeWidthActor().getActionPair(pSelected, mWidth);
	}
	
	private static String getWidthTitle(MindMapController controller, int width) {
		String returnValue;
		if (width == EdgeAdapter.WIDTH_PARENT) {
			returnValue = controller.getText("edge_width_parent");
		} else if (width == EdgeAdapter.WIDTH_THIN) {
			returnValue = controller.getText("edge_width_thin");
		} else {
			returnValue = Integer.toString(width);
		}
		return /* controller.getText("edge_width") + */returnValue;
	}

	/* (non-Javadoc)
	 * @see freemind.controller.MenuItemSelectedListener#isSelected(javax.swing.JMenuItem, javax.swing.Action)
	 */
	public boolean isSelected(JMenuItem pCheckItem, Action pAction) {
		int width = getMindMapController().getSelected().getEdge().getRealWidth();
		return width == mWidth;
	}

	public int getWidth() {
		return mWidth;
	}

}