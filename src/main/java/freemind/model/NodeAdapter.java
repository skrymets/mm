package freemind.model;

import freemind.controller.Controller;
import freemind.controller.filter.Filter;
import freemind.controller.filter.FilterInfo;
import freemind.extensions.DontSaveMarker;
import freemind.extensions.NodeHook;
import freemind.extensions.PermanentNodeHook;
import freemind.main.*;
import freemind.modes.*;
import freemind.modes.attributes.Attribute;
import freemind.preferences.FreemindPropertyListener;
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
import java.awt.font.TextAttribute;
import java.io.*;
import java.net.URL;
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

    private HashSet<PermanentNodeHook> activatedHooks;
    private List<PermanentNodeHook> hooks;
    protected Object userObject = "no text";
    private String xmlText = "no text";
    @Getter
    private String link = null; // Change this to vector in future for full
    // graph support
    private static final String TOOLTIP_PREVIEW_KEY = "preview";
    private TreeMap<String, String> toolTip = null; // lazy, fc, 30.6.2005

    // these Attributes have default values, so it can be useful to directly
    // access them in
    // the save() method instead of using getXXX(). This way the stored file is
    // smaller and looks better.
    // (if the default is used, it is not stored) Look at mindmapmode for an
    // example.
    @Setter
    protected String style;
    /**
     * stores the icons associated with this node.
     */
    protected List<MindIcon> icons = null; // lazy, fc, 30.6.2005

    protected TreeMap<String, ImageIcon> stateIcons = null; // lazy, fc,
    // 30.6.2005
    // /**stores the label associated with this node:*/
    // protected String mLabel;
    /**
     * parameters of an eventually associated cloud
     */
    @Getter
    protected MindMapCloud cloud;

    /**
     * -- GETTER --
     *  The Foreground/Font Color
     */
    @Setter
    @Getter
    protected Color color;
    // fc, 24.2.2004: background color:
    @Setter
    @Getter
    protected Color backgroundColor;
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

    @Setter
    @Getter
    protected Font font;
    // not implemented
    @Setter
    @Getter
    protected boolean underlined = false;

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
    /**
     */
    @Setter
    @Getter
    private MindMap map = null;
    private String noteText;
    private String xmlNoteText;
    private static FreemindPropertyListener sSaveIdPropertyChangeListener;
    private static boolean sSaveOnlyIntrinsicallyNeededIds = false;
    private List<Attribute> mAttributeVector = null;

    protected NodeAdapter(Object userObject, MindMap pMap) {
        this.map = pMap;
        setText((String) userObject);
        hooks = null; // lazy, fc, 30.6.2005.
        activatedHooks = null; // lazy, fc, 30.6.2005

        // create creation time:
        setHistoryInformation(new HistoryInformation());
        if (sSaveIdPropertyChangeListener == null) {
            sSaveIdPropertyChangeListener = (propertyName, newValue, oldValue) -> {
                if (propertyName.equals(FreeMindCommon.SAVE_ONLY_INTRISICALLY_NEEDED_IDS)) {
                    sSaveOnlyIntrinsicallyNeededIds = Boolean.valueOf(newValue).booleanValue();
                }
            };
            Controller.addPropertyChangeListenerAndPropagate(sSaveIdPropertyChangeListener);
        }

    }

    public String getText() {
        String string = "";
        if (userObject != null) {
            string = userObject.toString();
        }
        return string;
    }

    public final void setText(String text) {
        if (text == null) {
            userObject = null;
            xmlText = null;
            return;
        }
        userObject = HtmlTools.makeValidXml(text);
        xmlText = HtmlTools.getInstance().toXhtml((String) userObject);
    }

    public final String getXmlText() {
        return xmlText;
    }

    public final void setXmlText(String pXmlText) {
        this.xmlText = HtmlTools.makeValidXml(pXmlText);
        userObject = HtmlTools.getInstance().toHtml(xmlText);
    }

    /* ************************************************************
     * ******** Notes *******
     * ************************************************************
     */

    public final String getXmlNoteText() {
        return xmlNoteText;
    }

    public final String getNoteText() {
        // log.info("Note html: " + noteText);
        return noteText;
    }

    public final void setXmlNoteText(String pXmlNoteText) {
        if (pXmlNoteText == null) {
            xmlNoteText = null;
            noteText = null;
            return;
        }
        this.xmlNoteText = HtmlTools.makeValidXml(pXmlNoteText);
        noteText = HtmlTools.getInstance().toHtml(xmlNoteText);
    }

    public final void setNoteText(String pNoteText) {
        if (pNoteText == null) {
            xmlNoteText = null;
            noteText = null;
            return;
        }
        this.noteText = HtmlTools.makeValidXml(pNoteText);
        this.xmlNoteText = HtmlTools.getInstance().toXhtml(noteText);
    }

    public String getPlainTextContent() {
        // Redefined in MindMapNodeModel.
        return toString();
    }

    public String getShortText(ModeController controller) {
        String adaptedText = getPlainTextContent();
        // adaptedText = adaptedText.replaceAll("<html>", "");
        if (adaptedText.length() > 40)
            adaptedText = adaptedText.substring(0, 40) + " ...";
        return adaptedText;
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
        List<NodeAdapter> pathVector = new ArrayList<>();
        TreePath treePath;
        this.addToPathVector(pathVector);
        treePath = new TreePath(pathVector.toArray());
        return treePath;
    }

    public void setCloud(MindMapCloud cloud) {
        // Take care to keep the calculated iterative levels consistent
        if (cloud != null && this.cloud == null) {
            changeChildCloudIterativeLevels(1);
        } else if (cloud == null && this.cloud != null) {
            changeChildCloudIterativeLevels(-1);
        }
        this.cloud = cloud;
    }

    /**
     * Correct iterative level values of children
     */
    private void changeChildCloudIterativeLevels(int deltaLevel) {
        for (ListIterator<NodeAdapter> e = childrenUnfolded(); e.hasNext(); ) {
            NodeAdapter childNode = e.next();
            MindMapCloud childCloud = childNode.getCloud();
            if (childCloud != null) {
                childCloud.changeIterativeLevel(deltaLevel);
            }
            childNode.changeChildCloudIterativeLevels(deltaLevel);
        }
    }


    public String getBareStyle() {
        return style;
    }

    /**
     * A Node-Style like MindMapNode.STYLE_FORK or MindMapNode.STYLE_BUBBLE
     */
    public String getStyle() {
        String returnedString = style; /* Style string returned */
        if (style == null) {
            if (this.isRoot()) {
                returnedString = getMapFeedback().getProperty(
                        FreeMind.RESOURCES_ROOT_NODE_STYLE);
            } else {
                String stdstyle = getMapFeedback().getProperty(
                        FreeMind.RESOURCES_NODE_STYLE);
                if (stdstyle.equals(MindMapNode.STYLE_AS_PARENT)) {
                    returnedString = getParentNode().getStyle();
                } else {
                    returnedString = stdstyle;
                }
            }
        } else if (this.isRoot() && style.equals(MindMapNode.STYLE_AS_PARENT)) {
            returnedString = getMapFeedback().getProperty(
                    FreeMind.RESOURCES_ROOT_NODE_STYLE);
        } else if (style.equals(MindMapNode.STYLE_AS_PARENT)) {
            returnedString = getParentNode().getStyle();
        }

        // Handle the combined node style
        if (returnedString.equals(MindMapNode.STYLE_COMBINED)) {
            if (this.isFolded()) {
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

    // ////
    // The set methods. I'm not sure if they should be here or in the
    // implementing class.
    // ///

    //
    // font handling
    //

    // Remark to setBold and setItalic implementation
    //
    // Using deriveFont() is a bad idea, because it does not really choose
    // the appropriate face. For example, instead of choosing face
    // "Arial Bold", it derives the bold face from "Arial".

    // Node holds font only in the case that the font is not default.

    public void establishOwnFont() {
        font = (font != null) ? font : getMapFeedback().getDefaultFont();
    }

    public void setBold(boolean bold) {
        if (bold != isBold()) {
            toggleBold();
        }
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
        if (attributes.containsKey(TextAttribute.STRIKETHROUGH) && attributes.get(TextAttribute.STRIKETHROUGH) == TextAttribute.STRIKETHROUGH_ON) {
            attributes.remove(TextAttribute.STRIKETHROUGH);
        } else {
            attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
        }
        setFont(new Font(attributes));
    }

    public void toggleBold() {
        establishOwnFont();
        setFont(getMapFeedback().getFontThroughMap(font.deriveFont(font.getStyle() ^ Font.BOLD)));
    }

    public void setItalic(boolean italic) {
        if (italic != isItalic()) {
            toggleItalic();
        }
    }

    public void toggleItalic() {
        establishOwnFont();
        setFont(getMapFeedback().getFontThroughMap(font.deriveFont(font.getStyle() ^ Font.ITALIC)));
    }

    public MindMapNode getParentNode() {
        return parent;
    }

    public void setFontSize(int fontSize) {
        establishOwnFont();
        setFont(getMapFeedback().getFontThroughMap(
                font.deriveFont((float) fontSize)));
    }

    public String getFontSize() {
        if (getFont() != null) {
            return Integer.toString(getFont().getSize());
        } else {
            return getMapFeedback().getProperty("defaultfontsize");
        }
    }

    public String getFontFamilyName() {
        if (getFont() != null) {
            return getFont().getFamily();
        } else {
            return getMapFeedback().getProperty("defaultfont");
        }
    }

    public boolean isBold() {
        return font != null ? font.isBold() : false;
    }

    public boolean isItalic() {
        return font != null ? font.isItalic() : false;
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

    // fc, 24.9.2003:
    public List<MindIcon> getIcons() {
        if (icons == null)
            return Collections.emptyList();
        return icons;
    }

    public void addIcon(MindIcon _icon, int position) {
        createIcons();
        if (position == MindIcon.LAST) {
            icons.add(_icon);
        } else {
            icons.add(position, _icon);
        }
    }

    /**
     * @return returns the number of remaining icons.
     */
    public int removeIcon(int position) {
        createIcons();
        if (position == MindIcon.LAST) {
            position = icons.size() - 1;
        }
        icons.remove(position);
        int returnSize = icons.size();
        if (returnSize == 0) {
            icons = null;
        }
        return returnSize;
    }

    // end, fc, 24.9.2003

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

    public MindMapNode shallowCopy() {
        try {
            // get XML from me.
            StringWriter writer = new StringWriter();
            this.save(writer, this.getMap().getLinkRegistry(), true, false);
            String result = writer.toString();
            HashMap<String, NodeAdapter> IDToTarget = new HashMap<>();
            MindMapNode copy = getMap().createNodeTreeFromXml(new StringReader(result), IDToTarget);
            copy.setFolded(false);
            return copy;
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }

    //
    // other
    //

    /**
     */
    public MapFeedback getMapFeedback() {
        return getMap().getMapFeedback();
    }

    public String toString() {
        return getText();
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

    /*
     * (non-Javadoc)
     *
     * @see freemind.model.MindMapNode#sortedChildrenUnfolded()
     */
    public ListIterator<MindMapNode> sortedChildrenUnfolded() {
        if (children == null)
            return null;
        LinkedList<MindMapNode> sorted = new LinkedList<>(children);
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
            for (PermanentNodeHook hook : node.getActivatedHooks()) {
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
        for (PermanentNodeHook hook : node.getActivatedHooks()) {
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

    public void setUserObject(Object object) {
        setText((String) object);
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

    /*
     * (non-Javadoc)
     *
     * @see freemind.model.MindMapNode#addHook(freemind.modes.NodeHook)
     */
    public PermanentNodeHook addHook(PermanentNodeHook hook) {
        // add then
        if (hook == null)
            throw new IllegalArgumentException("Added null hook.");
        createHooks();
        hooks.add(hook);
        return hook;
    }

    public void invokeHook(NodeHook hook) {
        // initialize:
        hook.startupMapHook();
        // the main invocation:
        hook.setNode(this);
        try {
            hook.invoke(this);
        } catch (Exception e) {
            // FIXME: Do something special here, but in any case, do not add the
            // hook
            // to the activatedHooks:
            log.error(e.getLocalizedMessage(), e);
            return;
        }
        if (hook instanceof PermanentNodeHook) {
            createActivatedHooks();
            activatedHooks.add((PermanentNodeHook) hook);
        } else {
            // end of its short life:
            hook.shutdownMapHook();
        }
    }

    private void createActivatedHooks() {
        if (activatedHooks == null) {
            activatedHooks = new HashSet<>();
        }
    }

    private void createToolTip() {
        if (toolTip == null) {
            toolTip = new TreeMap<>();
        }
    }

    private void createHooks() {
        if (hooks == null) {
            hooks = new ArrayList<>();
        }
    }

    private void createStateIcons() {
        if (stateIcons == null) {
            stateIcons = new TreeMap<>();
        }
    }

    private void createIcons() {
        if (icons == null) {
            icons = new ArrayList<>();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see freemind.model.MindMapNode#getHooks()
     */
    public List<PermanentNodeHook> getHooks() {
        if (hooks == null)
            return Collections.emptyList();
        return Collections.unmodifiableList(hooks);
    }

    /*
     * (non-Javadoc)
     *
     * @see freemind.model.MindMapNode#getActivatedHooks()
     */
    public Collection<PermanentNodeHook> getActivatedHooks() {
        if (activatedHooks == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableCollection(activatedHooks);
    }

    /*
     * (non-Javadoc)
     *
     * @see freemind.model.MindMapNode#removeHook(freemind.modes.NodeHook)
     */
    public void removeHook(PermanentNodeHook hook) {
        // the order is crucial here: the shutdown method should be able to
        // perform "nodeChanged"
        // calls without having its own updateNodeHook method to be called
        // again.
        String name = hook.getName();
        createActivatedHooks();
        if (activatedHooks.contains(hook)) {
            activatedHooks.remove(hook);
            if (activatedHooks.isEmpty()) {
                activatedHooks = null;
            }
            hook.shutdownMapHook();
        }
        createHooks();
        hooks.remove(hook);
        if (hooks.isEmpty())
            hooks = null;
        log.trace("Removed hook {} at {}.", name, hook);
    }

    public void removeAllHooks() {
        int timeout = getHooks().size() * 2;
        while (!getHooks().isEmpty() && timeout-- > 0) {
            PermanentNodeHook hook = getHooks().get(0);
            try {
                removeHook(hook);
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
    }

    /**
     *
     */
    public SortedMap<String, String> getToolTip() {
        boolean toolTipChanged = false;
        TreeMap<String, String> result = toolTip;
        if (result == null)
            result = new TreeMap<>();
        // add preview to other map, if appropriate:
        String link = getLink();
        // replace jump mark
        if (link != null && link.matches(".*\\" + FreeMindCommon.FREEMIND_FILE_EXTENSION + "(#.*)?")) {
            link = link.replaceFirst("#.*?$", "");
        }
        if (link != null && link.endsWith(FreeMindCommon.FREEMIND_FILE_EXTENSION)) {
            String linkHtmlPart = "alt=\"" + link + "\"";
            boolean addIt = true;
            // this should be done only once per link, so we have to prevent doing that every time again.
            if (result.containsKey(TOOLTIP_PREVIEW_KEY)) {
                // check, if the contained link belongs to the same file (ie. hasn't change in between)
                String prev = result.get(TOOLTIP_PREVIEW_KEY);
                if (prev != null && prev.contains(linkHtmlPart)) {
                    addIt = false;
                }
            }
            if (addIt) {
                try {
                    File mmFile = Tools.urlToFile(new URL(getMap().getURL(), link));
                    String thumbnailFileName = Resources.getInstance().createThumbnailFileName(mmFile);
                    if (new File(thumbnailFileName).exists()) {
                        URL thumbUrl = Tools.fileToUrl(new File(thumbnailFileName));
                        String imgHtml = "<img src=\"" + thumbUrl + "\" " + linkHtmlPart + "/>";
                        log.info("Adding new tooltip: {}", imgHtml);
                        result.put(TOOLTIP_PREVIEW_KEY, imgHtml);
                        toolTipChanged = true;
                    }
                } catch (Exception e) {
                    log.error(e.getLocalizedMessage(), e);
                }
            }
        } else {
            if (result.containsKey(TOOLTIP_PREVIEW_KEY)) {
                result.remove(TOOLTIP_PREVIEW_KEY);
                toolTipChanged = true;
            }
        }
        if (toolTipChanged) {
            // write back, if changed
            if (result.isEmpty()) {
                toolTip = null;
            } else {
                toolTip = result;
            }
        }
        return Collections.unmodifiableSortedMap(result);
    }

    /**
     *
     */
    public void setToolTip(String key, String string) {
        createToolTip();
        if (string == null) {
            toolTip.remove(key);
            if (toolTip.isEmpty())
                toolTip = null;
        } else {
            toolTip.put(key, string);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see freemind.model.MindMapNode#getNodeId()
     */
    public String getObjectId(ModeController controller) {
        return controller.getNodeID(this);
    }

    public XMLElement save(Writer writer, MindMapLinkRegistry registry,
                           boolean saveInvisible, boolean saveChildren) throws IOException {
        // pre save event to save all contents of the node:
        getMapFeedback().firePreSaveEvent(this);
        XMLElement node = new XMLElement();

        // if (!isNodeClassToBeSaved()) {
        node.setName(XMLElementAdapter.XML_NODE);
        // } else {
        // node.setName(XMLElementAdapter.XML_NODE_CLASS_PREFIX
        // + this.getClass().getName());
        // }

        /* fc, 12.6.2005: XML must not contain any zero characters. */
        String text = this.toString().replace('\0', ' ');
        if (!HtmlTools.isHtmlNode(text)) {
            node.setAttribute(XMLElementAdapter.XML_NODE_TEXT, text);
        } else {
            // save <content> tag:
            XMLElement htmlElement = new XMLElement();
            htmlElement.setName(XMLElementAdapter.XML_NODE_XHTML_CONTENT_TAG);
            htmlElement.setAttribute(XMLElementAdapter.XML_NODE_XHTML_TYPE_TAG,
                    XMLElementAdapter.XML_NODE_XHTML_TYPE_NODE);
            htmlElement
                    .setEncodedContent(convertToEncodedContent(getXmlText()));
            node.addChild(htmlElement);
        }
        if (getXmlNoteText() != null) {
            XMLElement htmlElement = new XMLElement();
            htmlElement.setName(XMLElementAdapter.XML_NODE_XHTML_CONTENT_TAG);
            htmlElement.setAttribute(XMLElementAdapter.XML_NODE_XHTML_TYPE_TAG,
                    XMLElementAdapter.XML_NODE_XHTML_TYPE_NOTE);
            htmlElement
                    .setEncodedContent(convertToEncodedContent(getXmlNoteText()));
            node.addChild(htmlElement);

        }
        // save additional info:
        if (getAdditionalInfo() != null) {
            node.setAttribute(XMLElementAdapter.XML_NODE_ENCRYPTED_CONTENT,
                    getAdditionalInfo());
        }
        // ((MindMapEdgeModel)getEdge()).save(doc,node);

        XMLElement edge = (getEdge()).save();
        if (edge != null) {
            node.addChild(edge);
        }

        if (getCloud() != null) {
            XMLElement cloud = (getCloud()).save();
            node.addChild(cloud);
        }

        List<MindMapLink> linkVector = registry.getAllLinksFromMe(this);
        for (MindMapLink mapLink : linkVector) {
            if (mapLink instanceof ArrowLinkAdapter) {
                XMLElement arrowLinkElement = ((ArrowLinkAdapter) mapLink).save();
                node.addChild(arrowLinkElement);
            }
        }

        // virtual link targets:
        List<MindMapLink> targetVector = registry.getAllLinksIntoMe(this);
        for (MindMapLink mindMapLink : targetVector) {
            if (mindMapLink instanceof ArrowLinkAdapter) {
                XMLElement arrowLinkTargetElement = ((ArrowLinkAdapter) mindMapLink).createArrowLinkTarget(registry).save();
                node.addChild(arrowLinkTargetElement);
            }
        }

        if (isFolded()) {
            node.setAttribute("FOLDED", "true");
        }

        // fc, 17.12.2003: Remove the left/right bug.
        // VVV save if and only if parent is root.
        if (!(isRoot()) && (getParentNode().isRoot())) {
            node.setAttribute("POSITION", isLeft() ? "left" : "right");
        }

        // the id is used, if there is a local hyperlink pointing to me or a
        // real link.
        String label = registry.getLabel(this);
        if (!sSaveOnlyIntrinsicallyNeededIds
                || (registry.isTargetOfLocalHyperlinks(label) || (!registry
                .getAllLinksIntoMe(this).isEmpty()))) {
            if (label != null) {
                node.setAttribute("ID", label);
            }
        }
        if (color != null) {
            node.setAttribute("COLOR", Tools.colorToXml(getColor()));
        }

        // new background color.
        if (getBackgroundColor() != null) {
            node.setAttribute("BACKGROUND_COLOR",
                    Tools.colorToXml(getBackgroundColor()));
        }

        if (style != null) {
            node.setAttribute("STYLE", this.getStyle());
        }
        // ^ Here cannot be just getStyle() without super. This is because
        // getStyle's style depends on folded / unfolded. For example, when
        // real style is fork and node is folded, getStyle returns
        // MindMapNode.STYLE_BUBBLE, which is not what we want to save.

        // layout
        if (vGap != VGAP) {
            node.setAttribute("VGAP", Integer.toString(vGap));
        }
        if (hGap != HGAP) {
            node.setAttribute("HGAP", Integer.toString(hGap));
        }
        if (shiftY != 0) {
            node.setAttribute("VSHIFT", Integer.toString(shiftY));
        }
        // link
        if (getLink() != null) {
            node.setAttribute("LINK", getLink());
        }

        // history information, fc, 11.4.2005
        if (historyInformation != null) {
            node.setAttribute(XMLElementAdapter.XML_NODE_HISTORY_CREATED_AT,
                    Tools.dateToString(getHistoryInformation().getCreatedAt()));
            node.setAttribute(
                    XMLElementAdapter.XML_NODE_HISTORY_LAST_MODIFIED_AT, Tools
                            .dateToString(getHistoryInformation()
                                    .getLastModifiedAt()));
        }
        // font
        if (font != null) {
            XMLElement fontElement = new XMLElement();
            fontElement.setName("font");

            if (font != null) {
                fontElement.setAttribute("NAME", font.getFamily());
            }
            if (font.getSize() != 0) {
                fontElement.setAttribute("SIZE",
                        Integer.toString(font.getSize()));
            }
            if (isBold()) {
                fontElement.setAttribute("BOLD", "true");
            }
            if (isStrikethrough()) {
                fontElement.setAttribute("STRIKETHROUGH", "true");
            }
            if (isItalic()) {
                fontElement.setAttribute("ITALIC", "true");
            }
            if (isUnderlined()) {
                fontElement.setAttribute("UNDERLINE", "true");
            }
            node.addChild(fontElement);
        }
        for (int i = 0; i < getIcons().size(); ++i) {
            XMLElement iconElement = new XMLElement();
            iconElement.setName("icon");
            iconElement.setAttribute("BUILTIN",
                    getIcons().get(i).getName());
            node.addChild(iconElement);
        }

        for (PermanentNodeHook permHook : getActivatedHooks()) {
            if (permHook instanceof DontSaveMarker) {
                continue;
            }
            XMLElement hookElement = new XMLElement();
            hookElement.setName("hook");
            permHook.save(hookElement);
            node.addChild(hookElement);
        }
        if (mAttributeVector != null) {
            for (Attribute attribute : mAttributeVector) {
                XMLElement attributeElement = new XMLElement();
                attributeElement.setName(XMLElementAdapter.XML_NODE_ATTRIBUTE);
                Attribute attr = attribute;
                attributeElement.setAttribute("NAME", attr.getName());
                attributeElement.setAttribute("VALUE", attr.getValue());
                node.addChild(attributeElement);
            }
        }

        if (saveChildren && childrenUnfolded().hasNext()) {
            node.writeWithoutClosingTag(writer);
            // recursive
            saveChildren(writer, registry, this, saveInvisible);
            node.writeClosingTag(writer);
        } else {
            node.write(writer);
        }
        return node;
    }

    public static String convertToEncodedContent(String xmlText2) {
        String replace = HtmlTools.makeValidXml(xmlText2);
        return HtmlTools.unicodeToHTMLUnicodeEntity(replace, true);
    }

    private void saveChildren(Writer writer, MindMapLinkRegistry registry,
                              NodeAdapter node, boolean saveHidden) throws IOException {
        for (ListIterator<NodeAdapter> e = node.childrenUnfolded(); e.hasNext(); ) {
            NodeAdapter child = e.next();
            if (saveHidden || child.isVisible())
                child.save(writer, registry, saveHidden, true);
            else
                saveChildren(writer, registry, child, saveHidden);
        }
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

    /**
     *
     */

    public void setAdditionalInfo(String info) {
    }

    public String getAdditionalInfo() {
        return null;
    }

    /**
     * This method must be synchronized as the TreeMap isn't.
     */
    public synchronized void setStateIcon(String key, ImageIcon icon) {
        // log.info("Set state of key:"+key+", icon "+icon);
        createStateIcons();
        if (icon != null) {
            stateIcons.put(key, icon);
        } else stateIcons.remove(key);
        if (stateIcons.isEmpty())
            stateIcons = null;
    }

    public Map<String, ImageIcon> getStateIcons() {
        if (stateIcons == null)
            return Collections.emptyMap();
        return Collections.unmodifiableSortedMap(stateIcons);
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
        if (mAttributeVector == null) {
            return Collections.emptyList();
        }
        List<String> returnValue = new ArrayList<>();
        for (Attribute attr : mAttributeVector) {
            returnValue.add(attr.getName());
        }
        return returnValue;
    }

    @Override
    public List<Attribute> getAttributes() {
        if (mAttributeVector == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(mAttributeVector);
    }

    @Override
    public int getAttributeTableLength() {
        if (mAttributeVector == null) {
            return 0;
        }
        return mAttributeVector.size();
    }

    @Override
    public Attribute getAttribute(int pPosition) {
        checkAttributePosition(pPosition);
        return new Attribute(getAttributeVector().get(pPosition));
    }

    /**
     */
    public void checkAttributePosition(int pPosition) {
        if (mAttributeVector == null || getAttributeTableLength() <= pPosition || pPosition < 0) {
            throw new IllegalArgumentException("Attribute position out of range: " + pPosition);
        }
    }

    @Override
    public String getAttribute(String pKey) {
        if (mAttributeVector == null) {
            return null;
        }
        for (Attribute attr : mAttributeVector) {
            if (Objects.equals(attr.getName(), pKey)) {
                return attr.getValue();
            }
        }
        return null;
    }

    @Override
    public int getAttributePosition(String pKey) {
        if (mAttributeVector == null) {
            return -1;
        }
        int index = 0;
        for (Attribute attr : mAttributeVector) {
            if (Objects.equals(attr.getName(), pKey)) {
                return index;
            }
            index++;
        }
        return -1;
    }

    @Override
    public void setAttribute(int pPosition, Attribute pAttribute) {
        checkAttributePosition(pPosition);
        mAttributeVector.set(pPosition, pAttribute);
    }

    /* (non-Javadoc)
     * @see freemind.model.MindMapNode#addAttribute(freemind.modes.attributes.Attribute)
     */
    @Override
    public int addAttribute(Attribute pAttribute) {
        getAttributeVector().add(pAttribute);
        return getAttributeVector().indexOf(pAttribute);
    }

    /* (non-Javadoc)
     * @see freemind.model.MindMapNode#insertAttribute(int, freemind.modes.attributes.Attribute)
     */
    @Override
    public void insertAttribute(int pPosition, Attribute pAttribute) {
        checkAttributePosition(pPosition);
        getAttributeVector().add(pPosition, pAttribute);
    }

    @Override
    public void removeAttribute(int pPosition) {
        checkAttributePosition(pPosition);
        mAttributeVector.remove(pPosition);
    }

    private List<Attribute> getAttributeVector() {
        if (mAttributeVector == null) {
            mAttributeVector = new ArrayList<>();
        }
        return mAttributeVector;
    }
}
