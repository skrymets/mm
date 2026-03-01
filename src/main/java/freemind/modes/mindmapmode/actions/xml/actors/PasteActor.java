/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2014 Christian Foltin, Joerg Mueller, Daniel Polansky, Dimitri Polivaev and others.
 *
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

package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.MindMapNodesSelection;
import freemind.controller.actions.*;
import freemind.extensions.PermanentNodeHook;
import freemind.main.*;
import freemind.main.HtmlTools.NodeCreator;
import freemind.model.MapAdapter;
import freemind.model.MindMapNode;
import freemind.model.NodeAdapter;
import freemind.modes.ControllerAdapter;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.ModeController;
import freemind.modes.mindmapmode.MindMapNodeModel;
import freemind.modes.mindmapmode.actions.xml.ActionPair;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.capitalize;

/**
 * @author foltin
 * {@code @date} 20.03.2014
 */
@Slf4j
public class PasteActor extends XmlActorAdapter {

    public PasteActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public void act(XmlAction action) {
        PasteNodeAction pasteAction = (PasteNodeAction) action;
        _paste(getTransferable(pasteAction.getTransferableContent()), getNodeFromID(pasteAction.getNode()), pasteAction.isAsSibling(), pasteAction.isIsLeft());
    }

    public Class<PasteNodeAction> getDoActionClass() {
        return PasteNodeAction.class;
    }

    /**
     * @return a new PasteNodeAction.
     */
    public PasteNodeAction getPasteNodeAction(Transferable t, NodeCoordinate coord, UndoPasteNodeAction pUndoAction) {
        PasteNodeAction pasteAction = new PasteNodeAction();
        final String targetId = getNodeID(coord.target);
        pasteAction.setNode(targetId);
        pasteAction.setTransferableContent(getTransferableContent(t, pUndoAction));
        pasteAction.setAsSibling(coord.asSibling);
        pasteAction.setIsLeft(coord.isLeft);
        if (pUndoAction != null) {
            pUndoAction.setNode(targetId);
            pUndoAction.setAsSibling(coord.asSibling);
            pUndoAction.setIsLeft(coord.isLeft);
            if (log.isTraceEnabled()) {
                String s = Tools.marshall(pUndoAction);
                log.trace("Undo action: {}", s);
            }
        }
        return pasteAction;
    }

    public void paste(MindMapNode node, MindMapNode parent) {
        if (node == null) {
            return;
        }

        insertNodeInto(node, parent);
        getExMapFeedback().getMap().nodeStructureChanged(parent);
    }

    /**
     * @param t         the content
     * @param target    where to add the content
     * @param asSibling if true, the content is added beside the target, otherwise as
     *                  new children
     * @param isLeft    if something is pasted as a sibling to root, it must be
     *                  decided on which side of root
     * @return true, if successfully executed.
     */
    public boolean paste(Transferable t, MindMapNode target, boolean asSibling, boolean isLeft) {
        UndoPasteNodeAction undoAction = new UndoPasteNodeAction();
        PasteNodeAction pasteAction;
        pasteAction = getPasteNodeAction(t, new NodeCoordinate(target, asSibling, isLeft), undoAction);
        // Undo-action
        /*
         * how to construct the undo action for a complex paste? a) Paste pastes
         * a number of new nodes that are adjacent. This number should be
         * determined.
         *
         *
         * d) But, as there are many possibilities which data flavor is pasted,
         * it has to be determined before, which one will be taken.
         */
        return getExMapFeedback().doTransaction("paste", new ActionPair(pasteAction, undoAction));
    }

    public static class NodeCoordinate {

        public final MindMapNode target;
        public final boolean asSibling;
        public final boolean isLeft;

        public NodeCoordinate(MindMapNode target, boolean asSibling, boolean isLeft) {
            this.target = target;
            this.asSibling = asSibling;
            this.isLeft = isLeft;
        }

        public MindMapNode getNode() {
            if (asSibling) {
                MindMapNode parentNode = target.getParentNode();
                return (MindMapNode) parentNode.getChildAt(parentNode.getChildPosition(target) - 1);
            } else {
                log.trace("getChildCount = {}, target = {}", target.getChildCount(), target);
                return (MindMapNode) target.getChildAt(target.getChildCount() - 1);
            }
        }

