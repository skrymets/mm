package freemind.model;

import freemind.extensions.NodeHook;
import freemind.extensions.PermanentNodeHook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import freemind.model.attributes.Attribute;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a single Node of a Tree. It contains direct handles to
 * its parents and children and to its view.
 */
@Slf4j
public abstract class NodeAdapter implements MindMapNode {

    final static int SHIFT = -2;// height of the vertical shift between node and
    // its closest child
    public final static int HGAP = 20;// width of the horizontal gap that
    // contains the edges
    public final static int VGAP = 3;// height of the vertical gap between nodes

    public final static int LEFT_POSITION = -1;
    public final static int RIGHT_POSITION = 1;
    public final static int UNKNOWN_POSITION = 0;

    @Getter
    private String link = null; // Change this to vector in future for full
    // graph support
    @Getter
    private final freemind.model.services.NodeTooltipService tooltipService = new freemind.model.services.NodeTooltipService(this);
    @Getter
    private final freemind.model.services.NodeDecorationsService decorationsService = new freemind.model.services.NodeDecorationsService(this);
    @Getter
    private final freemind.model.services.NodeAttributesService attributesService = new freemind.model.services.NodeAttributesService(this);
    @Getter
    private final freemind.model.services.NodeContentService contentService = new freemind.model.services.NodeContentService(this);
    @Getter
    private final freemind.model.services.NodeStyleService styleService = new freemind.model.services.NodeStyleService(this);
    @Getter
    private final freemind.model.services.NodeHooksService hooksService = new freemind.model.services.NodeHooksService(this);
    @Getter
    private final freemind.model.services.NodeXmlSerializerService xmlSerializerService = new freemind.model.services.NodeXmlSerializerService(this);

    @Setter
    @Getter
    protected boolean folded;
    private int position = UNKNOWN_POSITION;

    @Getter
    private int vGap = VGAP;
    // hGap = Math.max(HGAP, gap);
    @Setter
    @Getter
    private int hGap = HGAP;
    /**
     * -- SETTER --
     *
     */
    @Setter
    @Getter
    private int shiftY = 0;

    protected List<MindMapNode> children;
    private MindMapNode preferredChild;

    @Getter
    private final FilterInfo filterInfo = new FilterInfo();

    private MindMapNode parent;
    /**
     * the edge which leads to this node, only root has none In future it has to
     * hold more than one view, maybe with a Vector in which the index specifies
     * the MapView which contains the NodeViews
     */
    @Setter
    @Getter
    private MindMapEdge edge;

    private static final boolean ALLOWSCHILDREN = true;
    @Setter
    @Getter
    private HistoryInformation historyInformation = null;
    @Setter
    @Getter
    private MindMap map = null;

    protected NodeAdapter(Object userObject, MindMap pMap) {
        this.map = pMap;
        contentService.setText((String) userObject);

        // create creation time:
        setHistoryInformation(new HistoryInformation());
    }

    @Override
    public String getText() {
        return contentService.getText();
    }

    @Override
    public final void setText(String text) {
        contentService.setText(text);
    }

    @Override
    public final String getXmlText() {
        return contentService.getXmlText();
    }

    @Override
    public final void setXmlText(String pXmlText) {
        contentService.setXmlText(pXmlText);
    }

    /* ************************************************************
     * ******** Notes *******
     * ************************************************************
     */

    @Override
    public final String getXmlNoteText() {
        return contentService.getXmlNoteText();
    }

    @Override
    public final String getNoteText() {
        return contentService.getNoteText();
    }

    @Override
    public final void setXmlNoteText(String pXmlNoteText) {
        contentService.setXmlNoteText(pXmlNoteText);
    }

    @Override
    public final void setNoteText(String pNoteText) {
        contentService.setNoteText(pNoteText);
    }

    @Override
    public String getPlainTextContent() {
        // MindMapNodeModel overrides this to convert HTML→plain.
        return contentService.getPlainTextContent();
    }

    @Override
    public String getShortText() {
        return contentService.getShortText();
    }

