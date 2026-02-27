package freemind.main;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * DOM-based XML utility class providing convenient access to JDK DOM operations.
 * All methods are static; this class cannot be instantiated.
 */
public final class FreeMindXml {

    private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY;
    private static final TransformerFactory TRANSFORMER_FACTORY;

    static {
        DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();
        DOCUMENT_BUILDER_FACTORY.setNamespaceAware(false);
        TRANSFORMER_FACTORY = TransformerFactory.newInstance();
    }

    private FreeMindXml() {
        // utility class
    }

    // --- Parsing ---

    /**
     * Parse XML from a Reader.
     */
    public static Document parse(Reader reader) {
        try {
            DocumentBuilder builder = createDocumentBuilder();
            return builder.parse(new InputSource(reader));
        } catch (SAXException | IOException e) {
            throw new FreeMindXmlException("Failed to parse XML", e);
        }
    }

    /**
     * Parse XML from a String.
     */
    public static Document parse(String xml) {
        return parse(new StringReader(xml));
    }

    /**
     * Create a new empty Document.
     */
    public static Document newDocument() {
        return createDocumentBuilder().newDocument();
    }

    // --- Element creation ---

    /**
     * Create a new Element in the given Document.
     */
    public static Element createElement(Document doc, String tagName) {
        return doc.createElement(tagName);
    }

    // --- Child element access ---

