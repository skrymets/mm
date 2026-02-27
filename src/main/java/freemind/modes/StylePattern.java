/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2001  Joerg Mueller <joergmueller@bigfoot.com>
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

// Daniel: this seems like a description of what pattern should do rather
// than of that what it actually does.

/**
 * THIS CLASS IS NO LONGER USED!
 * <p>
 * This class represents a StylePattern than can be applied to a node or a whole
 * branch. The properties of the nodes are replaced with the properties saved in
 * the pattern. If a property "text" is given, this pattern is automatically
 * applied to all nodes that contain the String saved in "text".
 */
public class StylePattern {
    /**
     * -- GETTER --
     *  Get the value of name.
     * <p>
     *
     * -- SETTER --
     *  Set the value of name.
     *
     */
    @Setter
    @Getter
    private String name;
    /**
     * NOT USED: The idea of recursive is redundant. You have a possibility to
     * select all nodes in a branch easily.
     * -- SETTER --
     *  Set the value of recursive.
     *

     */
    @Setter
    private boolean recursive;

    /**
     * -- GETTER --
     *  Get the value of text.
     * <p>
     *
     * -- SETTER --
     *  Set the value of text.
     *
     */
    @Setter
    @Getter
    private String text;

    /**
     * -- GETTER --
     *  Get the value of nodeColor.
     * <p>
     *
     * -- SETTER --
     *  Set the value of nodeColor.
     *
     */
    @Setter
    @Getter
    private Color nodeColor;
    @Setter
    @Getter
    private Color nodeBackgroundColor;
    /**
     * -- GETTER --
     *  Get the value of nodeStyle.
     * <p>
     *
     * -- SETTER --
     *  Set the value of nodeStyle.
     *
     */
    @Setter
    @Getter
    private String nodeStyle;

    /**
     * -- GETTER --
     * <p>
     *
     * -- SETTER --
     *
     */
    @Setter
    @Getter
    private String nodeFontFamily = null;
    /**
     * -- GETTER --
     * <p>
     *
     * -- SETTER --
     *
     */
    @Setter
    @Getter
    private Integer nodeFontSize = null;
    /**
     * -- GETTER --
     * <p>
     *
     * -- SETTER --
     *
     */
    @Setter
    @Getter
    private Boolean nodeFontBold = null;
    /**
     * -- GETTER --
     * <p>
     *
     * -- SETTER --
     *
     */
    @Setter
    @Getter
    private Boolean nodeFontItalic = null;

    /**
     * -- GETTER --
     *  Get the value of icon.
     * <p>
     *
     * -- SETTER --
     *  Set the value of icon.
     *
     */
    @Setter
    @Getter
    private MindIcon nodeIcon;

    /**
     * -- GETTER --
     *  Get the value of edgeColor.
     * <p>
     *
     * -- SETTER --
     *  Set the value of edgeColor.
     *
     */
    @Setter
    @Getter
    private Color edgeColor;
    /**
     * -- GETTER --
     *  Get the value of edgeStyle.
     * <p>
     *
     * -- SETTER --
     *  Set the value of edgeStyle.
     *
     */
    @Setter
    @Getter
    private String edgeStyle;
    /**
     * -- GETTER --
     *  Get the value of edgeWidth.
     * <p>
     *
     * -- SETTER --
     *  Set the value of edgeWidth.
     *
     */
    @Setter
    @Getter
    private Integer edgeWidth;

    /**
     * Inhertitable patterns, fc, 3.12.2003.
     */
    private StylePattern mChildrenStylePattern;

    /**
     * Empty constructor
     */
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
        nodeFontSize = node.getFontSize() == null ? null : Integer.valueOf(node
                .getFontSize());
        nodeFontFamily = node.getFontFamilyName();

        nodeIcon = null;
        // appliesToNodeIcon = node.getIcons().size()>0;
        // nodeIcon = (MindIcon)
        // (node.getIcons().size()==0?null:node.getIcons().get(0));

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
        if (FreeMindXml.getStringAttribute(pattern, "name") != null) {
            setName(FreeMindXml.getStringAttribute(pattern, "name"));
        }
        if (Objects.equals(FreeMindXml.getStringAttribute(pattern, "recursive"), "true")) {
            setRecursive(true);
        }

