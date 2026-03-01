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

import freemind.controller.MindMapNodesSelection;
import freemind.main.FreeMindXml;
import freemind.model.MindMapNode;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.mindmapmode.actions.xml.actors.XmlActorFactory;
import org.w3c.dom.Document;

import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.List;

/**
 * Service for clipboard operations (copy, cut, paste) on mind map nodes.
 * Extracted from MindMapController to reduce class coupling.
 */
public class ClipboardService {

    private static final String RESOURCE_UNFOLD_ON_PASTE = "unfold_on_paste";

    private final XmlActorFactory actorFactory;
    private final ExtendedMapFeedback mapFeedback;

    public ClipboardService(XmlActorFactory actorFactory, ExtendedMapFeedback mapFeedback) {
        this.actorFactory = actorFactory;
        this.mapFeedback = mapFeedback;
    }

    /**
     * Creates a transferable copy of the given node.
     */
    public Transferable copy(MindMapNode node, boolean saveInvisible) {
        StringWriter stringWriter = new StringWriter();
        try {
            Document doc = FreeMindXml.newDocument();
            node.save(stringWriter, doc, mapFeedback.getMap().getLinkRegistry(), saveInvisible, true);
        } catch (IOException ignored) {
        }
        List<String> nodeList = Collections.singletonList(mapFeedback.getNodeID(node));
        return new MindMapNodesSelection(stringWriter.toString(), null, null, null, null, null, null, nodeList);
    }

    /**
     * Cuts the given list of nodes.
     */
    public Transferable cut(List<MindMapNode> nodeList) {
        return actorFactory.getCutActor().cut(nodeList);
    }

    /**
     * Pastes the transferable content as a child of the parent node.
     */
    public void paste(Transferable t, MindMapNode parent) {
        paste(t, /* target= */ parent, /* asSibling= */ false, parent.isNewChildLeft());
    }

    /**
     * Pastes the transferable content at the target node position.
     */
    public boolean paste(Transferable t, MindMapNode target, boolean asSibling, boolean isLeft) {
        if (!asSibling && target.isFolded() && mapFeedback.getResources().getBoolProperty(RESOURCE_UNFOLD_ON_PASTE)) {
            mapFeedback.setFolded(target, false);
        }
        return actorFactory.getPasteActor().paste(t, target, asSibling, isLeft);
    }

    /**
     * Pastes a node as a child of the parent node.
     */
    public void paste(MindMapNode node, MindMapNode parent) {
        actorFactory.getPasteActor().paste(node, parent);
    }
}
