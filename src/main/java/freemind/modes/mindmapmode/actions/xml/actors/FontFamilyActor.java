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

import freemind.controller.actions.FontNodeAction;
import freemind.controller.actions.XmlAction;
import freemind.model.MindMapNode;
import freemind.model.NodeAdapter;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

import java.awt.*;
import java.util.Objects;

/**
 * @author foltin
 * {@code @date} 26.03.2014
 */
public class FontFamilyActor extends XmlActorAdapter {

    /**
     */
    public FontFamilyActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public Class<FontNodeAction> getDoActionClass() {
        return FontNodeAction.class;
    }

    /**
     *
     */
    public void setFontFamily(MindMapNode node, String fontFamilyValue) {
        execute(getActionPair(node, fontFamilyValue));
    }

    public ActionPair getActionPair(MindMapNode node, String fontFamilyValue) {
        FontNodeAction fontFamilyAction = createFontNodeAction(node,
                fontFamilyValue);
        FontNodeAction undoFontFamilyAction = createFontNodeAction(node,
                node.getFontFamilyName());
        return new ActionPair(fontFamilyAction, undoFontFamilyAction);
    }

    private FontNodeAction createFontNodeAction(MindMapNode node,
                                                String fontValue) {
        FontNodeAction fontFamilyAction = new FontNodeAction();
        fontFamilyAction.setNode(getNodeID(node));
        fontFamilyAction.setFont(fontValue);
        return fontFamilyAction;

    }

    /**
     *
     */

    public void act(XmlAction action) {
        if (action instanceof FontNodeAction) {
            FontNodeAction fontFamilyAction = (FontNodeAction) action;
            NodeAdapter node = getNodeFromID(fontFamilyAction.getNode());
            String fontFamily = fontFamilyAction.getFont();
            if (!Objects.equals(node.getFontFamilyName(), fontFamily)) {
                node.establishOwnFont();
                node.setFont(getExMapFeedback().getFontThroughMap(
                        new Font(fontFamily, node.getFont().getStyle(), node
                                .getFont().getSize())));
                getExMapFeedback().nodeChanged(node);
            }
        }
    }


}
