package freemind.modes.browsemode;

import freemind.model.MindMapNode;
import freemind.modes.CloudAdapter;
import freemind.modes.MapFeedback;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class BrowseCloudModel extends CloudAdapter {

    public BrowseCloudModel(MindMapNode node, MapFeedback pMapFeedback) {
        super(node, pMapFeedback);
    }

    public Element save(Document doc) {
        return null;
    }

}