        public NodeCoordinate(MindMapNode node, boolean isLeft) {
            this.isLeft = isLeft;
            MindMapNode parentNode = node.getParentNode();
            int childPosition = parentNode.getChildPosition(node);
            if (childPosition == parentNode.getChildCount() - 1) {
                target = parentNode;
                asSibling = false;
            } else {
                target = (MindMapNode) parentNode.getChildAt(childPosition + 1);
                asSibling = true;
            }
        }
    }

    private interface DataFlavorHandler {

        void paste(Object TransferData, MindMapNode target, boolean asSibling, boolean isLeft, Transferable t) throws UnsupportedFlavorException, IOException;

        DataFlavor getDataFlavor();
    }

    private class FileListFlavorHandler implements DataFlavorHandler {

        public void paste(Object TransferData, MindMapNode target, boolean asSibling, boolean isLeft, Transferable t) {
            // TODO: Does not correctly interpret asSibling.
            List<File> fileList = (List<File>) TransferData;
            for (File file : fileList) {
                MindMapNode node = getExMapFeedback().newNode(file.getName(), target.getMap());
                node.setLeft(isLeft);
                node.setLink(Tools.fileToRelativeUrlString(file, getExMapFeedback().getMap().getFile()));
                insertNodeInto((MindMapNodeModel) node, target, asSibling, isLeft, false);
                // addUndoAction(node);
            }
        }

        public DataFlavor getDataFlavor() {
            return MindMapNodesSelection.fileListFlavor;
        }
    }

    private class MindMapNodesFlavorHandler implements DataFlavorHandler {

        public void paste(Object TransferData, MindMapNode target, boolean asSibling, boolean isLeft, Transferable t) {
            String textFromClipboard = (String) TransferData;
            if (textFromClipboard != null) {
                String[] textLines = textFromClipboard.split(ModeController.NODESEPARATOR);
                if (textLines.length > 1) {
                    setWaitingCursor(true);
                }
                // and now? paste it:
                StringBuilder mapContent = new StringBuilder(MapAdapter.MAP_INITIAL_START + FreeMind.XML_VERSION + "\"><node TEXT=\"DUMMY\">");
                for (String textLine : textLines) {
                    mapContent.append(textLine);
                }
                mapContent.append("</node></map>");
                // log.info("Pasting " + mapContent);
                try {
                    MindMapNode node = getExMapFeedback().getMap().loadTree(new Tools.StringReaderCreator(mapContent.toString()), MapAdapter.sDontAskInstance);
                    for (ListIterator<MindMapNode> i = node.childrenUnfolded(); i.hasNext(); ) {
                        MindMapNodeModel importNode = (MindMapNodeModel) i.next();
                        insertNodeInto(importNode, target, asSibling, isLeft, true);
                        // addUndoAction(importNode);
                    }
                    for (ListIterator<MindMapNode> i = node.childrenUnfolded(); i.hasNext(); ) {
                        MindMapNodeModel importNode = (MindMapNodeModel) i.next();
                        getExMapFeedback().invokeHooksRecursively(importNode, getExMapFeedback().getMap());
                    }
                    for (ListIterator<MindMapNode> i = node.childrenUnfolded(); i.hasNext(); ) {
                        MindMapNodeModel importNode = (MindMapNodeModel) i.next();
                        processUnfinishedLinksInHooks(importNode);
                    }
                } catch (Exception e) {
                    log.error(e.getLocalizedMessage(), e);
                }
            }
        }

        public DataFlavor getDataFlavor() {
            return MindMapNodesSelection.mindMapNodesFlavor;
        }
    }

    private static final Pattern HREF_PATTERN = Pattern.compile("<html>\\s*<body>\\s*<a\\s+href=\"([^>]+)\">(.*)</a>\\s*</body>\\s*</html>");

    private class DirectHtmlFlavorHandler implements DataFlavorHandler {

        private NodeCreator mNodeCreator;

        /**
         * @param pNodeCreator the nodeCreator to set
         */
        public void setNodeCreator(NodeCreator pNodeCreator) {
            mNodeCreator = pNodeCreator;
        }

