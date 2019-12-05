
package freemind.controller.actions.instance;

import java.util.ArrayList;
import java.util.List;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="compound_action">
 *   &lt;xs:complexType>
 *     &lt;xs:complexContent>
 *       &lt;xs:extension base="node_action">
 *         &lt;xs:choice minOccurs="0" maxOccurs="unbounded">
 *           &lt;!-- Reference to inner class Choice -->
 *         &lt;/xs:choice>
 *       &lt;/xs:extension>
 *     &lt;/xs:complexContent>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
public class CompoundAction extends NodeAction
{
    private List<Choice> choiceList = new ArrayList<Choice>();

    /** 
     * Get the list of choice items.
     * 
     * @return list
     */
    public List<Choice> getChoiceList() {
        return choiceList;
    }

    /** 
     * Set the list of choice items.
     * 
     * @param list
     */
    public void setChoiceList(List<Choice> list) {
        choiceList = list;
    }

    /** 
     * Get the number of choice items.
     * @return count
     */
    public int sizeChoiceList() {
        return choiceList.size();
    }

    /** 
     * Add a choice item.
     * @param item
     */
    public void addChoice(Choice item) {
        choiceList.add(item);
    }

    /** 
     * Get choice item by position.
     * @return item
     * @param index
     */
    public Choice getChoice(int index) {
        return choiceList.get(index);
    }