        for (Element child : FreeMindXml.getChildElements(pattern)) {
            // this has to be improved!
            // NODE
            if (child.getTagName().equals("node")) {
                if (FreeMindXml.getStringAttribute(child, "color") != null
                        && FreeMindXml.getStringAttribute(child, "color").length() == 7) {
                    setNodeColor(ColorUtils.xmlToColor(FreeMindXml
                            .getStringAttribute(child, "color")));
                }
                if (FreeMindXml.getStringAttribute(child, "background_color") != null
                        && FreeMindXml.getStringAttribute(child, "background_color")
                        .length() == 7) {
                    setNodeBackgroundColor(ColorUtils.xmlToColor(FreeMindXml
                            .getStringAttribute(child, "background_color")));
                }
                if (FreeMindXml.getStringAttribute(child, "style") != null) {
                    setNodeStyle(FreeMindXml.getStringAttribute(child, "style"));
                }
                if (FreeMindXml.getStringAttribute(child, "icon") != null) {
                    setNodeIcon(FreeMindXml.getStringAttribute(child, "icon").equals("none") ? null
                            : MindIcon
                            .factory(FreeMindXml.getStringAttribute(child, "icon")));
                }
                setText(FreeMindXml.getStringAttribute(child, "text"));

                for (Element nodeChild : FreeMindXml.getChildElements(child)) {
                    // FONT
                    if (nodeChild.getTagName().equals("font")) {

                        if (FreeMindXml.getStringAttribute(nodeChild, "name") != null) {
                            setNodeFontFamily(FreeMindXml.getStringAttribute(nodeChild, "name"));
                        }
                        if (Objects.equals(FreeMindXml.getStringAttribute(nodeChild, "bold"), "true")) {
                            setNodeFontBold(Boolean.TRUE);
                        }
                        if (Objects.equals(FreeMindXml.getStringAttribute(nodeChild, "italic"), "true")) {
                            setNodeFontItalic(Boolean.TRUE);
                        }
                        // if (font.getProperty("underline")!=null &&
                        // nodeChild.getProperty("underline").equals("true"))
                        // setUnderlined(true);
                        if (FreeMindXml.getStringAttribute(nodeChild, "size") != null) {
                            setNodeFontSize(Integer.valueOf(FreeMindXml.getStringAttribute(nodeChild, "size")));
                        }

                    }
                }
            }

            // EDGE
            if (child.getTagName().equals("edge")) {
                if (FreeMindXml.getStringAttribute(child, "style") != null) {
                    setEdgeStyle(FreeMindXml.getStringAttribute(child, "style"));
                }
                if (FreeMindXml.getStringAttribute(child, "color") != null) {
                    setEdgeColor(ColorUtils.xmlToColor(FreeMindXml
                            .getStringAttribute(child, "color")));
                }
                if (FreeMindXml.getStringAttribute(child, "width") != null) {
                    if (FreeMindXml.getStringAttribute(child, "width").equals("thin")) {
                        setEdgeWidth(Integer.valueOf(EdgeAdapter.WIDTH_THIN));
                    } else {
                        setEdgeWidth(Integer.valueOf(Integer.parseInt(FreeMindXml
                                .getStringAttribute(child, "width"))));
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
                    if (anythingFound == false)
                        System.err.println("Cannot find the children "
                                + searchName + " to the pattern " + getName());
                }
            }
        }
    }

}

/*
 * Is saving necessary? public void savePattern(File file) { try { //CODE FOR
 * NANOXML XMLElement pattern = new XMLElement(); pattern.setTagName("pattern");
 * XMLElement node = new XMLElement(); node.setTagName("node");
 * pattern.addChild(node); XMLElement edge = new XMLElement();
 * edge.setTagName("edge"); pattern.addChild(edge);
 *
 *
 * pattern.addChild(((MindMapNodeModel)getRoot()).save());
 *
 * XMLElement node = new XMLElement(); node.setTagName("node");
 *
 * node.addProperty("text",this.toString());
 *
 * // ((MindMapEdgeModel)getEdge()).save(doc,node);
 *
 * XMLElement edge = ((MindMapEdgeModel)getEdge()).save(); if (edge != null) {
 * node.addChild(edge); }
 *
 * if (isFolded()) { node.addProperty("folded","true"); }
 *
 * if (color != null) { node.addProperty("color", ColorUtils.colorToXml(getColor()));
 * }
 *
 * if (style != null) { node.addProperty("style", getStyle()); }
 *
 * //link if (getLink() != null) { node.addProperty("link", getLink()); }
 *
 * //font if (font!=null || font.getSize()!=0 || isBold() || isItalic() ||
 * isUnderlined() ) { XMLElement fontElement = new XMLElement();
 * fontElement.setTagName("font");
 *
 * if (font != null) { fontElement.addProperty("name",getFont().getFontName());
 * } if (font.getSize() != 0) {
 * fontElement.addProperty("size",Integer.toString(getFont().getSize())); } if
 * (isBold()) { fontElement.addProperty("bold","true"); } if (isItalic()) {
 * fontElement.addProperty("italic","true"); } if (isUnderlined()) {
 * fontElement.addProperty("underline","true"); } node.addChild(fontElement); }
 *
 *
 *
 * //Generating output Stream BufferedWriter fileout = new BufferedWriter( new
 * OutputStreamWriter( new FileOutputStream(file) ) ); pattern.write(fileout);
 *
 * fileout.close();
 *
 * } catch(Exception e) {
 * System.err.println("Error in MindMapMapModel.saveXML(): ");
 * freemind.main.Resources.getInstance().logExecption(e); } }
 */