        /**
         *
         */
        private DirectHtmlFlavorHandler() {
            mNodeCreator = new NodeCreator() {

                @Override
                public MindMapNode createChild(MindMapNode pParent) {
                    MindMapNode node = getExMapFeedback().newNode("", getExMapFeedback().getMap());
                    insertNodeInto(node, pParent);
                    node.setParent(pParent);
                    getExMapFeedback().nodeChanged(pParent);
                    return node;
                }

                @Override
                public void setText(String pText, MindMapNode pNode) {
                    pNode.setText(pText);
                    getExMapFeedback().nodeChanged(pNode);
                }

                @Override
                public void setLink(String pLink, MindMapNode pNode) {
                    pNode.setLink(pLink);
                    getExMapFeedback().nodeChanged(pNode);
                }
            };
        }

        public void paste(Object transferData, MindMapNode target, boolean asSibling, boolean isLeft, Transferable t) {
            String textFromClipboard = (String) transferData;
            // workaround for java decoding bug
            // http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6740877
            textFromClipboard = textFromClipboard.replace((char) 65533, ' ');
//			if (textFromClipboard.charAt(0) == 65533) {
//				throw new UnsupportedFlavorException(
//						MindMapNodesSelection.htmlFlavor);
//			}
            // ^ This outputs transfer data to standard output. I don't know
            // why.
            // { Alternative pasting of HTML
            setWaitingCursor(true);
            log.trace("directHtmlFlavor (original): {}", textFromClipboard);
            textFromClipboard = textFromClipboard.replaceAll("(?i)(?s)<meta[^>]*>", "").replaceAll("(?i)(?s)<head>.*?</head>", "").replaceAll("(?i)(?s)</?html[^>]*>", "").replaceAll("(?i)(?s)</?body[^>]*>", "").replaceAll("(?i)(?s)<script.*?>.*?</script>", "")
                    // Java HTML Editor
                    // does not like
                    // the tag.
                    .replaceAll("(?i)(?s)</?tbody.*?>", "").
                    // Java HTML Editor
                    // shows comments in
                    // not very nice
                    // manner.
                            replaceAll("(?i)(?s)<!--.*?-->", "").
                    // Java HTML Editor
                    // does not like
                    // Microsoft Word's
                    // <o> tag.
                            replaceAll("(?i)(?s)</?o[^>]*>", "");
            textFromClipboard = "<html><body>" + textFromClipboard + "</body></html>";
            log.trace("directHtmlFlavor: {}", textFromClipboard);
            if (mMapFeedback.getResources().getBoolProperty(FreeMind.RESOURCES_PASTE_HTML_STRUCTURE)) {
                HtmlTools.getInstance().insertHtmlIntoNodes(textFromClipboard, target, mNodeCreator);
            } else {
                if (Objects.equals(getExMapFeedback().getProperty("cut_out_pictures_when_pasting_html"), "true")) {
                    textFromClipboard = textFromClipboard.replaceAll("(?i)(?s)<img[^>]*>", "");
                } // Cut out images.

                textFromClipboard = HtmlTools.unescapeHTMLUnicodeEntity(textFromClipboard);

                MindMapNode node = getExMapFeedback().newNode(textFromClipboard, getExMapFeedback().getMap());
                // if only one <a>...</a> element found, set link
                Matcher m = HREF_PATTERN.matcher(textFromClipboard);
                if (m.matches()) {
                    final String body = m.group(2);
                    if (!body.matches(".*<\\s*a.*")) {
                        final String href = m.group(1);
                        node.setLink(href);
                    }
                }

                insertNodeInto(node, target);
                // addUndoAction(node);
            }
            setWaitingCursor(false);
        }

        public DataFlavor getDataFlavor() {
            return MindMapNodesSelection.htmlFlavor;
        }
    }

    private class StringFlavorHandler implements DataFlavorHandler {

        public void paste(Object TransferData, MindMapNode target, boolean asSibling, boolean isLeft, Transferable t) throws UnsupportedFlavorException, IOException {
            pasteStringWithoutRedisplay(t, target, asSibling, isLeft);
        }

        public DataFlavor getDataFlavor() {
            return DataFlavor.stringFlavor;
        }
    }

    private class ImageFlavorHandler implements DataFlavorHandler {

