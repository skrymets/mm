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
package freemind.model;

/**
 * Filter flag constants shared between the model and controller filter layers.
 */
public final class FilterConstants {
    public static final int FILTER_INITIAL_VALUE = 1;
    public static final int FILTER_SHOW_MATCHED = 2;
    public static final int FILTER_SHOW_ANCESTOR = 4;
    public static final int FILTER_SHOW_DESCENDANT = 8;
    public static final int FILTER_SHOW_ECLIPSED = 16;
    public static final int FILTER_SHOW_HIDDEN = 32;

    private FilterConstants() {
        // utility class
    }
}