    /** 
     * Remove all choice items.
     */
    public void clearChoiceList() {
        choiceList.clear();
    }
    /** 
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:choice xmlns:xs="http://www.w3.org/2001/XMLSchema" minOccurs="0" maxOccurs="unbounded">
     *   &lt;xs:element ref="compound_action"/>
     *   &lt;xs:element ref="select_node_action"/>
     *   &lt;xs:element ref="cut_node_action"/>
     *   &lt;xs:element ref="paste_node_action"/>
     *   &lt;xs:element ref="undo_paste_node_action"/>
     *   &lt;xs:element ref="revert_xml_action"/>
     *   &lt;xs:element ref="bold_node_action"/>
     *   &lt;xs:element ref="strikethrough_node_action"/>
     *   &lt;xs:element ref="italic_node_action"/>
     *   &lt;xs:element ref="underlined_node_action"/>
     *   &lt;xs:element ref="font_size_node_action"/>
     *   &lt;xs:element ref="font_node_action"/>
     *   &lt;xs:element ref="node_color_format_action"/>
     *   &lt;xs:element ref="node_background_color_format_action"/>
     *   &lt;xs:element ref="node_style_format_action"/>
     *   &lt;xs:element ref="edge_color_format_action"/>
     *   &lt;xs:element ref="edge_width_format_action"/>
     *   &lt;xs:element ref="edge_style_format_action"/>
     *   &lt;xs:element ref="delete_node_action"/>
     *   &lt;xs:element ref="edit_node_action"/>
     *   &lt;xs:element ref="new_node_action"/>
     *   &lt;xs:element ref="fold_action"/>
     *   &lt;xs:element ref="move_nodes_action"/>
     *   &lt;xs:element ref="hook_node_action"/>
     *   &lt;xs:element ref="add_icon_action"/>
     *   &lt;xs:element ref="remove_icon_xml_action"/>
     *   &lt;xs:element ref="remove_all_icons_xml_action"/>
     *   &lt;xs:element ref="move_node_xml_action"/>
     *   &lt;xs:element ref="add_cloud_xml_action"/>
     *   &lt;xs:element ref="cloud_color_xml_action"/>
     *   &lt;xs:element ref="add_arrow_link_xml_action"/>
     *   &lt;xs:element ref="add_link_xml_action"/>
     *   &lt;xs:element ref="remove_arrow_link_xml_action"/>
     *   &lt;xs:element ref="arrow_link_color_xml_action"/>
     *   &lt;xs:element ref="arrow_link_arrow_xml_action"/>
     *   &lt;xs:element ref="arrow_link_point_xml_action"/>
     *   &lt;xs:element ref="set_attribute_action"/>
     *   &lt;xs:element ref="insert_attribute_action"/>
     *   &lt;xs:element ref="add_attribute_action"/>
     *   &lt;xs:element ref="remove_attribute_action"/>
     *   &lt;xs:element ref="edit_note_to_node_action"/>
     *   &lt;xs:element ref="place_node_xml_action"/>
     * &lt;/xs:choice>
     * </pre>
     */
    public static class Choice
    {
        private int choiceListSelect = -1;
        /** 
         * ChoiceListSelect value when compoundAction is set
         */
        public static final int COMPOUND_ACTION_CHOICE = 0;
        /** 
         * ChoiceListSelect value when selectNodeAction is set
         */
        public static final int SELECT_NODE_ACTION_CHOICE = 1;
        /** 
         * ChoiceListSelect value when cutNodeAction is set
         */
        public static final int CUT_NODE_ACTION_CHOICE = 2;
        /** 
         * ChoiceListSelect value when pasteNodeAction is set
         */
        public static final int PASTE_NODE_ACTION_CHOICE = 3;
        /** 
         * ChoiceListSelect value when undoPasteNodeAction is set
         */
        public static final int UNDO_PASTE_NODE_ACTION_CHOICE = 4;
        /** 
         * ChoiceListSelect value when revertXmlAction is set
         */
        public static final int REVERT_XML_ACTION_CHOICE = 5;
        /** 
         * ChoiceListSelect value when boldNodeAction is set
         */
        public static final int BOLD_NODE_ACTION_CHOICE = 6;
        /** 
         * ChoiceListSelect value when strikethroughNodeAction is set
         */
        public static final int STRIKETHROUGH_NODE_ACTION_CHOICE = 7;
        /** 
         * ChoiceListSelect value when italicNodeAction is set
         */
        public static final int ITALIC_NODE_ACTION_CHOICE = 8;
        /** 
         * ChoiceListSelect value when underlinedNodeAction is set
         */
        public static final int UNDERLINED_NODE_ACTION_CHOICE = 9;
        /** 
         * ChoiceListSelect value when fontSizeNodeAction is set
         */
        public static final int FONT_SIZE_NODE_ACTION_CHOICE = 10;
        /** 
         * ChoiceListSelect value when fontNodeAction is set
         */
        public static final int FONT_NODE_ACTION_CHOICE = 11;
        /** 
         * ChoiceListSelect value when nodeColorFormatAction is set
         */
        public static final int NODE_COLOR_FORMAT_ACTION_CHOICE = 12;
        /** 
         * ChoiceListSelect value when nodeBackgroundColorFormatAction is set
         */
        public static final int NODE_BACKGROUND_COLOR_FORMAT_ACTION_CHOICE = 13;
        /** 
         * ChoiceListSelect value when nodeStyleFormatAction is set
         */
        public static final int NODE_STYLE_FORMAT_ACTION_CHOICE = 14;
        /** 
         * ChoiceListSelect value when edgeColorFormatAction is set
         */
        public static final int EDGE_COLOR_FORMAT_ACTION_CHOICE = 15;
        /** 
         * ChoiceListSelect value when edgeWidthFormatAction is set
         */
        public static final int EDGE_WIDTH_FORMAT_ACTION_CHOICE = 16;
        /** 
         * ChoiceListSelect value when edgeStyleFormatAction is set
         */
        public static final int EDGE_STYLE_FORMAT_ACTION_CHOICE = 17;
        /** 
         * ChoiceListSelect value when deleteNodeAction is set
         */
        public static final int DELETE_NODE_ACTION_CHOICE = 18;
        /** 
         * ChoiceListSelect value when editNodeAction is set
         */
        public static final int EDIT_NODE_ACTION_CHOICE = 19;
        /** 
         * ChoiceListSelect value when newNodeAction is set
         */
        public static final int NEW_NODE_ACTION_CHOICE = 20;
        /** 
         * ChoiceListSelect value when foldAction is set
         */
        public static final int FOLD_ACTION_CHOICE = 21;
        /** 
         * ChoiceListSelect value when moveNodesAction is set
         */
        public static final int MOVE_NODES_ACTION_CHOICE = 22;
        /** 
         * ChoiceListSelect value when hookNodeAction is set
         */
        public static final int HOOK_NODE_ACTION_CHOICE = 23;
        /** 
         * ChoiceListSelect value when addIconAction is set
         */
        public static final int ADD_ICON_ACTION_CHOICE = 24;
        /** 
         * ChoiceListSelect value when removeIconXmlAction is set
         */
        public static final int REMOVE_ICON_XML_ACTION_CHOICE = 25;
        /** 
         * ChoiceListSelect value when removeAllIconsXmlAction is set
         */
        public static final int REMOVE_ALL_ICONS_XML_ACTION_CHOICE = 26;
        /** 
         * ChoiceListSelect value when moveNodeXmlAction is set
         */
        public static final int MOVE_NODE_XML_ACTION_CHOICE = 27;
        /** 
         * ChoiceListSelect value when addCloudXmlAction is set
         */
        public static final int ADD_CLOUD_XML_ACTION_CHOICE = 28;
        /** 
         * ChoiceListSelect value when cloudColorXmlAction is set
         */
        public static final int CLOUD_COLOR_XML_ACTION_CHOICE = 29;
        /** 
         * ChoiceListSelect value when addArrowLinkXmlAction is set
         */
        public static final int ADD_ARROW_LINK_XML_ACTION_CHOICE = 30;
        /** 
         * ChoiceListSelect value when addLinkXmlAction is set
         */
        public static final int ADD_LINK_XML_ACTION_CHOICE = 31;
        /** 
         * ChoiceListSelect value when removeArrowLinkXmlAction is set
         */
        public static final int REMOVE_ARROW_LINK_XML_ACTION_CHOICE = 32;
        /** 
         * ChoiceListSelect value when arrowLinkColorXmlAction is set
         */
        public static final int ARROW_LINK_COLOR_XML_ACTION_CHOICE = 33;
        /** 
         * ChoiceListSelect value when arrowLinkArrowXmlAction is set
         */
        public static final int ARROW_LINK_ARROW_XML_ACTION_CHOICE = 34;
        /** 
         * ChoiceListSelect value when arrowLinkPointXmlAction is set
         */
        public static final int ARROW_LINK_POINT_XML_ACTION_CHOICE = 35;
        /** 
         * ChoiceListSelect value when setAttributeAction is set
         */
        public static final int SET_ATTRIBUTE_ACTION_CHOICE = 36;
        /** 
         * ChoiceListSelect value when insertAttributeAction is set
         */
        public static final int INSERT_ATTRIBUTE_ACTION_CHOICE = 37;
        /** 
         * ChoiceListSelect value when addAttributeAction is set
         */
        public static final int ADD_ATTRIBUTE_ACTION_CHOICE = 38;
        /** 
         * ChoiceListSelect value when removeAttributeAction is set
         */
        public static final int REMOVE_ATTRIBUTE_ACTION_CHOICE = 39;
        /** 
         * ChoiceListSelect value when editNoteToNodeAction is set
         */
        public static final int EDIT_NOTE_TO_NODE_ACTION_CHOICE = 40;
        /** 
         * ChoiceListSelect value when placeNodeXmlAction is set
         */
        public static final int PLACE_NODE_XML_ACTION_CHOICE = 41;
        private CompoundAction compoundAction;
        private SelectNodeAction selectNodeAction;
        private CutNodeAction cutNodeAction;
        private PasteNodeAction pasteNodeAction;
        private UndoPasteNodeAction undoPasteNodeAction;
        private RevertXmlAction revertXmlAction;
        private BoldNodeAction boldNodeAction;
        private StrikethroughNodeAction strikethroughNodeAction;
        private ItalicNodeAction italicNodeAction;
        private UnderlinedNodeAction underlinedNodeAction;
        private FontSizeNodeAction fontSizeNodeAction;
        private FontNodeAction fontNodeAction;
        private NodeColorFormatAction nodeColorFormatAction;
        private NodeBackgroundColorFormatAction nodeBackgroundColorFormatAction;
        private NodeStyleFormatAction nodeStyleFormatAction;
        private EdgeColorFormatAction edgeColorFormatAction;
        private EdgeWidthFormatAction edgeWidthFormatAction;
        private EdgeStyleFormatAction edgeStyleFormatAction;
        private DeleteNodeAction deleteNodeAction;
        private EditNodeAction editNodeAction;
        private NewNodeAction newNodeAction;
        private FoldAction foldAction;
        private MoveNodesAction moveNodesAction;
        private HookNodeAction hookNodeAction;
        private AddIconAction addIconAction;
        private RemoveIconXmlAction removeIconXmlAction;
        private RemoveAllIconsXmlAction removeAllIconsXmlAction;
        private MoveNodeXmlAction moveNodeXmlAction;
        private AddCloudXmlAction addCloudXmlAction;
        private CloudColorXmlAction cloudColorXmlAction;
        private AddArrowLinkXmlAction addArrowLinkXmlAction;
        private AddLinkXmlAction addLinkXmlAction;
        private RemoveArrowLinkXmlAction removeArrowLinkXmlAction;
        private ArrowLinkColorXmlAction arrowLinkColorXmlAction;
        private ArrowLinkArrowXmlAction arrowLinkArrowXmlAction;
        private ArrowLinkPointXmlAction arrowLinkPointXmlAction;
        private SetAttributeAction setAttributeAction;
        private InsertAttributeAction insertAttributeAction;
        private AddAttributeAction addAttributeAction;
        private RemoveAttributeAction removeAttributeAction;
        private EditNoteToNodeAction editNoteToNodeAction;
        private PlaceNodeXmlAction placeNodeXmlAction;