        public void paste(Object transferData, MindMapNode target, boolean asSibling, boolean isLeft, Transferable t) throws IOException {
            log.info("imageFlavor");

            setWaitingCursor(true);

            File mindmapFile = getExMapFeedback().getMap().getFile();
            String imgfile;
            if (mindmapFile == null) {
                JOptionPane.showMessageDialog(getExMapFeedback().getMapView().getSelected(), getExMapFeedback().getResourceString("map_not_saved"), "FreeMind", JOptionPane.ERROR_MESSAGE);
                return;
            }
            File parentFile = mindmapFile.getParentFile();
            StringBuilder filePrefix = new StringBuilder(mindmapFile.getName().replace(FreeMindCommon.FREEMIND_FILE_EXTENSION, "_"));
			/* prefix for createTempFile must be at least three characters long.
			 See  [bugs:#1261] Unable to paste images from clipboard */
            while (filePrefix.length() < 3) {
                filePrefix.append("_");
            }
            File tempFile = File.createTempFile(filePrefix.toString(), ".jpeg", parentFile);
            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.write(EncryptionUtils.fromBase64(transferData.toString()));
            fos.close();

            // Absolute, if not in the correct directory!
            imgfile = tempFile.getName();
            log.info("Writing image to {}", imgfile);

            String strText = "<html><body><img src=\"" + imgfile + "\"/></body></html>";

            MindMapNode node = getExMapFeedback().newNode(strText, getExMapFeedback().getMap());
            // if only one <a>...</a> element found, set link

            insertNodeInto(node, target);
            // addUndoAction(node);
            setWaitingCursor(false);

        }

        public DataFlavor getDataFlavor() {
            return DataFlavor.imageFlavor;
        }
    }

    /*
     *
     */
    private void _paste(Transferable t, MindMapNode target, boolean asSibling, boolean isLeft) {
        if (t == null) {
            return;
        }
        // Uncomment to print obtained data flavors

        /*
         * DataFlavor[] fl = t.getTransferDataFlavors(); for (int i = 0; i <
         * fl.length; i++) { System.out.println(fl[i]); }
         */
        DataFlavorHandler[] dataFlavorHandlerList = getFlavorHandlers();
        for (DataFlavorHandler handler : dataFlavorHandlerList) {
            DataFlavor flavor = handler.getDataFlavor();
            if (t.isDataFlavorSupported(flavor)) {
                try {
                    handler.paste(t.getTransferData(flavor), target, asSibling, isLeft, t);
                    break;
                } catch (UnsupportedFlavorException | IOException e) {
                    log.error(e.getLocalizedMessage(), e);
                }
            }
        }
        setWaitingCursor(false);
    }

    /**
     *
     */
    private DataFlavorHandler[] getFlavorHandlers() {
        DataFlavorHandler[] dataFlavorHandlerList = new DataFlavorHandler[]{new FileListFlavorHandler(), new MindMapNodesFlavorHandler(), new DirectHtmlFlavorHandler(), new StringFlavorHandler(), new ImageFlavorHandler()};
        // %%% Make dependent on an option?: new HtmlFlavorHandler(),
        return dataFlavorHandlerList;
    }

    public MindMapNodeModel pasteXMLWithoutRedisplay(String pasted, MindMapNode target, boolean asSibling, boolean changeSide, boolean isLeft, HashMap<String, NodeAdapter> pIDToTarget) {
        // Call nodeStructureChanged(target) after this function.
        log.trace("Pasting {} to {}", pasted, target);
        try {
            MindMapNodeModel node = (MindMapNodeModel) getExMapFeedback().getMap().createNodeTreeFromXml(new StringReader(pasted), pIDToTarget);
            insertNodeInto(node, target, asSibling, isLeft, changeSide);
            getExMapFeedback().invokeHooksRecursively(node, getExMapFeedback().getMap());
            processUnfinishedLinksInHooks(node);
            return node;
        } catch (IOException ee) {
            log.error(ee.getLocalizedMessage(), ee);
            return null;
        }
    }

