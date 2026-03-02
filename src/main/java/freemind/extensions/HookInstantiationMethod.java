package freemind.extensions;

import freemind.model.MindMapNode;
import freemind.modes.MapFeedback;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class HookInstantiationMethod {
    private interface DestinationNodesGetter {
        Collection<MindMapNode> getDestinationNodes(MapFeedback controller, MindMapNode focussed, List<MindMapNode> selecteds);

        MindMapNode getCenterNode(MapFeedback controller, MindMapNode focussed, List<MindMapNode> selecteds);
    }

    private static class DefaultDestinationNodesGetter implements DestinationNodesGetter {
        public Collection<MindMapNode> getDestinationNodes(MapFeedback controller, MindMapNode focussed, List<MindMapNode> selecteds) {
            return selecteds;
        }

        public MindMapNode getCenterNode(MapFeedback controller,
                                         MindMapNode focussed, List<MindMapNode> selecteds) {
            return focussed;
        }
    }

    private static class RootDestinationNodesGetter implements DestinationNodesGetter {
        public Collection<MindMapNode> getDestinationNodes(MapFeedback controller, MindMapNode focussed, List<MindMapNode> selecteds) {
            List<MindMapNode> returnValue = new ArrayList<>();
            returnValue.add(controller.getMap().getRootNode());
            return returnValue;
        }

        public MindMapNode getCenterNode(MapFeedback controller, MindMapNode focussed, List<MindMapNode> selecteds) {
            return controller.getMap().getRootNode();
        }
    }

    private static class AllDestinationNodesGetter implements
            DestinationNodesGetter {
        private void addChilds(MindMapNode node, Collection<MindMapNode> allNodeCollection) {
            allNodeCollection.add(node);
            for (Iterator<MindMapNode> i = node.childrenFolded(); i.hasNext(); ) {
                MindMapNode child = i.next();
                addChilds(child, allNodeCollection);
            }
        }

        public Collection<MindMapNode> getDestinationNodes(MapFeedback controller, MindMapNode focussed, List<MindMapNode> selecteds) {
            List<MindMapNode> returnValue = new ArrayList<>();
            addChilds(controller.getMap().getRootNode(), returnValue);
            return returnValue;
        }

        public MindMapNode getCenterNode(MapFeedback controller, MindMapNode focussed, List<MindMapNode> selecteds) {
            return focussed;
        }

    }

    @Getter
    private final boolean isSingleton;
    private final DestinationNodesGetter getter;
    /**
     * -- GETTER --
     *
     */
    @Getter
    private final boolean isPermanent;
    @Getter
    private final boolean isUndoable;

    private HookInstantiationMethod(boolean isPermanent, boolean isSingleton,
                                    DestinationNodesGetter getter, boolean isUndoable) {
        this.isPermanent = isPermanent;
        this.isSingleton = isSingleton;
        this.getter = getter;
        this.isUndoable = isUndoable;
    }

    static final public HookInstantiationMethod Once = new HookInstantiationMethod(
            true, true, new DefaultDestinationNodesGetter(), true);
    /**
     * The hook should only be added/removed to the root node.
     */
    static final public HookInstantiationMethod OnceForRoot = new HookInstantiationMethod(
            true, true, new RootDestinationNodesGetter(), true);
    /**
     * Each (or none) node should have the hook.
     */
    static final public HookInstantiationMethod OnceForAllNodes = new HookInstantiationMethod(
            true, true, new AllDestinationNodesGetter(), true);
    /**
     * This is for MindMapHooks in general. Here, no undo- or redoaction are
     * performed, the undo information is given by the actions the hook
     * performs.
     */
    static final public HookInstantiationMethod Other = new HookInstantiationMethod(
            false, false, new DefaultDestinationNodesGetter(), false);
    /**
     * This is for MindMapHooks that wish to be applied to root, whereevery they
     * are called from. Here, no undo- or redoaction are performed, the undo
     * information is given by the actions the hook performs.
     */
    static final public HookInstantiationMethod ApplyToRoot = new HookInstantiationMethod(
            false, false, new RootDestinationNodesGetter(), false);

    static public HashMap<String, HookInstantiationMethod> getAllInstanciationMethods() {
        HashMap<String, HookInstantiationMethod> res = new HashMap<>();
        res.put("Once", Once);
        res.put("OnceForRoot", OnceForRoot);
        res.put("OnceForAllNodes", OnceForAllNodes);
        res.put("Other", Other);
        res.put("ApplyToRoot", ApplyToRoot);
        return res;
    }

    public Collection<MindMapNode> getDestinationNodes(MapFeedback controller,
                                                       MindMapNode focussed, List<MindMapNode> selecteds) {
        return getter.getDestinationNodes(controller, focussed, selecteds);
    }

    public boolean isAlreadyPresent(String hookName, MindMapNode focussed) {
        for (PermanentNodeHook hook : focussed.getActivatedHooks()) {
            if (hookName.equals(hook.getName())) {
                return true;
            }
        }
        return false;
    }

    public MindMapNode getCenterNode(MapFeedback controller,
                                     MindMapNode focussed, List<MindMapNode> selecteds) {
        return getter.getCenterNode(controller, focussed, selecteds);
    }

}
