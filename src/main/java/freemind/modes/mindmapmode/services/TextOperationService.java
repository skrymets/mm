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

import freemind.main.FixedHTMLWriter;
import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import lombok.extern.slf4j.Slf4j;

import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Service for text splitting operations on mind map nodes.
 * Extracted from MindMapController to reduce class size.
 */
@Slf4j
public class TextOperationService {

    private final MindMapController controller;

    public TextOperationService(MindMapController controller) {
        this.controller = controller;
    }

    /**
     * Splits a node at the given caret position, creating a new sibling below
     * with the text after the caret. The original node keeps the text before the caret.
     */
    public void splitNode(MindMapNode node, int caretPosition, String newText) {
        if (node.isRoot()) {
            return;
        }
        String futureText = newText != null ? newText : node.toString();

        String[] strings = getContent(futureText, caretPosition);
        if (strings == null) {
            return;
        }
        String newUpperContent = strings[0];
        String newLowerContent = strings[1];
        controller.setNodeText(node, newUpperContent);

        MindMapNode parent = node.getParentNode();
        MindMapNode lowerNode = controller.addNewNode(parent,
                parent.getChildPosition(node) + 1, node.isLeft());
        lowerNode.setColor(node.getColor());
        lowerNode.setFont(node.getFont());
        controller.setNodeText(lowerNode, newLowerContent);
    }

    /**
     * Splits text content at the given position, handling both plain text and HTML content.
     *
     * @return a two-element array with [before, after] text, or null if the split is invalid
     */
    private String[] getContent(String text, int pos) {
        if (pos <= 0) {
            return null;
        }
        String[] strings = new String[2];
        if (text.startsWith("<html>")) {
            HTMLEditorKit kit = new HTMLEditorKit();
            HTMLDocument doc = new HTMLDocument();
            StringReader buf = new StringReader(text);
            try {
                kit.read(buf, doc, 0);
                final char[] firstText = doc.getText(0, pos).toCharArray();
                int firstStart = 0;
                int firstLen = pos;
                while ((firstStart < firstLen) && (firstText[firstStart] <= ' ')) {
                    firstStart++;
                }
                while ((firstStart < firstLen) && (firstText[firstLen - 1] <= ' ')) {
                    firstLen--;
                }
                int secondStart = 0;
                int secondLen = doc.getLength() - pos;
                final char[] secondText = doc.getText(pos, secondLen).toCharArray();
                while ((secondStart < secondLen) && (secondText[secondStart] <= ' ')) {
                    secondStart++;
                }
                while ((secondStart < secondLen) && (secondText[secondLen - 1] <= ' ')) {
                    secondLen--;
                }
                if (firstStart == firstLen || secondStart == secondLen) {
                    return null;
                }
                StringWriter out = new StringWriter();
                new FixedHTMLWriter(out, doc, firstStart, firstLen - firstStart).write();
                strings[0] = out.toString();
                out = new StringWriter();
                new FixedHTMLWriter(out, doc, pos + secondStart, secondLen - secondStart).write();
                strings[1] = out.toString();
                return strings;
            } catch (IOException | BadLocationException e) {
                log.error(e.getLocalizedMessage(), e);
            }
        } else {
            if (pos >= text.length()) {
                return null;
            }
            strings[0] = text.substring(0, pos);
            strings[1] = text.substring(pos);
        }
        return strings;
    }
}
