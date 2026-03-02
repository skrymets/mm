
package freemind.controller.actions;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="pattern">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="xml_action">
 *         &lt;xs:sequence>
 *           &lt;xs:element ref="pattern_node_background_color" minOccurs="0" maxOccurs="1"/>
 *           &lt;xs:element ref="pattern_node_color" minOccurs="0" maxOccurs="1"/>
 *           &lt;xs:element ref="pattern_node_style" minOccurs="0" maxOccurs="1"/>
 *           &lt;xs:element ref="pattern_node_text" minOccurs="0" maxOccurs="1"/>
 *           &lt;xs:element ref="pattern_node_font_name" minOccurs="0" maxOccurs="1"/>
 *           &lt;xs:element ref="pattern_node_font_bold" minOccurs="0" maxOccurs="1"/>
 *           &lt;xs:element ref="pattern_node_font_strikethrough" minOccurs="0" maxOccurs="1"/>
 *           &lt;xs:element ref="pattern_node_font_italic" minOccurs="0" maxOccurs="1"/>
 *           &lt;xs:element ref="pattern_node_font_size" minOccurs="0" maxOccurs="1"/>
 *           &lt;xs:element ref="pattern_icon" minOccurs="0" maxOccurs="1"/>
 *           &lt;xs:element ref="pattern_edge_color" minOccurs="0" maxOccurs="1"/>
 *           &lt;xs:element ref="pattern_edge_style" minOccurs="0" maxOccurs="1"/>
 *           &lt;xs:element ref="pattern_edge_width" minOccurs="0" maxOccurs="1"/>
 *           &lt;xs:element ref="pattern_child" minOccurs="0" maxOccurs="1"/>
 *           &lt;xs:element ref="pattern_script" minOccurs="0" maxOccurs="1"/>
 *         &lt;/xs:sequence>
 *         &lt;xs:attribute type="xs:string" use="optional" name="name"/>
 *         &lt;xs:attribute type="xs:string" use="optional" name="original_name"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "pattern")
public class Pattern extends XmlAction
{
    private PatternNodeBackgroundColor patternNodeBackgroundColor;
    private PatternNodeColor patternNodeColor;
    private PatternNodeStyle patternNodeStyle;
    private PatternNodeText patternNodeText;
    private PatternNodeFontName patternNodeFontName;
    private PatternNodeFontBold patternNodeFontBold;
    private PatternNodeFontStrikethrough patternNodeFontStrikethrough;
    private PatternNodeFontItalic patternNodeFontItalic;
    private PatternNodeFontSize patternNodeFontSize;
    private PatternIcon patternIcon;
    private PatternEdgeColor patternEdgeColor;
    private PatternEdgeStyle patternEdgeStyle;
    private PatternEdgeWidth patternEdgeWidth;
    private PatternChild patternChild;
    private PatternScript patternScript;
    @XmlAttribute(name = "name")
    private String name;
    @XmlAttribute(name = "original_name")
    private String originalName;

    /**
     * Get the 'pattern_node_background_color' element value.
     *
     * @return value
     */
    public PatternNodeBackgroundColor getPatternNodeBackgroundColor() {
        return patternNodeBackgroundColor;
    }

    /**
     * Set the 'pattern_node_background_color' element value.
     */
    public void setPatternNodeBackgroundColor(
            PatternNodeBackgroundColor patternNodeBackgroundColor) {
        this.patternNodeBackgroundColor = patternNodeBackgroundColor;
    }

    /**
     * Get the 'pattern_node_color' element value.
     *
     * @return value
     */
    public PatternNodeColor getPatternNodeColor() {
        return patternNodeColor;
    }

    /**
     * Set the 'pattern_node_color' element value.
     */
    public void setPatternNodeColor(PatternNodeColor patternNodeColor) {
        this.patternNodeColor = patternNodeColor;
    }

    /**
     * Get the 'pattern_node_style' element value.
     *
     * @return value
     */
    public PatternNodeStyle getPatternNodeStyle() {
        return patternNodeStyle;
    }

    /**
     * Set the 'pattern_node_style' element value.
     */
    public void setPatternNodeStyle(PatternNodeStyle patternNodeStyle) {
        this.patternNodeStyle = patternNodeStyle;
    }

    /**
     * Get the 'pattern_node_text' element value.
     *
     * @return value
     */
    public PatternNodeText getPatternNodeText() {
        return patternNodeText;
    }

    /**
     * Set the 'pattern_node_text' element value.
     */
    public void setPatternNodeText(PatternNodeText patternNodeText) {
        this.patternNodeText = patternNodeText;
    }

    /**
     * Get the 'pattern_node_font_name' element value.
     *
     * @return value
     */
    public PatternNodeFontName getPatternNodeFontName() {
        return patternNodeFontName;
    }

