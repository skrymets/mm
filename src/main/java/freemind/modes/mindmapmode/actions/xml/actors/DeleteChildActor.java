package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.DeleteNodeAction;
import freemind.controller.actions.PasteNodeAction;
import freemind.controller.actions.XmlAction;
import freemind.extensions.PermanentNodeHook;
import freemind.model.MindMap;
import freemind.model.MindMapNode;
import freemind.modes.ExtendedMapFeedback;
import freemind.view.mindmapview.MapView;
import freemind.modes.mindmapmode.actions.xml.ActionPair;
import freemind.modes.mindmapmode.actions.xml.actors.PasteActor.NodeCoordinate;
import freemind.view.mindmapview.NodeView;

import java.awt.datatransfer.Transferable;
import java.util.Iterator;

public class DeleteChildActor extends XmlActorAdapter {

    public DeleteChildActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public void act(XmlAction action) {
        DeleteNodeAction deleteNodeAction = (DeleteNodeAction) action;
        MindMapNode selectedNode = getNodeFromID(deleteNodeAction.getNode());
        deleteWithoutUndo(selectedNode);
    }

    public void deleteWithoutUndo(MindMapNode selectedNode) {
        if (selectedNode.isRoot()) {
            throw new IllegalArgumentException("Root node can't be deleted");
        }
        // remove hooks:
        removeHooks(selectedNode);
        MindMapNode parent = selectedNode.getParentNode();
        getExMapFeedback().fireNodePreDeleteEvent(selectedNode);
        // deregister node:
        MindMap map = getExMapFeedback().getMap();
        map.getLinkRegistry()
                .deregisterLinkTarget(selectedNode);
        // deselect
        MapView view = getExMapFeedback().getMapView();
        if (view != null) {
            NodeView nodeView = view.getViewerRegistryService().getNodeView(selectedNode);
            view.getSelectionService().deselect(nodeView);
            if (view.getSelectionService().getSelecteds().isEmpty()) {
                NodeView newSelectedView;
                int childIndex = parent.getChildPosition(selectedNode);
                if (parent.getChildCount() > childIndex + 1) {
                    // the next node
                    newSelectedView = view.getViewerRegistryService().getNodeView((MindMapNode) parent.getChildAt(childIndex + 1));
                } else if (childIndex > 0) {
                    // the node before:
                    newSelectedView = view.getViewerRegistryService().getNodeView((MindMapNode) parent.getChildAt(childIndex - 1));
                } else {
                    // no other node on same level. take the parent.
                    newSelectedView = view.getViewerRegistryService().getNodeView(parent);
                }
                view.getSelectionService().select(newSelectedView);
            }
        }
        getExMapFeedback().removeNodeFromParent(selectedNode);
        // post event
        getExMapFeedback().fireNodePostDeleteEvent(selectedNode, parent);
    }

    private void removeHooks(MindMapNode selectedNode) {
        for (Iterator<MindMapNode> it = selectedNode.childrenUnfolded(); it.hasNext(); ) {
            MindMapNode child = it.next();
            removeHooks(child);
        }
        long currentRun = 0;
        // determine timeout:
        long timeout = selectedNode.getActivatedHooks().size() * 2L + 2;
        while (!selectedNode.getActivatedHooks().isEmpty()) {
            PermanentNodeHook hook = selectedNode
                    .getActivatedHooks().iterator().next();
            selectedNode.removeHook(hook);
            if (currentRun++ > timeout) {
                throw new IllegalStateException(
                        "Timeout reached shutting down the hooks.");
            }
        }
    }

    public Class<DeleteNodeAction> getDoActionClass() {
        return DeleteNodeAction.class;
    }

    public void deleteNode(MindMapNode selectedNode) {
        if (selectedNode.isRoot()) {
            throw new IllegalArgumentException("Root node can't be deleted");
        }
        String newId = getNodeID(selectedNode);

        Transferable copy = getExMapFeedback().copy(selectedNode, true);
        NodeCoordinate coord = new NodeCoordinate(selectedNode,
                selectedNode.isLeft());
        // Undo-action
        PasteNodeAction pasteNodeAction = null;
        pasteNodeAction = getExMapFeedback().getActorFactory().
                getPasteActor().getPasteNodeAction(copy,
                        coord, null);

        DeleteNodeAction deleteAction = getDeleteNodeAction(newId);
        getExMapFeedback().doTransaction(getDoActionClass().getName(),
                new ActionPair(deleteAction, pasteNodeAction));
    }

    public DeleteNodeAction getDeleteNodeAction(String newId) {
        DeleteNodeAction deleteAction = new DeleteNodeAction();
        deleteAction.setNode(newId);
        return deleteAction;
    }

}
