package freemind.modes;

import freemind.extensions.NodeHook;
import freemind.main.Resources;
import freemind.model.MindMap;
import freemind.model.MindMapNode;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.ViewFeedback;

import java.util.List;

/**
 * This interface describes the services, the {@link ModeController} provides to
 * a MindMap and its descendants. Extends {@link freemind.model.MapFeedback}
 * with view/hook/property-listener concerns required by the modes layer.
 */
public interface ModeFeedback extends freemind.model.MapFeedback {

    void paste(MindMapNode pNode, MindMapNode pParent);

    /**
     * Show the message to the user.
     */
    void out(String pFormat);

    /**
     * MapFeedback and MindMap are closely intertwined.
     */
    MindMap getMap();

    NodeHook createNodeHook(String pLoadName, MindMapNode pNode);

    void invokeHooksRecursively(MindMapNode pNode, MindMap pModel);

    /**
     * @return the MapView, if a view is attached, null otherwise.
     */
    MapView getMapView();

    /**
     * @return null, if no feedback is available.
     */
    ViewFeedback getViewFeedback();

    void sortNodesByDepth(List<MindMapNode> inPlaceList);

    /**
     * @return the Resources instance for accessing properties, translations, and file paths.
     */
    Resources getResources();

}
