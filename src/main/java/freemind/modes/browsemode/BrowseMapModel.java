package freemind.modes.browsemode;

import freemind.model.EdgeAdapter;
import freemind.model.MapAdapter;
import freemind.model.MindMap;
import freemind.model.NodeAdapter;
import freemind.modes.*;
import lombok.Getter;

import java.io.File;
import java.io.Writer;
import java.net.URL;
import java.util.Objects;

@SuppressWarnings("serial")
public class BrowseMapModel extends MapAdapter {

    private static final String ENCRYPTED_BROWSE_NODE = EncryptedBrowseNode.class
            .getName();
    private URL url;
    //
    // Other methods
    //
    @Getter
    private final MindMapLinkRegistry linkRegistry;

    public BrowseMapModel(BrowseNodeModel root, ModeController modeController) {
        super(modeController);
        setRoot(Objects.requireNonNullElseGet(root, () -> new BrowseNodeModel(modeController.getResourceString(
                "new_mindmap"), this)));
        // register new LinkRegistryAdapter
        linkRegistry = new MindMapLinkRegistry();
    }

    public String toString() {
        if (getURL() == null) {
            return null;
        } else {
            return getURL().toString();
        }
    }

    public File getFile() {
        return null;
    }

    protected void setFile() {
    }

    /**
     * Get the value of url.
     *
     * @return Value of url.
     */
    public URL getURL() {
        return url;
    }

    /**
     * Set the value of url.
     *
     * @param v Value to assign to url.
     */
    public void setURL(URL v) {
        this.url = v;
    }

    public boolean save(File file) {
        return true;
    }

    public boolean isSaved() {
        return true;
    }

    public void setLinkInclinationChanged() {
    }

    public void getXml(Writer fileout) {
        throw new UnsupportedOperationException("Not implemented for browse mode.");
    }

    public void getFilteredXml(Writer fileout) {
        throw new UnsupportedOperationException("Not implemented for browse mode.");
    }

    protected NodeAdapter createNodeAdapter(MapFeedback pMapFeedback, String nodeClass) {
        if (nodeClass == ENCRYPTED_BROWSE_NODE) {
            return new EncryptedBrowseNode(null, pMapFeedback);
        }
        return new BrowseNodeModel(null, pMapFeedback.getMap());
    }

    public EdgeAdapter createEdgeAdapter(NodeAdapter node) {
        return new BrowseEdgeModel(node, mMapFeedback);
    }

    public CloudAdapter createCloudAdapter(NodeAdapter node) {
        return new BrowseCloudModel(node, mMapFeedback);
    }

    public ArrowLinkAdapter createArrowLinkAdapter(NodeAdapter source,
                                                   NodeAdapter target) {
        return new BrowseArrowLinkModel(source, target, mMapFeedback);
    }

    public ArrowLinkTarget createArrowLinkTarget(NodeAdapter source,
                                                 NodeAdapter target) {
        return null;
    }

    public NodeAdapter createEncryptedNode(String additionalInfo) {
        NodeAdapter node = createNodeAdapter(mMapFeedback, ENCRYPTED_BROWSE_NODE);
        node.setAdditionalInfo(additionalInfo);
        return node;
    }

    @Override
    public NodeAdapter createNodeAdapter(MindMap pMap, String pNodeClass) {
        return createNodeAdapter(mMapFeedback, null);
    }

}
