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

import java.awt.*;

/**
 * Service for node formatting operations.
 * Handles fonts, colors, edges, clouds, and node styles.
 * Extracted from MindMapController to reduce class coupling.
 */
public class NodeFormattingService {

    private final XmlActorFactory actorFactory;

    public NodeFormattingService(XmlActorFactory actorFactory) {
        this.actorFactory = actorFactory;
    }

    // Font style methods

    public void setBold(MindMapNode node, boolean bolded) {
        actorFactory.getBoldActor().setBold(node, bolded);
    }

    public void setStrikethrough(MindMapNode node, boolean strikethrough) {
        actorFactory.getStrikethroughActor().setStrikethrough(node, strikethrough);
    }

    public void setItalic(MindMapNode node, boolean isItalic) {
        actorFactory.getItalicActor().setItalic(node, isItalic);
    }

    // Font properties

    public void setFontSize(MindMapNode node, String fontSizeValue) {
        actorFactory.getFontSizeActor().setFontSize(node, fontSizeValue);
    }

    public void increaseFontSize(MindMapNode node, int increment) {
        int newSize = Integer.valueOf(node.getFontSize()).intValue() + increment;
        if (newSize > 0) {
            setFontSize(node, Integer.toString(newSize));
        }
    }

    public void setFontFamily(MindMapNode node, String fontFamilyValue) {
        actorFactory.getFontFamilyActor().setFontFamily(node, fontFamilyValue);
    }

    // Node colors

    public void setNodeColor(MindMapNode node, Color color) {
        actorFactory.getNodeColorActor().setNodeColor(node, color);
    }

    public void setNodeBackgroundColor(MindMapNode node, Color color) {
        actorFactory.getNodeBackgroundColorActor().setNodeBackgroundColor(node, color);
    }

    // Edge properties

    public void setEdgeColor(MindMapNode node, Color color) {
        actorFactory.getEdgeColorActor().setEdgeColor(node, color);
    }

    public void setEdgeWidth(MindMapNode node, int width) {
        actorFactory.getEdgeWidthActor().setEdgeWidth(node, width);
    }

    public void setEdgeStyle(MindMapNode node, String style) {
        actorFactory.getEdgeStyleActor().setEdgeStyle(node, style);
    }

    // Cloud properties

    public void setCloud(MindMapNode node, boolean enable) {
        actorFactory.getCloudActor().setCloud(node, enable);
    }

    public void setCloudColor(MindMapNode node, Color color) {
        actorFactory.getCloudColorActor().setCloudColor(node, color);
    }

    // Node style

    public void setNodeStyle(MindMapNode node, String style) {
        actorFactory.getNodeStyleActor().setStyle(node, style);
    }
}