    public void setLink(String link) {
        if (link != null && link.startsWith("#")) {
            getMap().getLinkRegistry().registerLocalHyperlinkId(
                    link.substring(1));
        }
        this.link = link;
    }

    //
    // Interface MindMapNode
    //

    //
    // get/set methods
    //

    /**
     * Creates the TreePath recursively
     */
    public TreePath getPath() {
        var pathVector = new ArrayList<NodeAdapter>();
        TreePath treePath;
        this.addToPathVector(pathVector);
        treePath = new TreePath(pathVector.toArray());
        return treePath;
    }

    @Override
    public MindMapCloud getCloud() {
        return decorationsService.getCloud();
    }

    @Override
    public void setCloud(MindMapCloud cloud) {
        decorationsService.setCloud(cloud);
    }

    @Override
    public String getStyle() {
        return styleService.getStyle();
    }

    @Override
    public void setStyle(String style) {
        styleService.setStyle(style);
    }

    @Override
    public String getBareStyle() {
        return styleService.getBareStyle();
    }

    @Override
    public boolean hasStyle() {
        return styleService.hasStyle();
    }

    public void establishOwnFont() {
        styleService.establishOwnFont();
    }

    public void setBold(boolean bold) {
        styleService.setBold(bold);
    }

    public void toggleBold() {
        styleService.toggleBold();
    }

    public void setItalic(boolean italic) {
        styleService.setItalic(italic);
    }

    public void toggleItalic() {
        styleService.toggleItalic();
    }

    public void setStrikethrough(boolean strikethrough) {
        styleService.setStrikethrough(strikethrough);
    }

    public void toggleStrikethrough() {
        styleService.toggleStrikethrough();
    }

    @Override
    public boolean isBold() {
        return styleService.isBold();
    }

    @Override
    public boolean isItalic() {
        return styleService.isItalic();
    }

    @Override
    public boolean isStrikethrough() {
        return styleService.isStrikethrough();
    }

    @Override
    public boolean isUnderlined() {
        return styleService.isUnderlined();
    }

    public void setUnderlined(boolean underlined) {
        styleService.setUnderlined(underlined);
    }

    @Override
    public void setFontSize(int fontSize) {
        styleService.setFontSize(fontSize);
    }

    @Override
    public String getFontSize() {
        return styleService.getFontSize();
    }

    @Override
    public String getFontFamilyName() {
        return styleService.getFontFamilyName();
    }

    @Override
    public Color getColor() {
        return styleService.getColor();
    }

    @Override
    public void setColor(Color color) {
        styleService.setColor(color);
    }

    @Override
    public Color getBackgroundColor() {
        return styleService.getBackgroundColor();
    }

    @Override
    public void setBackgroundColor(Color color) {
        styleService.setBackgroundColor(color);
    }

    @Override
    public Font getFont() {
        return styleService.getFont();
    }

    @Override
    public void setFont(Font font) {
        styleService.setFont(font);
    }

    public MindMapNode getParentNode() {
        return parent;
    }

    @Override
    public List<NodeIcon> getIcons() {
        return decorationsService.getIcons();
    }

    @Override
    public void addIcon(NodeIcon icon, int position) {
        decorationsService.addIcon(icon, position);
    }

    @Override
    public int removeIcon(int position) {
        return decorationsService.removeIcon(position);
    }

