/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2006  Christian Foltin <christianfoltin@users.sourceforge.net>
 *See COPYING for Details
 *
 *This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
/*$Id: HtmlTools.java,v 1.1.2.28 2010/12/04 21:07:23 christianfoltin Exp $*/

package freemind.main;

import freemind.model.MindMapNode;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.swing.text.BadLocationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
public class HtmlTools {

    public static final String NBSP = "\u00A0";

    private static HtmlTools sInstance = new HtmlTools();

    private static final Pattern HTML_PATTERN = Pattern
            .compile("(?s).*<\\s*html.*?>.*");
    private static final Pattern FIND_TAGS_PATTERN = Pattern
            .compile("([^<]*)(<[^>]+>)");
    private static final Pattern SLASHED_TAGS_PATTERN = Pattern.compile("<(("
            + "br|area|base|basefont|" + "bgsound|button|col|colgroup|embed|hr"
            + "|img|input|isindex|keygen|link|meta"
            + "|object|plaintext|spacer|wbr" + ")(\\s[^>]*)?)/>");

    private static final Pattern TAGS_PATTERN = Pattern.compile("(?s)<[^><]*>");

    private static final Pattern LEVEL_PATTERN = Pattern.compile("level([0-9]+)");

    public static final String SP = "&#160;";

    private HtmlTools() {
        super();
    }

    public static HtmlTools getInstance() {
        return sInstance;
    }

    public String toXhtml(String htmlText) {
        if (!isHtmlNode(htmlText)) {
            return null;
        }
        log.trace("Enter toXhtml with " + htmlText);
        StringReader reader = new StringReader(htmlText);
        StringWriter writer = new StringWriter();
        try {
            XHTMLWriter.html2xhtml(reader, writer);
            String resultXml = writer.toString();
            // for safety:
            if (isWellformedXml(resultXml)) {
                log.trace("Leave toXhtml with " + resultXml);
                return resultXml;
            }
        } catch (IOException e) {
            freemind.main.Resources.getInstance().logException(e);
        } catch (BadLocationException e) {
            freemind.main.Resources.getInstance().logException(e);
        }
        // fallback:
        String fallbackText = removeAllTagsFromString(htmlText);
        log.trace("Leave toXhtml with fallback " + fallbackText);
        return fallbackText;
    }

    public String toHtml(String xhtmlText) {
        // Remove '/' from <.../> of elements that do not have '/' there in HTML
        return SLASHED_TAGS_PATTERN.matcher(xhtmlText).replaceAll("<$1>");
    }

    public static class IndexPair {
        public int originalStart;
        public int originalEnd;
        public int replacedStart;
        public int replacedEnd;
        public boolean mIsTag;
        public boolean mIsAlreadyAppended = false;

        /**
         * @param pIsTag TODO
         */
        public IndexPair(int pOriginalStart, int pOriginalEnd,
                         int pReplacedStart, int pReplacedEnd, boolean pIsTag) {
            super();

            originalStart = pOriginalStart;
            originalEnd = pOriginalEnd;
            replacedStart = pReplacedStart;
            replacedEnd = pReplacedEnd;
            mIsTag = pIsTag;
        }

        /**
         * generated by CodeSugar http://sourceforge.net/projects/codesugar
         */

        public String toString() {
            StringBuffer buffer = new StringBuffer();
            buffer.append("[IndexPair:");
            buffer.append(" originalStart: ");
            buffer.append(originalStart);
            buffer.append(" originalEnd: ");
            buffer.append(originalEnd);
            buffer.append(" replacedStart: ");
            buffer.append(replacedStart);
            buffer.append(" replacedEnd: ");
            buffer.append(replacedEnd);
            buffer.append(" is a tag: ");
            buffer.append(mIsTag);
            buffer.append("]");
            return buffer.toString();
        }
    }

