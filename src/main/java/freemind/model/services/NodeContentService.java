package freemind.model.services;

import freemind.main.HtmlTools;
import freemind.model.NodeAdapter;

/**
 * Owns the node's textual content in its various forms: the plain text {@code userObject},
 * the {@code xmlText} (HTML→XML normalized), the rich note ({@code noteText}/{@code xmlNoteText}),
 * and the read-only derivatives ({@code plainTextContent}, {@code shortText}). Extracted from
 * {@link NodeAdapter} to consolidate the HtmlTools-mediated coupling between {@code text} and {@code xmlText}.
 */
public class NodeContentService {

    private final NodeAdapter node;

    private Object userObject = "no text";
    private String xmlText = "no text";
    private String noteText;
    private String xmlNoteText;

    public NodeContentService(NodeAdapter node) {
        this.node = node;
    }

    public String getText() {
        if (userObject == null) {
            return "";
        }
        return userObject.toString();
    }

    public void setText(String text) {
        if (text == null) {
            userObject = null;
            xmlText = null;
            return;
        }
        userObject = HtmlTools.makeValidXml(text);
        xmlText = HtmlTools.getInstance().toXhtml((String) userObject);
    }

    public String getXmlText() {
        return xmlText;
    }

    public void setXmlText(String pXmlText) {
        this.xmlText = HtmlTools.makeValidXml(pXmlText);
        userObject = HtmlTools.getInstance().toHtml(xmlText);
    }

    public String getXmlNoteText() {
        return xmlNoteText;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setXmlNoteText(String pXmlNoteText) {
        if (pXmlNoteText == null) {
            xmlNoteText = null;
            noteText = null;
            return;
        }
        this.xmlNoteText = HtmlTools.makeValidXml(pXmlNoteText);
        noteText = HtmlTools.getInstance().toHtml(xmlNoteText);
    }

    public void setNoteText(String pNoteText) {
        if (pNoteText == null) {
            xmlNoteText = null;
            noteText = null;
            return;
        }
        this.noteText = HtmlTools.makeValidXml(pNoteText);
        this.xmlNoteText = HtmlTools.getInstance().toXhtml(noteText);
    }

    public String getPlainTextContent() {
        // Default impl. MindMapNodeModel overrides at the NodeAdapter level to convert HTML→plain.
        return toString();
    }

    public String getShortText() {
        String adaptedText = node.getPlainTextContent();
        if (adaptedText.length() > 40)
            adaptedText = adaptedText.substring(0, 40) + " ...";
        return adaptedText;
    }

    @Override
    public String toString() {
        return getText();
    }
}