        private void setChoiceListSelect(int choice) {
            choiceListSelect = choice;
        }

        /** 
         * Clear the choice selection.
         */
        public void clearChoiceListSelect() {
            choiceListSelect = -1;
        }

        /** 
         * Get the current choice state.
         * @return state
         */
        public int stateChoiceListSelect() {
            return choiceListSelect;
        }

        /** 
         * Check if CompoundAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifCompoundAction() {
            return choiceListSelect == COMPOUND_ACTION_CHOICE;
        }

        /** 
         * Get the 'compound_action' element value.
         * 
         * @return value
         */
        public CompoundAction getCompoundAction() {
            return compoundAction;
        }

        /** 
         * Set the 'compound_action' element value.
         * 
         * @param compoundAction
         */
        public void setCompoundAction(CompoundAction compoundAction) {
            choiceListSelect = COMPOUND_ACTION_CHOICE;
            this.compoundAction = compoundAction;
        }

        /** 
         * Check if SelectNodeAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifSelectNodeAction() {
            return choiceListSelect == SELECT_NODE_ACTION_CHOICE;
        }

        /** 
         * Get the 'select_node_action' element value.
         * 
         * @return value
         */
        public SelectNodeAction getSelectNodeAction() {
            return selectNodeAction;
        }

        /** 
         * Set the 'select_node_action' element value.
         * 
         * @param selectNodeAction
         */
        public void setSelectNodeAction(SelectNodeAction selectNodeAction) {
            choiceListSelect = SELECT_NODE_ACTION_CHOICE;
            this.selectNodeAction = selectNodeAction;
        }

        /** 
         * Check if CutNodeAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifCutNodeAction() {
            return choiceListSelect == CUT_NODE_ACTION_CHOICE;
        }

        /** 
         * Get the 'cut_node_action' element value.
         * 
         * @return value
         */
        public CutNodeAction getCutNodeAction() {
            return cutNodeAction;
        }

        /** 
         * Set the 'cut_node_action' element value.
         * 
         * @param cutNodeAction
         */
        public void setCutNodeAction(CutNodeAction cutNodeAction) {
            choiceListSelect = CUT_NODE_ACTION_CHOICE;
            this.cutNodeAction = cutNodeAction;
        }

        /** 
         * Check if PasteNodeAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifPasteNodeAction() {
            return choiceListSelect == PASTE_NODE_ACTION_CHOICE;
        }

        /** 
         * Get the 'paste_node_action' element value.
         * 
         * @return value
         */
        public PasteNodeAction getPasteNodeAction() {
            return pasteNodeAction;
        }

        /** 
         * Set the 'paste_node_action' element value.
         * 
         * @param pasteNodeAction
         */
        public void setPasteNodeAction(PasteNodeAction pasteNodeAction) {
            choiceListSelect = PASTE_NODE_ACTION_CHOICE;
            this.pasteNodeAction = pasteNodeAction;
        }

        /** 
         * Check if UndoPasteNodeAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifUndoPasteNodeAction() {
            return choiceListSelect == UNDO_PASTE_NODE_ACTION_CHOICE;
        }

        /** 
         * Get the 'undo_paste_node_action' element value.
         * 
         * @return value
         */
        public UndoPasteNodeAction getUndoPasteNodeAction() {
            return undoPasteNodeAction;
        }

