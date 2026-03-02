package freemind.modes.browsemode;

import freemind.model.MindMapNode;
import freemind.modes.ArrowLinkAdapter;
import freemind.modes.MapFeedback;
import freemind.view.mindmapview.MapView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class BrowseArrowLinkModel extends ArrowLinkAdapter {

    public BrowseArrowLinkModel(MindMapNode source, MindMapNode target,
                                MapFeedback pMapFeedback) {
        super(source, target, pMapFeedback);
    }

    /* maybe this method is wrong here, but ... */
    public Object clone() {
        return super.clone();
    }

    public Element save(Document doc) {
        return null;
    }

    public String toString() {
        return "Source=" + getSource() + ", target=" + getTarget();
    }

    /**
     * @see freemind.modes.MindMapArrowLink#changeInclination(MapView, int, int,
     * int, int)
     */
    public void changeInclination(MapView map, int oldX, int oldY, int deltaX,
                                  int deltaY) {

    }

}
