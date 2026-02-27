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
 * A no-op filter that makes all nodes visible.
 * Used as the default filter when no filtering is active,
 * replacing the previous dependency on controller-layer
 * DefaultFilter and NoFilteringCondition classes.
 */
public class NoFilter implements Filter {
    public static final NoFilter INSTANCE = new NoFilter();

    @Override
    public void applyFilter(FilterContext context) {
        /* no-op */
    }

    @Override
    public boolean isVisible(MindMapNode node) {
        return true;
    }

    @Override
    public boolean areMatchedShown() {
        return true;
    }

    @Override
    public boolean areHiddenShown() {
        return false;
    }

    @Override
    public boolean areAncestorsShown() {
        return true;
    }

    @Override
    public boolean areDescendantsShown() {
        return true;
    }

    @Override
    public boolean areEclipsedShown() {
        return false;
    }

    @Override
    public Object getCondition() {
        return null;
    }
}
