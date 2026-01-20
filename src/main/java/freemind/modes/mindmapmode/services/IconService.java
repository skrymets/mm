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
import freemind.modes.MindIcon;
import freemind.modes.mindmapmode.actions.xml.actors.XmlActorFactory;

/**
 * Service for icon operations on mind map nodes.
 * Extracted from MindMapController to reduce class coupling.
 */
public class IconService {

    private final XmlActorFactory actorFactory;

    public IconService(XmlActorFactory actorFactory) {
        this.actorFactory = actorFactory;
    }

    /**
     * Adds an icon to the specified node.
     */
    public void addIcon(MindMapNode node, MindIcon icon) {
        actorFactory.getAddIconActor().addIcon(node, icon);
    }

    /**
     * Removes all icons from the specified node.
     */
    public void removeAllIcons(MindMapNode node) {
        actorFactory.getRemoveAllIconsActor().removeAllIcons(node);
    }

    /**
     * Removes the last icon from the specified node.
     * @return the index of the removed icon, or -1 if no icon was removed
     */
    public int removeLastIcon(MindMapNode node) {
        return actorFactory.getRemoveIconActor().removeLastIcon(node);
    }
}
