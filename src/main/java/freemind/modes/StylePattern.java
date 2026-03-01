package freemind.modes;

import freemind.main.ColorUtils;
import freemind.main.FreeMindXml;
import freemind.model.EdgeAdapter;
import freemind.model.MindMapNode;
import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * THIS CLASS IS NO LONGER USED!
 * <p>
 * This class represents a StylePattern than can be applied to a node or a whole
 * branch. The properties of the nodes are replaced with the properties saved in
 * the pattern. If a property "text" is given, this pattern is automatically
 * applied to all nodes that contain the String saved in "text".
 */
public class StylePattern {
    private static final String COLOR_ATTRIBUTE = "color";
    private static final String BACKGROUND_COLOR_ATTRIBUTE = "background_color";
    private static final String STYLE_ATTRIBUTE = "style";
    private static final String ICON_ATTRIBUTE = "icon";
    private static final String NAME_ATTRIBUTE = "name";
    private static final String BOLD_ATTRIBUTE = "bold";
    private static final String ITALIC_ATTRIBUTE = "italic";
    private static final String FONT_SIZE_ATTRIBUTE = "size";
    @Setter
    @Getter
    private String name;

    @Setter
    private boolean recursive;

    @Setter
    @Getter
    private String text;

    @Setter
    @Getter
    private Color nodeColor;

    @Setter
    @Getter
    private Color nodeBackgroundColor;

    @Setter
    @Getter
    private String nodeStyle;

    @Setter
    @Getter
    private String nodeFontFamily = null;

    @Setter
    @Getter
    private Integer nodeFontSize = null;

    @Setter
    @Getter
    private Boolean nodeFontBold = null;

    @Setter
    @Getter
    private Boolean nodeFontItalic = null;

    @Setter
    @Getter
    private MindIcon nodeIcon;

    @Setter
    @Getter

    private Color edgeColor;
    @Setter
    @Getter

    private String edgeStyle;
    @Setter
    @Getter
    private Integer edgeWidth;

    private StylePattern mChildrenStylePattern;

    public StylePattern() {
    }

    public StylePattern(Element elm, List<StylePattern> justConstructedPatterns) {
        loadPattern(elm, justConstructedPatterns);
    }

    /**
     * Constructs a style pattern from a node:
     */
    public StylePattern(MindMapNode node) {
        nodeColor = node.getColor();
        nodeBackgroundColor = node.getBackgroundColor();
        nodeStyle = node.getStyle();

        nodeFontBold = Boolean.valueOf(node.isBold());
        nodeFontItalic = Boolean.valueOf(node.isItalic());
        nodeFontSize = node.getFontSize() == null ? null : Integer.valueOf(node.getFontSize());
        nodeFontFamily = node.getFontFamilyName();

        nodeIcon = null;
        edgeColor = node.getEdge().getColor();
        edgeStyle = node.getEdge().getStyle();
        edgeWidth = Integer.valueOf(node.getEdge().getWidth());

    }

    public String toString() {
        return "node: " + nodeColor + ", " + nodeBackgroundColor + ", "
                + nodeStyle + ", " + nodeFontFamily + ", " + nodeFontSize
                + ", " + nodeIcon + ", " + text + ", " + "\nedge: " + edgeColor
                + ", " + edgeStyle + ", " + edgeWidth;
    }

    public boolean getAppliesToEdge() {
        return edgeColor != null || edgeStyle != null || edgeWidth != null;
    }

    public boolean getAppliesToNode() {
        return nodeBackgroundColor != null || nodeColor != null
                || nodeStyle != null;
    }

    public boolean getAppliesToNodeFont() {
        return nodeFontBold != null || nodeFontFamily != null
                || nodeFontItalic != null || nodeFontSize != null;
    }

    public boolean getAppliesToNodeIcon() {
        return nodeIcon != null;
    }

    public boolean getAppliesToChildren() {
        return mChildrenStylePattern != null;
    }

    /**
     * Determine if the properies of this pattern, of course except the "text"
     * attribute, apply to all the child nodes of this node.
     *
     * @return Value of recursive.
     */
    public boolean getRecursive() {
        return recursive;
    }

    /**
     * Get the value of ChildrenStylePattern.
     *
     * @return Value of ChildrenStylePattern.
     */
    public StylePattern getChildrenStylePattern() {
        return mChildrenStylePattern;
    }

    /**
     * Set the value of ChildrenStylePattern.
     *
     * @param pChildrenStylePattern Value to assign to ChildrenStylePattern.
     */
    public void setChildrenStylePattern(StylePattern pChildrenStylePattern) {
        this.mChildrenStylePattern = pChildrenStylePattern;
    }

    public static List loadPatterns(File file) throws Exception {
        return loadPatterns(new BufferedReader(new FileReader(file)));
    }

    public static List loadPatterns(Reader reader) throws Exception {
        List list = new ArrayList();

        Document doc = FreeMindXml.parse(reader);
        Element root = doc.getDocumentElement();
        for (Element childElement : FreeMindXml.getChildElements(root)) {
            list.add(new StylePattern(childElement, list));
        }
        return list;
    }

