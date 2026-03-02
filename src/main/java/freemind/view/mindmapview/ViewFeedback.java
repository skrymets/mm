package freemind.view.mindmapview;

import freemind.controller.*;
import freemind.main.Resources;
import freemind.model.MindMapNode;

import java.awt.*;
import java.awt.event.MouseWheelEvent;

/**
 * ViewFeedback is an interface implemented by the ModeController classes
 * to offer view related methods.
 */
public interface ViewFeedback {

    void changeSelection(NodeView pNode, boolean pIsSelected);

    void onLostFocusNode(NodeView pNode);

    void onFocusNode(NodeView pNode);

    void setFolded(MindMapNode pModel, boolean pFold);

    void onViewCreatedHook(NodeView pNewView);

    void onViewRemovedHook(NodeView pNodeView);

    /**
     * @return the setting of freemind.properties resp. auto.properties.
     */
    String getProperty(String pResourceId);

    Font getDefaultFont();

    NodeMouseMotionListener getNodeMouseMotionListener();

    NodeMotionListener getNodeMotionListener();

    NodeKeyListener getNodeKeyListener();

    NodeDragListener getNodeDragListener();

    NodeDropListener getNodeDropListener();

    MapMouseMotionListener getMapMouseMotionListener();

    MapMouseWheelListener getMapMouseWheelListener();

    interface MouseWheelEventHandler {
        /**
         * @return true if the event was sucessfully processed and false if the
         * event did not apply.
         */
        boolean handleMouseWheelEvent(MouseWheelEvent e);
    }

    void registerMouseWheelEventHandler(MouseWheelEventHandler handler);

    void deRegisterMouseWheelEventHandler(MouseWheelEventHandler handler);

    /**
     * @return the Resources instance for accessing properties and localized strings.
     */
    Resources getResources();

}
