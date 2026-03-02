package freemind.modes.browsemode;

import freemind.model.MindMap;
import freemind.model.NodeAdapter;

import java.util.LinkedList;

/**
 * This class represents a single Node of a Tree. It contains direct handles to
 * its parent and children and to its view.
 */
public class BrowseNodeModel extends NodeAdapter {

    //
    // Constructors
    //

    public BrowseNodeModel(Object userObject, MindMap map) {
        super(userObject, map);
        children = new LinkedList<>();
        setEdge(new BrowseEdgeModel(this, getMapFeedback()));
    }

    public boolean isWriteable() {
        return false;
    }
}
