package freemind.model.services;

import freemind.extensions.DontSaveMarker;
import freemind.extensions.PermanentNodeHook;
import freemind.main.ColorUtils;
import freemind.main.DateTimeTools;
import freemind.main.FreeMindCommon;
import freemind.main.FreeMindXml;
import freemind.main.HtmlTools;
import freemind.main.Resources;
import freemind.model.MindMapLink;
import freemind.model.MindMapLinkRegistry;
import freemind.model.MindMapNode;
import freemind.model.NodeAdapter;
import freemind.model.XmlNodeConstants;
import freemind.model.attributes.Attribute;
import freemind.preferences.FreemindPropertyListener;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.Font;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

/**
 * Owns the XML serialization for a single {@link NodeAdapter}: the {@code save(...)} method,
 * the recursive {@code saveChildren} helper, the static {@code convertToEncodedContent}, and
 * the {@code shallowCopy()} round-trip. Extracted from {@link NodeAdapter} as the largest
 * single piece of behavior. {@code EncryptedMindMapNode.save} continues to wrap
 * {@code super.save(...)} which now lands on a thin delegator.
 *
 * <p>NOTE on the {@code STYLE} attribute: the original {@code NodeAdapter.save} called
 * {@code this.getStyle()} (the resolved style — accounts for {@code STYLE_AS_PARENT} and
 * folded-vs-unfolded). The comment in the original disclaimed this, but the code did it.
 * We preserve that behavior verbatim.
 */
@Slf4j
public class NodeXmlSerializerService {

    private static FreemindPropertyListener sSaveIdPropertyChangeListener;
    private static boolean sSaveOnlyIntrinsicallyNeededIds = false;

    private final NodeAdapter node;

    public NodeXmlSerializerService(NodeAdapter node) {
        this.node = node;
        if (sSaveIdPropertyChangeListener == null) {
            sSaveIdPropertyChangeListener = (propertyName, newValue, oldValue) -> {
                if (propertyName.equals(FreeMindCommon.SAVE_ONLY_INTRISICALLY_NEEDED_IDS)) {
                    sSaveOnlyIntrinsicallyNeededIds = Boolean.valueOf(newValue).booleanValue();
                }
            };
            Resources.addPropertyChangeListenerAndPropagate(sSaveIdPropertyChangeListener);
        }
    }

