
package freemind.controller.actions;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:element xmlns:xs="http://www.w3.org/2001/XMLSchema" name="node">
 *   &lt;xs:complexType>
 *     &lt;xs:choice minOccurs="0" maxOccurs="unbounded">
 *       &lt;!-- Reference to inner class Choice -->
 *     &lt;/xs:choice>
 *     &lt;xs:attribute type="xs:string" use="optional" name="BACKGROUND_COLOR"/>
 *     &lt;xs:attribute type="xs:string" use="optional" name="COLOR"/>
 *     &lt;xs:attribute use="optional" name="FOLDED">
 *       &lt;xs:simpleType>
 *         &lt;!-- Reference to inner class FOLDED -->
 *       &lt;/xs:simpleType>
 *     &lt;/xs:attribute>
 *     &lt;xs:attribute type="xs:string" use="optional" name="ID"/>
 *     &lt;xs:attribute type="xs:string" use="optional" name="LINK"/>
 *     &lt;xs:attribute use="optional" name="POSITION">
 *       &lt;xs:simpleType>
 *         &lt;!-- Reference to inner class POSITION -->
 *       &lt;/xs:simpleType>
 *     &lt;/xs:attribute>
 *     &lt;xs:attribute type="xs:string" use="optional" name="STYLE"/>
 *     &lt;xs:attribute type="xs:string" use="optional" name="TEXT"/>
 *     &lt;xs:attribute type="xs:integer" use="optional" name="CREATED"/>
 *     &lt;xs:attribute type="xs:integer" use="optional" name="MODIFIED"/>
 *     &lt;xs:attribute type="xs:integer" use="optional" name="HGAP"/>
 *     &lt;xs:attribute type="xs:integer" use="optional" name="VGAP"/>
 *     &lt;xs:attribute type="xs:integer" use="optional" name="VSHIFT"/>
 *     &lt;xs:attribute type="xs:string" use="optional" name="ENCRYPTED_CONTENT"/>
 *   &lt;/xs:complexType>
 * &lt;/xs:element>
 * </pre>
 */
import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "node")
public class Node
{
    private List<Choice> choiceList = new ArrayList<Choice>();
    @XmlAttribute(name = "BACKGROUND_COLOR")
    private String BACKGROUNDCOLOR;
    @XmlAttribute(name = "COLOR")
    private String COLOR;
    private FOLDED FOLDED1;
    @XmlAttribute(name = "ID")
    private String ID;
    @XmlAttribute(name = "LINK")
    private String LINK;
    private POSITION POSITION1;
    @XmlAttribute(name = "STYLE")
    private String STYLE;
    @XmlAttribute(name = "TEXT")
    private String TEXT;
    @XmlAttribute(name = "CREATED")
    private BigInteger CREATED;
    @XmlAttribute(name = "MODIFIED")
    private BigInteger MODIFIED;
    @XmlAttribute(name = "HGAP")
    private BigInteger HGAP;
    @XmlAttribute(name = "VGAP")
    private BigInteger VGAP;
    @XmlAttribute(name = "VSHIFT")
    private BigInteger VSHIFT;
    @XmlAttribute(name = "ENCRYPTED_CONTENT")
    private String ENCRYPTEDCONTENT;

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
     * Get the 'BACKGROUND_COLOR' attribute value.
     *
     * @return value
     */
    public String getBACKGROUNDCOLOR() {
        return BACKGROUNDCOLOR;
    }

    /**
     * Set the 'BACKGROUND_COLOR' attribute value.
     *
     * @param BACKGROUNDCOLOR
     */
    public void setBACKGROUNDCOLOR(String BACKGROUNDCOLOR) {
        this.BACKGROUNDCOLOR = BACKGROUNDCOLOR;
    }

    /**
     * Get the 'COLOR' attribute value.
     *
     * @return value
     */
    public String getCOLOR() {
        return COLOR;
    }

    /**
     * Set the 'COLOR' attribute value.
     *
     * @param COLOR
     */
    public void setCOLOR(String COLOR) {
        this.COLOR = COLOR;
    }

    /**
     * Get the 'FOLDED' attribute value.
     *
     * @return value
     */
    public FOLDED getFOLDED1() {
        return FOLDED1;
    }

    /**
     * Set the 'FOLDED' attribute value.
     *
     * @param FOLDED1
     */
    public void setFOLDED1(FOLDED FOLDED1) {
        this.FOLDED1 = FOLDED1;
    }

