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
import freemind.modes.mindmapmode.actions.xml.actors.XmlActorFactory;

/**
 * Service for text editing operations on mind map nodes.
 * Extracted from MindMapController to reduce class coupling.
 */
public class EditingService {

    private final XmlActorFactory actorFactory;

    public EditingService(XmlActorFactory actorFactory) {
        this.actorFactory = actorFactory;
    }

    /**
     * Sets the text content of the specified node.
     */
    public void setNodeText(MindMapNode selected, String newText) {
        actorFactory.getEditActor().setNodeText(selected, newText);
    }

    /**
     * Sets the note text of the specified node.
     */
    public void setNoteText(MindMapNode selected, String newText) {
        actorFactory.getChangeNoteTextActor().setNoteText(selected, newText);
    }
}
