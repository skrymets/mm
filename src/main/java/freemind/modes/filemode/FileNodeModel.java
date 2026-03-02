package freemind.modes.filemode;

import freemind.main.Tools;
import freemind.model.MindMap;
import freemind.model.MindMapNode;
import freemind.model.NodeAdapter;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * This class represents a single Node of a Tree. It contains direct handles to
 * its parent and children and to its view.
 */
@Slf4j
public class FileNodeModel extends NodeAdapter {
    private final File file;
    private Color color;

    //
    // Constructors
    //

    public FileNodeModel(File file, MindMap map) {
        super(null, map);
        setEdge(new FileEdgeModel(this, getMapFeedback()));
        this.file = file;
        setFolded(!file.isFile());
    }

    // Overwritten get Methods
    public String getStyle() {
        return MindMapNode.STYLE_FORK;
        // // This condition shows the code is not quite logical:
        // // ordinary file should not be considered folded and
        // // therefore the clause !isLeaf() should not be necessary.
        // if (isFolded()) { // && !isLeaf()) {
        // return MindMapNode.STYLE_BUBBLE;
        // } else {
        // return MindMapNode.STYLE_FORK;
        // }
    }

    /*
     * if (file.isFile()) { return MindMapNode.STYLE_FORK; } else { return
     * MindMapNode.STYLE_BUBBLE; } }
     */

    File getFile() {
        return file;
    }

    /**
     * This could be a nice feature. Improve it!
     */
    public Color getColor() {
        if (color == null) {

            // float hue = (float)getFile().length() / 100000;
            // float hue = 6.3F;
            // if (hue > 1) {
            // hue = 1;
            // }
            // color = Color.getHSBColor(hue,0.5F, 0.5F);
            // int red = (int)(1 / (getFile().length()+1) * 255);
            // color = new Color(red,0,0);
            color = isLeaf() ? Color.BLACK : Color.GRAY;
        }
        return color;
    }

    // void setFile(File file) {
    // this.file = file;
    // }

    public String toString() {
        String name = file.getName();
        if (name.isEmpty()) {
            name = "Root";
        }
        return name;
    }

    public String getText() {
        return toString();
    }

    public boolean hasChildren() {
        return !file.isFile() || (children != null && !children.isEmpty());
    }

    public ListIterator<MindMapNode> childrenFolded() {
        if (!isRoot()) {
            if (isFolded() || isLeaf()) {
                return Collections.emptyListIterator();
                // return null;//Empty Enumeration
            }
        }
        return childrenUnfolded();
    }

    public ListIterator<MindMapNode> childrenUnfolded() {
        if (children != null) {
            return children.listIterator();
        }
        // Create new nodes by reading children from file system
        try {
            String[] files = file.list();
            if (files != null) {
                children = new LinkedList<>();

                String path = file.getPath();
                for (String s : files) {
                    File childFile = new File(path, s);
                    if (!childFile.isHidden()) {
                        final FileNodeModel fileNodeModel = new FileNodeModel(
                                childFile, getMap());
                        fileNodeModel.setLeft(isNewChildLeft());
                        insert(fileNodeModel, getChildCount());
                    }
                }
            }
        } catch (SecurityException ignored) {
        }
        // return children.listIterator();
        return children != null ? children.listIterator() : Collections.emptyListIterator();
    }

    public boolean isLeaf() {
        return file.isFile();
    }

    public String getLink() {
        try {
            return Tools.fileToUrl(file).toString();
        } catch (MalformedURLException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        return file.toString();
    }

    public boolean isWriteable() {
        return false;
    }

}