    /**
     * Get the 'ID' attribute value.
     *
     * @return value
     */
    public String getID() {
        return ID;
    }

    /**
     * Set the 'ID' attribute value.
     *
     * @param ID
     */
    public void setID(String ID) {
        this.ID = ID;
    }

    /**
     * Get the 'LINK' attribute value.
     *
     * @return value
     */
    public String getLINK() {
        return LINK;
    }

    /**
     * Set the 'LINK' attribute value.
     *
     * @param LINK
     */
    public void setLINK(String LINK) {
        this.LINK = LINK;
    }

    /**
     * Get the 'POSITION' attribute value.
     *
     * @return value
     */
    public POSITION getPOSITION1() {
        return POSITION1;
    }

    /**
     * Set the 'POSITION' attribute value.
     *
     * @param POSITION1
     */
    public void setPOSITION1(POSITION POSITION1) {
        this.POSITION1 = POSITION1;
    }

    /**
     * Get the 'STYLE' attribute value.
     *
     * @return value
     */
    public String getSTYLE() {
        return STYLE;
    }

    /**
     * Set the 'STYLE' attribute value.
     *
     * @param STYLE
     */
    public void setSTYLE(String STYLE) {
        this.STYLE = STYLE;
    }

    /**
     * Get the 'TEXT' attribute value.
     *
     * @return value
     */
    public String getTEXT() {
        return TEXT;
    }

    /**
     * Set the 'TEXT' attribute value.
     *
     * @param TEXT
     */
    public void setTEXT(String TEXT) {
        this.TEXT = TEXT;
    }

    /**
     * Get the 'CREATED' attribute value.
     *
     * @return value
     */
    public BigInteger getCREATED() {
        return CREATED;
    }

    /**
     * Set the 'CREATED' attribute value.
     *
     * @param CREATED
     */
    public void setCREATED(BigInteger CREATED) {
        this.CREATED = CREATED;
    }

    /**
     * Get the 'MODIFIED' attribute value.
     *
     * @return value
     */
    public BigInteger getMODIFIED() {
        return MODIFIED;
    }

    /**
     * Set the 'MODIFIED' attribute value.
     *
     * @param MODIFIED
     */
    public void setMODIFIED(BigInteger MODIFIED) {
        this.MODIFIED = MODIFIED;
    }

    /**
     * Get the 'HGAP' attribute value.
     *
     * @return value
     */
    public BigInteger getHGAP() {
        return HGAP;
    }

    /**
     * Set the 'HGAP' attribute value.
     *
     * @param HGAP
     */
    public void setHGAP(BigInteger HGAP) {
        this.HGAP = HGAP;
    }

    /**
     * Get the 'VGAP' attribute value.
     *
     * @return value
     */
    public BigInteger getVGAP() {
        return VGAP;
    }

    /**
     * Set the 'VGAP' attribute value.
     *
     * @param VGAP
     */
    public void setVGAP(BigInteger VGAP) {
        this.VGAP = VGAP;
    }

    /**
     * Get the 'VSHIFT' attribute value.
     *
     * @return value
     */
    public BigInteger getVSHIFT() {
        return VSHIFT;
    }

    /**
     * Set the 'VSHIFT' attribute value.
     *
     * @param VSHIFT
     */
    public void setVSHIFT(BigInteger VSHIFT) {
        this.VSHIFT = VSHIFT;
    }

    /**
     * Get the 'ENCRYPTED_CONTENT' attribute value.
     *
     * @return value
     */
    public String getENCRYPTEDCONTENT() {
        return ENCRYPTEDCONTENT;
    }