    private void insertNodeInto(MindMapNodeModel node, MindMapNode target, boolean asSibling, boolean isLeft, boolean changeSide) {
        MindMapNode parent;
        if (asSibling) {
            parent = target.getParentNode();
        } else {
            parent = target;
        }
        if (changeSide) {
            node.setParent(parent);
            node.setLeft(isLeft);
        }
        // now, the import is finished. We can inform others about the new
        // nodes:
        if (asSibling) {
            insertNodeInto(node, parent, parent.getChildPosition(target));
        } else {
            insertNodeInto(node, target);
        }
    }

    static final Pattern nonLinkCharacter = Pattern.compile("[ \n()'\",;]");

    /**
     * Paste String (as opposed to other flavors)
     * <p>
     * Split the text into lines; determine the new tree structure by the number
     * of leading spaces in lines. In case that trimmed line starts with
     * protocol (http:, https:, ftp:), create a link with the same content.
     * <p>
     * If there was only one line to be pasted, return the pasted node, null
     * otherwise.
     *
     */
    private MindMapNode pasteStringWithoutRedisplay(Transferable t, MindMapNode parent, boolean asSibling, boolean isLeft) throws UnsupportedFlavorException, IOException {

        String textFromClipboard = (String) t.getTransferData(DataFlavor.stringFlavor);
        Pattern mailPattern = Pattern.compile("([^@ <>\\*']+@[^@ <>\\*']+)");

        String[] textLines = textFromClipboard.split("\n");

        if (textLines.length > 1) {
            setWaitingCursor(true);
        }

        if (asSibling) {
            // When pasting as sibling, we use virtual node as parent. When the
            // pasting to
            // virtual node is completed, we insert the children of that virtual
            // node to
            // the parent of real parent.
            parent = new MindMapNodeModel(getExMapFeedback().getMap());
        }

        ArrayList<MindMapNode> parentNodes = new ArrayList<>();
        ArrayList<Integer> parentNodesDepths = new ArrayList<>();

        parentNodes.add(parent);
        parentNodesDepths.add(-1);

        String[] linkPrefixes = {"ftp://", "https://"};

        MindMapNode pastedNode = null;

        for (String textLine : textLines) {
            String text = textLine;
//			System.out.println("Text to paste: "+text);
            text = text.replaceAll("\t", "        ");
            if (text.matches(" *")) {
                continue;
            }

            int depth = 0;
            while (depth < text.length() && text.charAt(depth) == ' ') {
                ++depth;
            }
            StringBuilder visibleText = new StringBuilder(text.trim());

            // If the text is a recognizable link (e.g.
            // http://www.google.com/index.html),
            // make it more readable by look nicer by cutting off obvious prefix
            // and other
            // transforamtions.

            if (visibleText.toString().matches("^http://(www\\.)?[^ ]*$")) {
                visibleText = new StringBuilder(visibleText.toString().replaceAll("^http://(www\\.)?", "").replaceAll("(/|\\.[^\\./\\?]*)$", "").replaceAll("((\\.[^\\./]*\\?)|\\?)[^/]*$", " ? ...").replaceAll("_|%20", " "));
                String[] textParts = visibleText.toString().split("/");
                visibleText = new StringBuilder();
                for (int textPartIdx = 0; textPartIdx < textParts.length; textPartIdx++) {
                    if (textPartIdx > 0) {
                        visibleText.append(" > ");
                    }
                    visibleText.append(textPartIdx == 0 ? textParts[textPartIdx] : capitalize(textParts[textPartIdx].replaceAll("^~*", "")));
                }
            }

            MindMapNode node = getExMapFeedback().newNode(visibleText.toString(), parent.getMap());
            if (textLines.length == 1) {
                pastedNode = node;
            }

            // Heuristically determine, if there is a mail.

            Matcher mailMatcher = mailPattern.matcher(visibleText.toString());
            if (mailMatcher.find()) {
                node.setLink("mailto:" + mailMatcher.group());
            }

            // Heuristically determine, if there is a link. Because this is
            // heuristic, it is probable that it can be improved to include
            // some matches or exclude some matches.

            for (String linkPrefix : linkPrefixes) {
                int linkStart = text.indexOf(linkPrefix);
                if (linkStart != -1) {
                    int linkEnd = linkStart;
                    while (linkEnd < text.length() && !nonLinkCharacter.matcher(text.substring(linkEnd, linkEnd + 1)).matches()) {
                        linkEnd++;
                    }
                    node.setLink(text.substring(linkStart, linkEnd));
                }
            }

            // Determine parent among candidate parents
            // Change the array of candidate parents accordingly

            for (int j = parentNodes.size() - 1; j >= 0; --j) {
                if (depth > parentNodesDepths.get(j).intValue()) {
                    for (int k = j + 1; k < parentNodes.size(); ++k) {
                        MindMapNode n = parentNodes.get(k);
                        if (n.getParentNode() == parent) {
                            // addUndoAction(n);
                        }
                        parentNodes.remove(k);
                        parentNodesDepths.remove(k);
                    }
                    MindMapNode target = parentNodes.get(j);
                    node.setLeft(isLeft);
                    insertNodeInto(node, target);
                    parentNodes.add(node);
                    parentNodesDepths.add(Integer.valueOf(depth));
                    break;
                }
            }
        }

        for (MindMapNode n : parentNodes) {
            if (n.getParentNode() == parent) {
                // addUndoAction(n);
            }
        }
        return pastedNode;
    }

