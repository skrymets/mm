/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2001  Joerg Mueller <joergmueller@bigfoot.com>
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
/*$Id: BrowseEdgeModel.java,v 1.6 2003/11/03 11:00:13 sviles Exp $*/

package freemind.modes.browsemode;

import freemind.main.XMLElement;
import freemind.model.EdgeAdapter;
import freemind.model.MindMapNode;
import freemind.modes.MapFeedback;

import java.awt.*;

public class BrowseEdgeModel extends EdgeAdapter {

	public BrowseEdgeModel(MindMapNode node, MapFeedback pMapFeedback) {
		super(node, pMapFeedback);
	}

	public XMLElement save() {
		return null;
	}

	public void setColor(Color color) {
		super.setColor(color);
	}

	public void setStyle(String style) {
		super.setStyle(style);
	}
}