    /**
     * Set the 'ENCRYPTED_CONTENT' attribute value.
     *
     * @param ENCRYPTEDCONTENT
     */
    public void setENCRYPTEDCONTENT(String ENCRYPTEDCONTENT) {
        this.ENCRYPTEDCONTENT = ENCRYPTEDCONTENT;
    }
    /**
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:choice xmlns:xs="http://www.w3.org/2001/XMLSchema" minOccurs="0" maxOccurs="unbounded">
     *   &lt;xs:element ref="arrowlink"/>
     *   &lt;xs:element ref="attribute"/>
     *   &lt;xs:element ref="attribute_layout"/>
     *   &lt;xs:element ref="linktarget"/>
     *   &lt;xs:element ref="cloud"/>
     *   &lt;xs:element ref="edge"/>
     *   &lt;xs:element ref="font"/>
     *   &lt;xs:element ref="hook"/>
     *   &lt;xs:element ref="icon"/>
     *   &lt;xs:element ref="node"/>
     *   &lt;xs:element ref="richcontent"/>
     * &lt;/xs:choice>
     * </pre>
     */
    public static class Choice
    {
        private int choiceListSelect = -1;
        private static final int ARROWLINK_CHOICE = 0;
        private static final int ATTRIBUTE_CHOICE = 1;
        private static final int ATTRIBUTE_LAYOUT_CHOICE = 2;
        private static final int LINKTARGET_CHOICE = 3;
        private static final int CLOUD_CHOICE = 4;
        private static final int EDGE_CHOICE = 5;
        private static final int FONT_CHOICE = 6;
        private static final int HOOK_CHOICE = 7;
        private static final int ICON_CHOICE = 8;
        private static final int NODE_CHOICE = 9;
        private static final int RICHCONTENT_CHOICE = 10;
        private Arrowlink arrowlink;
        private Attribute attribute;
        private AttributeLayout attributeLayout;
        private Linktarget linktarget;
        private Cloud cloud;
        private Edge edge;
        private Font font;
        private Hook hook;
        private Icon icon;
        private Node node;
        private Richcontent richcontent;

        private void setChoiceListSelect(int choice) {
            if (choiceListSelect == -1) {
                choiceListSelect = choice;
            } else if (choiceListSelect != choice) {
                throw new IllegalStateException(
                        "Need to call clearChoiceListSelect() before changing existing choice");
            }
        }

        /**
         * Clear the choice selection.
         */
        public void clearChoiceListSelect() {
            choiceListSelect = -1;
        }

        /**
         * Check if Arrowlink is current selection for choice.
         *
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifArrowlink() {
            return choiceListSelect == ARROWLINK_CHOICE;
        }

        /**
         * Get the 'arrowlink' element value.
         *
         * @return value
         */
        public Arrowlink getArrowlink() {
            return arrowlink;
        }

        /**
         * Set the 'arrowlink' element value.
         *
         * @param arrowlink
         */
        public void setArrowlink(Arrowlink arrowlink) {
            setChoiceListSelect(ARROWLINK_CHOICE);
            this.arrowlink = arrowlink;
        }

        /**
         * Check if Attribute is current selection for choice.
         *
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifAttribute() {
            return choiceListSelect == ATTRIBUTE_CHOICE;
        }

        /**
         * Get the 'attribute' element value.
         *
         * @return value
         */
        public Attribute getAttribute() {
            return attribute;
        }

        /**
         * Set the 'attribute' element value.
         *
         * @param attribute
         */
        public void setAttribute(Attribute attribute) {
            setChoiceListSelect(ATTRIBUTE_CHOICE);
            this.attribute = attribute;
        }

        /**
         * Check if AttributeLayout is current selection for choice.
         *
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifAttributeLayout() {
            return choiceListSelect == ATTRIBUTE_LAYOUT_CHOICE;
        }

        /**
         * Get the 'attribute_layout' element value.
         *
         * @return value
         */
        public AttributeLayout getAttributeLayout() {
            return attributeLayout;
        }

        /**
         * Set the 'attribute_layout' element value.
         *
         * @param attributeLayout
         */
        public void setAttributeLayout(AttributeLayout attributeLayout) {
            setChoiceListSelect(ATTRIBUTE_LAYOUT_CHOICE);
            this.attributeLayout = attributeLayout;
        }

        /**
         * Check if Linktarget is current selection for choice.
         *
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifLinktarget() {
            return choiceListSelect == LINKTARGET_CHOICE;
        }

        /**
         * Get the 'linktarget' element value.
         *
         * @return value
         */
        public Linktarget getLinktarget() {
            return linktarget;
        }

        /**
         * Set the 'linktarget' element value.
         *
         * @param linktarget
         */
        public void setLinktarget(Linktarget linktarget) {
            setChoiceListSelect(LINKTARGET_CHOICE);
            this.linktarget = linktarget;
        }

        /**
         * Check if Cloud is current selection for choice.
         *
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifCloud() {
            return choiceListSelect == CLOUD_CHOICE;
        }

        /**
         * Get the 'cloud' element value.
         *
         * @return value
         */
        public Cloud getCloud() {
            return cloud;
        }

        /**
         * Set the 'cloud' element value.
         *
         * @param cloud
         */
        public void setCloud(Cloud cloud) {
            setChoiceListSelect(CLOUD_CHOICE);
            this.cloud = cloud;
        }

