package freemind.modes.mindmapmode;

import freemind.main.HtmlTools;
import freemind.model.MindMap;
import freemind.model.MindMapNode;
import freemind.model.NodeAdapter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * This class represents a single Node of a Tree. It contains direct handles to
 * its parent and children and to its view.
 */
@Slf4j
public class MindMapNodeModel extends NodeAdapter {

    public MindMapNodeModel(MindMap pMap) {
        this(null, pMap);
    }

    public MindMapNodeModel(Object userObject, MindMap pMap) {
        super(userObject, pMap);
        children = new LinkedList<>();
        setEdge(new MindMapEdgeModel(this, getMapFeedback()));
    }

    //
    // The mandatory load and save methods
    //

    public String getPlainTextContent() {
        return HtmlTools.htmlToPlain(toString());
    }

    public void saveTXT(Writer fileout, int depth) throws IOException {
        String plainTextContent = getPlainTextContent();
        for (int i = 0; i < depth; ++i) {
            fileout.write("    ");
        }
        if (plainTextContent.matches(" *")) {
            fileout.write("o");
        } else {
            if (getLink() != null) {
                String link = getLink();
                if (!link.equals(plainTextContent)) {
                    fileout.write(plainTextContent + " ");
                }
                fileout.write("<" + link + ">");
            } else {
                fileout.write(plainTextContent);
            }
        }

        fileout.write("\n");
        // fileout.write(System.getProperty("line.separator"));
        // fileout.newLine();

        // ^ One would rather expect here one of the above commands
        // commented out. However, it does not work as expected on
        // Windows. My unchecked hypothesis is, that the String Java stores
        // in Clipboard carries information that it actually is \n
        // separated string. The current coding works fine with pasting on
        // Windows (and I expect, that on Unix too, because \n is a Unix
        // separator). This method is actually used only for pasting
        // purposes, it is never used for writing to file. As a result, the
        // writing to file is not tested.

        // Another hypothesis is, that something goes astray when creating
        // StringWriter.

        saveChildrenText(fileout, depth);
    }

    private void saveChildrenText(Writer fileout, int depth) throws IOException {
        for (ListIterator<MindMapNode> e = sortedChildrenUnfolded(); e.hasNext(); ) {
            final MindMapNodeModel child = (MindMapNodeModel) e.next();
            if (child.isVisible()) {
                child.saveTXT(fileout, depth + 1);
            } else {
                child.saveChildrenText(fileout, depth);
            }
        }
    }

    public void collectColors(HashSet<Color> colors) {
        if (color != null) {
            colors.add(getColor());
        }
        for (ListIterator e = childrenUnfolded(); e.hasNext(); ) {
            ((MindMapNodeModel) e.next()).collectColors(colors);
        }
    }

    private String saveRFT_escapeUnicodeAndSpecialCharacters(String text) {
        int len = text.length();
        StringBuilder result = new StringBuilder(len);
        int intValue;
        char myChar;
        for (int i = 0; i < len; ++i) {
            myChar = text.charAt(i);
            intValue = text.charAt(i);
            if (intValue > 128) {
                result.append("\\u").append(intValue).append("?");
            } else {
                switch (myChar) {
                    case '\\':
                        result.append("\\\\");
                        break;
                    case '{':
                        result.append("\\{");
                        break;
                    case '}':
                        result.append("\\}");
                        break;
                    case '\n':
                        result.append(" \\line ");
                        break;
                    default:
                        result.append(myChar);
                }
            }
        }
        return result.toString();
    }

    public void saveRTF(Writer fileout, int depth, HashMap<Color, Integer> colorTable)
            throws IOException {
        String pre = "{" + "\\li" + depth * 350;
        String level;
        if (depth <= 8) {
            level = "\\outlinelevel" + depth;
        } else {
            level = "";
        }
        String fontsize = "";
        if (color != null) {
            pre += "\\cf" + colorTable.get(getColor());
        }

        if (isItalic()) {
            pre += "\\i ";
        }
        if (isBold()) {
            pre += "\\b ";
        }
        if (isStrikethrough()) {
            pre += "\\strike ";
        }
        if (font != null && font.getSize() != 0) {
            fontsize = "\\fs" + Math.round(1.5 * getFont().getSize());
            pre += fontsize;
        }

        pre += "{}"; // make sure setting of properties is separated from the
        // text itself

        fileout.write("\\li" + depth * 350 + level + "{}");
        if (this.toString().matches(" *")) {
            fileout.write("o");
        } else {
            String text = saveRFT_escapeUnicodeAndSpecialCharacters(this
                    .getPlainTextContent());
            if (getLink() != null) {
                String link = saveRFT_escapeUnicodeAndSpecialCharacters(getLink());
                if (link.equals(this.toString())) {
                    fileout.write(pre + "<{\\ul\\cf1 " + link + "}>" + "}");
                } else {
                    fileout.write("{" + fontsize + pre + text + "} ");
                    fileout.write("<{\\ul\\cf1 " + link + "}}>");
                }
            } else {
                fileout.write(pre + text + "}");
            }
        }

        fileout.write("\\par");
        fileout.write("\n");

        saveChildrenRTF(fileout, depth, colorTable);
    }

    private void saveChildrenRTF(Writer fileout, int depth, HashMap<Color, Integer> colorTable)
            throws IOException {
        for (ListIterator<MindMapNode> e = sortedChildrenUnfolded(); e.hasNext(); ) {
            final MindMapNodeModel child = (MindMapNodeModel) e.next();
            if (child.isVisible()) {
                child.saveRTF(fileout, depth + 1, colorTable);
            } else {
                child.saveChildrenRTF(fileout, depth, colorTable);
            }
        }
    }

    public boolean isWriteable() {
        return true;
    }

}
