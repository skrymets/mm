package freemind.controller;

import lombok.extern.slf4j.Slf4j;

import java.awt.datatransfer.*;
import java.io.ByteArrayInputStream;
import java.util.List;

@Slf4j
public class MindMapNodesSelection implements Transferable, ClipboardOwner {

    private final String nodesContent;
    private final String stringContent;
    private final String imageContent;
    private final String rtfContent;
    private final String htmlContent;
    private String dropActionContent;
    private final List fileList;
    private final List<String> nodeIdsContent;
    public static DataFlavor mindMapNodesFlavor = null;
    public static DataFlavor rtfFlavor = null;
    public static DataFlavor htmlFlavor = null;
    public static DataFlavor fileListFlavor = null;
    /**
     * fc, 7.8.2004: This is a quite interesting flavor, but how does it
     * works???
     */
    public static DataFlavor dropActionFlavor = null;
    /**
     * This flavor contains the node ids only. Thus, it works only on the same
     * map.
     */
    public static DataFlavor copyNodeIdsFlavor = null;

    static {
        try {
            mindMapNodesFlavor = new DataFlavor("text/freemind-nodes; class=java.lang.String");
            rtfFlavor = new DataFlavor("text/rtf; class=java.io.InputStream");
            htmlFlavor = new DataFlavor("text/html; class=java.lang.String");
            fileListFlavor = new DataFlavor("application/x-java-file-list; class=java.util.List");
            dropActionFlavor = new DataFlavor("text/drop-action; class=java.lang.String");
            copyNodeIdsFlavor = new DataFlavor("application/freemind-node-ids; class=java.util.List");
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }

    //
    public MindMapNodesSelection(String nodesContent, String imageContent,
                                 String stringContent, String rtfContent, String htmlContent,
                                 String dropActionContent, List fileList, List<String> nodeIdsContent) {
        this.nodesContent = nodesContent;
        this.rtfContent = rtfContent;
        this.imageContent = imageContent;
        this.stringContent = stringContent;
        this.dropActionContent = dropActionContent;
        this.htmlContent = htmlContent;
        this.fileList = fileList;
        this.nodeIdsContent = nodeIdsContent;
    }

    public Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException {
        if (flavor.equals(DataFlavor.imageFlavor)) {
            return imageContent;
        }
        if (flavor.equals(DataFlavor.stringFlavor)) {
            return stringContent;
        }
        if (flavor.equals(mindMapNodesFlavor)) {
            return nodesContent;
        }
        if (flavor.equals(dropActionFlavor)) {
            return dropActionContent;
        }
        if (flavor.equals(rtfFlavor)) {
            byte[] byteArray = rtfContent.getBytes();
            // for (int i = 0; i < byteArray.length; ++i) {
            // System.out.println(byteArray[i]); }

            return new ByteArrayInputStream(byteArray);
        }
        if (flavor.equals(htmlFlavor) && htmlContent != null) {
            return htmlContent;
        }
        if (flavor.equals(fileListFlavor)) {
            return fileList;
        }
        if (flavor.equals(copyNodeIdsFlavor)) {
            return nodeIdsContent;
        }
        throw new UnsupportedFlavorException(flavor);
    }

    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{DataFlavor.imageFlavor,
                DataFlavor.stringFlavor, mindMapNodesFlavor, rtfFlavor,
                htmlFlavor, dropActionFlavor, copyNodeIdsFlavor};
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        if (flavor.equals(DataFlavor.imageFlavor) && imageContent != null) {
            return true;
        }
        if (flavor.equals(DataFlavor.stringFlavor) && stringContent != null) {
            return true;
        }
        if (flavor.equals(mindMapNodesFlavor) && nodesContent != null) {
            return true;
        }
        if (flavor.equals(rtfFlavor) && rtfContent != null) {
            return true;
        }
        if (flavor.equals(dropActionFlavor) && dropActionContent != null) {
            return true;
        }
        if (flavor.equals(htmlFlavor) && htmlContent != null) {
            return true;
        }
        if (flavor.equals(fileListFlavor) && (fileList != null)
                && !fileList.isEmpty()) {
            return true;
        }
        if (flavor.equals(copyNodeIdsFlavor) && nodeIdsContent != null) {
            return true;
        }
        return false;
    }

    public void lostOwnership(Clipboard clipboard, Transferable contents) {
    }

    public void setDropAction(String dropActionContent) {
        this.dropActionContent = dropActionContent;
    }
}