    protected void loadPattern(Element pattern, List<StylePattern> justConstructedPatterns) {
        // PATTERN
        if (FreeMindXml.getStringAttribute(pattern, NAME_ATTRIBUTE) != null) {
            setName(FreeMindXml.getStringAttribute(pattern, NAME_ATTRIBUTE));
        }
        if (Objects.equals(FreeMindXml.getStringAttribute(pattern, "recursive"), "true")) {
            setRecursive(true);
        }

        for (Element child : FreeMindXml.getChildElements(pattern)) {
            // this has to be improved!
            // NODE
            if (child.getTagName().equals("node")) {
                if (FreeMindXml.getStringAttribute(child, COLOR_ATTRIBUTE) != null
                        && FreeMindXml.getStringAttribute(child, COLOR_ATTRIBUTE).length() == 7) {
                    setNodeColor(ColorUtils.xmlToColor(FreeMindXml.getStringAttribute(child, COLOR_ATTRIBUTE)));
                }
                if (FreeMindXml.getStringAttribute(child, BACKGROUND_COLOR_ATTRIBUTE) != null
                        && FreeMindXml.getStringAttribute(child, BACKGROUND_COLOR_ATTRIBUTE).length() == 7) {
                    setNodeBackgroundColor(ColorUtils.xmlToColor(FreeMindXml.getStringAttribute(child, BACKGROUND_COLOR_ATTRIBUTE)));
                }
                if (FreeMindXml.getStringAttribute(child, STYLE_ATTRIBUTE) != null) {
                    setNodeStyle(FreeMindXml.getStringAttribute(child, STYLE_ATTRIBUTE));
                }
                if (FreeMindXml.getStringAttribute(child, ICON_ATTRIBUTE) != null) {
                    setNodeIcon(FreeMindXml.getStringAttribute(child, ICON_ATTRIBUTE).equals("none") ? null
                            : MindIcon
                            .factory(FreeMindXml.getStringAttribute(child, ICON_ATTRIBUTE)));
                }
                setText(FreeMindXml.getStringAttribute(child, "text"));

                for (Element nodeChild : FreeMindXml.getChildElements(child)) {
                    // FONT
                    if (nodeChild.getTagName().equals("font")) {

                        if (FreeMindXml.getStringAttribute(nodeChild, NAME_ATTRIBUTE) != null) {
                            setNodeFontFamily(FreeMindXml.getStringAttribute(nodeChild, NAME_ATTRIBUTE));
                        }
                        if (Objects.equals(FreeMindXml.getStringAttribute(nodeChild, BOLD_ATTRIBUTE), "true")) {
                            setNodeFontBold(Boolean.TRUE);
                        }
                        if (Objects.equals(FreeMindXml.getStringAttribute(nodeChild, ITALIC_ATTRIBUTE), "true")) {
                            setNodeFontItalic(Boolean.TRUE);
                        }
                        if (FreeMindXml.getStringAttribute(nodeChild, FONT_SIZE_ATTRIBUTE) != null) {
                            setNodeFontSize(Integer.valueOf(FreeMindXml.getStringAttribute(nodeChild, FONT_SIZE_ATTRIBUTE)));
                        }

                    }
                }
            }

            // EDGE
            if (child.getTagName().equals("edge")) {
                if (FreeMindXml.getStringAttribute(child, STYLE_ATTRIBUTE) != null) {
                    setEdgeStyle(FreeMindXml.getStringAttribute(child, STYLE_ATTRIBUTE));
                }
                if (FreeMindXml.getStringAttribute(child, COLOR_ATTRIBUTE) != null) {
                    setEdgeColor(ColorUtils.xmlToColor(FreeMindXml.getStringAttribute(child, COLOR_ATTRIBUTE)));
                }
                if (FreeMindXml.getStringAttribute(child, "width") != null) {
                    if (FreeMindXml.getStringAttribute(child, "width").equals("thin")) {
                        setEdgeWidth(EdgeAdapter.WIDTH_THIN);
                    } else {
                        setEdgeWidth(Integer.parseInt(FreeMindXml.getStringAttribute(child, "width")));
                    }
                }
            }

            // CHILD
            if (child.getTagName().equals("child")) {
                if (FreeMindXml.getStringAttribute(child, "pattern") != null) {
                    // find name in list of justConstructedPatterns:
                    String searchName = FreeMindXml.getStringAttribute(child, "pattern");
                    boolean anythingFound = false;
                    for (StylePattern patternFound : justConstructedPatterns) {
                        if (patternFound.getName().equals(searchName)) {
                            setChildrenStylePattern(patternFound);
                            anythingFound = true;
                            break;
                        }
                    }
                    // perhaps our own pattern?
                    if (getName().equals(searchName)) {
                        setChildrenStylePattern(this);
                        anythingFound = true;
                    }
                    if (!anythingFound)
                        System.err.println("Cannot find the children " + searchName + " to the pattern " + getName());
                }
            }
        }
    }
}
