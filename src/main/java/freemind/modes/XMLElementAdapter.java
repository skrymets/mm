package freemind.modes;

import freemind.extensions.PermanentNodeHook;
import freemind.extensions.PermanentNodeHookSubstituteUnknown;
import freemind.main.*;
import freemind.model.*;
import freemind.modes.attributes.Attribute;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Builds FreeMind model objects (nodes, edges, clouds, etc.) from a DOM tree.
 * Replaces the former XMLElement-subclass approach where model objects were built
 * via parser callbacks (setName/setAttribute/addChild). Now uses DOM tree walking.
 */
@Slf4j
public class XMLElementAdapter {

    @Getter
    private NodeAdapter mapChild = null;

    public static final String XML_NODE_TEXT = "TEXT";
    public static final String XML_NODE = "node";
    public static final String XML_NODE_ATTRIBUTE = "attribute";
    public static final String XML_NODE_ATTRIBUTE_LAYOUT = "attribute_layout";
    public static final String XML_NODE_ATTRIBUTE_REGISTRY = "attribute_registry";
    public static final String XML_NODE_REGISTERED_ATTRIBUTE_NAME = "attribute_name";
    public static final String XML_NODE_REGISTERED_ATTRIBUTE_VALUE = "attribute_value";
    public static final String XML_NODE_CLASS = "AA_NODE_CLASS";
    public static final String XML_NODE_ADDITIONAL_INFO = "ADDITIONAL_INFO";
    public static final String XML_NODE_ENCRYPTED_CONTENT = "ENCRYPTED_CONTENT";
    public static final String XML_NODE_HISTORY_CREATED_AT = "CREATED";
    public static final String XML_NODE_HISTORY_LAST_MODIFIED_AT = "MODIFIED";

    public static final String XML_NODE_XHTML_CONTENT_TAG = "richcontent";
    public static final String XML_NODE_XHTML_TYPE_TAG = "TYPE";
    public static final String XML_NODE_XHTML_TYPE_NODE = "NODE";
    public static final String XML_NODE_XHTML_TYPE_NOTE = "NOTE";

    // arrow link attributes:
    protected final List<ArrowLinkAdapter> mArrowLinkAdapters;
    protected HashMap<String, NodeAdapter> mIdToTarget;

    protected final MapFeedback mMapFeedback;

    public XMLElementAdapter(MapFeedback pMapFeedback) {
        this(pMapFeedback, new ArrayList<>(), new HashMap<>());
    }

    public XMLElementAdapter(MapFeedback pMapFeedback, List<ArrowLinkAdapter> arrowLinkAdapters, HashMap<String, NodeAdapter> IDToTarget) {
        this.mMapFeedback = pMapFeedback;
        this.mArrowLinkAdapters = arrowLinkAdapters;
        this.mIdToTarget = IDToTarget;
    }

    protected MindMap getMap() {
        return mMapFeedback.getMap();
    }

    /**
     * Build the FreeMind model tree from a DOM Element (typically the document element).
     * This replaces the old parseFromReader() + callback approach.
     */
    public void buildFromDom(Element domElement) {
        String tagName = domElement.getTagName();
        if ("map".equals(tagName)) {
            // Process child elements of the map element
            for (Element child : FreeMindXml.getChildElements(domElement)) {
                BuildResult childResult = buildElement(child);
                if (childResult != null && childResult.userObject instanceof NodeAdapter) {
                    mapChild = (NodeAdapter) childResult.userObject;
                }
            }
        } else if (XML_NODE.equals(tagName)) {
            // Direct node element (e.g. from clipboard paste)
            BuildResult result = buildElement(domElement);
            if (result != null && result.userObject instanceof NodeAdapter) {
                mapChild = (NodeAdapter) result.userObject;
            }
        }
    }

