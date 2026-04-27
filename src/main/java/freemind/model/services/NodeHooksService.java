package freemind.model.services;

import freemind.extensions.NodeHook;
import freemind.extensions.PermanentNodeHook;
import freemind.model.NodeAdapter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Owns the per-node hooks collections: all installed hooks ({@code hooks}) and the subset
 * currently invoked ({@code activatedHooks}). Provides hook lifecycle (add / invoke / remove).
 * Cross-tree event distribution for {@code onAddChildren} / {@code onRemoveChildren} stays
 * on {@link NodeAdapter} because it's coupled to {@code insert} / {@code remove} (tree
 * mutations) and walks ancestors via the public {@link NodeAdapter#getActivatedHooks()}.
 */
@Slf4j
public class NodeHooksService {

    private final NodeAdapter node;

    private List<PermanentNodeHook> hooks = null;          // lazy
    private HashSet<PermanentNodeHook> activatedHooks = null; // lazy

    public NodeHooksService(NodeAdapter node) {
        this.node = node;
    }

    public PermanentNodeHook addHook(PermanentNodeHook hook) {
        if (hook == null) {
            throw new IllegalArgumentException("Added null hook.");
        }
        if (hooks == null) {
            hooks = new ArrayList<>();
        }
        hooks.add(hook);
        return hook;
    }

    public void invokeHook(NodeHook hook) {
        hook.startupMapHook();
        hook.setNode(node);
        try {
            hook.invoke(node);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return;
        }
        if (hook instanceof PermanentNodeHook) {
            if (activatedHooks == null) {
                activatedHooks = new HashSet<>();
            }
            activatedHooks.add((PermanentNodeHook) hook);
        } else {
            hook.shutdownMapHook();
        }
    }

    public List<PermanentNodeHook> getHooks() {
        if (hooks == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(hooks);
    }

    public Collection<PermanentNodeHook> getActivatedHooks() {
        if (activatedHooks == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableCollection(activatedHooks);
    }

    public void removeHook(PermanentNodeHook hook) {
        // The order is crucial: shutdown must run before the activatedHooks removal so the
        // shutdown method can perform "nodeChanged" calls without re-entering its own update.
        String name = hook.getName();
        if (activatedHooks == null) {
            activatedHooks = new HashSet<>();
        }
        if (activatedHooks.contains(hook)) {
            activatedHooks.remove(hook);
            if (activatedHooks.isEmpty()) {
                activatedHooks = null;
            }
            hook.shutdownMapHook();
        }
        if (hooks == null) {
            hooks = new ArrayList<>();
        }
        hooks.remove(hook);
        if (hooks.isEmpty()) {
            hooks = null;
        }
        log.trace("Removed hook {} at {}.", name, hook);
    }

    public void removeAllHooks() {
        int timeout = getHooks().size() * 2;
        while (!getHooks().isEmpty() && timeout-- > 0) {
            PermanentNodeHook hook = getHooks().get(0);
            try {
                removeHook(hook);
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
            }
        }
    }
}