    /**
     * Replaces text in node content without replacing tags. fc, 19.12.06: This
     * method is very difficult. If you have a simplier method, please supply
     * it. But look that it complies with FindTextTests!!!
     */
    public String getReplaceResult(Pattern pattern, String replacement,
                                   String text) {
        ArrayList<IndexPair> splittedStringList = new ArrayList<>();

        // remove tags and denote their positions:
        {
            StringBuffer sb = new StringBuffer();
            Matcher matcher = FIND_TAGS_PATTERN.matcher(text);
            int lastMatchEnd = 0;
            while (matcher.find()) {
                String textWithoutTag = matcher.group(1);
                // Append text without tags:
                int replStart = sb.length();
                matcher.appendReplacement(sb, "$1");
                IndexPair indexPair;
                if (textWithoutTag.length() > 0) {
                    indexPair = new IndexPair(lastMatchEnd, matcher.end(1),
                            replStart, sb.length(), false);
                    lastMatchEnd = matcher.end(1);
                    // System.out.println(sb.toString()
                    // + ", "
                    // + input.substring(indexPair.originalStart,
                    // indexPair.originalEnd) + ", " + indexPair);
                    splittedStringList.add(indexPair);
                }
                // String tag = matcher.group(2);
                replStart = sb.length();
                indexPair = new IndexPair(lastMatchEnd, matcher.end(2),
                        replStart, sb.length(), true);
                lastMatchEnd = matcher.end(2);
                // System.out.println(sb.toString() + ", " +
                // input.substring(indexPair.originalStart,
                // indexPair.originalEnd)+ ", " + indexPair);
                splittedStringList.add(indexPair);
            }
            int replStart = sb.length();
            matcher.appendTail(sb);
            // append tail only if there is a tail
            if (sb.length() != replStart) {
                IndexPair indexPair = new IndexPair(lastMatchEnd,
                        text.length(), replStart, sb.length(), false);
                // System.out.println(sb.toString() + ", " + indexPair);
                splittedStringList.add(indexPair);
            }

        }

        // // give it out:
        // for (Iterator iter = splittedStringList.iterator(); iter.hasNext();)
        // {
        // IndexPair pair = (IndexPair) iter.next();
        // System.out.println(text.substring(pair.originalStart,
        // pair.originalEnd) + ", " + pair);
        // }

        /**
         * For each pair which is not a tag we find concurrences and replace
         * them, if pair is a tag then we just append
         */
        StringBuffer sbResult = new StringBuffer();
        for (IndexPair pair : splittedStringList) {
            if (pair.mIsTag)
                append(sbResult, text, pair.originalStart, pair.originalEnd);
            else {

                Matcher matcher = pattern.matcher(text.substring(
                        pair.originalStart, pair.originalEnd));
                int mStart = 0;
                int mEnd = 0;
                int mEndOld = 0;

                while (matcher.find()) {
                    mStart = matcher.start();
                    mEnd = matcher.end();

                    append(sbResult, text, pair.originalStart + mEndOld,
                            pair.originalStart + mStart);
                    /**
                     * If it's a first iteration then we append text between
                     * start and first concurrence, and when it's not first
                     * iteration (mEndOld != 0) we append text between two
                     * concurrences
                     */

                    // sbResult.append(text, pair.originalStart + mStart,
                    // pair.originalStart + mEnd);
                    // original text
                    sbResult.append(replacement);
                    mEndOld = mEnd;
                }
                append(sbResult, text, pair.originalStart + mEndOld,
                        pair.originalEnd);
                // append tail
            }
        }
        // System.out.println("Result:'"+sbResult.toString()+"'");
        return sbResult.toString();
    }

    /**
     * Need to program this, as the stringbuffer method appears in java 1.5
     * first.
     */
    private void append(StringBuffer pSbResult, String pText, int pStart,
                        int pEnd) {
        pSbResult.append(pText.substring(pStart, pEnd));
    }

    public int getMinimalOriginalPosition(int pI, ArrayList<IndexPair> pListOfIndices) {
        for (IndexPair pair : pListOfIndices) {

            if (pI >= pair.replacedStart && pI <= pair.replacedEnd) {
                return pair.originalStart + pI - pair.replacedStart;
            }
        }
        throw new IllegalArgumentException("Position " + pI + " not found.");
    }

    /**
     * @return the maximal index i such that pI is mapped to i by removing all
     * tags from the original input.
     */
    public int getMaximalOriginalPosition(int pI, ArrayList<IndexPair> pListOfIndices) {
        for (int i = pListOfIndices.size() - 1; i >= 0; --i) {
            IndexPair pair = pListOfIndices.get(i);
            if (pI >= pair.replacedStart) {
                if (!pair.mIsTag) {
                    return pair.originalStart + pI - pair.replacedStart;
                } else {
                    return pair.originalEnd;
                }
            }
        }
        throw new IllegalArgumentException("Position " + pI + " not found.");
    }