    /**
     *
     */
    private void insertNodeInto(MindMapNodeModel node, MindMapNode parent, int i) {
        getExMapFeedback().insertNodeInto(node, parent, i);
    }

    private void insertNodeInto(MindMapNode node, MindMapNode parent) {
        getExMapFeedback().insertNodeInto(node, parent, parent.getChildCount());
    }

    private TransferableContent getTransferableContent(Transferable t, UndoPasteNodeAction pUndoAction) {
        boolean amountAlreadySet = false;
        try {
            TransferableContent trans = new TransferableContent();
            if (t.isDataFlavorSupported(MindMapNodesSelection.fileListFlavor)) {
                /*
                 * Since the JAXB-generated interface TransferableContent
                 * doesn't supply a setTranserableAsFileList method, we have to
                 * get the fileList, clear it, and then set it to the new value.
                 */
                List<File> fileList = (List<File>) t.getTransferData(MindMapNodesSelection.fileListFlavor);
                for (File fileName : fileList) {
                    TransferableFile transferableFile = new TransferableFile();
                    transferableFile.setFileName(fileName.getAbsolutePath());
                    trans.addTransferableFile(transferableFile);
                }
                if (pUndoAction != null && !amountAlreadySet) {
                    pUndoAction.setNodeAmount(fileList.size());
                    amountAlreadySet = true;
                }
            }
            if (t.isDataFlavorSupported(MindMapNodesSelection.mindMapNodesFlavor)) {
                String textFromClipboard;
                textFromClipboard = (String) t.getTransferData(MindMapNodesSelection.mindMapNodesFlavor);
                trans.setTransferable(HtmlTools.makeValidXml(textFromClipboard));
                if (pUndoAction != null && !amountAlreadySet) {
                    pUndoAction.setNodeAmount(StringUtils.countMatches(textFromClipboard, ControllerAdapter.NODESEPARATOR) + 1);
                    amountAlreadySet = true;
                }
            }
            if (t.isDataFlavorSupported(MindMapNodesSelection.htmlFlavor)) {
                String textFromClipboard;
                textFromClipboard = (String) t.getTransferData(MindMapNodesSelection.htmlFlavor);
                trans.setTransferableAsHtml(HtmlTools.makeValidXml(textFromClipboard));
                if (pUndoAction != null && !amountAlreadySet) {
                    // on html paste, the string text is taken and "improved".
                    // Thus, we count its lines.
                    try {
                        pUndoAction.setNodeAmount(determineAmountOfNewNodes(t));
                        amountAlreadySet = true;
                    } catch (Exception e) {
                        log.error(e.getLocalizedMessage(), e);
                        // ok, something went wrong, but this breaks undo, only.
                        pUndoAction.setNodeAmount(1);
                    }
                }
            }
            if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String textFromClipboard;
                textFromClipboard = (String) t.getTransferData(DataFlavor.stringFlavor);
                trans.setTransferableAsPlainText(HtmlTools.makeValidXml(textFromClipboard));
                if (pUndoAction != null && !amountAlreadySet) {
                    // determine amount of new nodes using the algorithm:
                    final int childCount = determineAmountOfNewTextNodes(t);
                    pUndoAction.setNodeAmount(childCount);
                    amountAlreadySet = true;
                }
            }
            if (t.isDataFlavorSupported(MindMapNodesSelection.rtfFlavor)) {
                // byte[] textFromClipboard = (byte[])
                // t.getTransferData(MindMapNodesSelection.rtfFlavor);
                // trans.setTransferableAsRTF(textFromClipboard.toString());
            }
            if (t.isDataFlavorSupported(DataFlavor.imageFlavor)) {
                log.info("image...");

                try {
                    // Get data from clipboard and assign it to an image.
                    // clipboard.getData() returns an object, so we need to cast
                    // it to a BufferdImage.
                    BufferedImage image = (BufferedImage) t.getTransferData(DataFlavor.imageFlavor);

                    log.info("Starting to write clipboard image {}", image);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(image, "jpg", baos);
                    String base64String = EncryptionUtils.toBase64(baos.toByteArray());
                    trans.setTransferableAsImage(base64String);


                    if (pUndoAction != null && !amountAlreadySet) {
                        pUndoAction.setNodeAmount(1);
                        amountAlreadySet = true;
                    }
                } // getData throws this.
                catch (UnsupportedFlavorException | IOException e) {
                    log.error(e.getLocalizedMessage(), e);
                }

            }
            return trans;
        } catch (UnsupportedFlavorException | IOException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return null;
    }

