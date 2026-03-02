package freemind.modes.mindmapmode.services;

import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import freemind.view.mindmapview.MainView;
import freemind.view.mindmapview.NodeView;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SystemUtils;

import java.awt.event.MouseEvent;

/**
 * Service for mouse-driven selection operations on mind map nodes.
 * Extracted from MindMapController to reduce class size.
 */
@Slf4j
public class SelectionService {

    private final MindMapController controller;

    public SelectionService(MindMapController controller) {
        this.controller = controller;
    }

    /**
     * Handles a plain (single) click on a node. If the click is in the follow-link
     * region, loads the URL; otherwise toggles folding or starts editing for leaf nodes.
     */
    public void plainClick(MouseEvent e) {
        if (controller.getSelecteds().size() != 1) {
            return;
        }
        final MainView component = (MainView) e.getComponent();
        if (component.isInFollowLinkRegion(e.getX())) {
            controller.loadURL();
        } else {
            MindMapNode node = component.getNodeView().getModel();
            if (!node.hasChildren()) {
                doubleClick(e);
                return;
            }
            controller.toggleFolded();
        }
    }

    /**
     * Handles a double-click on a node, starting inline editing if conditions are met.
     */
    public void doubleClick(MouseEvent e) {
        if (controller.getSelecteds().size() != 1) {
            return;
        }
        MindMapNode node = ((MainView) e.getComponent()).getNodeView().getModel();
        if (!e.isAltDown() && !e.isControlDown() && !e.isShiftDown()
                && !e.isPopupTrigger() && e.getButton() == MouseEvent.BUTTON1
                && (node.getLink() == null)) {
            controller.edit(null, false, false);
        }
    }

    /**
     * Extends the current selection based on modifier keys (Ctrl for toggle,
     * Shift for continuous range, Alt for branch selection).
     *
     * @return true if the selection was changed
     */
    public boolean extendSelection(MouseEvent e) {
        NodeView newlySelectedNodeView = ((MainView) e.getComponent()).getNodeView();
        boolean extend = e.isControlDown();
        if (SystemUtils.IS_OS_MAC) {
            extend |= e.isMetaDown();
        }
        boolean range = e.isShiftDown();
        boolean branch = e.isAltGraphDown() || e.isAltDown();
        boolean retValue = false;

        if (extend || range || branch || !controller.getView().getSelectionService().isSelected(newlySelectedNodeView)) {
            if (!range) {
                if (extend) {
                    controller.getView().getSelectionService().toggleSelected(newlySelectedNodeView);
                } else {
                    controller.select(newlySelectedNodeView);
                }
                retValue = true;
            } else {
                retValue = controller.getView().getSelectionService().selectContinuous(newlySelectedNodeView);
            }
            if (branch) {
                controller.getView().getSelectionService().selectBranch(newlySelectedNodeView, extend);
                retValue = true;
            }
        }

        if (retValue) {
            e.consume();

            String link = newlySelectedNodeView.getModel().getLink();
            if (link != null) {
                controller.getController().getFrame().setStatusText(link);
            }
        }
        log.trace("MouseEvent: extend:{}, range:{}, branch:{}, event:{}, retValue:{}",
                extend, range, branch, e, retValue);
        controller.obtainFocusForSelected();
        return retValue;
    }
}