        /** 
         * Set the 'undo_paste_node_action' element value.
         * 
         * @param undoPasteNodeAction
         */
        public void setUndoPasteNodeAction(
                UndoPasteNodeAction undoPasteNodeAction) {
            choiceListSelect = UNDO_PASTE_NODE_ACTION_CHOICE;
            this.undoPasteNodeAction = undoPasteNodeAction;
        }

        /** 
         * Check if RevertXmlAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifRevertXmlAction() {
            return choiceListSelect == REVERT_XML_ACTION_CHOICE;
        }

        /** 
         * Get the 'revert_xml_action' element value.
         * 
         * @return value
         */
        public RevertXmlAction getRevertXmlAction() {
            return revertXmlAction;
        }

        /** 
         * Set the 'revert_xml_action' element value.
         * 
         * @param revertXmlAction
         */
        public void setRevertXmlAction(RevertXmlAction revertXmlAction) {
            choiceListSelect = REVERT_XML_ACTION_CHOICE;
            this.revertXmlAction = revertXmlAction;
        }

        /** 
         * Check if BoldNodeAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifBoldNodeAction() {
            return choiceListSelect == BOLD_NODE_ACTION_CHOICE;
        }

        /** 
         * Get the 'bold_node_action' element value.
         * 
         * @return value
         */
        public BoldNodeAction getBoldNodeAction() {
            return boldNodeAction;
        }

        /** 
         * Set the 'bold_node_action' element value.
         * 
         * @param boldNodeAction
         */
        public void setBoldNodeAction(BoldNodeAction boldNodeAction) {
            choiceListSelect = BOLD_NODE_ACTION_CHOICE;
            this.boldNodeAction = boldNodeAction;
        }

        /** 
         * Check if StrikethroughNodeAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifStrikethroughNodeAction() {
            return choiceListSelect == STRIKETHROUGH_NODE_ACTION_CHOICE;
        }

        /** 
         * Get the 'strikethrough_node_action' element value.
         * 
         * @return value
         */
        public StrikethroughNodeAction getStrikethroughNodeAction() {
            return strikethroughNodeAction;
        }

        /** 
         * Set the 'strikethrough_node_action' element value.
         * 
         * @param strikethroughNodeAction
         */
        public void setStrikethroughNodeAction(
                StrikethroughNodeAction strikethroughNodeAction) {
            choiceListSelect = STRIKETHROUGH_NODE_ACTION_CHOICE;
            this.strikethroughNodeAction = strikethroughNodeAction;
        }

        /** 
         * Check if ItalicNodeAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifItalicNodeAction() {
            return choiceListSelect == ITALIC_NODE_ACTION_CHOICE;
        }

        /** 
         * Get the 'italic_node_action' element value.
         * 
         * @return value
         */
        public ItalicNodeAction getItalicNodeAction() {
            return italicNodeAction;
        }

        /** 
         * Set the 'italic_node_action' element value.
         * 
         * @param italicNodeAction
         */
        public void setItalicNodeAction(ItalicNodeAction italicNodeAction) {
            choiceListSelect = ITALIC_NODE_ACTION_CHOICE;
            this.italicNodeAction = italicNodeAction;
        }

        /** 
         * Check if UnderlinedNodeAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifUnderlinedNodeAction() {
            return choiceListSelect == UNDERLINED_NODE_ACTION_CHOICE;
        }

        /** 
         * Get the 'underlined_node_action' element value.
         * 
         * @return value
         */
        public UnderlinedNodeAction getUnderlinedNodeAction() {
            return underlinedNodeAction;
        }

        /** 
         * Set the 'underlined_node_action' element value.
         * 
         * @param underlinedNodeAction
         */
        public void setUnderlinedNodeAction(
                UnderlinedNodeAction underlinedNodeAction) {
            choiceListSelect = UNDERLINED_NODE_ACTION_CHOICE;
            this.underlinedNodeAction = underlinedNodeAction;
        }

        /** 
         * Check if FontSizeNodeAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifFontSizeNodeAction() {
            return choiceListSelect == FONT_SIZE_NODE_ACTION_CHOICE;
        }

        /** 
         * Get the 'font_size_node_action' element value.
         * 
         * @return value
         */
        public FontSizeNodeAction getFontSizeNodeAction() {
            return fontSizeNodeAction;
        }

        /** 
         * Set the 'font_size_node_action' element value.
         * 
         * @param fontSizeNodeAction
         */
        public void setFontSizeNodeAction(FontSizeNodeAction fontSizeNodeAction) {
            choiceListSelect = FONT_SIZE_NODE_ACTION_CHOICE;
            this.fontSizeNodeAction = fontSizeNodeAction;
        }

        /** 
         * Check if FontNodeAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifFontNodeAction() {
            return choiceListSelect == FONT_NODE_ACTION_CHOICE;
        }

        /** 
         * Get the 'font_node_action' element value.
         * 
         * @return value
         */
        public FontNodeAction getFontNodeAction() {
            return fontNodeAction;
        }

        /** 
         * Set the 'font_node_action' element value.
         * 
         * @param fontNodeAction
         */
        public void setFontNodeAction(FontNodeAction fontNodeAction) {
            choiceListSelect = FONT_NODE_ACTION_CHOICE;
            this.fontNodeAction = fontNodeAction;
        }

        /** 
         * Check if NodeColorFormatAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifNodeColorFormatAction() {
            return choiceListSelect == NODE_COLOR_FORMAT_ACTION_CHOICE;
        }

        /** 
         * Get the 'node_color_format_action' element value.
         * 
         * @return value
         */
        public NodeColorFormatAction getNodeColorFormatAction() {
            return nodeColorFormatAction;
        }

