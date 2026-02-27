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

import freemind.controller.actions.EdgeColorFormatAction;
import freemind.controller.actions.XmlAction;
import freemind.main.ColorUtils;
import freemind.model.EdgeAdapter;
import freemind.model.MindMapNode;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.mindmapmode.MindMapEdgeModel;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

import java.awt.*;
import java.util.Objects;

/**
 * @author foltin
 * {@code @date} 01.04.2014
 */
public class EdgeColorActor extends XmlActorAdapter {

    /**
     */
    public EdgeColorActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public void setEdgeColor(MindMapNode node, Color color) {
        EdgeColorFormatAction doAction = createEdgeColorFormatAction(node,
                color);
        EdgeColorFormatAction undoAction = createEdgeColorFormatAction(node,
                ((EdgeAdapter) node.getEdge()).getRealColor());
        execute(new ActionPair(doAction, undoAction));

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * freemind.controller.actions.ActorXml#act(freemind.controller.actions.
     * generated.instance.XmlAction)
     */
    public void act(XmlAction action) {
        if (action instanceof EdgeColorFormatAction) {
            EdgeColorFormatAction edgeAction = (EdgeColorFormatAction) action;
            Color color = ColorUtils.xmlToColor(edgeAction.getColor());
            MindMapNode node = getNodeFromID(edgeAction.getNode());
            Color oldColor = ((EdgeAdapter) node.getEdge()).getRealColor();
            if (!Objects.equals(color, oldColor)) {
                ((MindMapEdgeModel) node.getEdge()).setColor(color);
                getExMapFeedback().nodeChanged(node);
            }
        }
    }

    public EdgeColorFormatAction createEdgeColorFormatAction(MindMapNode node,
                                                             Color color) {
        EdgeColorFormatAction edgeAction = new EdgeColorFormatAction();
        edgeAction.setNode(getNodeID(node));
        if (color != null) {
            edgeAction.setColor(ColorUtils.colorToXml(color));
        }
        return edgeAction;
    }

    /*
     * (non-Javadoc)
     *
     * @see freemind.controller.actions.ActorXml#getDoActionClass()
     */
    public Class<EdgeColorFormatAction> getDoActionClass() {
        return EdgeColorFormatAction.class;
    }

}
