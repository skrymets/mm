package freemind.modes;

import freemind.extensions.NodeHook;
import freemind.main.Resources;
import freemind.model.MindMap;
import freemind.model.MindMapNode;
import freemind.preferences.FreemindPropertyListener;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.ViewFeedback;

import java.awt.*;
import java.util.List;

/**
 * This interface describes the services, the {@link ModeController} provides to
 * a MindMap and its descendants.
 */
public interface MapFeedback {
    /**
     * Is issued before a node is deleted. It is issued via NodeLifetimeListener.
     */
    void fireNodePreDeleteEvent(MindMapNode node);

    /**
     * Is issued after a node is deleted. It is issued via NodeLifetimeListener.
     */
    void fireNodePostDeleteEvent(MindMapNode node, MindMapNode parent);

    void firePreSaveEvent(MindMapNode pNode);

    /**
     * Invoke this method after you've changed how a node is to be represented in the tree.
     */
    void nodeChanged(MindMapNode node);

    void nodeRefresh(MindMapNode node);

    void fireRecursiveNodeCreateEvent(MindMapNode node);

    void paste(MindMapNode pNode, MindMapNode pParent);

    /**
     * @return the string from Resources_<lang>.properties belonging to the pResourceId.
     */
    String getResourceString(String pTextId);

    /**
     * @return the setting of freemind.properties resp. auto.properties.
     */
    String getProperty(String pResourceId);

    int getIntProperty(String key, int defaultValue);

    void setProperty(String pProperty, String pValue);

    /**
     * Show the message to the user.
     *
     */
    void out(String pFormat);

    Font getDefaultFont();

    Font getFontThroughMap(Font pFont);

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
     * Registers a listener that will be notified when a FreeMind property changes.
     */
    void addPropertyChangeListener(FreemindPropertyListener listener);

    /**
     * @return the Resources instance for accessing properties, translations, and file paths.
     */
    Resources getResources();

}

