package freemind.modes.browsemode;

import freemind.model.EdgeAdapter;
import freemind.model.MindMapNode;
import freemind.modes.MapFeedback;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.*;

public class BrowseEdgeModel extends EdgeAdapter {

    public BrowseEdgeModel(MindMapNode node, MapFeedback pMapFeedback) {
        super(node, pMapFeedback);
    }

    public Element save(Document doc) {
        return null;
    }

    public void setColor(Color color) {
        super.setColor(color);
    }

    public void setStyle(String style) {
        super.setStyle(style);
    }
}