    /**
     * True iff one of node's <i>strict</i> descendants is folded. A node N is
     * not its strict descendant - the fact that node itself is folded is not
     * sufficient to return true.
     */
    public boolean hasFoldedStrictDescendant() {

        for (ListIterator<NodeAdapter> e = childrenUnfolded(); e.hasNext(); ) {
            NodeAdapter child = e.next();
            if (child.isFolded() || child.hasFoldedStrictDescendant()) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return true, if one of its parents is folded. If itself is folded,
     * doesn't matter.
     */
    public boolean hasFoldedParents() {
        if (isRoot())
            return false;
        if (getParentNode().isFolded()) {
            return true;
        }
        return getParentNode().hasFoldedParents();
    }

    @Override
    public MindMapNode shallowCopy() {
        return xmlSerializerService.shallowCopy();
    }

    //
    // other
    //

    public MapFeedback getMapFeedback() {
        return getMap().getMapFeedback();
    }

    @Override
    public String toString() {
        return contentService.toString();
    }

    public boolean isDescendantOf(MindMapNode pParentNode) {
        if (this.isRoot())
            return false;
        else if (pParentNode == getParentNode())
            return true;
        else
            return getParentNode().isDescendantOf(pParentNode);
    }

    public boolean isRoot() {
        return (parent == null);
    }

    public boolean isDescendantOfOrEqual(MindMapNode pParentNode) {
        if (this == pParentNode) {
            return true;
        }
        return isDescendantOf(pParentNode);
    }

    public boolean hasChildren() {
        return children != null && !children.isEmpty();
    }

    public int getChildPosition(MindMapNode childNode) {
        int position = 0;
        for (ListIterator<MindMapNode> i = children.listIterator(); i.hasNext(); ++position) {
            if (i.next() == childNode) {
                return position;
            }
        }
        return -1;
    }

    public ListIterator childrenUnfolded() {
        return children != null ? children.listIterator()
                : Collections.EMPTY_LIST.listIterator();
    }

    public ListIterator<MindMapNode> sortedChildrenUnfolded() {
        if (children == null)
            return null;
        var sorted = new LinkedList<MindMapNode>(children);
        /*
         * Using this stable sort, we assure that the left nodes came in front
         * of the right ones.
         */
        sorted.sort(new Comparator<>() {

            public int compare(MindMapNode pO1, MindMapNode pO2) {
                return comp(pO2.isLeft(),
                        pO1.isLeft());
            }

            private int comp(boolean pLeft, boolean pLeft2) {
                if (pLeft == pLeft2) {
                    return 0;
                }
                if (pLeft) {
                    return 1;
                }
                return -1;
            }
        });
        return sorted.listIterator();
    }

    public ListIterator<MindMapNode> childrenFolded() {
        if (isFolded()) {
            return Collections.emptyListIterator();
        }
        return childrenUnfolded();
    }

    public List<MindMapNode> getChildren() {
        return Collections.unmodifiableList((children != null) ? children
                : Collections.emptyList());
    }

    //
    // Interface TreeNode
    //

    /**
     * AFAIK there is no way to get an enumeration out of a linked list. So this
     * exception must be thrown, or we can't implement TreeNode anymore (maybe
     * we shouldn't?)
     */
    public Enumeration children() {
        throw new UnsupportedOperationException(
                "Use childrenFolded or childrenUnfolded instead");
    }

    public boolean getAllowsChildren() {
        return ALLOWSCHILDREN;
    }

    public TreeNode getChildAt(int childIndex) {
        // fc, 11.12.2004: This is not understandable, that a child does not
        // exist if the parent is folded.
        // if (isFolded()) {
        // return null;
        // }
        return children.get(childIndex);
    }

    public int getChildCount() {
        return children == null ? 0 : children.size();
    }

    // (PN)
    // public int getChildCount() {
    // if (isFolded()) {
    // return 0;
    // }
    // return children.size();
    // }
    // // Daniel: ^ The name of this method is confusing. It does nto convey
    // // the meaning, at least not to me.

    public int getIndex(TreeNode node) {
        return children.indexOf((MindMapNode) node); // uses equals()
    }

    public TreeNode getParent() {
        return parent;
    }

    public boolean isLeaf() {
        return getChildCount() == 0;
    }

    // fc, 16.12.2003 left-right bug:
    public boolean isLeft() {
        if (getParent() != null && !getParentNode().isRoot()) {
            return getParentNode().isLeft();
        }
        if (position == UNKNOWN_POSITION && !isRoot()) {
            setLeft(getParentNode().isLeft());
        }
        return position == LEFT_POSITION;
    }

    public void setLeft(boolean isLeft) {
        position = isLeft ? LEFT_POSITION : RIGHT_POSITION;
        if (!isRoot()) {
            for (int i = 0; i < getChildCount(); i++) {
                final NodeAdapter child = (NodeAdapter) getChildAt(i);
                child.position = position;
            }
        }
    }

    public boolean isNewChildLeft() {
        if (!isRoot()) {
            return isLeft();
        }
        int rightChildrenCount = 0;
        for (int i = 0; i < getChildCount(); i++) {
            if (!((MindMapNode) getChildAt(i)).isLeft())
                rightChildrenCount++;
            if (rightChildrenCount > getChildCount() / 2) {
                return true;
            }
        }
        return false;
    }

    //
    // Interface MutableTreeNode
    //

    // do all remove methods have to work recursively to make the
    // Garbage Collection work (Nodes in removed Sub-Trees reference each
    // other)?

    public void insert(MutableTreeNode child, int index) {
        log.trace("Insert at {} the node {}", index, child);
        final MindMapNode childNode = (MindMapNode) child;
        if (index < 0) { // add to the end (used in xml load) (PN)
            index = getChildCount();
            children.add(index, childNode);
        } else { // mind preferred child :-)
            children.add(index, childNode);
            preferredChild = childNode;
        }
        child.setParent(this);
        recursiveCallAddChildren(this, childNode);
    }

    public void remove(int index) {
        MutableTreeNode node = children.get(index);
        remove(node);
    }

    public void remove(MutableTreeNode node) {
        if (node == this.preferredChild) { // mind preferred child :-) (PN)
            int index = children.indexOf(node);
            if (children.size() > index + 1) {
                this.preferredChild = children.get(index + 1);
            } else {
                this.preferredChild = (index > 0) ? children
                        .get(index - 1) : null;
            }
        }
        node.setParent(null);
        children.remove(node);
        // call remove child hook after removal.
        recursiveCallRemoveChildren(this, (MindMapNode) node, this);
    }

    private void recursiveCallAddChildren(MindMapNode node,
                                          MindMapNode addedChild) {
        // Tell any node hooks that the node is added:
        if (node instanceof MindMapNode) {
            for (var hook : node.getActivatedHooks()) {
                if (addedChild.getParentNode() == node) {
                    hook.onAddChild(addedChild);
                }
                hook.onAddChildren(addedChild);
            }
        }
        if (!node.isRoot() && node.getParentNode() != null)
            recursiveCallAddChildren(node.getParentNode(), addedChild);
    }

    /**
     * @param oldDad the last dad node had.
     */
    private void recursiveCallRemoveChildren(MindMapNode node,
                                             MindMapNode removedChild, MindMapNode oldDad) {
        for (var hook : node.getActivatedHooks()) {
            if (removedChild.getParentNode() == node) {
                hook.onRemoveChild(removedChild);
            }
            hook.onRemoveChildren(removedChild, oldDad);
        }
        if (!node.isRoot() && node.getParentNode() != null)
            recursiveCallRemoveChildren(node.getParentNode(), removedChild,
                    oldDad);
    }

    public void removeFromParent() {
        parent.remove(this);
    }

    public void setParent(MutableTreeNode newParent) {
        parent = (MindMapNode) newParent;
    }

    public void setParent(MindMapNode newParent) {
        parent = newParent;
    }

    @Override
    public void setUserObject(Object object) {
        contentService.setText((String) object);
    }

    // //////////////
    // Private methods. Internal Implementation
    // ////////////

    /**
     * Recursive Method for getPath()
     */
    private void addToPathVector(List<NodeAdapter> pathVector) {
        pathVector.add(0, this); // Add myself to beginning of Vector
        if (parent != null) {
            ((NodeAdapter) parent).addToPathVector(pathVector);
        }
    }

    public int getNodeLevel() { // for cursor navigation within a level (PN)
        int level = 0;
        MindMapNode parent;
        for (parent = this; !parent.isRoot(); parent = parent.getParentNode()) {
            if (parent.isVisible()) {
                level++;
            }
        }
        return level;
    }

    @Override
    public PermanentNodeHook addHook(PermanentNodeHook hook) {
        return hooksService.addHook(hook);
    }

    @Override
    public void invokeHook(NodeHook hook) {
        hooksService.invokeHook(hook);
    }

    @Override
    public List<PermanentNodeHook> getHooks() {
        return hooksService.getHooks();
    }

    @Override
    public Collection<PermanentNodeHook> getActivatedHooks() {
        return hooksService.getActivatedHooks();
    }

    @Override
    public void removeHook(PermanentNodeHook hook) {
        hooksService.removeHook(hook);
    }

    @Override
    public void removeAllHooks() {
        hooksService.removeAllHooks();
    }

    public SortedMap<String, String> getToolTip() {
        return tooltipService.getToolTip();
    }

    public void setToolTip(String key, String string) {
        tooltipService.setToolTip(key, string);
    }

    @Override
    public Element save(Writer writer, Document doc, MindMapLinkRegistry registry,
                         boolean saveInvisible, boolean saveChildren) throws IOException {
        return xmlSerializerService.save(writer, doc, registry, saveInvisible, saveChildren);
    }

    public static String convertToEncodedContent(String xmlText) {
        return freemind.model.services.NodeXmlSerializerService.convertToEncodedContent(xmlText);
    }

    public boolean hasExactlyOneVisibleChild() {
        int count = 0;
        for (ListIterator<MindMapNode> i = childrenUnfolded(); i.hasNext(); ) {
            if (i.next().isVisible())
                count++;
            if (count == 2)
                return false;
        }
        return count == 1;
    }

    public boolean hasVisibleChilds() {
        for (ListIterator<MindMapNode> i = childrenUnfolded(); i.hasNext(); ) {
            if (i.next().isVisible())
                return true;
        }
        return false;
    }

    public int calcShiftY() {
        try {
            // return 0;
            return shiftY + (parent.hasExactlyOneVisibleChild() ? SHIFT : 0);
        } catch (NullPointerException e) {
            return 0;
        }

    }

    public void setAdditionalInfo(String info) {
    }

    public String getAdditionalInfo() {
        return null;
    }

    @Override
    public void setStateIcon(String key, ImageIcon icon) {
        decorationsService.setStateIcon(key, icon);
    }

    @Override
    public Map<String, ImageIcon> getStateIcons() {
        return decorationsService.getStateIcons();
    }

    public void setVGap(int gap) {
        vGap = Math.max(gap, 0);
    }

    public boolean isVisible() {
        Filter filter = getMap().getFilter();
        return filter == null || filter.isVisible(this);
    }

    final EventListenerList listenerList = new EventListenerList();

    public void addTreeModelListener(TreeModelListener l) {
        listenerList.add(TreeModelListener.class, l);
    }

    public void removeTreeModelListener(TreeModelListener l) {
        listenerList.remove(TreeModelListener.class, l);
    }

    public EventListenerList getListeners() {
        return listenerList;
    }

    //
    // Attributes
    //

    @Override
    public List<String> getAttributeKeyList() {
        return attributesService.getAttributeKeyList();
    }

    @Override
    public List<Attribute> getAttributes() {
        return attributesService.getAttributes();
    }

    @Override
    public int getAttributeTableLength() {
        return attributesService.getAttributeTableLength();
    }

    @Override
    public Attribute getAttribute(int position) {
        return attributesService.getAttribute(position);
    }

    @Override
    public String getAttribute(String key) {
        return attributesService.getAttribute(key);
    }

    @Override
    public int getAttributePosition(String key) {
        return attributesService.getAttributePosition(key);
    }

    @Override
    public void setAttribute(int position, Attribute attribute) {
        attributesService.setAttribute(position, attribute);
    }

    @Override
    public int addAttribute(Attribute attribute) {
        return attributesService.addAttribute(attribute);
    }

    @Override
    public void insertAttribute(int position, Attribute attribute) {
        attributesService.insertAttribute(position, attribute);
    }

    @Override
    public void removeAttribute(int position) {
        attributesService.removeAttribute(position);
    }

    public void checkAttributePosition(int position) {
        attributesService.checkAttributePosition(position);
    }
}