        /** 
         * Set the 'node_color_format_action' element value.
         * 
         * @param nodeColorFormatAction
         */
        public void setNodeColorFormatAction(
                NodeColorFormatAction nodeColorFormatAction) {
            choiceListSelect = NODE_COLOR_FORMAT_ACTION_CHOICE;
            this.nodeColorFormatAction = nodeColorFormatAction;
        }

        /** 
         * Check if NodeBackgroundColorFormatAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifNodeBackgroundColorFormatAction() {
            return choiceListSelect == NODE_BACKGROUND_COLOR_FORMAT_ACTION_CHOICE;
        }

        /** 
         * Get the 'node_background_color_format_action' element value.
         * 
         * @return value
         */
        public NodeBackgroundColorFormatAction getNodeBackgroundColorFormatAction() {
            return nodeBackgroundColorFormatAction;
        }

        /** 
         * Set the 'node_background_color_format_action' element value.
         * 
         * @param nodeBackgroundColorFormatAction
         */
        public void setNodeBackgroundColorFormatAction(
                NodeBackgroundColorFormatAction nodeBackgroundColorFormatAction) {
            choiceListSelect = NODE_BACKGROUND_COLOR_FORMAT_ACTION_CHOICE;
            this.nodeBackgroundColorFormatAction = nodeBackgroundColorFormatAction;
        }

        /** 
         * Check if NodeStyleFormatAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifNodeStyleFormatAction() {
            return choiceListSelect == NODE_STYLE_FORMAT_ACTION_CHOICE;
        }

        /** 
         * Get the 'node_style_format_action' element value.
         * 
         * @return value
         */
        public NodeStyleFormatAction getNodeStyleFormatAction() {
            return nodeStyleFormatAction;
        }

        /** 
         * Set the 'node_style_format_action' element value.
         * 
         * @param nodeStyleFormatAction
         */
        public void setNodeStyleFormatAction(
                NodeStyleFormatAction nodeStyleFormatAction) {
            choiceListSelect = NODE_STYLE_FORMAT_ACTION_CHOICE;
            this.nodeStyleFormatAction = nodeStyleFormatAction;
        }

        /** 
         * Check if EdgeColorFormatAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifEdgeColorFormatAction() {
            return choiceListSelect == EDGE_COLOR_FORMAT_ACTION_CHOICE;
        }

        /** 
         * Get the 'edge_color_format_action' element value.
         * 
         * @return value
         */
        public EdgeColorFormatAction getEdgeColorFormatAction() {
            return edgeColorFormatAction;
        }

        /** 
         * Set the 'edge_color_format_action' element value.
         * 
         * @param edgeColorFormatAction
         */
        public void setEdgeColorFormatAction(
                EdgeColorFormatAction edgeColorFormatAction) {
            choiceListSelect = EDGE_COLOR_FORMAT_ACTION_CHOICE;
            this.edgeColorFormatAction = edgeColorFormatAction;
        }

        /** 
         * Check if EdgeWidthFormatAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifEdgeWidthFormatAction() {
            return choiceListSelect == EDGE_WIDTH_FORMAT_ACTION_CHOICE;
        }

        /** 
         * Get the 'edge_width_format_action' element value.
         * 
         * @return value
         */
        public EdgeWidthFormatAction getEdgeWidthFormatAction() {
            return edgeWidthFormatAction;
        }

        /** 
         * Set the 'edge_width_format_action' element value.
         * 
         * @param edgeWidthFormatAction
         */
        public void setEdgeWidthFormatAction(
                EdgeWidthFormatAction edgeWidthFormatAction) {
            choiceListSelect = EDGE_WIDTH_FORMAT_ACTION_CHOICE;
            this.edgeWidthFormatAction = edgeWidthFormatAction;
        }

        /** 
         * Check if EdgeStyleFormatAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifEdgeStyleFormatAction() {
            return choiceListSelect == EDGE_STYLE_FORMAT_ACTION_CHOICE;
        }

        /** 
         * Get the 'edge_style_format_action' element value.
         * 
         * @return value
         */
        public EdgeStyleFormatAction getEdgeStyleFormatAction() {
            return edgeStyleFormatAction;
        }

        /** 
         * Set the 'edge_style_format_action' element value.
         * 
         * @param edgeStyleFormatAction
         */
        public void setEdgeStyleFormatAction(
                EdgeStyleFormatAction edgeStyleFormatAction) {
            choiceListSelect = EDGE_STYLE_FORMAT_ACTION_CHOICE;
            this.edgeStyleFormatAction = edgeStyleFormatAction;
        }

        /** 
         * Check if DeleteNodeAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifDeleteNodeAction() {
            return choiceListSelect == DELETE_NODE_ACTION_CHOICE;
        }

        /** 
         * Get the 'delete_node_action' element value.
         * 
         * @return value
         */
        public DeleteNodeAction getDeleteNodeAction() {
            return deleteNodeAction;
        }

        /** 
         * Set the 'delete_node_action' element value.
         * 
         * @param deleteNodeAction
         */
        public void setDeleteNodeAction(DeleteNodeAction deleteNodeAction) {
            choiceListSelect = DELETE_NODE_ACTION_CHOICE;
            this.deleteNodeAction = deleteNodeAction;
        }

        /** 
         * Check if EditNodeAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifEditNodeAction() {
            return choiceListSelect == EDIT_NODE_ACTION_CHOICE;
        }

        /** 
         * Get the 'edit_node_action' element value.
         * 
         * @return value
         */
        public EditNodeAction getEditNodeAction() {
            return editNodeAction;
        }

