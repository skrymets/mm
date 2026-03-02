package freemind.controller;

import freemind.main.Resources;
import freemind.model.MindMapNode;
import freemind.view.mindmapview.MainView;
import org.apache.commons.lang3.SystemUtils;

import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.InputEvent;

/**
 * The NodeDragListener which belongs to every NodeView
 */
public class NodeDragListener implements DragGestureListener {

    private final Controller c;
    private final Resources resources;

    public NodeDragListener(Controller controller, Resources resources) {
        c = controller;
        this.resources = resources;
    }

    public Cursor getCursorByAction(int dragAction) {
        return switch (dragAction) {
            case DnDConstants.ACTION_COPY -> DragSource.DefaultCopyDrop;
            case DnDConstants.ACTION_LINK -> DragSource.DefaultLinkDrop;
            default -> DragSource.DefaultMoveDrop;
        };
    }

    public void dragGestureRecognized(DragGestureEvent e) {
        if (!resources.getBoolProperty("draganddrop"))
            return;

        MindMapNode node = ((MainView) e.getComponent()).getNodeView().getModel();
        if (node.isRoot())
            return;

        String dragAction = "MOVE";

        Cursor cursor = getCursorByAction(e.getDragAction());

        int modifiersEx = e.getTriggerEvent().getModifiersEx();
        boolean macLinkAction = SystemUtils.IS_OS_MAC
                && ((modifiersEx & InputEvent.BUTTON1_DOWN_MASK) != 0)
                && e.getTriggerEvent().isMetaDown();

        boolean otherOsLinkAction = (modifiersEx & InputEvent.BUTTON3_DOWN_MASK) != 0;
        if (macLinkAction || otherOsLinkAction) {
            // Change drag action
            cursor = DragSource.DefaultLinkDrop;
            dragAction = "LINK";
        }

        if ((modifiersEx & InputEvent.BUTTON2_DOWN_MASK) != 0) {
            // Change drag action
            cursor = DragSource.DefaultCopyDrop;
            dragAction = "COPY";
        }

        Transferable t = c.getModeController().copy();
        // new MindMapNodesSelection("Ahoj","Ahoj","Ahoj", dragAction);
        ((MindMapNodesSelection) t).setDropAction(dragAction);
        // public void setDropAction(String dropActionContent) {

        // starts the dragging
        // DragSource dragSource = DragSource.getDefaultDragSource();

        // Initiates drag operation with empty listener handling cursor updates
        e.startDrag(cursor, t, new DragSourceListener() {
            public void dragDropEnd(DragSourceDropEvent event) {
           }

            public void dragEnter(DragSourceDragEvent event) {
            }

            public void dragExit(DragSourceEvent event) {
            }

            public void dragOver(DragSourceDragEvent event) {
            }

            public void dropActionChanged(DragSourceDragEvent event) {
                event.getDragSourceContext().setCursor(getCursorByAction(event.getUserAction()));
            }
        });
    }
}
