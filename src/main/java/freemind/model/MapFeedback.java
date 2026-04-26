package freemind.model;

import freemind.preferences.FreemindPropertyListener;

import java.awt.Font;

/**
 * Model-layer view of feedback callbacks the mode/controller layer provides
 * to the model. Contains only the methods that model code calls -- see the
 * extending {@code freemind.modes.ModeFeedback} for view/hook/property-listener
 * methods.
 */
public interface MapFeedback {

    String getResourceString(String pTextId);

    String getProperty(String pResourceId);

    int getIntProperty(String key, int defaultValue);

    void setProperty(String pProperty, String pValue);

    Font getDefaultFont();

    Font getFontThroughMap(Font pFont);

    void fireNodePreDeleteEvent(MindMapNode node);

    void fireNodePostDeleteEvent(MindMapNode node, MindMapNode parent);

    void fireRecursiveNodeCreateEvent(MindMapNode node);

    void firePreSaveEvent(MindMapNode pNode);

    void nodeChanged(MindMapNode node);

    void nodeRefresh(MindMapNode node);

    /**
     * Registers a listener that will be notified when a FreeMind property changes.
     * Note: this method is included in the model interface because adapter classes
     * (EdgeAdapter, LineAdapter, CloudAdapter) register property listeners during
     * construction.
     */
    void addPropertyChangeListener(FreemindPropertyListener listener);
}