        /** 
         * Set the 'edit_node_action' element value.
         * 
         * @param editNodeAction
         */
        public void setEditNodeAction(EditNodeAction editNodeAction) {
            choiceListSelect = EDIT_NODE_ACTION_CHOICE;
            this.editNodeAction = editNodeAction;
        }

        /** 
         * Check if NewNodeAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifNewNodeAction() {
            return choiceListSelect == NEW_NODE_ACTION_CHOICE;
        }

        /** 
         * Get the 'new_node_action' element value.
         * 
         * @return value
         */
        public NewNodeAction getNewNodeAction() {
            return newNodeAction;
        }

        /** 
         * Set the 'new_node_action' element value.
         * 
         * @param newNodeAction
         */
        public void setNewNodeAction(NewNodeAction newNodeAction) {
            choiceListSelect = NEW_NODE_ACTION_CHOICE;
            this.newNodeAction = newNodeAction;
        }

        /** 
         * Check if FoldAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifFoldAction() {
            return choiceListSelect == FOLD_ACTION_CHOICE;
        }

        /** 
         * Get the 'fold_action' element value.
         * 
         * @return value
         */
        public FoldAction getFoldAction() {
            return foldAction;
        }

        /** 
         * Set the 'fold_action' element value.
         * 
         * @param foldAction
         */
        public void setFoldAction(FoldAction foldAction) {
            choiceListSelect = FOLD_ACTION_CHOICE;
            this.foldAction = foldAction;
        }

        /** 
         * Check if MoveNodesAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifMoveNodesAction() {
            return choiceListSelect == MOVE_NODES_ACTION_CHOICE;
        }

        /** 
         * Get the 'move_nodes_action' element value.
         * 
         * @return value
         */
        public MoveNodesAction getMoveNodesAction() {
            return moveNodesAction;
        }

        /** 
         * Set the 'move_nodes_action' element value.
         * 
         * @param moveNodesAction
         */
        public void setMoveNodesAction(MoveNodesAction moveNodesAction) {
            choiceListSelect = MOVE_NODES_ACTION_CHOICE;
            this.moveNodesAction = moveNodesAction;
        }

        /** 
         * Check if HookNodeAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifHookNodeAction() {
            return choiceListSelect == HOOK_NODE_ACTION_CHOICE;
        }

        /** 
         * Get the 'hook_node_action' element value.
         * 
         * @return value
         */
        public HookNodeAction getHookNodeAction() {
            return hookNodeAction;
        }

        /** 
         * Set the 'hook_node_action' element value.
         * 
         * @param hookNodeAction
         */
        public void setHookNodeAction(HookNodeAction hookNodeAction) {
            choiceListSelect = HOOK_NODE_ACTION_CHOICE;
            this.hookNodeAction = hookNodeAction;
        }

        /** 
         * Check if AddIconAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifAddIconAction() {
            return choiceListSelect == ADD_ICON_ACTION_CHOICE;
        }

        /** 
         * Get the 'add_icon_action' element value.
         * 
         * @return value
         */
        public AddIconAction getAddIconAction() {
            return addIconAction;
        }

        /** 
         * Set the 'add_icon_action' element value.
         * 
         * @param addIconAction
         */
        public void setAddIconAction(AddIconAction addIconAction) {
            choiceListSelect = ADD_ICON_ACTION_CHOICE;
            this.addIconAction = addIconAction;
        }

        /** 
         * Check if RemoveIconXmlAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifRemoveIconXmlAction() {
            return choiceListSelect == REMOVE_ICON_XML_ACTION_CHOICE;
        }

        /** 
         * Get the 'remove_icon_xml_action' element value.
         * 
         * @return value
         */
        public RemoveIconXmlAction getRemoveIconXmlAction() {
            return removeIconXmlAction;
        }

        /** 
         * Set the 'remove_icon_xml_action' element value.
         * 
         * @param removeIconXmlAction
         */
        public void setRemoveIconXmlAction(
                RemoveIconXmlAction removeIconXmlAction) {
            choiceListSelect = REMOVE_ICON_XML_ACTION_CHOICE;
            this.removeIconXmlAction = removeIconXmlAction;
        }

        /** 
         * Check if RemoveAllIconsXmlAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifRemoveAllIconsXmlAction() {
            return choiceListSelect == REMOVE_ALL_ICONS_XML_ACTION_CHOICE;
        }

        /** 
         * Get the 'remove_all_icons_xml_action' element value.
         * 
         * @return value
         */
        public RemoveAllIconsXmlAction getRemoveAllIconsXmlAction() {
            return removeAllIconsXmlAction;
        }

        /** 
         * Set the 'remove_all_icons_xml_action' element value.
         * 
         * @param removeAllIconsXmlAction
         */
        public void setRemoveAllIconsXmlAction(
                RemoveAllIconsXmlAction removeAllIconsXmlAction) {
            choiceListSelect = REMOVE_ALL_ICONS_XML_ACTION_CHOICE;
            this.removeAllIconsXmlAction = removeAllIconsXmlAction;
        }

        /** 
         * Check if MoveNodeXmlAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifMoveNodeXmlAction() {
            return choiceListSelect == MOVE_NODE_XML_ACTION_CHOICE;
        }

        /** 
         * Get the 'move_node_xml_action' element value.
         * 
         * @return value
         */
        public MoveNodeXmlAction getMoveNodeXmlAction() {
            return moveNodeXmlAction;
        }

        /** 
         * Set the 'move_node_xml_action' element value.
         * 
         * @param moveNodeXmlAction
         */
        public void setMoveNodeXmlAction(MoveNodeXmlAction moveNodeXmlAction) {
            choiceListSelect = MOVE_NODE_XML_ACTION_CHOICE;
            this.moveNodeXmlAction = moveNodeXmlAction;
        }