    /**
     * Return all direct child Elements of the given parent, filtering out text and other non-element nodes.
     */
    public static List<Element> getChildElements(Element parent) {
        List<Element> result = new ArrayList<>();
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                result.add((Element) child);
            }
        }
        return result;
    }

    /**
     * Return the first direct child Element with the given tag name, or null if none found.
     */
    public static Element getChildElement(Element parent, String tagName) {
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE && tagName.equals(child.getNodeName())) {
                return (Element) child;
            }
        }
        return null;
    }

    // --- Attribute access ---

    /**
     * Return the string value of the named attribute, or null if not present.
     * Unlike DOM's {@code getAttribute()}, this returns null rather than empty string for missing attributes.
     */
    public static String getStringAttribute(Element el, String name) {
        if (!el.hasAttribute(name)) {
            return null;
        }
        return el.getAttribute(name);
    }

    /**
     * Return the int value of the named attribute, or defaultValue if not present or not parseable.
     */
    public static int getIntAttribute(Element el, String name, int defaultValue) {
        String value = getStringAttribute(el, name);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Return the double value of the named attribute, or defaultValue if not present or not parseable.
     */
    public static double getDoubleAttribute(Element el, String name, double defaultValue) {
        String value = getStringAttribute(el, name);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Return the boolean value of the named attribute, or defaultValue if not present.
     * Recognizes "true" (case-insensitive) as true, anything else as false.
     */
    public static boolean getBooleanAttribute(Element el, String name, boolean defaultValue) {
        String value = getStringAttribute(el, name);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }

    // --- Content ---

    /**
     * Return the text content of the element.
     */
    public static String getTextContent(Element el) {
        return el.getTextContent();
    }

    // --- Writing ---

    /**
     * Write a Document to a Writer, omitting the XML declaration, using UTF-8 encoding.
     */
    public static void write(Document doc, Writer writer) {
        transform(new DOMSource(doc), writer);
    }

    /**
     * Write a single Element to a Writer, omitting the XML declaration.
     */
    public static void write(Element element, Writer writer) {
        transform(new DOMSource(element), writer);
    }

    /**
     * Serialize an Element to a String.
     */
    public static String toString(Element element) {
        StringWriter sw = new StringWriter();
        write(element, sw);
        return sw.toString();
    }

    // --- FreeMind-format writing ---

    /**
     * Write a DOM Element tree to a Writer in the FreeMind .mm file format.
     * This produces output compatible with the legacy XMLElement.write() method:
     * attributes are XML-encoded, self-closing tags for empty elements, newlines after tags.
     * <p>
     * Elements may contain a "data-encoded-content" attribute which signals that
     * the text content should be written verbatim (not XML-escaped). This is used
     * for XHTML content embedded in nodes.
     */
    public static void writeFreeMindElement(Element element, Writer writer) throws IOException {
        writeFreeMindElement(element, writer, true);
    }

    /**
     * Write opening tag, attributes, and content of a DOM Element, optionally omitting the closing tag.
     * When withClosingTag is false, child elements and the closing tag are NOT written.
     * Use {@link #writeFreeMindClosingTag(Element, Writer)} to write the closing tag later.
     */
    public static void writeFreeMindElement(Element element, Writer writer, boolean withClosingTag) throws IOException {
        String tagName = element.getTagName();
        String encodedContent = element.getAttribute(ENCODED_CONTENT_ATTR);
        boolean hasEncodedContent = element.hasAttribute(ENCODED_CONTENT_ATTR);
        String textContent = hasEncodedContent ? null : getDirectTextContent(element);
        List<Element> children = getChildElements(element);

        writer.write('<');
        writer.write(tagName);

        // Write attributes (skip the internal encoded-content marker)
        var attrs = element.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            var attr = attrs.item(i);
            String name = attr.getNodeName();
            if (ENCODED_CONTENT_ATTR.equals(name)) {
                continue;
            }
            writer.write(' ');
            writer.write(name);
            writer.write('=');
            writer.write('"');
            writeXmlEncoded(writer, attr.getNodeValue());
            writer.write('"');
        }

        if (hasEncodedContent && encodedContent != null && !encodedContent.isEmpty()) {
            // Element has raw (pre-encoded) content - write it verbatim
            writer.write('>');
            writer.write(encodedContent);
            if (withClosingTag) {
                writer.write('<');
                writer.write('/');
                writer.write(tagName);
                writer.write('>');
                writer.write('\n');
            }
        } else if (textContent != null && !textContent.isEmpty()) {
            // Element has plain text content - XML-encode it
            writer.write('>');
            writeXmlEncoded(writer, textContent);
            if (withClosingTag) {
                writer.write('<');
                writer.write('/');
                writer.write(tagName);
                writer.write('>');
                writer.write('\n');
            }
        } else if (children.isEmpty()) {
            // Self-closing empty element
            if (withClosingTag) {
                writer.write('/');
            }
            writer.write('>');
            writer.write('\n');
        } else {
            // Element with child elements
            writer.write('>');
            writer.write('\n');
            if (withClosingTag) {
                for (Element child : children) {
                    writeFreeMindElement(child, writer, true);
                }
                writer.write('<');
                writer.write('/');
                writer.write(tagName);
                writer.write('>');
                writer.write('\n');
            }
        }
    }

    /**
     * Write just the closing tag for an element. Used for streaming child nodes.
     */
    public static void writeFreeMindClosingTag(Element element, Writer writer) throws IOException {
        writer.write('<');
        writer.write('/');
        writer.write(element.getTagName());
        writer.write('>');
        writer.write('\n');
    }

    /**
     * Internal attribute name used to mark elements that contain pre-encoded (raw) content.
     * This attribute is NOT written to output; it is a processing instruction for writeFreeMindElement.
     */
    static final String ENCODED_CONTENT_ATTR = "_freemind_encoded_content";

    /**
     * Set pre-encoded (raw HTML) content on an element. The content will be written verbatim
     * without XML-escaping when using writeFreeMindElement.
     */
    public static void setEncodedContent(Element element, String content) {
        element.setAttribute(ENCODED_CONTENT_ATTR, content);
    }

    /**
     * Get the direct text content of an element (only immediate text nodes, not descendant text).
     */
    private static String getDirectTextContent(Element element) {
        StringBuilder sb = new StringBuilder();
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.TEXT_NODE) {
                sb.append(child.getNodeValue());
            }
        }
        String result = sb.toString();
        return result.isEmpty() ? null : result;
    }

    /**
     * Write a string with XML encoding matching the legacy FreeMind format.
     * Characters outside ASCII 32-126 are written as &#xHHHH; entities.
     */
    public static void writeXmlEncoded(Writer writer, String str) throws IOException {
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            switch (ch) {
                case '<':
                    writer.write("&lt;");
                    break;
                case '>':
                    writer.write("&gt;");
                    break;
                case '&':
                    writer.write("&amp;");
                    break;
                case '"':
                    writer.write("&quot;");
                    break;
                case '\'':
                    writer.write("&apos;");
                    break;
                default:
                    int unicode = ch;
                    if ((unicode < 32) || (unicode > 126)) {
                        writer.write("&#x");
                        writer.write(Integer.toString(unicode, 16));
                        writer.write(';');
                    } else {
                        writer.write(ch);
                    }
            }
        }
    }

    // --- Internal helpers ---

    private static DocumentBuilder createDocumentBuilder() {
        try {
            synchronized (DOCUMENT_BUILDER_FACTORY) {
                return DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
            }
        } catch (ParserConfigurationException e) {
            throw new FreeMindXmlException("Failed to create DocumentBuilder", e);
        }
    }

    private static void transform(DOMSource source, Writer writer) {
        try {
            Transformer transformer;
            synchronized (TRANSFORMER_FACTORY) {
                transformer = TRANSFORMER_FACTORY.newTransformer();
            }
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(source, new StreamResult(writer));
        } catch (TransformerException e) {
            throw new FreeMindXmlException("Failed to write XML", e);
        }
    }

    /**
     * Runtime exception for XML processing errors.
     */
    public static class FreeMindXmlException extends RuntimeException {
        public FreeMindXmlException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
