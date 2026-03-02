package freemind.modes.services;

import freemind.events.FreeMindEventBus;
import freemind.events.NodeSelectionChangedEvent;
import freemind.extensions.PermanentNodeHook;
import freemind.model.MindMapNode;
import freemind.modes.ModeController.NodeLifetimeListener;
import freemind.modes.ModeController.NodeSelectionListener;
import freemind.view.mindmapview.NodeView;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Manages node selection listeners and node lifetime listeners,
 * firing events for node focus changes, creation, deletion, and saves.
 * Extracted from ControllerAdapter to reduce god-object complexity.
 */
@Slf4j
public class NodeLifecycleService {

    private final HashSet<NodeSelectionListener> selectionListeners = new HashSet<>();
    private final HashSet<NodeLifetimeListener> lifetimeListeners = new HashSet<>();
    private FreeMindEventBus eventBus;

    public void setEventBus(FreeMindEventBus eventBus) {
        this.eventBus = eventBus;
    }

    // --- Selection listener management ---

    public void addSelectionListener(NodeSelectionListener listener) {
        selectionListeners.add(listener);
    }

    public void removeSelectionListener(NodeSelectionListener listener) {
        selectionListeners.remove(listener);
    }

    // --- Lifetime listener management ---

    public void addLifetimeListener(NodeLifetimeListener listener) {
        lifetimeListeners.add(listener);
    }

    public void removeLifetimeListener(NodeLifetimeListener listener) {
        lifetimeListeners.remove(listener);
    }

    public HashSet<NodeLifetimeListener> getLifetimeListeners() {
        return lifetimeListeners;
    }

    // --- Node update ---

    public void updateNode(MindMapNode node) {
        for (NodeSelectionListener listener : selectionListeners) {
            listener.onUpdateNodeHook(node);
        }
    }

    // --- Focus / selection events ---

    public void onLostFocusNode(NodeView node) {
        try {
            HashSet<NodeSelectionListener> copy = new HashSet<>(selectionListeners);
            for (NodeSelectionListener listener : copy) {
                listener.onLostFocusNode(node);
            }
            for (PermanentNodeHook hook : node.getModel().getActivatedHooks()) {
                hook.onLostFocusNode(node);
            }
            if (eventBus != null) {
                eventBus.post(new NodeSelectionChangedEvent(node.getModel(), false));
            }
        } catch (RuntimeException e) {
            log.info("Error in node selection listeners", e);
        }
    }

    public void onFocusNode(NodeView node) {
        try {
            HashSet<NodeSelectionListener> copy = new HashSet<>(selectionListeners);
            for (NodeSelectionListener listener : copy) {
                listener.onFocusNode(node);
            }
            for (PermanentNodeHook hook : node.getModel().getActivatedHooks()) {
                hook.onFocusNode(node);
            }
            if (eventBus != null) {
                eventBus.post(new NodeSelectionChangedEvent(node.getModel(), true));
            }
        } catch (RuntimeException e) {
            log.info("Error in node selection listeners", e);
        }
    }

    public void changeSelection(NodeView pNode, boolean pIsSelected) {
        try {
            HashSet<NodeSelectionListener> copy = new HashSet<>(selectionListeners);
            for (NodeSelectionListener listener : copy) {
                listener.onSelectionChange(pNode, pIsSelected);
            }
        } catch (RuntimeException e) {
            log.info("Error in node selection listeners", e);
        }
    }

    // --- View hook events ---

    public void onViewCreatedHook(NodeView node) {
        for (PermanentNodeHook hook : node.getModel().getActivatedHooks()) {
            hook.onViewCreatedHook(node);
        }
    }

    public void onViewRemovedHook(NodeView node) {
        for (PermanentNodeHook hook : node.getModel().getActivatedHooks()) {
            hook.onViewRemovedHook(node);
        }
    }

    // --- Registration with initial callback ---

    public void registerNodeSelectionListener(NodeSelectionListener listener,
                                              boolean pCallWithCurrentSelection,
                                              NodeView selectedView,
                                              Iterable<NodeView> allSelecteds) {
        selectionListeners.add(listener);
        if (pCallWithCurrentSelection) {
            try {
                listener.onFocusNode(selectedView);
            } catch (RuntimeException e) {
                log.error(e.getLocalizedMessage(), e);
            }
            for (NodeView view : allSelecteds) {
                try {
                    listener.onSelectionChange(view, true);
                } catch (RuntimeException e) {
                    log.error(e.getLocalizedMessage(), e);
                }
            }
        }
    }

    public void registerNodeLifetimeListener(NodeLifetimeListener listener,
                                             boolean pFireCreateEvent,
                                             MindMapNode rootNode) {
        lifetimeListeners.add(listener);
        if (pFireCreateEvent) {
            fireRecursiveNodeCreateEvent(rootNode);
        }
    }

    // --- Lifetime events ---

    public void fireNodePreDeleteEvent(MindMapNode node) {
        for (NodeLifetimeListener listener : lifetimeListeners) {
            listener.onPreDeleteNode(node);
        }
    }

    public void fireNodePostDeleteEvent(MindMapNode node, MindMapNode parent) {
        for (NodeLifetimeListener listener : lifetimeListeners) {
            listener.onPostDeleteNode(node, parent);
        }
    }

    public void fireRecursiveNodeCreateEvent(MindMapNode node) {
        for (Iterator<MindMapNode> i = node.childrenUnfolded(); i.hasNext(); ) {
            MindMapNode child = i.next();
            fireRecursiveNodeCreateEvent(child);
        }
        for (NodeLifetimeListener listener : lifetimeListeners) {
            listener.onCreateNodeHook(node);
        }
    }

    public void firePreSaveEvent(MindMapNode node) {
        HashSet<NodeSelectionListener> listenerCopy = new HashSet<>(selectionListeners);
        for (NodeSelectionListener listener : listenerCopy) {
            listener.onSaveNode(node);
        }
    }
}