    /**
     * Recursively builds a model object from a DOM element.
     * Returns a BuildResult containing the created user object and the element name.
     */
    private BuildResult buildElement(Element domElement) {
        String name = domElement.getTagName();

        // --- Phase 1: Create user object based on element name (was setName()) ---
        Object userObject = createUserObject(name);

        // --- Phase 2: Apply attributes (was setAttribute()) ---
        HashMap<String, String> nodeAttributes = new HashMap<>();
        NamedNodeMap attrs = domElement.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            String attrName = attrs.item(i).getNodeName().toUpperCase();
            String attrValue = attrs.item(i).getNodeValue();
            userObject = applyAttribute(name, userObject, attrName, attrValue, nodeAttributes);
        }

        // --- Phase 3: Process children recursively (was addChild()) ---
        for (Element childElement : FreeMindXml.getChildElements(domElement)) {
            String childName = childElement.getTagName();

            if (XML_NODE_XHTML_CONTENT_TAG.equals(childName)) {
                // richcontent: serialize inner content back to string
                BuildResult childResult = new BuildResult(childName, null);
                String xmlText = serializeInnerContent(childElement);
                childResult.content = xmlText;
                // Copy attributes from the richcontent element
                NamedNodeMap childAttrs = childElement.getAttributes();
                childResult.attributes = new HashMap<>();
                for (int i = 0; i < childAttrs.getLength(); i++) {
                    childResult.attributes.put(
                            childAttrs.item(i).getNodeName().toUpperCase(),
                            childAttrs.item(i).getNodeValue());
                }
                addChildResult(name, userObject, childResult);
            } else if ("hook".equals(childName)) {
                // hook elements need special handling: pass the DOM element directly
                addHookChild(userObject, childElement);
            } else {
                BuildResult childResult = buildElement(childElement);
                if (childResult != null) {
                    addChildResult(name, userObject, childResult);
                }
            }
        }

        // --- Phase 4: Complete element (was completeElement()) ---
        Object[] completed = completeElement(name, userObject, attrs, nodeAttributes);
        userObject = completed[0];

        // Track mapChild for top-level node
        if (XML_NODE.equals(name) && mapChild == null && userObject instanceof NodeAdapter) {
            mapChild = (NodeAdapter) userObject;
        }

