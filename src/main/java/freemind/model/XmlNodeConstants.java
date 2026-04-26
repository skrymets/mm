package freemind.model;

/**
 * String literals for XML element and attribute names used in the .mm file
 * format. Lives in the model layer so node serialization (NodeAdapter#save)
 * can reference them without depending on the modes-layer XML reader.
 */
public final class XmlNodeConstants {

    public static final String XML_NODE = "node";
    public static final String XML_NODE_ATTRIBUTE = "attribute";
    public static final String XML_NODE_ENCRYPTED_CONTENT = "ENCRYPTED_CONTENT";
    public static final String XML_NODE_HISTORY_CREATED_AT = "CREATED";
    public static final String XML_NODE_HISTORY_LAST_MODIFIED_AT = "MODIFIED";
    public static final String XML_NODE_TEXT = "TEXT";
    public static final String XML_NODE_XHTML_CONTENT_TAG = "richcontent";
    public static final String XML_NODE_XHTML_TYPE_NODE = "NODE";
    public static final String XML_NODE_XHTML_TYPE_NOTE = "NOTE";
    public static final String XML_NODE_XHTML_TYPE_TAG = "TYPE";

    private XmlNodeConstants() {}
}