    /**
     * Searches for <html> tag in text.
     */
    public static boolean isHtmlNode(String text) {
        return HTML_PATTERN.matcher(text.toLowerCase(Locale.ENGLISH)).matches();
    }

    /**
     * Changes all unicode characters into &#xxx values.
     * Opposite to {@link HtmlTools#unescapeHTMLUnicodeEntity(String)}
     */
    public static String unicodeToHTMLUnicodeEntity(String text, boolean pPreserveNewlines) {
        /*
         * Heuristic reserve for expansion : factor 1.2
         */
        StringBuffer result = new StringBuffer((int) (text.length() * 1.2));
        int intValue;
        char myChar;
        for (int i = 0; i < text.length(); ++i) {
            myChar = text.charAt(i);
            intValue = (int) text.charAt(i);
            boolean outOfRange = intValue < 32 || intValue > 126;
            if (pPreserveNewlines && myChar == '\n') {
                outOfRange = false;
            }
            if (pPreserveNewlines && myChar == '\r') {
                outOfRange = false;
            }
            if (outOfRange) {
                result.append("&#x").append(Integer.toString(intValue, 16))
                        .append(';');
            } else {
                result.append(myChar);
            }
        }
        return result.toString();
    }

    /**
     * Converts XML unicode entity-encoded characters into plain Java unicode
     * characters; for example, ''&amp;#xff;'' gets converted. Removes all
     * XML-invalid entity characters, such as &amp;#xb;.
     * <p>
     * Opposite to {@link HtmlTools#unicodeToHTMLUnicodeEntity(String, boolean)}
     *
     * @param text input
     * @return the converted output.
     */
    public static String unescapeHTMLUnicodeEntity(String text) {
        StringBuffer result = new StringBuffer(text.length());
        StringBuffer entity = new StringBuffer();
        boolean readingEntity = false;
        char myChar;
        char entityChar;
        for (int i = 0; i < text.length(); ++i) {
            myChar = text.charAt(i);
            if (readingEntity) {
                if (myChar == ';') {
                    if (entity.charAt(0) == '#') {
                        try {
                            if (entity.charAt(1) == 'x') {
                                // Hexadecimal
                                entityChar = (char) Integer.parseInt(
                                        entity.substring(2), 16);
                            } else {
                                // Decimal
                                entityChar = (char) Integer.parseInt(
                                        entity.substring(1), 10);
                            }
                            if (isXMLValidCharacter(entityChar))
                                result.append(entityChar);
                        } catch (NumberFormatException e) {
                            result.append('&').append(entity).append(';');
                        }
                    } else {
                        result.append('&').append(entity).append(';');
                    }
                    entity.setLength(0);
                    readingEntity = false;
                } else {
                    if (isXMLValidCharacter(myChar))
                        entity.append(myChar);
                }
            } else {
                if (myChar == '&') {
                    readingEntity = true;
                } else {
                    if (isXMLValidCharacter(myChar))
                        result.append(myChar);
                }
            }
        }
        if (entity.length() > 0) {
            result.append('&').append(entity).append(';');
        }
        return result.toString();
    }

    /**
     * Removes all tags (<..>) from a string if it starts with "<html>..." to
     * make it compareable.
     */
    public static String removeHtmlTagsFromString(String text) {
        if (HtmlTools.isHtmlNode(text)) {
            return removeAllTagsFromString(text); // (?s) enables that . matches
            // newline.
        } else {
            return text;
        }
    }

    public static String removeAllTagsFromString(String text) {
        return TAGS_PATTERN.matcher(text).replaceAll("");
    }

    public static String htmlToPlain(String text) {
        return htmlToPlain(text, /* strictHTMLOnly= */true);
    }