        /** 
         * Check if AddCloudXmlAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifAddCloudXmlAction() {
            return choiceListSelect == ADD_CLOUD_XML_ACTION_CHOICE;
        }

        /** 
         * Get the 'add_cloud_xml_action' element value.
         * 
         * @return value
         */
        public AddCloudXmlAction getAddCloudXmlAction() {
            return addCloudXmlAction;
        }

        /** 
         * Set the 'add_cloud_xml_action' element value.
         * 
         * @param addCloudXmlAction
         */
        public void setAddCloudXmlAction(AddCloudXmlAction addCloudXmlAction) {
            choiceListSelect = ADD_CLOUD_XML_ACTION_CHOICE;
            this.addCloudXmlAction = addCloudXmlAction;
        }

        /** 
         * Check if CloudColorXmlAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifCloudColorXmlAction() {
            return choiceListSelect == CLOUD_COLOR_XML_ACTION_CHOICE;
        }

        /** 
         * Get the 'cloud_color_xml_action' element value.
         * 
         * @return value
         */
        public CloudColorXmlAction getCloudColorXmlAction() {
            return cloudColorXmlAction;
        }

        /** 
         * Set the 'cloud_color_xml_action' element value.
         * 
         * @param cloudColorXmlAction
         */
        public void setCloudColorXmlAction(
                CloudColorXmlAction cloudColorXmlAction) {
            choiceListSelect = CLOUD_COLOR_XML_ACTION_CHOICE;
            this.cloudColorXmlAction = cloudColorXmlAction;
        }

        /** 
         * Check if AddArrowLinkXmlAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifAddArrowLinkXmlAction() {
            return choiceListSelect == ADD_ARROW_LINK_XML_ACTION_CHOICE;
        }

        /** 
         * Get the 'add_arrow_link_xml_action' element value.
         * 
         * @return value
         */
        public AddArrowLinkXmlAction getAddArrowLinkXmlAction() {
            return addArrowLinkXmlAction;
        }

        /** 
         * Set the 'add_arrow_link_xml_action' element value.
         * 
         * @param addArrowLinkXmlAction
         */
        public void setAddArrowLinkXmlAction(
                AddArrowLinkXmlAction addArrowLinkXmlAction) {
            choiceListSelect = ADD_ARROW_LINK_XML_ACTION_CHOICE;
            this.addArrowLinkXmlAction = addArrowLinkXmlAction;
        }

        /** 
         * Check if AddLinkXmlAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifAddLinkXmlAction() {
            return choiceListSelect == ADD_LINK_XML_ACTION_CHOICE;
        }

        /** 
         * Get the 'add_link_xml_action' element value.
         * 
         * @return value
         */
        public AddLinkXmlAction getAddLinkXmlAction() {
            return addLinkXmlAction;
        }

        /** 
         * Set the 'add_link_xml_action' element value.
         * 
         * @param addLinkXmlAction
         */
        public void setAddLinkXmlAction(AddLinkXmlAction addLinkXmlAction) {
            choiceListSelect = ADD_LINK_XML_ACTION_CHOICE;
            this.addLinkXmlAction = addLinkXmlAction;
        }

        /** 
         * Check if RemoveArrowLinkXmlAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifRemoveArrowLinkXmlAction() {
            return choiceListSelect == REMOVE_ARROW_LINK_XML_ACTION_CHOICE;
        }

        /** 
         * Get the 'remove_arrow_link_xml_action' element value.
         * 
         * @return value
         */
        public RemoveArrowLinkXmlAction getRemoveArrowLinkXmlAction() {
            return removeArrowLinkXmlAction;
        }

        /** 
         * Set the 'remove_arrow_link_xml_action' element value.
         * 
         * @param removeArrowLinkXmlAction
         */
        public void setRemoveArrowLinkXmlAction(
                RemoveArrowLinkXmlAction removeArrowLinkXmlAction) {
            choiceListSelect = REMOVE_ARROW_LINK_XML_ACTION_CHOICE;
            this.removeArrowLinkXmlAction = removeArrowLinkXmlAction;
        }

        /** 
         * Check if ArrowLinkColorXmlAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifArrowLinkColorXmlAction() {
            return choiceListSelect == ARROW_LINK_COLOR_XML_ACTION_CHOICE;
        }

        /** 
         * Get the 'arrow_link_color_xml_action' element value.
         * 
         * @return value
         */
        public ArrowLinkColorXmlAction getArrowLinkColorXmlAction() {
            return arrowLinkColorXmlAction;
        }

        /** 
         * Set the 'arrow_link_color_xml_action' element value.
         * 
         * @param arrowLinkColorXmlAction
         */
        public void setArrowLinkColorXmlAction(
                ArrowLinkColorXmlAction arrowLinkColorXmlAction) {
            choiceListSelect = ARROW_LINK_COLOR_XML_ACTION_CHOICE;
            this.arrowLinkColorXmlAction = arrowLinkColorXmlAction;
        }

        /** 
         * Check if ArrowLinkArrowXmlAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifArrowLinkArrowXmlAction() {
            return choiceListSelect == ARROW_LINK_ARROW_XML_ACTION_CHOICE;
        }

        /** 
         * Get the 'arrow_link_arrow_xml_action' element value.
         * 
         * @return value
         */
        public ArrowLinkArrowXmlAction getArrowLinkArrowXmlAction() {
            return arrowLinkArrowXmlAction;
        }