    public Element save(Writer writer, Document doc, MindMapLinkRegistry registry,
                        boolean saveInvisible, boolean saveChildren) throws IOException {
        // pre save event to save all contents of the node:
        node.getMapFeedback().firePreSaveEvent(node);
        Element element = doc.createElement(XmlNodeConstants.XML_NODE);

        /* XML must not contain any zero characters. */
        String text = node.toString().replace('\0', ' ');
        if (!HtmlTools.isHtmlNode(text)) {
            element.setAttribute(XmlNodeConstants.XML_NODE_TEXT, text);
        } else {
            // save <content> tag:
            Element htmlElement = doc.createElement(XmlNodeConstants.XML_NODE_XHTML_CONTENT_TAG);
            htmlElement.setAttribute(XmlNodeConstants.XML_NODE_XHTML_TYPE_TAG,
                    XmlNodeConstants.XML_NODE_XHTML_TYPE_NODE);
            FreeMindXml.setEncodedContent(htmlElement, convertToEncodedContent(node.getXmlText()));
            element.appendChild(htmlElement);
        }
        if (node.getXmlNoteText() != null) {
            Element htmlElement = doc.createElement(XmlNodeConstants.XML_NODE_XHTML_CONTENT_TAG);
            htmlElement.setAttribute(XmlNodeConstants.XML_NODE_XHTML_TYPE_TAG,
                    XmlNodeConstants.XML_NODE_XHTML_TYPE_NOTE);
            FreeMindXml.setEncodedContent(htmlElement, convertToEncodedContent(node.getXmlNoteText()));
            element.appendChild(htmlElement);
        }
        if (node.getAdditionalInfo() != null) {
            element.setAttribute(XmlNodeConstants.XML_NODE_ENCRYPTED_CONTENT, node.getAdditionalInfo());
        }

        Element edge = (node.getEdge()).save(doc);
        if (edge != null) {
            element.appendChild(edge);
        }

        if (node.getCloud() != null) {
            Element cloudEl = (node.getCloud()).save(doc);
            element.appendChild(cloudEl);
        }

        List<MindMapLink> linkVector = registry.getAllLinksFromMe(node);
        for (var mapLink : linkVector) {
            Element linkElement = mapLink.saveLink(doc);
            if (linkElement != null) {
                element.appendChild(linkElement);
            }
        }

        // virtual link targets:
        List<MindMapLink> targetVector = registry.getAllLinksIntoMe(node);
        for (var mindMapLink : targetVector) {
            Element linkTargetElement = mindMapLink.saveTarget(doc, registry);
            if (linkTargetElement != null) {
                element.appendChild(linkTargetElement);
            }
        }

        if (node.isFolded()) {
            element.setAttribute("FOLDED", "true");
        }

        // save POSITION if and only if parent is root.
        if (!(node.isRoot()) && (node.getParentNode().isRoot())) {
            element.setAttribute("POSITION", node.isLeft() ? "left" : "right");
        }

        // The id is used if there is a local hyperlink pointing to me or a real link.
        String label = registry.getLabel(node);
        if (!sSaveOnlyIntrinsicallyNeededIds
                || (registry.isTargetOfLocalHyperlinks(label) || (!registry.getAllLinksIntoMe(node).isEmpty()))) {
            if (label != null) {
                element.setAttribute("ID", label);
            }
        }
        if (node.getColor() != null) {
            element.setAttribute("COLOR", ColorUtils.colorToXml(node.getColor()));
        }

        if (node.getBackgroundColor() != null) {
            element.setAttribute("BACKGROUND_COLOR", ColorUtils.colorToXml(node.getBackgroundColor()));
        }

        // Preserve original NodeAdapter.save behavior verbatim: serialize the RESOLVED style via
        // node.getStyle() (which resolves STYLE_AS_PARENT and folded/unfolded), gated on the raw
        // stored field being non-null. The comment in the original disclaimed this, but the code
        // did it. This refactor preserves behavior, not the comment's intent.
        if (node.getBareStyle() != null) {
            element.setAttribute("STYLE", node.getStyle());
        }

        // layout
        if (node.getVGap() != NodeAdapter.VGAP) {
            element.setAttribute("VGAP", Integer.toString(node.getVGap()));
        }
        if (node.getHGap() != NodeAdapter.HGAP) {
            element.setAttribute("HGAP", Integer.toString(node.getHGap()));
        }
        if (node.getShiftY() != 0) {
            element.setAttribute("VSHIFT", Integer.toString(node.getShiftY()));
        }
        // link
        if (node.getLink() != null) {
            element.setAttribute("LINK", node.getLink());
        }

        // history information
        if (node.getHistoryInformation() != null) {
            element.setAttribute(XmlNodeConstants.XML_NODE_HISTORY_CREATED_AT,
                    DateTimeTools.dateToString(node.getHistoryInformation().getCreatedAt()));
            element.setAttribute(XmlNodeConstants.XML_NODE_HISTORY_LAST_MODIFIED_AT,
                    DateTimeTools.dateToString(node.getHistoryInformation().getLastModifiedAt()));
        }
        // font
        Font font = node.getFont();
        if (font != null) {
            Element fontElement = doc.createElement("font");
            fontElement.setAttribute("NAME", font.getFamily());
            if (font.getSize() != 0) {
                fontElement.setAttribute("SIZE", Integer.toString(font.getSize()));
            }
            if (node.isBold()) {
                fontElement.setAttribute("BOLD", "true");
            }
            if (node.isStrikethrough()) {
                fontElement.setAttribute("STRIKETHROUGH", "true");
            }
            if (node.isItalic()) {
                fontElement.setAttribute("ITALIC", "true");
            }
            if (node.isUnderlined()) {
                fontElement.setAttribute("UNDERLINE", "true");
            }
            element.appendChild(fontElement);
        }
        for (int i = 0; i < node.getIcons().size(); ++i) {
            Element iconElement = doc.createElement("icon");
            iconElement.setAttribute("BUILTIN", node.getIcons().get(i).getName());
            element.appendChild(iconElement);
        }

        for (var permHook : node.getActivatedHooks()) {
            if (permHook instanceof DontSaveMarker) {
                continue;
            }
            Element hookElement = doc.createElement("hook");
            permHook.save(doc, hookElement);
            element.appendChild(hookElement);
        }
        if (node.getAttributeTableLength() > 0) {
            for (var attribute : node.getAttributes()) {
                Element attributeElement = doc.createElement(XmlNodeConstants.XML_NODE_ATTRIBUTE);
                attributeElement.setAttribute("NAME", attribute.getName());
                attributeElement.setAttribute("VALUE", attribute.getValue());
                element.appendChild(attributeElement);
            }
        }

        if (saveChildren && node.childrenUnfolded().hasNext()) {
            FreeMindXml.writeFreeMindElement(element, writer, false);
            // recursive
            saveChildren(writer, doc, registry, node, saveInvisible);
            FreeMindXml.writeFreeMindClosingTag(element, writer);
        } else {
            FreeMindXml.writeFreeMindElement(element, writer, true);
        }
        return element;
    }

    public static String convertToEncodedContent(String xmlText) {
        String replace = HtmlTools.makeValidXml(xmlText);
        return HtmlTools.unicodeToHTMLUnicodeEntity(replace, true);
    }

    private void saveChildren(Writer writer, Document doc, MindMapLinkRegistry registry,
                              NodeAdapter parent, boolean saveHidden) throws IOException {
        for (ListIterator<MindMapNode> e = parent.childrenUnfolded(); e.hasNext(); ) {
            MindMapNode child = e.next();
            NodeAdapter childAdapter = (NodeAdapter) child;
            if (saveHidden || childAdapter.isVisible()) {
                childAdapter.save(writer, doc, registry, saveHidden, true);
            } else {
                saveChildren(writer, doc, registry, childAdapter, saveHidden);
            }
        }
    }

    public MindMapNode shallowCopy() {
        try {
            var writer = new StringWriter();
            Document doc = FreeMindXml.newDocument();
            this.save(writer, doc, node.getMap().getLinkRegistry(), true, false);
            String result = writer.toString();
            var idToTarget = new HashMap<String, NodeAdapter>();
            MindMapNode copy = node.getMap().createNodeTreeFromXml(new StringReader(result), idToTarget);
            copy.setFolded(false);
            return copy;
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return null;
        }
    }
}