        BuildResult result = new BuildResult(name, userObject);
        return result;
    }

    /**
     * Create the appropriate user object based on the element tag name.
     * Equivalent to the old setName() method.
     */
    private Object createUserObject(String name) {
        switch (name) {
            case XML_NODE:
                return getMap().createNodeAdapter(getMap(), null);
            case "edge":
                return getMap().createEdgeAdapter(null);
            case "cloud":
                return getMap().createCloudAdapter(null);
            case "arrowlink":
                return getMap().createArrowLinkAdapter(null, null);
            case "linktarget":
                return getMap().createArrowLinkTarget(null, null);
            case "font":
            case "icon":
            case XML_NODE_REGISTERED_ATTRIBUTE_VALUE:
            case XML_NODE_REGISTERED_ATTRIBUTE_NAME:
            case XML_NODE_ATTRIBUTE_REGISTRY:
            case "map":
            case XML_NODE_ATTRIBUTE_LAYOUT:
            case XML_NODE_ATTRIBUTE:
                return null;
            default:
                return null;
        }
    }

    /**
     * Apply a single attribute to the current user object.
     * Equivalent to the old setAttribute() method.
     * Returns the (possibly replaced) user object.
     */
    private Object applyAttribute(String elementName, Object userObject, String attrName, String sValue,
                                   HashMap<String, String> nodeAttributes) {
        if (userObject instanceof NodeAdapter) {
            NodeAdapter node = (NodeAdapter) userObject;
            NodeAdapter result = setNodeAttribute(attrName, sValue, node);
            nodeAttributes.put(attrName, sValue);
            if (result != node) {
                // Node was replaced (e.g., encrypted node)
                copyAttributesToNode(result, nodeAttributes);
                return result;
            }
            return result;
        }

        if (userObject instanceof EdgeAdapter) {
            EdgeAdapter edge = (EdgeAdapter) userObject;
            switch (attrName) {
                case "STYLE":
                    edge.setStyle(sValue);
                    break;
                case "COLOR":
                    edge.setColor(ColorUtils.xmlToColor(sValue));
                    break;
                case "WIDTH":
                    if (sValue.equals(EdgeAdapter.EDGE_WIDTH_THIN_STRING)) {
                        edge.setWidth(EdgeAdapter.WIDTH_THIN);
                    } else {
                        edge.setWidth(Integer.parseInt(sValue));
                    }
                    break;
            }
            return userObject;
        }

        if (userObject instanceof CloudAdapter) {
            CloudAdapter cloud = (CloudAdapter) userObject;
            switch (attrName) {
                case "STYLE":
                    cloud.setStyle(sValue);
                    break;
                case "COLOR":
                    cloud.setColor(ColorUtils.xmlToColor(sValue));
                    break;
                case "WIDTH":
                    cloud.setWidth(Integer.parseInt(sValue));
                    break;
            }
            return userObject;
        }

        if (userObject instanceof ArrowLinkAdapter) {
            ArrowLinkAdapter arrowLink = (ArrowLinkAdapter) userObject;
            switch (attrName) {
                case "STYLE":
                    arrowLink.setStyle(sValue);
                    break;
                case "ID":
                    arrowLink.setUniqueId(sValue);
                    break;
                case "COLOR":
                    arrowLink.setColor(ColorUtils.xmlToColor(sValue));
                    break;
                case "DESTINATION":
                    arrowLink.setDestinationLabel(sValue);
                    break;
                case "REFERENCETEXT":
                    arrowLink.setReferenceText((sValue));
                    break;
                case "STARTINCLINATION":
                    arrowLink.setStartInclination(PointUtils.xmlToPoint(sValue));
                    break;
                case "ENDINCLINATION":
                    arrowLink.setEndInclination(PointUtils.xmlToPoint(sValue));
                    break;
                case "STARTARROW":
                    arrowLink.setStartArrow(sValue);
                    break;
                case "ENDARROW":
                    arrowLink.setEndArrow(sValue);
                    break;
                case "WIDTH":
                    arrowLink.setWidth(Integer.parseInt(sValue));
                    break;
            }
            if (userObject instanceof ArrowLinkTarget) {
                ArrowLinkTarget arrowLinkTarget = (ArrowLinkTarget) userObject;
                if (attrName.equals("SOURCE")) {
                    arrowLinkTarget.setSourceLabel(sValue);
                }
            }
            return userObject;
        }

        // For elements with null userObject: font, icon, attribute, etc.
        // These are handled via per-element state tracked in BuildResult/completeElement
        return userObject;
    }

    /**
     * Font/icon/attribute state holder used during element building.
     * In the old code these were instance fields; now they're tracked per-element.
     */
    private static class ElementState {
        String fontName;
        int fontStyle = 0;
        int fontSize = 0;
        boolean fontStyleStrikethrough;
        String iconName;
        String attributeName;
        String attributeValue;
    }

    /**
     * Apply font/icon/attribute-specific attributes.
     * Returns the element state for use in completeElement.
     */
    private ElementState applyElementAttributes(String elementName, NamedNodeMap attrs) {
        ElementState state = new ElementState();
        for (int i = 0; i < attrs.getLength(); i++) {
            String attrName = attrs.item(i).getNodeName().toUpperCase();
            String sValue = attrs.item(i).getNodeValue();

            if (elementName.equals("font")) {
                if (attrName.equals("SIZE")) {
                    state.fontSize = Integer.parseInt(sValue);
                } else if (attrName.equals("NAME")) {
                    state.fontName = sValue;
                } else if (sValue.equals("true")) {
                    switch (attrName) {
                        case "BOLD":
                            state.fontStyle += Font.BOLD;
                            break;
                        case "ITALIC":
                            state.fontStyle += Font.ITALIC;
                            break;
                        case "STRIKETHROUGH":
                            state.fontStyleStrikethrough = true;
                            break;
                    }
                }
            } else if (elementName.equals("icon")) {
                if (attrName.equals("BUILTIN")) {
                    state.iconName = sValue;
                }
            } else if (elementName.equals(XML_NODE_ATTRIBUTE)) {
                if (attrName.equals("NAME")) {
                    state.attributeName = sValue;
                } else if (attrName.equals("VALUE")) {
                    state.attributeValue = sValue;
                }
            } else if (elementName.equals(XML_NODE_REGISTERED_ATTRIBUTE_NAME)) {
                if (attrName.equals("NAME")) {
                    state.attributeName = sValue;
                }
            } else if (elementName.equals(XML_NODE_REGISTERED_ATTRIBUTE_VALUE)) {
                if (attrName.equals("VALUE")) {
                    state.attributeValue = sValue;
                }
            }
        }
        return state;
    }

    /**
     * Complete an element after all attributes and children are processed.
     * Returns array: [userObject].
     */
    private Object[] completeElement(String name, Object userObject, NamedNodeMap attrs,
                                      HashMap<String, String> nodeAttributes) {
        switch (name) {
            case "font": {
                ElementState state = applyElementAttributes(name, attrs);
                Font font = new Font(state.fontName, state.fontStyle, state.fontSize);
                if (state.fontStyleStrikethrough) {
                    Map attr = font.getAttributes();
                    attr.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
                    font = new Font(attr);
                }
                userObject = mMapFeedback.getFontThroughMap(font);
                break;
            }
            case "icon": {
                ElementState state = applyElementAttributes(name, attrs);
                userObject = MindIcon.factory(state.iconName);
                break;
            }
            case XML_NODE_ATTRIBUTE: {
                ElementState state = applyElementAttributes(name, attrs);
                userObject = new Attribute(state.attributeName, state.attributeValue);
                break;
            }
        }
        return new Object[]{userObject};
    }

    /**
     * Wire a child result into its parent's user object.
     * Equivalent to the old addChild() method.
     */
    private void addChildResult(String parentName, Object parentUserObject, BuildResult child) {
        if ("map".equals(parentName)) {
            if (child.userObject instanceof NodeAdapter) {
                mapChild = (NodeAdapter) child.userObject;
            }
            return;
        }

        if (parentUserObject instanceof NodeAdapter) {
            NodeAdapter node = (NodeAdapter) parentUserObject;
            if (child.userObject instanceof NodeAdapter) {
                node.insert((NodeAdapter) child.userObject, -1);
            } else if (child.userObject instanceof EdgeAdapter) {
                EdgeAdapter edge = (EdgeAdapter) child.userObject;
                edge.setTarget(node);
                node.setEdge(edge);
            } else if (child.userObject instanceof CloudAdapter) {
                CloudAdapter cloud = (CloudAdapter) child.userObject;
                cloud.setTarget(node);
                node.setCloud(cloud);
            } else if (child.userObject instanceof ArrowLinkTarget) {
                ArrowLinkTarget arrowLinkTarget = (ArrowLinkTarget) child.userObject;
                arrowLinkTarget.setTarget(node);
                mArrowLinkAdapters.add(arrowLinkTarget);
            } else if (child.userObject instanceof ArrowLinkAdapter) {
                ArrowLinkAdapter arrowLink = (ArrowLinkAdapter) child.userObject;
                arrowLink.setSource(node);
                mArrowLinkAdapters.add(arrowLink);
            } else if (child.name.equals("font")) {
                node.setFont((Font) child.userObject);
            } else if (child.name.equals(XML_NODE_ATTRIBUTE)) {
                node.addAttribute((Attribute) child.userObject);
            } else if (child.name.equals(XML_NODE_ATTRIBUTE_LAYOUT)) {
                // Attribute layout - currently no-op in legacy code
            } else if (child.name.equals("icon")) {
                node.addIcon((MindIcon) child.userObject, MindIcon.LAST);
            } else if (child.name.equals(XML_NODE_XHTML_CONTENT_TAG)) {
                String xmlText = child.content;
                String typeAttribute = child.attributes != null
                        ? child.attributes.get(XML_NODE_XHTML_TYPE_TAG)
                        : null;
                if (typeAttribute == null
                        || XML_NODE_XHTML_TYPE_NODE.equals(typeAttribute)) {
                    log.trace("Setting node html content to:{}", xmlText);
                    node.setXmlText(xmlText);
                } else {
                    log.trace("Setting note html content to:{}", xmlText);
                    node.setXmlNoteText(xmlText);
                }
            }
        }
    }

    /**
     * Handle a hook child element. Hooks receive the DOM element directly.
     */
    private void addHookChild(Object parentUserObject, Element hookElement) {
        if (!(parentUserObject instanceof NodeAdapter)) {
            return;
        }
        NodeAdapter node = (NodeAdapter) parentUserObject;
        String loadName = hookElement.getAttribute("NAME");
        PermanentNodeHook hook = null;
        try {
            hook = (PermanentNodeHook) mMapFeedback.createNodeHook(loadName, node);
            hook.setNode(node);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            hook = new PermanentNodeHookSubstituteUnknown(loadName);
        }
        hook.loadFrom(hookElement);
        node.addHook(hook);
    }

    /**
     * Serialize the inner content of a DOM element (its children) back to a string.
     * Used for richcontent elements that contain XHTML.
     */
    private String serializeInnerContent(Element element) {
        StringBuilder sb = new StringBuilder();
        org.w3c.dom.NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            org.w3c.dom.Node child = children.item(i);
            if (child.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                sb.append(FreeMindXml.toString((Element) child));
            } else if (child.getNodeType() == org.w3c.dom.Node.TEXT_NODE) {
                String text = child.getNodeValue();
                if (text != null && !text.trim().isEmpty()) {
                    sb.append(text);
                }
            }
        }
        return sb.toString().trim();
    }

    private NodeAdapter setNodeAttribute(String name, String sValue, NodeAdapter node) {
        switch (name) {
            case XML_NODE_TEXT:
                log.trace("Setting node text content to:{}", sValue);
                node.setUserObject(sValue);
                break;
            case XML_NODE_ENCRYPTED_CONTENT:
                node = getMap().createEncryptedNode(sValue);
                break;
            case XML_NODE_HISTORY_CREATED_AT:
                if (node.getHistoryInformation() == null) {
                    node.setHistoryInformation(new HistoryInformation());
                }
                node.getHistoryInformation().setCreatedAt(Tools.xmlToDate(sValue));
                break;
            case XML_NODE_HISTORY_LAST_MODIFIED_AT:
                if (node.getHistoryInformation() == null) {
                    node.setHistoryInformation(new HistoryInformation());
                }
                node.getHistoryInformation().setLastModifiedAt(Tools.xmlToDate(sValue));
                break;
            case "FOLDED":
                if (sValue.equals("true")) {
                    node.setFolded(true);
                }
                break;
            case "POSITION":
                node.setLeft(sValue.equals("left"));
                break;
            case "COLOR":
                if (sValue.length() == 7) {
                    node.setColor(ColorUtils.xmlToColor(sValue));
                }
                break;
            case "BACKGROUND_COLOR":
                if (sValue.length() == 7) {
                    node.setBackgroundColor(ColorUtils.xmlToColor(sValue));
                }
                break;
            case "LINK":
                node.setLink(sValue);
                break;
            case "STYLE":
                node.setStyle(sValue);
                break;
            case "ID":
                mIdToTarget.put(sValue, node);
                break;
            case "VSHIFT":
                node.setShiftY(Integer.parseInt(sValue));
                break;
            case "VGAP":
                node.setVGap(Integer.parseInt(sValue));
                break;
            case "HGAP":
                node.setHGap(Integer.parseInt(sValue));
                break;
        }
        return node;
    }

    /**
     * Re-apply previously collected attributes to a new node instance.
     * Used when the node type changes (e.g., to EncryptedMindMapNode).
     */
    protected void copyAttributesToNode(NodeAdapter node, HashMap<String, String> nodeAttributes) {
        for (Map.Entry<String, String> entry : nodeAttributes.entrySet()) {
            setNodeAttribute(entry.getKey(), entry.getValue(), node);
        }
    }

    /**
     * Completes the links within the map. They are registered in the registry.
     */
    public void processUnfinishedLinks(MindMapLinkRegistry registry) {
        // add labels to the nodes:
        for (String key : mIdToTarget.keySet()) {
            NodeAdapter target1 = mIdToTarget.get(key);
            registry.registerLinkTarget(target1, key);
        }
        // complete arrow links with right labels:
        for (ArrowLinkAdapter arrowObject : mArrowLinkAdapters) {
            if (arrowObject instanceof ArrowLinkTarget) {
                ArrowLinkTarget linkTarget = (ArrowLinkTarget) arrowObject;
                String oldId = linkTarget.getSourceLabel();
                MindMapNode source = registry.getTargetForId(oldId);
                if (mIdToTarget.containsKey(oldId)) {
                    continue;
                } else if (source == null) {
                    log.error("Cannot find the label {} in the map. The link target {} is not restored.", oldId, linkTarget);
                    continue;
                }
                linkTarget.setSource(source);
                MindMapNode target = linkTarget.getTarget();
                String targetCurrentId = registry.getLabel(target);
                if (!mIdToTarget.containsKey(targetCurrentId)) {
                    MindMapLink link = registry.getLinkForId(linkTarget.getUniqueId());
                    if (link == null) {
                        log.error("Cannot find the label {} for the link in the map. The link target {} is not restored.", linkTarget.getUniqueId(), linkTarget);
                    } else {
                        MindMapLink clone = (MindMapLink) link.clone();
                        clone.setTarget(target);
                        ((ArrowLinkAdapter) clone).setDestinationLabel(targetCurrentId);
                        registry.registerLink(clone);
                        linkTarget.setUniqueId(clone.getUniqueId());
                    }
                } else {
                    ArrowLinkAdapter linkAdapter = linkTarget.createArrowLinkAdapter(registry);
                    registry.registerLink(linkAdapter);
                }
            } else if (arrowObject instanceof ArrowLinkAdapter) {
                ArrowLinkAdapter arrowLink = arrowObject;
                String oldId = arrowLink.getDestinationLabel();
                NodeAdapter target = null;
                String newId = null;
                if (mIdToTarget.containsKey(oldId)) {
                    target = mIdToTarget.get(oldId);
                    newId = registry.getLabel(target);
                } else if (registry.getTargetForId(oldId) != null) {
                    target = (NodeAdapter) registry.getTargetForId(oldId);
                    newId = oldId;
                } else {
                    log.error("Cannot find the label {} in the map. The link {} is not restored.", oldId, arrowLink);
                    continue;
                }
                arrowLink.setDestinationLabel(newId);
                arrowLink.setTarget(target);
                registry.registerLink(arrowLink);
            }
        }
    }

    public HashMap<String, NodeAdapter> getIDToTarget() {
        return mIdToTarget;
    }

    public void setIDToTarget(HashMap<String, NodeAdapter> pToTarget) {
        mIdToTarget = pToTarget;
    }

    /**
     * Holds the result of building a single DOM element.
     */
    private static class BuildResult {
        final String name;
        Object userObject;
        String content; // for richcontent elements
        Map<String, String> attributes; // for richcontent elements

        BuildResult(String name, Object userObject) {
            this.name = name;
            this.userObject = userObject;
        }
    }
}