    public static String htmlToPlain(String text, boolean strictHTMLOnly) {
        // 0. remove all newlines
        // 1. replace newlines, paragraphs, and table rows
        // 2. remove XML tags
        // 3. replace HTML entities including &nbsp;
        // 4. unescape unicode entities
        // This is a very basic conversion, fixing the most annoying
        // inconvenience. You can imagine much better conversion of
        // HTML to plain text. Most of HTML tags can be handled
        // sensibly, like web browsers do it.
        if (strictHTMLOnly && !isHtmlNode(text)) {
            return text;
        }
        // System.err.println("base:"+text);
        String intermediate = text
                .replaceAll("(?ims)[\n\t]", "")
                . // Remove newlines
                        replaceAll("(?ims) +", " ")
                . // Condense spaces
                        replaceAll("(?ims)<br.*?>", "\n")
                .replaceAll("(?ims)<p.*?>", "\n\n")
                . // Paragraph
                        replaceAll("(?ims)<div.*?>", "\n")
                . // Div - block
                        replaceAll("(?ims)<tr.*?>", "\n")
                .replaceAll("(?ims)<dt.*?>", "\n")
                . // Defined term
                        replaceAll("(?ims)<dd.*?>", "\n   ")
                . // Definition of defined term
                        replaceAll("(?ims)<td.*?>", " ")
                .replaceAll("(?ims)<[uo]l.*?>", "\n")
                . // Beginning of a list
                        replaceAll("(?ims)<li.*?>", "\n   * ")
                .replaceAll("(?ims) *</[^>]*>", ""). // Remaining closing HTML
                // tags
                        replaceAll("(?ims)<[^/][^>]*> *", ""). // Remaining opening HTML
                // tags
                // FIXME Dimitry: is removing of all new lines at the begin a
                // good idea?
                        replaceAll("^\n+", "").
                // fc: to remove start and end spaces.
                        trim();

        intermediate = HtmlTools.unescapeHTMLUnicodeEntity(intermediate);

        // Entities, with the exception of &.

        intermediate = intermediate.replaceAll("(?ims)&lt;", "<")
                .replaceAll("(?ims)&gt;", ">").replaceAll("(?ims)&quot;", "\"")
                .replaceAll("(?ims)&nbsp;", " ");
        // System.err.println("intermediate:"+intermediate);
        return intermediate.replaceAll("(?ims)&amp;", "&");
    }

    public static String plainToHTML(String text) {
        char myChar;
        String textTabsExpanded = text.replaceAll("\t", "         "); // Use
        // eight
        // spaces
        // as
        // tab
        // width.
        StringBuffer result = new StringBuffer(textTabsExpanded.length()); // Heuristic
        int lengthMinus1 = textTabsExpanded.length() - 1;
        result.append("<html><body><p>");
        for (int i = 0; i < textTabsExpanded.length(); ++i) {
            myChar = textTabsExpanded.charAt(i);
            switch (myChar) {
                case '&':
                    result.append("&amp;");
                    break;
                case '<':
                    result.append("&lt;");
                    break;
                case '>':
                    result.append("&gt;");
                    break;
                case ' ':
                    if (i > 0 && i < lengthMinus1
                            && (int) textTabsExpanded.charAt(i - 1) > 32
                            && (int) textTabsExpanded.charAt(i + 1) > 32) {
                        result.append(' ');
                    } else {
                        result.append("&nbsp;");
                    }
                    break;
                case '\n':
                    result.append("<br>");
                    break;
                default:
                    result.append(myChar);
            }
        }
        return result.toString();
    }

    public static String toXMLUnescapedText(String text) {
        return text.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
                .replaceAll("&quot;", "\"").replaceAll("&amp;", "&");
    }

    public static String toXMLEscapedTextExpandingWhitespace(String text) {
        // Spaces and tabs are handled
        text = text.replaceAll("\t", "         "); // Use eight spaces as tab
        // width.
        int len = text.length();
        StringBuffer result = new StringBuffer(len);
        char myChar;
        for (int i = 0; i < len; ++i) {
            myChar = text.charAt(i);
            switch (myChar) {
                case '&':
                    result.append("&amp;");
                    break;
                case '<':
                    result.append("&lt;");
                    break;
                case '>':
                    result.append("&gt;");
                    break;
                case ' ':
                    if (i > 0 && i < len - 1 && (int) text.charAt(i - 1) > 32
                            && (int) text.charAt(i + 1) > 32) {
                        result.append(' ');
                    } else {
                        result.append("&nbsp;");
                    }
                    break;
                default:
                    result.append(myChar);
            }
        }
        return result.toString();
    }

