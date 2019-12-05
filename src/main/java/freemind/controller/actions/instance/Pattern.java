
package freemind.controller.actions.instance;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="pattern">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="xml_action">
 *         &lt;xs:choice>
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
 *         &lt;/xs:choice>
 *         &lt;xs:attribute type="xs:string" use="optional" name="name"/>
 *         &lt;xs:attribute type="xs:string" use="optional" name="original_name"/>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class Pattern extends XmlAction
{
    private int choiceSelect = -1;
    /** 
     * ChoiceSelect value when patternNodeBackgroundColor is set
     */
    public static final int PATTERN_NODE_BACKGROUND_COLOR_CHOICE = 0;
    /** 
     * ChoiceSelect value when patternNodeColor is set
     */
    public static final int PATTERN_NODE_COLOR_CHOICE = 1;
    /** 
     * ChoiceSelect value when patternNodeStyle is set
     */
    public static final int PATTERN_NODE_STYLE_CHOICE = 2;
    /** 
     * ChoiceSelect value when patternNodeText is set
     */
    public static final int PATTERN_NODE_TEXT_CHOICE = 3;
    /** 
     * ChoiceSelect value when patternNodeFontName is set
     */
    public static final int PATTERN_NODE_FONT_NAME_CHOICE = 4;
    /** 
     * ChoiceSelect value when patternNodeFontBold is set
     */
    public static final int PATTERN_NODE_FONT_BOLD_CHOICE = 5;
    /** 
     * ChoiceSelect value when patternNodeFontStrikethrough is set
     */
    public static final int PATTERN_NODE_FONT_STRIKETHROUGH_CHOICE = 6;
    /** 
     * ChoiceSelect value when patternNodeFontItalic is set
     */
    public static final int PATTERN_NODE_FONT_ITALIC_CHOICE = 7;
    /** 
     * ChoiceSelect value when patternNodeFontSize is set
     */
    public static final int PATTERN_NODE_FONT_SIZE_CHOICE = 8;
    /** 
     * ChoiceSelect value when patternIcon is set
     */
    public static final int PATTERN_ICON_CHOICE = 9;
    /** 
     * ChoiceSelect value when patternEdgeColor is set
     */
    public static final int PATTERN_EDGE_COLOR_CHOICE = 10;
    /** 
     * ChoiceSelect value when patternEdgeStyle is set
     */
    public static final int PATTERN_EDGE_STYLE_CHOICE = 11;
    /** 
     * ChoiceSelect value when patternEdgeWidth is set
     */
    public static final int PATTERN_EDGE_WIDTH_CHOICE = 12;
    /** 
     * ChoiceSelect value when patternChild is set
     */
    public static final int PATTERN_CHILD_CHOICE = 13;
    /** 
     * ChoiceSelect value when patternScript is set
     */
    public static final int PATTERN_SCRIPT_CHOICE = 14;
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
    private String name;
    private String originalName;

    private void setChoiceSelect(int choice) {
        choiceSelect = choice;
    }

    /** 
     * Clear the choice selection.
     */
    public void clearChoiceSelect() {
        choiceSelect = -1;
    }

    /** 
     * Get the current choice state.
     * @return state
     */
    public int stateChoiceSelect() {
        return choiceSelect;
    }

    /** 
     * Check if PatternNodeBackgroundColor is current selection for choice.
     * 
     * @return <code>true</code> if selection, <code>false</code> if not
     */
    public boolean ifPatternNodeBackgroundColor() {
        return choiceSelect == PATTERN_NODE_BACKGROUND_COLOR_CHOICE;
    }

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
     * 
     * @param patternNodeBackgroundColor
     */
    public void setPatternNodeBackgroundColor(
            PatternNodeBackgroundColor patternNodeBackgroundColor) {
        choiceSelect = PATTERN_NODE_BACKGROUND_COLOR_CHOICE;
        this.patternNodeBackgroundColor = patternNodeBackgroundColor;
    }

    /** 
     * Check if PatternNodeColor is current selection for choice.
     * 
     * @return <code>true</code> if selection, <code>false</code> if not
     */
    public boolean ifPatternNodeColor() {
        return choiceSelect == PATTERN_NODE_COLOR_CHOICE;
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
     * 
     * @param patternNodeColor
     */
    public void setPatternNodeColor(PatternNodeColor patternNodeColor) {
        choiceSelect = PATTERN_NODE_COLOR_CHOICE;
        this.patternNodeColor = patternNodeColor;
    }

    /** 
     * Check if PatternNodeStyle is current selection for choice.
     * 
     * @return <code>true</code> if selection, <code>false</code> if not
     */
    public boolean ifPatternNodeStyle() {
        return choiceSelect == PATTERN_NODE_STYLE_CHOICE;
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
     * 
     * @param patternNodeStyle
     */
    public void setPatternNodeStyle(PatternNodeStyle patternNodeStyle) {
        choiceSelect = PATTERN_NODE_STYLE_CHOICE;
        this.patternNodeStyle = patternNodeStyle;
    }

    /** 
     * Check if PatternNodeText is current selection for choice.
     * 
     * @return <code>true</code> if selection, <code>false</code> if not
     */
    public boolean ifPatternNodeText() {
        return choiceSelect == PATTERN_NODE_TEXT_CHOICE;
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
     * 
     * @param patternNodeText
     */
    public void setPatternNodeText(PatternNodeText patternNodeText) {
        choiceSelect = PATTERN_NODE_TEXT_CHOICE;
        this.patternNodeText = patternNodeText;
    }

    /** 
     * Check if PatternNodeFontName is current selection for choice.
     * 
     * @return <code>true</code> if selection, <code>false</code> if not
     */
    public boolean ifPatternNodeFontName() {
        return choiceSelect == PATTERN_NODE_FONT_NAME_CHOICE;
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
     * 
     * @param patternNodeFontName
     */
    public void setPatternNodeFontName(PatternNodeFontName patternNodeFontName) {
        choiceSelect = PATTERN_NODE_FONT_NAME_CHOICE;
        this.patternNodeFontName = patternNodeFontName;
    }

    /** 
     * Check if PatternNodeFontBold is current selection for choice.
     * 
     * @return <code>true</code> if selection, <code>false</code> if not
     */
    public boolean ifPatternNodeFontBold() {
        return choiceSelect == PATTERN_NODE_FONT_BOLD_CHOICE;
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
     * 
     * @param patternNodeFontBold
     */
    public void setPatternNodeFontBold(PatternNodeFontBold patternNodeFontBold) {
        choiceSelect = PATTERN_NODE_FONT_BOLD_CHOICE;
        this.patternNodeFontBold = patternNodeFontBold;
    }

    /** 
     * Check if PatternNodeFontStrikethrough is current selection for choice.
     * 
     * @return <code>true</code> if selection, <code>false</code> if not
     */
    public boolean ifPatternNodeFontStrikethrough() {
        return choiceSelect == PATTERN_NODE_FONT_STRIKETHROUGH_CHOICE;
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
     * 
     * @param patternNodeFontStrikethrough
     */
    public void setPatternNodeFontStrikethrough(
            PatternNodeFontStrikethrough patternNodeFontStrikethrough) {
        choiceSelect = PATTERN_NODE_FONT_STRIKETHROUGH_CHOICE;
        this.patternNodeFontStrikethrough = patternNodeFontStrikethrough;
    }

    /** 
     * Check if PatternNodeFontItalic is current selection for choice.
     * 
     * @return <code>true</code> if selection, <code>false</code> if not
     */
    public boolean ifPatternNodeFontItalic() {
        return choiceSelect == PATTERN_NODE_FONT_ITALIC_CHOICE;
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
     * 
     * @param patternNodeFontItalic
     */
    public void setPatternNodeFontItalic(
            PatternNodeFontItalic patternNodeFontItalic) {
        choiceSelect = PATTERN_NODE_FONT_ITALIC_CHOICE;
        this.patternNodeFontItalic = patternNodeFontItalic;
    }

    /** 
     * Check if PatternNodeFontSize is current selection for choice.
     * 
     * @return <code>true</code> if selection, <code>false</code> if not
     */
    public boolean ifPatternNodeFontSize() {
        return choiceSelect == PATTERN_NODE_FONT_SIZE_CHOICE;
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
     * 
     * @param patternNodeFontSize
     */
    public void setPatternNodeFontSize(PatternNodeFontSize patternNodeFontSize) {
        choiceSelect = PATTERN_NODE_FONT_SIZE_CHOICE;
        this.patternNodeFontSize = patternNodeFontSize;
    }

    /** 
     * Check if PatternIcon is current selection for choice.
     * 
     * @return <code>true</code> if selection, <code>false</code> if not
     */
    public boolean ifPatternIcon() {
        return choiceSelect == PATTERN_ICON_CHOICE;
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
     * 
     * @param patternIcon
     */
    public void setPatternIcon(PatternIcon patternIcon) {
        choiceSelect = PATTERN_ICON_CHOICE;
        this.patternIcon = patternIcon;
    }

    /** 
     * Check if PatternEdgeColor is current selection for choice.
     * 
     * @return <code>true</code> if selection, <code>false</code> if not
     */
    public boolean ifPatternEdgeColor() {
        return choiceSelect == PATTERN_EDGE_COLOR_CHOICE;
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
     * 
     * @param patternEdgeColor
     */
    public void setPatternEdgeColor(PatternEdgeColor patternEdgeColor) {
        choiceSelect = PATTERN_EDGE_COLOR_CHOICE;
        this.patternEdgeColor = patternEdgeColor;
    }

    /** 
     * Check if PatternEdgeStyle is current selection for choice.
     * 
     * @return <code>true</code> if selection, <code>false</code> if not
     */
    public boolean ifPatternEdgeStyle() {
        return choiceSelect == PATTERN_EDGE_STYLE_CHOICE;
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
     * 
     * @param patternEdgeStyle
     */
    public void setPatternEdgeStyle(PatternEdgeStyle patternEdgeStyle) {
        choiceSelect = PATTERN_EDGE_STYLE_CHOICE;
        this.patternEdgeStyle = patternEdgeStyle;
    }

    /** 
     * Check if PatternEdgeWidth is current selection for choice.
     * 
     * @return <code>true</code> if selection, <code>false</code> if not
     */
    public boolean ifPatternEdgeWidth() {
        return choiceSelect == PATTERN_EDGE_WIDTH_CHOICE;
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
     * 
     * @param patternEdgeWidth
     */
    public void setPatternEdgeWidth(PatternEdgeWidth patternEdgeWidth) {
        choiceSelect = PATTERN_EDGE_WIDTH_CHOICE;
        this.patternEdgeWidth = patternEdgeWidth;
    }

    /** 
     * Check if PatternChild is current selection for choice.
     * 
     * @return <code>true</code> if selection, <code>false</code> if not
     */
    public boolean ifPatternChild() {
        return choiceSelect == PATTERN_CHILD_CHOICE;
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
     * 
     * @param patternChild
     */
    public void setPatternChild(PatternChild patternChild) {
        choiceSelect = PATTERN_CHILD_CHOICE;
        this.patternChild = patternChild;
    }

    /** 
     * Check if PatternScript is current selection for choice.
     * 
     * @return <code>true</code> if selection, <code>false</code> if not
     */
    public boolean ifPatternScript() {
        return choiceSelect == PATTERN_SCRIPT_CHOICE;
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
     * 
     * @param patternScript
     */
    public void setPatternScript(PatternScript patternScript) {
        choiceSelect = PATTERN_SCRIPT_CHOICE;
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
     * 
     * @param name
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
     * 
     * @param originalName
     */
    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }
}
