/*
 * Created on 19.04.2004
 *
 */
package accessories.plugins;

import freemind.main.FixedHTMLWriter;
import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.hooks.MindMapNodeHookAdapter;
import freemind.view.mindmapview.MapView;
import lombok.extern.slf4j.Slf4j;

import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

@Slf4j
public class SplitNode extends MindMapNodeHookAdapter {

    public SplitNode() {
        super();
    }

    public void invoke(MindMapNode node) {
        super.invoke(node);
        final List<MindMapNode> list = getMindMapController().getSelecteds();

        for (MindMapNode next : list) {
            splitNode(next);
        }
    }

    private void splitNode(MindMapNode node) {
        if (node.isRoot()) {
            return;
        }
        String text = node.toString();
        String[] parts = splitNode(text);
        if (parts == null || parts.length == 1) {
            return;
        }
        final MindMapController c = getMindMapController();
        int firstPartNumber = 0;
        while (parts[firstPartNumber] == null) {
            firstPartNumber++;
        }
        c.setNodeText(node, parts[firstPartNumber]);
        MindMapNode parent = node.getParentNode();
        final int nodePosition = parent.getChildPosition(node) + 1;
        for (int i = parts.length - 1; i > firstPartNumber; i--) {
            final MindMapNode lowerNode = c.addNewNode(parent, nodePosition,
                    node.isLeft());
            final String part = parts[i];
            if (part == null) {
                continue;
            }
            lowerNode.setColor(node.getColor());
            lowerNode.setFont(node.getFont());
            c.setNodeText(lowerNode, part);
            EventQueue.invokeLater(() -> {
                final MapView mapView = c.getView();
                mapView.getSelectionService().toggleSelected(mapView.getViewerRegistryService().getNodeView(lowerNode));
            });
        }
    }

    private String[] splitNode(String text) {
        if (text.startsWith("<html>")) {
            String[] parts = null;
            HTMLEditorKit kit = new HTMLEditorKit();
            HTMLDocument doc = new HTMLDocument();
            StringReader buf = new StringReader(text);
            try {
                kit.read(buf, doc, 0);
                Element parent = getParentElement(doc);
                if (parent == null) {
                    return null;
                }
                final int elementCount = parent.getElementCount();
                int notEmptyElementCount = 0;
                parts = new String[elementCount];
                for (int i = 0; i < elementCount; i++) {
                    Element current = parent.getElement(i);
                    final int start = current.getStartOffset();
                    final int end = current.getEndOffset();
                    final String paragraphText = doc
                            .getText(start, end - start).trim();
                    if (!paragraphText.isEmpty()) {
                        StringWriter out = new StringWriter();
                        new FixedHTMLWriter(out, doc, start, end - start)
                                .write();
                        final String string = out.toString();
                        if (!string.isEmpty()) {
                            parts[i] = string;
                            notEmptyElementCount++;
                        } else {
                            parts[i] = null;
                        }
                    }
                }
                if (notEmptyElementCount <= 1) {
                    return null;
                }
            } catch (IOException | BadLocationException e) {
                log.error(e.getLocalizedMessage(), e);
            }
            return parts;
        }
        return text.split("\n");
    }

    private Element getParentElement(HTMLDocument doc) {
        final Element htmlRoot = doc.getDefaultRootElement();
        Element parentCandidate = htmlRoot.getElement(htmlRoot
                .getElementCount() - 1);
        do {
            if (parentCandidate.getElementCount() > 1) {
                return parentCandidate;
            }
            parentCandidate = parentCandidate.getElement(0);
        } while (!(parentCandidate.isLeaf() || parentCandidate.getName()
                .equalsIgnoreCase("p-implied")));
        return null;
    }

}