        /**
         * Check if Edge is current selection for choice.
         *
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifEdge() {
            return choiceListSelect == EDGE_CHOICE;
        }

        /**
         * Get the 'edge' element value.
         *
         * @return value
         */
        public Edge getEdge() {
            return edge;
        }

        /**
         * Set the 'edge' element value.
         *
         * @param edge
         */
        public void setEdge(Edge edge) {
            setChoiceListSelect(EDGE_CHOICE);
            this.edge = edge;
        }

        /**
         * Check if Font is current selection for choice.
         *
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifFont() {
            return choiceListSelect == FONT_CHOICE;
        }

        /**
         * Get the 'font' element value.
         *
         * @return value
         */
        public Font getFont() {
            return font;
        }

        /**
         * Set the 'font' element value.
         *
         * @param font
         */
        public void setFont(Font font) {
            setChoiceListSelect(FONT_CHOICE);
            this.font = font;
        }

        /**
         * Check if Hook is current selection for choice.
         *
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifHook() {
            return choiceListSelect == HOOK_CHOICE;
        }

        /**
         * Get the 'hook' element value.
         *
         * @return value
         */
        public Hook getHook() {
            return hook;
        }

        /**
         * Set the 'hook' element value.
         *
         * @param hook
         */
        public void setHook(Hook hook) {
            setChoiceListSelect(HOOK_CHOICE);
            this.hook = hook;
        }

        /**
         * Check if Icon is current selection for choice.
         *
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifIcon() {
            return choiceListSelect == ICON_CHOICE;
        }

        /**
         * Get the 'icon' element value.
         *
         * @return value
         */
        public Icon getIcon() {
            return icon;
        }

        /**
         * Set the 'icon' element value.
         *
         * @param icon
         */
        public void setIcon(Icon icon) {
            setChoiceListSelect(ICON_CHOICE);
            this.icon = icon;
        }

        /**
         * Check if Node is current selection for choice.
         *
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifNode() {
            return choiceListSelect == NODE_CHOICE;
        }

        /**
         * Get the 'node' element value.
         *
         * @return value
         */
        public Node getNode() {
            return node;
        }

        /**
         * Set the 'node' element value.
         *
         * @param node
         */
        public void setNode(Node node) {
            setChoiceListSelect(NODE_CHOICE);
            this.node = node;
        }

        /**
         * Check if Richcontent is current selection for choice.
         *
         * @return <code>true</code> if selection, <code>false</code> if not
         */
        public boolean ifRichcontent() {
            return choiceListSelect == RICHCONTENT_CHOICE;
        }

        /**
         * Get the 'richcontent' element value.
         *
         * @return value
         */
        public Richcontent getRichcontent() {
            return richcontent;
        }

        /**
         * Set the 'richcontent' element value.
         *
         * @param richcontent
         */
        public void setRichcontent(Richcontent richcontent) {
            setChoiceListSelect(RICHCONTENT_CHOICE);
            this.richcontent = richcontent;
        }
    }
    /**
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:simpleType xmlns:xs="http://www.w3.org/2001/XMLSchema">
     *   &lt;xs:restriction base="xs:string">
     *     &lt;xs:enumeration value="true"/>
     *     &lt;xs:enumeration value="false"/>
     *   &lt;/xs:restriction>
     * &lt;/xs:simpleType>
     * </pre>
     */
    public static enum FOLDED {
        TRUE("true"), FALSE("false");
        private final String value;

        private FOLDED(String value) {
            this.value = value;
        }

        public String xmlValue() {
            return value;
        }

        public static FOLDED convert(String value) {
            for (FOLDED inst : values()) {
                if (inst.xmlValue().equals(value)) {
                    return inst;
                }
            }
            return null;
        }
    }
    /**
     * Schema fragment(s) for this class:
     * <pre>
     * &lt;xs:simpleType xmlns:xs="http://www.w3.org/2001/XMLSchema">
     *   &lt;xs:restriction base="xs:string">
     *     &lt;xs:enumeration value="left"/>
     *     &lt;xs:enumeration value="right"/>
     *   &lt;/xs:restriction>
     * &lt;/xs:simpleType>
     * </pre>
     */
    public static enum POSITION {
        LEFT("left"), RIGHT("right");
        private final String value;

        private POSITION(String value) {
            this.value = value;
        }

        public String xmlValue() {
            return value;
        }

        public static POSITION convert(String value) {
            for (POSITION inst : values()) {
                if (inst.xmlValue().equals(value)) {
                    return inst;
                }
            }
            return null;
        }
    }
}
