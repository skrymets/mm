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
import freemind.modes.attributes.Attribute;
import freemind.modes.mindmapmode.actions.xml.actors.XmlActorFactory;

/**
 * Service for attribute operations on mind map nodes.
 * Extracted from MindMapController to reduce class coupling.
 */
public class AttributeService {

    private final XmlActorFactory actorFactory;

    public AttributeService(XmlActorFactory actorFactory) {
        this.actorFactory = actorFactory;
    }

    /**
     * Sets an attribute at the given position on the specified node.
     */
    public void setAttribute(MindMapNode node, int position, Attribute attribute) {
        actorFactory.getSetAttributeActor().setAttribute(node, position, attribute);
    }

    /**
     * Inserts an attribute at the given position on the specified node.
     */
    public void insertAttribute(MindMapNode node, int position, Attribute attribute) {
        actorFactory.getInsertAttributeActor().insertAttribute(node, position, attribute);
    }

    /**
     * Adds an attribute to the specified node.
     * @return the index of the added attribute
     */
    public int addAttribute(MindMapNode node, Attribute attribute) {
        return actorFactory.getAddAttributeActor().addAttribute(node, attribute);
    }

    /**
     * Removes the attribute at the given position from the specified node.
     */
    public void removeAttribute(MindMapNode node, int position) {
        actorFactory.getRemoveAttributeActor().removeAttribute(node, position);
    }
}
