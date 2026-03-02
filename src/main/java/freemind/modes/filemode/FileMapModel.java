package freemind.modes.filemode;

import freemind.main.FreeMindMain;
import freemind.model.*;
import freemind.modes.*;
import lombok.Getter;

import java.io.File;
import java.io.Writer;

@Getter
@SuppressWarnings("serial")
public class FileMapModel extends MapAdapter {

    //
    // Other methods
    //
    private final MindMapLinkRegistry linkRegistry;

    //
    // Constructors
    //

    public FileMapModel(FreeMindMain frame, ModeController modeController) {
        this(new File(File.separator), frame, modeController);
    }

    public FileMapModel(File root, FreeMindMain frame,
                        ModeController modeController) {
        super(modeController);
        setRoot(new FileNodeModel(root, this));
        getRootNode().setFolded(false);
        linkRegistry = new MindMapLinkRegistry();
    }

    //
    // Other methods
    //
    public boolean save(File file) {
        return true;
    }

    public void destroy() {
        /*
         * fc, 8.8.2004: don't call super.destroy as this method tries to remove
         * the hooks recursively. This must fail.
         */
        // super.destroy();
        cancelFileChangeObservationTimer();
    }

    public boolean isSaved() {
        return true;
    }

    public String toString() {
        return "File: " + getRoot().toString();
    }

    public void changeNode(MindMapNode node, String newText) {
        // File file = ((FileNodeModel)node).getFile();
        // File newFile = new File(file.getParentFile(), newText);
        // file.renameTo(newFile);
        // System.out.println(file);
        // FileNodeModel parent = (FileNodeModel)node.getParent();
        // // removeNodeFromParent(node);

        // insertNodeInto(new FileNodeModel(newFile),parent,0);

        // nodeChanged(node);
    }

    public void setLinkInclinationChanged() {
    }

    public void getXml(Writer fileout) {
        // nothing.
        throw new UnsupportedOperationException("Not implemented for file mode.");
    }

    public void getFilteredXml(Writer fileout) {
        // nothing.
        throw new UnsupportedOperationException("Not implemented for file mode.");
    }

    @Override
    public NodeAdapter createNodeAdapter(MindMap pMap, String pNodeClass) {
        throw new UnsupportedOperationException("Not implemented for file mode.");
    }

    @Override
    public EdgeAdapter createEdgeAdapter(NodeAdapter pNode) {
        throw new UnsupportedOperationException("Not implemented for file mode.");
    }

    @Override
    public CloudAdapter createCloudAdapter(NodeAdapter pNode) {
        throw new UnsupportedOperationException("Not implemented for file mode.");
    }

    @Override
    public ArrowLinkAdapter createArrowLinkAdapter(NodeAdapter pSource,
                                                   NodeAdapter pTarget) {
        throw new UnsupportedOperationException("Not implemented for file mode.");
    }

    @Override
    public ArrowLinkTarget createArrowLinkTarget(NodeAdapter pSource,
                                                 NodeAdapter pTarget) {
        throw new UnsupportedOperationException("Not implemented for file mode.");
    }

    @Override
    public NodeAdapter createEncryptedNode(String pAdditionalInfo) {
        throw new UnsupportedOperationException("Not implemented for file mode.");
    }

}

// public class FileSystemModel extends AbstractTreeTableModel
// implements TreeTableModel {

// // The the returned file length for directories.
// public static final Integer ZERO = new Integer(0);

// //
// // Some convenience methods.
// //

// protected File getFile(Object node) {
// FileNode fileNode = ((FileNode)node);
// return fileNode.getFile();
// }

// protected Object[] getChildren(Object node) {
// FileNode fileNode = ((FileNode)node);
// return fileNode.getChildren();
// }

// //
// // The TreeModel interface
// //

// public int getChildCount(Object node) {
// Object[] children = getChildren(node);
// return (children == null) ? 0 : children.length;
// }

// public Object getChild(Object node, int i) {
// return getChildren(node)[i];
// }
// }
