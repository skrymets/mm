package freemind.model.services;

import freemind.main.FreeMind;
import freemind.model.MindMapNode;
import freemind.model.NodeAdapter;
import lombok.Getter;
import lombok.Setter;

import java.awt.Color;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.Map;

/**
 * Owns the node's style facet: style enum (fork/bubble/combined/as_parent), font, foreground
 * and background colors, the bold/italic/strikethrough/underline flags, and the parent-chain
 * resolution for {@code STYLE_AS_PARENT}. Extracted from {@link NodeAdapter}.
 */
public class NodeStyleService {

    private final NodeAdapter node;

    @Setter
    private String style;

    @Setter
    @Getter
    private Color color;

    @Setter
    @Getter
    private Color backgroundColor;

    @Setter
    @Getter
    private Font font;

    @Setter
    @Getter
    private boolean underlined = false;

    public NodeStyleService(NodeAdapter node) {
        this.node = node;
    }

    public String getBareStyle() {
        return style;
    }

    public String getStyle() {
        String returnedString = style;
        if (style == null) {
            if (node.isRoot()) {
                returnedString = node.getMapFeedback().getProperty(FreeMind.RESOURCES_ROOT_NODE_STYLE);
            } else {
                String stdstyle = node.getMapFeedback().getProperty(FreeMind.RESOURCES_NODE_STYLE);
                if (stdstyle.equals(MindMapNode.STYLE_AS_PARENT)) {
                    returnedString = node.getParentNode().getStyle();
                } else {
                    returnedString = stdstyle;
                }
            }
        } else if (node.isRoot() && style.equals(MindMapNode.STYLE_AS_PARENT)) {
            returnedString = node.getMapFeedback().getProperty(FreeMind.RESOURCES_ROOT_NODE_STYLE);
        } else if (style.equals(MindMapNode.STYLE_AS_PARENT)) {
            returnedString = node.getParentNode().getStyle();
        }

        if (returnedString.equals(MindMapNode.STYLE_COMBINED)) {
            if (node.isFolded()) {
                return MindMapNode.STYLE_BUBBLE;
            } else {
                return MindMapNode.STYLE_FORK;
            }
        }
        return returnedString;
    }

    public boolean hasStyle() {
        return style != null;
    }

    public void establishOwnFont() {
        font = (font != null) ? font : node.getMapFeedback().getDefaultFont();
    }

    public void setBold(boolean bold) {
        if (bold != isBold()) {
            toggleBold();
        }
    }

    public void toggleBold() {
        establishOwnFont();
        font = node.getMapFeedback().getFontThroughMap(font.deriveFont(font.getStyle() ^ Font.BOLD));
    }

    public void setItalic(boolean italic) {
        if (italic != isItalic()) {
            toggleItalic();
        }
    }

    public void toggleItalic() {
        establishOwnFont();
        font = node.getMapFeedback().getFontThroughMap(font.deriveFont(font.getStyle() ^ Font.ITALIC));
    }

    public void setStrikethrough(boolean strikethrough) {
        if (strikethrough != isStrikethrough()) {
            toggleStrikethrough();
        }
    }

    public void toggleStrikethrough() {
        establishOwnFont();
        @SuppressWarnings("unchecked")
        Map<TextAttribute, Object> attributes = (Map<TextAttribute, Object>) font.getAttributes();
        if (attributes.containsKey(TextAttribute.STRIKETHROUGH)
                && attributes.get(TextAttribute.STRIKETHROUGH) == TextAttribute.STRIKETHROUGH_ON) {
            attributes.remove(TextAttribute.STRIKETHROUGH);
        } else {
            attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
        }
        font = new Font(attributes);
    }

    public boolean isBold() {
        return font != null && font.isBold();
    }

    public boolean isItalic() {
        return font != null && font.isItalic();
    }

    public boolean isStrikethrough() {
        if (font != null) {
            Map<TextAttribute, ?> attr = font.getAttributes();
            if (attr.containsKey(TextAttribute.STRIKETHROUGH)) {
                return attr.get(TextAttribute.STRIKETHROUGH) == TextAttribute.STRIKETHROUGH_ON;
            }
        }
        return false;
    }

    public void setFontSize(int fontSize) {
        establishOwnFont();
        font = node.getMapFeedback().getFontThroughMap(font.deriveFont((float) fontSize));
    }

    public String getFontSize() {
        if (font != null) {
            return Integer.toString(font.getSize());
        } else {
            return node.getMapFeedback().getProperty("defaultfontsize");
        }
    }

    public String getFontFamilyName() {
        if (font != null) {
            return font.getFamily();
        } else {
            return node.getMapFeedback().getProperty("defaultfont");
        }
    }
}
