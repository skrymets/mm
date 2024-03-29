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
/*$Id: MindMapArrowLink.java,v 1.2.18.2.16.2 2007/04/21 15:11:21 dpolivaev Exp $*/

package freemind.modes;

import freemind.main.XMLElement;
import freemind.model.MindMapLink;
import freemind.view.mindmapview.MapView;

import java.awt.*;

public interface MindMapArrowLink extends MindMapLink {

	/**
	 * Means: yes, it has the default arrow 
	 */
	public static final String ARROW_DEFAULT = "Default";
	/**
	 * Means: no, it hasn't an arrow at this side.
	 */
	public static final String ARROW_NONE = "None";

	/* for arrows: */
	public Point getStartInclination(); // the zero is the start point of the
										// line;

	public Point getEndInclination(); // the zero is the end point of the line;

	public void setStartInclination(Point startInclination);

	public void setEndInclination(Point endInclination);

	/** the type of the start arrow: currently "None" and "Default". */
	public String getStartArrow();

	/** the type of the end arrow: currently "None" and "Default". */
	public String getEndArrow();

	/**
	 * @param map
	 *            TODO
	 */
	public void changeInclination(MapView map, int originX, int originY,
			int deltaX, int deltaY);

	public void showControlPoints(boolean bShowControlPointsFlag);

	public boolean getShowControlPointsFlag();

	public XMLElement save();

}