    public static String toXMLEscapedText(String text) {
        if (text == null) {
            return "ERROR: none";
        }
        return text.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;").replaceAll("\"", "&quot;");
    }

    /**
     * @return true, if well formed XML.
     */
    public boolean isWellformedXml(String xml) {
        try {
            // Create a builder factory
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);

            // Create the builder and parse the file
            factory.newSAXParser().parse(
                    new InputSource(new StringReader(xml)),
                    new DefaultHandler());
            return true;
        } catch (SAXParseException e) {
            log.info(
                    "XmlParseError on line " + e.getLineNumber() + " of " + xml,
                    e);
        } catch (Exception e) {
            log.info("XmlParseError", e);
        }
        return false;
    }

    /**
     * \0 is not allowed:
     */
    public static String makeValidXml(String pXmlNoteText) {
        return pXmlNoteText.replaceAll("\0", "").replaceAll("&#0;", "");
    }

    public static String replaceIllegalXmlCharacters(String fileContents) {
        // replace &xa; by newline.
        fileContents = fileContents.replaceAll("&#x0*[Aa];", "\n");
        /*
         * &#xb; is illegal, but sometimes occurs in 0.8.x maps. Thus, we
         * exclude all from 0 - 1f and replace them by nothing. TODO: Which more
         * are illegal??
         */
        fileContents = fileContents.replaceAll("&#x0*1?[0-9A-Fa-f];", "");
        // decimal: 0-31
        fileContents = fileContents.replaceAll("&#0*[1-2]?[0-9];", "");
        fileContents = fileContents.replaceAll("&#0*3[0-1];", "");
        return fileContents;
    }

    /**
     * Determines whether the character is valid in XML. Invalid characters
     * include most of the range x00-x1F, and more.
     *
     * @see http://www.w3.org/TR/2000/REC-xml-20001006#NT-Char.
     */
    public static boolean isXMLValidCharacter(char character) {
        // Order the tests in such a sequence that the most probable
        // conditions are tested first.
        return character >= 0x20 && character <= 0xD7FF || character == 0x9
                || character == 0xA || character == 0xD || character >= 0xE000
                && character <= 0xFFFD || character >= 0x10000
                && character <= 0x10FFFF;
    }

    /**
     * Precondition: The input text contains XML unicode entities rather
     * than Java unicode text.
     * <p>
     * The algorithm:
     * Search the string for XML entities. For each XML entity inspect
     * whether it is valid. If valid, append it. To be on the safe side,
     * also inspect for no-entity unicode whether it is XML-valid, and
     * pass on only XML-valid characters.
     * <p>
     * This method uses the method isXMLValidCharacter, which makes use
     * of http://www.w3.org/TR/2000/REC-xml-20001006#NT-Char.
     */
    public static String removeInvalidXmlCharacters(String text) {
        StringBuffer result = new StringBuffer(text.length());
        StringBuffer entity = new StringBuffer();
        boolean readingEntity = false;
        char myChar;
        char entityChar;
        for (int i = 0; i < text.length(); ++i) {
            myChar = text.charAt(i);
            if (readingEntity) {
                if (myChar == ';') {
                    if (entity.charAt(0) == '#') {
                        try {
                            if (entity.charAt(1) == 'x') {
                                // Hexadecimal
                                entityChar = (char) Integer.parseInt(
                                        entity.substring(2), 16);
                            } else {
                                // Decimal
                                entityChar = (char) Integer.parseInt(
                                        entity.substring(1), 10);
                            }
                            if (isXMLValidCharacter(entityChar))
                                result.append('&').append(entity).append(';');
                        } catch (NumberFormatException e) {
                            result.append('&').append(entity).append(';');
                        }
                    } else {
                        result.append('&').append(entity).append(';');
                    }
                    entity.setLength(0);
                    readingEntity = false;
                } else {
                    entity.append(myChar);
                }
            } else {
                if (myChar == '&') {
                    readingEntity = true;
                } else {
                    // The following test is superfluous under the assumption
                    // that the string only contains unicode in XML entities.
                    // Removing this test could significantly speed up this
                    // method; maybe.
                    if (isXMLValidCharacter(myChar))
                        result.append(myChar);
                }
            }
        }
        if (entity.length() > 0) {
            result.append('&').append(entity).append(';');
        }
        return result.toString();
    }

    public static String extractHtmlBody(String output) {
        if (output.startsWith("<html")) {
            output = output.substring(6); // do not write
        }
        int start = output.indexOf("<body");
        if (start == -1) {
            start = output.indexOf('>') + 1;
        } else {
            start = output.indexOf('>', start + 5) + 1;
        }
        int end = output.indexOf("</body>");
        if (end == -1) {
            end = output.indexOf("</html>");
        }
        if (end == -1) {
            end = output.length();
        }
        output = output.substring(start, end);
        return output;
    }

    /**
     * Is used from XSLT! Don't change, unless you change the freemind_version_updater.xslt, too.
     *
     * @param input
     * @return
     */
    public static String replaceSpacesToNonbreakableSpaces(String input) {
        StringBuffer result = new StringBuffer(input.length());
        boolean readingSpaces = false;
        char myChar;
        for (int i = 0; i < input.length(); ++i) {
            myChar = input.charAt(i);
            if (myChar == ' ') {
                if (readingSpaces) {
                    result.append(NBSP);
                } else {
                    result.append(myChar);
                    readingSpaces = true;
                }
            } else {
                readingSpaces = false;
                result.append(myChar);
            }
        }
        return result.toString();
    }

    public interface NodeCreator {
        MindMapNode createChild(MindMapNode pParent);

        void setText(String pText, MindMapNode pNode);

        void setLink(String pLink, MindMapNode pNode);
    }

    /**
     * @author foltin
     * @date 10.12.2014
     */
    private final class HtmlNodeVisitor implements NodeVisitor {
        boolean isNewline = true;
        int mLevel = 0;
        private MindMapNode mParentNode;
        private MindMapNode mCurrentNode = null;
        private NodeCreator mCreator;
        private boolean mFirstUl;
        private String mLink;

        /**
         * @param pParentNode
         * @param pCreator
         */
        public HtmlNodeVisitor(MindMapNode pParentNode, NodeCreator pCreator) {
            mParentNode = pParentNode;
            mCreator = pCreator;
            mFirstUl = true;
        }

        @Override
        public void head(Node node, int depth) {
            try {
                if (node instanceof TextNode) {
                    TextNode textNode = (TextNode) node;
                    String text = textNode.text().replace('\u00A0', ' ').trim();
                    if (!text.isEmpty()) {
                        if (mCurrentNode == null) {
                            // create a new sibling:
                            mCurrentNode = mCreator.createChild(mParentNode);
                        }
//						System.out.println("TEXT+: " + text);
                        mCreator.setText(mCurrentNode.getText() + text, mCurrentNode);
                        if (mLink != null) {
                            mCreator.setLink(mLink, mCurrentNode);
                            mLink = null;
                        }
                        isNewline = false;
                    }
                } else if (node instanceof Element) {
                    Element element = (Element) node;
                    if ("a".equals(element.tagName())) {
                        mLink = element.attr("href");
                    }
                    if (element.tagName().equals("ul")) {
                        createChild();
                    } else {
                        Matcher matcher = LEVEL_PATTERN.matcher(element.attr("style"));
                        if (element.tagName().equals("p") && matcher.find()) {
                            // special handling for outlook
                            int newLevel = Integer.valueOf(matcher.group(1));
                            while (newLevel > mLevel) {
//								System.out.println("Level increase from: " + mLevel + " to " + newLevel);
                                createChild();
                                mLevel++;
                            }
                            // test for other direction
                            while (newLevel < mLevel) {
//								System.out.println("Level decrease from: " + mLevel + " to " + newLevel);
                                backToParent();
                                mLevel--;
                            }
                            isNewline = false;
                        }
                        if (!isNewline) {
                            if ((element.isBlock()
                                    || element.tagName().equals("br") || element
                                    .tagName().equals("li"))) {
                                isNewline = true;
                                if (mCurrentNode == null || !mCurrentNode.getText().isEmpty()) {
                                    // next sibling, same parent, only if already content present.
                                    mCurrentNode = null;
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                freemind.main.Resources.getInstance().logException(e);
            }
        }

        @Override
        public void tail(Node node, int depth) {
            try {
                if (node instanceof Element) {
                    Element element = (Element) node;
                    if (element.tagName().equals("ul")) {
                        backToParent();
                    }
                }
            } catch (Exception e) {
                freemind.main.Resources.getInstance().logException(e);
            }
        }

        private void createChild() {
            if (mFirstUl) {
                // the first ul is ignored.
                mFirstUl = false;
            } else {
                if (mCurrentNode == null) {
                    // create one:
                    mCurrentNode = mCreator.createChild(mParentNode);
                }
                mParentNode = mCurrentNode;
                mCurrentNode = null;
            }
        }

        private void backToParent() {
            mParentNode = mParentNode.getParentNode();
        }
    }


    /**
     * Uses JSoup to parse HTML
     */
    public void insertHtmlIntoNodes(String pText, MindMapNode pParentNode, NodeCreator pCreator) {
        new NodeTraversor(new HtmlNodeVisitor(pParentNode, pCreator)).traverse(Jsoup.parse(pText));
    }
}
