package freemind.main;

import freemind.controller.MindMapNodesSelection;
import freemind.model.EdgeAdapter;
import freemind.model.MindMapNode;
import freemind.modes.MindIcon;
import freemind.modes.mindmapmode.MindMapController;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;

/**
 * Domain-specific utility methods for mind map operations such as node
 * manipulation, clipboard handling, edge parsing, and ID generation.
 * Extracted from {@link Tools}.
 */
@Slf4j
public final class MindMapUtils {

    private static final Random ran = new Random();

    private MindMapUtils() {
        // utility class
    }

    /**
     * Simple data holder pairing two {@link MindMapNode}s, typically used for
     * clone/corresponding-node relationships.
     */
    public static class MindMapNodePair {

        final MindMapNode first;
        final MindMapNode second;

        public MindMapNodePair(MindMapNode first, MindMapNode second) {
            this.first = first;
            this.second = second;
        }

        public MindMapNode getCorresponding() {
            return first;
        }

        public MindMapNode getCloneNode() {
            return second;
        }
    }

    /**
     * Returns a filename-safe version of the node's plain text content by
     * stripping problematic characters.
     */
    public static String getFileNameProposal(MindMapNode node) {
        String rootText = node.getPlainTextContent();
        rootText = rootText.replaceAll("[&:/\\\\\0%$#~\\?\\*]+", "");
        return rootText;
    }

    /**
     * Builds a hierarchy string for the given node, e.g.
     * {@code "child <- parent <- root"}.
     */
    public static String getNodeTextHierarchy(MindMapNode pNode,
                                              MindMapController pMindMapController) {
        return pNode.getShortText(pMindMapController)
                + ((pNode.isRoot()) ? "" : (" <- " + getNodeTextHierarchy(
                pNode.getParentNode(), pMindMapController)));
    }

    /**
     * Returns the system clipboard.
     */
    public static Clipboard getClipboard() {
        return Toolkit.getDefaultToolkit().getSystemClipboard();
    }

    /**
     * @return a list of MindMapNodes if they are currently contained in the
     *         clipboard. An empty list otherwise.
     */
    public static List<MindMapNode> getMindMapNodesFromClipboard(MindMapController pMindMapController) {
        List<MindMapNode> mindMapNodes = new ArrayList<>();
        Transferable clipboardContents = pMindMapController.getClipboardContents();
        if (clipboardContents != null) {
            try {
                @SuppressWarnings("unchecked")
                List<String> transferData = (List<String>) clipboardContents.getTransferData(MindMapNodesSelection.copyNodeIdsFlavor);
                for (String nodeId : transferData) {
                    MindMapNode node = pMindMapController.getNodeFromID(nodeId);
                    mindMapNodes.add(node);
                }
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
        return mindMapNodes;
    }

    /**
     * Returns the index of the first icon with the given name, or -1 if not
     * found.
     */
    public static int iconFirstIndex(MindMapNode node, String iconName) {
        List<MindIcon> icons = node.getIcons();
        for (ListIterator<MindIcon> i = icons.listIterator(); i.hasNext(); ) {
            MindIcon nextIcon = i.next();
            if (iconName.equals(nextIcon.getName())) {
                return i.previousIndex();
            }
        }
        return -1;
    }

    /**
     * Returns the index of the last icon with the given name, or -1 if not
     * found.
     */
    public static int iconLastIndex(MindMapNode node, String iconName) {
        List<MindIcon> icons = node.getIcons();
        ListIterator<MindIcon> i = icons.listIterator(icons.size());
        while (i.hasPrevious()) {
            MindIcon nextIcon = i.previous();
            if (iconName.equals(nextIcon.getName())) {
                return i.nextIndex();
            }
        }
        return -1;
    }

    /**
     * Converts an edge width string (e.g. "thin", or a number) to an integer.
     */
    public static int edgeWidthStringToInt(String value) {
        if (value == null) {
            return EdgeAdapter.DEFAULT_WIDTH;
        }
        if (value.equals(EdgeAdapter.EDGE_WIDTH_THIN_STRING)) {
            return EdgeAdapter.WIDTH_THIN;
        }
        return Integer.valueOf(value).intValue();
    }

    /**
     * Extracts the file name from a restorable string of the form
     * {@code "mode:filename"}.
     */
    public static String getFileNameFromRestorable(String restoreable) {
        StringTokenizer token = new StringTokenizer(restoreable, ":");
        String fileName;
        if (token.hasMoreTokens()) {
            token.nextToken();
            // fix for windows (??, fc, 25.11.2005).
            fileName = token.nextToken("").substring(1);
        } else {
            fileName = null;
        }
        return fileName;
    }

    /**
     * Extracts the mode name from a restorable string of the form
     * {@code "mode:filename"}.
     */
    public static String getModeFromRestorable(String restoreable) {
        StringTokenizer token = new StringTokenizer(restoreable, ":");
        String mode;
        if (token.hasMoreTokens()) {
            mode = token.nextToken();
        } else {
            mode = null;
        }
        return mode;
    }

    /**
     * Generates a unique ID. If the proposed ID is already in the map (or
     * {@code null}), a random one with the given prefix is generated.
     */
    public static String generateID(String proposedID, Map map,
                                    String prefix) {
        String myProposedID = (proposedID != null) ? proposedID : "";
        String returnValue;
        do {
            if (!myProposedID.isEmpty()) {
                // there is a proposal:
                returnValue = myProposedID;
                // this string is tried only once:
                myProposedID = "";
            } else {
                /*
                 * The prefix is to enable the id to be an ID in the sense of
                 * XML/DTD.
                 */
                returnValue = prefix
                        + ran.nextInt(2000000000);
            }
        } while (map.containsKey(returnValue));
        return returnValue;
    }

    /**
     * Looks up a named public field value across an array of objects. Returns
     * {@code null} if the field is not found on any object.
     */
    public static Object getField(Object[] pObjects, String pField)
            throws IllegalArgumentException, SecurityException,
            IllegalAccessException, NoSuchFieldException {
        for (Object object : pObjects) {
            for (int j = 0; j < object.getClass().getFields().length; j++) {
                Field f = object.getClass().getFields()[j];
                if (Objects.equals(pField, f.getName())) {
                    return object.getClass().getField(pField).get(object);
                }
            }
        }
        return null;
    }
}