    /*
     * TODO: This is a bit dirty here. Better would be to separate the algorithm
     * from the node creation and use the pure algo.
     */
    protected int determineAmountOfNewTextNodes(Transferable t) throws UnsupportedFlavorException, IOException {
        // create a new node for testing purposes.
        MindMapNodeModel parent = new MindMapNodeModel(getExMapFeedback().getMap());
        pasteStringWithoutRedisplay(t, parent, false, false);
        final int childCount = parent.getChildCount();
        return childCount;
    }


    /**
     * Only for HTML nodes.
     *
     */
    public int determineAmountOfNewNodes(Transferable t) throws UnsupportedFlavorException, IOException {
        // create a new node for testing purposes.
        MindMapNodeModel parent = new MindMapNodeModel(getExMapFeedback().getMap());
        parent.setText("ROOT");
        DirectHtmlFlavorHandler handler = new DirectHtmlFlavorHandler();
        // creator, that only creates dummy nodes.
        handler.setNodeCreator(new NodeCreator() {

            @Override
            public MindMapNode createChild(MindMapNode pParent) {
                try {
                    MindMapNodeModel newNode = new MindMapNodeModel("", getExMapFeedback().getMap());
                    pParent.insert(newNode, pParent.getChildCount());
                    newNode.setParent(pParent);
                    return newNode;
                } catch (Exception e) {
                    log.error(e.getLocalizedMessage(), e);
                }
                return null;
            }

            @Override
            public void setText(String pText, MindMapNode pNode) {
                pNode.setText(pText);
            }

            @Override
            public void setLink(String pLink, MindMapNode pNode) {
            }
        });
        handler.paste(t.getTransferData(handler.getDataFlavor()), parent, false, true, t);
        final int childCount = parent.getChildCount();
        return childCount;
    }

    private Transferable getTransferable(TransferableContent trans) {
        // create Transferable:
        // Add file list to this selection.
        List<File> fileList = new ArrayList<>();
        for (TransferableFile tFile : trans.getTransferableFileList()) {
            fileList.add(new File(tFile.getFileName()));
        }
        Transferable copy = new MindMapNodesSelection(trans.getTransferable(), trans.getTransferableAsImage(), trans.getTransferableAsPlainText(), trans.getTransferableAsRTF(), trans.getTransferableAsHtml(), trans.getTransferableAsDrop(), fileList, null);
        return copy;
    }

    protected void setWaitingCursor(boolean waitingCursor) {
        getExMapFeedback().setWaitingCursor(waitingCursor);
    }

    /**
     *
     */
    public void processUnfinishedLinksInHooks(MindMapNode node) {
        for (Iterator<MindMapNode> i = node.childrenUnfolded(); i.hasNext(); ) {
            MindMapNode child = i.next();
            processUnfinishedLinksInHooks(child);
        }
        for (PermanentNodeHook hook : node.getHooks()) {
            hook.processUnfinishedLinks();
        }
    }


}