        /** 
         * Set the 'arrow_link_arrow_xml_action' element value.
         * 
         * @param arrowLinkArrowXmlAction
         */
        public void setArrowLinkArrowXmlAction(
                ArrowLinkArrowXmlAction arrowLinkArrowXmlAction) {
            choiceListSelect = ARROW_LINK_ARROW_XML_ACTION_CHOICE;
            this.arrowLinkArrowXmlAction = arrowLinkArrowXmlAction;
        }

        /** 
         * Check if ArrowLinkPointXmlAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifArrowLinkPointXmlAction() {
            return choiceListSelect == ARROW_LINK_POINT_XML_ACTION_CHOICE;
        }

        /** 
         * Get the 'arrow_link_point_xml_action' element value.
         * 
         * @return value
         */
        public ArrowLinkPointXmlAction getArrowLinkPointXmlAction() {
            return arrowLinkPointXmlAction;
        }

        /** 
         * Set the 'arrow_link_point_xml_action' element value.
         * 
         * @param arrowLinkPointXmlAction
         */
        public void setArrowLinkPointXmlAction(
                ArrowLinkPointXmlAction arrowLinkPointXmlAction) {
            choiceListSelect = ARROW_LINK_POINT_XML_ACTION_CHOICE;
            this.arrowLinkPointXmlAction = arrowLinkPointXmlAction;
        }

        /** 
         * Check if SetAttributeAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifSetAttributeAction() {
            return choiceListSelect == SET_ATTRIBUTE_ACTION_CHOICE;
        }

        /** 
         * Get the 'set_attribute_action' element value.
         * 
         * @return value
         */
        public SetAttributeAction getSetAttributeAction() {
            return setAttributeAction;
        }

        /** 
         * Set the 'set_attribute_action' element value.
         * 
         * @param setAttributeAction
         */
        public void setSetAttributeAction(SetAttributeAction setAttributeAction) {
            choiceListSelect = SET_ATTRIBUTE_ACTION_CHOICE;
            this.setAttributeAction = setAttributeAction;
        }

        /** 
         * Check if InsertAttributeAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifInsertAttributeAction() {
            return choiceListSelect == INSERT_ATTRIBUTE_ACTION_CHOICE;
        }

        /** 
         * Get the 'insert_attribute_action' element value.
         * 
         * @return value
         */
        public InsertAttributeAction getInsertAttributeAction() {
            return insertAttributeAction;
        }

        /** 
         * Set the 'insert_attribute_action' element value.
         * 
         * @param insertAttributeAction
         */
        public void setInsertAttributeAction(
                InsertAttributeAction insertAttributeAction) {
            choiceListSelect = INSERT_ATTRIBUTE_ACTION_CHOICE;
            this.insertAttributeAction = insertAttributeAction;
        }

        /** 
         * Check if AddAttributeAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifAddAttributeAction() {
            return choiceListSelect == ADD_ATTRIBUTE_ACTION_CHOICE;
        }

        /** 
         * Get the 'add_attribute_action' element value.
         * 
         * @return value
         */
        public AddAttributeAction getAddAttributeAction() {
            return addAttributeAction;
        }

        /** 
         * Set the 'add_attribute_action' element value.
         * 
         * @param addAttributeAction
         */
        public void setAddAttributeAction(AddAttributeAction addAttributeAction) {
            choiceListSelect = ADD_ATTRIBUTE_ACTION_CHOICE;
            this.addAttributeAction = addAttributeAction;
        }

        /** 
         * Check if RemoveAttributeAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifRemoveAttributeAction() {
            return choiceListSelect == REMOVE_ATTRIBUTE_ACTION_CHOICE;
        }

        /** 
         * Get the 'remove_attribute_action' element value.
         * 
         * @return value
         */
        public RemoveAttributeAction getRemoveAttributeAction() {
            return removeAttributeAction;
        }

        /** 
         * Set the 'remove_attribute_action' element value.
         * 
         * @param removeAttributeAction
         */
        public void setRemoveAttributeAction(
                RemoveAttributeAction removeAttributeAction) {
            choiceListSelect = REMOVE_ATTRIBUTE_ACTION_CHOICE;
            this.removeAttributeAction = removeAttributeAction;
        }

        /** 
         * Check if EditNoteToNodeAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifEditNoteToNodeAction() {
            return choiceListSelect == EDIT_NOTE_TO_NODE_ACTION_CHOICE;
        }

        /** 
         * Get the 'edit_note_to_node_action' element value.
         * 
         * @return value
         */
        public EditNoteToNodeAction getEditNoteToNodeAction() {
            return editNoteToNodeAction;
        }

        /** 
         * Set the 'edit_note_to_node_action' element value.
         * 
         * @param editNoteToNodeAction
         */
        public void setEditNoteToNodeAction(
                EditNoteToNodeAction editNoteToNodeAction) {
            choiceListSelect = EDIT_NOTE_TO_NODE_ACTION_CHOICE;
            this.editNoteToNodeAction = editNoteToNodeAction;
        }

        /** 
         * Check if PlaceNodeXmlAction is current selection for choice.
         * 
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifPlaceNodeXmlAction() {
            return choiceListSelect == PLACE_NODE_XML_ACTION_CHOICE;
        }

        /** 
         * Get the 'place_node_xml_action' element value.
         * 
         * @return value
         */
        public PlaceNodeXmlAction getPlaceNodeXmlAction() {
            return placeNodeXmlAction;
        }

        /** 
         * Set the 'place_node_xml_action' element value.
         * 
         * @param placeNodeXmlAction
         */
        public void setPlaceNodeXmlAction(PlaceNodeXmlAction placeNodeXmlAction) {
            choiceListSelect = PLACE_NODE_XML_ACTION_CHOICE;
            this.placeNodeXmlAction = placeNodeXmlAction;
        }
    }
}