    /**
     * Set the 'pattern_node_font_name' element value.
     */
    public void setPatternNodeFontName(PatternNodeFontName patternNodeFontName) {
        this.patternNodeFontName = patternNodeFontName;
    }

    /**
     * Get the 'pattern_node_font_bold' element value.
     *
     * @return value
     */
    public PatternNodeFontBold getPatternNodeFontBold() {
        return patternNodeFontBold;
    }

    /**
     * Set the 'pattern_node_font_bold' element value.
     */
    public void setPatternNodeFontBold(PatternNodeFontBold patternNodeFontBold) {
        this.patternNodeFontBold = patternNodeFontBold;
    }

    /**
     * Get the 'pattern_node_font_strikethrough' element value.
     *
     * @return value
     */
    public PatternNodeFontStrikethrough getPatternNodeFontStrikethrough() {
        return patternNodeFontStrikethrough;
    }

    /**
     * Set the 'pattern_node_font_strikethrough' element value.
     */
    public void setPatternNodeFontStrikethrough(
            PatternNodeFontStrikethrough patternNodeFontStrikethrough) {
        this.patternNodeFontStrikethrough = patternNodeFontStrikethrough;
    }

    /**
     * Get the 'pattern_node_font_italic' element value.
     *
     * @return value
     */
    public PatternNodeFontItalic getPatternNodeFontItalic() {
        return patternNodeFontItalic;
    }

    /**
     * Set the 'pattern_node_font_italic' element value.
     */
    public void setPatternNodeFontItalic(
            PatternNodeFontItalic patternNodeFontItalic) {
        this.patternNodeFontItalic = patternNodeFontItalic;
    }

    /**
     * Get the 'pattern_node_font_size' element value.
     *
     * @return value
     */
    public PatternNodeFontSize getPatternNodeFontSize() {
        return patternNodeFontSize;
    }

    /**
     * Set the 'pattern_node_font_size' element value.
     */
    public void setPatternNodeFontSize(PatternNodeFontSize patternNodeFontSize) {
        this.patternNodeFontSize = patternNodeFontSize;
    }

    /**
     * Get the 'pattern_icon' element value.
     *
     * @return value
     */
    public PatternIcon getPatternIcon() {
        return patternIcon;
    }

    /**
     * Set the 'pattern_icon' element value.
     */
    public void setPatternIcon(PatternIcon patternIcon) {
        this.patternIcon = patternIcon;
    }

    /**
     * Get the 'pattern_edge_color' element value.
     *
     * @return value
     */
    public PatternEdgeColor getPatternEdgeColor() {
        return patternEdgeColor;
    }

    /**
     * Set the 'pattern_edge_color' element value.
     */
    public void setPatternEdgeColor(PatternEdgeColor patternEdgeColor) {
        this.patternEdgeColor = patternEdgeColor;
    }

    /**
     * Get the 'pattern_edge_style' element value.
     *
     * @return value
     */
    public PatternEdgeStyle getPatternEdgeStyle() {
        return patternEdgeStyle;
    }

    /**
     * Set the 'pattern_edge_style' element value.
     */
    public void setPatternEdgeStyle(PatternEdgeStyle patternEdgeStyle) {
        this.patternEdgeStyle = patternEdgeStyle;
    }

    /**
     * Get the 'pattern_edge_width' element value.
     *
     * @return value
     */
    public PatternEdgeWidth getPatternEdgeWidth() {
        return patternEdgeWidth;
    }

    /**
     * Set the 'pattern_edge_width' element value.
     */
    public void setPatternEdgeWidth(PatternEdgeWidth patternEdgeWidth) {
        this.patternEdgeWidth = patternEdgeWidth;
    }

    /**
     * Get the 'pattern_child' element value.
     *
     * @return value
     */
    public PatternChild getPatternChild() {
        return patternChild;
    }

    /**
     * Set the 'pattern_child' element value.
     */
    public void setPatternChild(PatternChild patternChild) {
        this.patternChild = patternChild;
    }

    /**
     * Get the 'pattern_script' element value.
     *
     * @return value
     */
    public PatternScript getPatternScript() {
        return patternScript;
    }

    /**
     * Set the 'pattern_script' element value.
     */
    public void setPatternScript(PatternScript patternScript) {
        this.patternScript = patternScript;
    }

    /**
     * Get the 'name' attribute value.
     *
     * @return value
     */
    public String getName() {
        return name;
    }

    /**
     * Set the 'name' attribute value.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the 'original_name' attribute value.
     *
     * @return value
     */
    public String getOriginalName() {
        return originalName;
    }

    /**
     * Set the 'original_name' attribute value.
     */
    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }
}
