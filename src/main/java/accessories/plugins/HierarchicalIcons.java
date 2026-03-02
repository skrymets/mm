package accessories.plugins;

import freemind.extensions.UndoEventReceiver;
import freemind.model.MindMapNode;
import freemind.modes.MindIcon;
import freemind.modes.mindmapmode.hooks.PermanentMindMapNodeHookAdapter;
import freemind.view.mindmapview.MultipleImage;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

@Slf4j
public class HierarchicalIcons extends PermanentMindMapNodeHookAdapter implements UndoEventReceiver {

    private final HashMap<MindMapNode, TreeSet<String>> nodeIconSets = new HashMap<>();

    public void shutdownMapHook() {
        // remove all icons:
        MindMapNode root = getMindMapController().getRootNode();
        removeIcons(root);
        super.shutdownMapHook();
    }

    private void removeIcons(MindMapNode node) {
        node.setStateIcon(getName(), null);
        getMindMapController().nodeRefresh(node);
        for (Iterator<MindMapNode> i = node.childrenUnfolded(); i.hasNext(); ) {
            MindMapNode child = i.next();
            removeIcons(child);
        }
    }

    public HierarchicalIcons() {
        super();

    }

    private void setStyle(MindMapNode node) {
        // precondition: all children are contained in nodeIconSets

        // gather all icons of my children and of me here:
        TreeSet<String> iconSet = new TreeSet<>();
        for (Iterator<MindMapNode> i = node.childrenUnfolded(); i.hasNext(); ) {
            MindMapNode child = i.next();
            addAccumulatedIconsToTreeSet(child, iconSet, nodeIconSets.get(child));
        }
        // remove my icons from the treeset:
        for (MindIcon icon : node.getIcons()) {
            iconSet.remove(icon.getName());
        }
        boolean dirty = true;
        // look for a change:
        if (nodeIconSets.containsKey(node)) {
            TreeSet<String> storedIconSet = nodeIconSets.get(node);
            if (storedIconSet.equals(iconSet)) {
                dirty = false;
            }
        }
        nodeIconSets.put(node, iconSet);

        if (dirty) {
            if (!iconSet.isEmpty()) {
                // create multiple image:
                MultipleImage image = new MultipleImage(0.75f);
                for (String iconName : iconSet) {
                    // log.info("Adding icon "+iconName + " to node "+
                    // node.toString());
                    MindIcon icon = MindIcon.factory(iconName);
                    image.addImage(icon.getIcon());
                }
                node.setStateIcon(getName(), image);
            } else {
                node.setStateIcon(getName(), null);
            }
            getMindMapController().nodeRefresh(node);
        }

    }

    private void addAccumulatedIconsToTreeSet(MindMapNode child,
                                              TreeSet<String> iconSet, TreeSet<String> childsTreeSet) {
        for (MindIcon icon : child.getIcons()) {
            iconSet.add(icon.getName());
        }
        if (childsTreeSet == null)
            return;
        iconSet.addAll(childsTreeSet);
    }

    public void onAddChildren(MindMapNode newChildNode) {
        log.trace("onAddChildren {}", newChildNode);
        super.onAddChild(newChildNode);
        setStyleRecursive(newChildNode);
    }

    public void onRemoveChildren(MindMapNode removedChild, MindMapNode oldDad) {
        log.trace("onRemoveChildren {}", removedChild);
        super.onRemoveChildren(removedChild, oldDad);
        setStyleRecursive(oldDad);
    }

    public void onUpdateChildrenHook(MindMapNode updatedNode) {
        super.onUpdateChildrenHook(updatedNode);
        setStyleRecursive(updatedNode);
    }

    public void onUpdateNodeHook() {
        super.onUpdateNodeHook();
        setStyle(getNode());
    }

    public void invoke(MindMapNode node) {
        super.invoke(node);
        gatherLeavesAndSetStyle(node);
        gatherLeavesAndSetParentsStyle(node);
    }

    private void gatherLeavesAndSetStyle(MindMapNode node) {
        if (node.getChildCount() == 0) {
            // call setStyle for all leaves:
            setStyle(node);
            return;
        }
        for (Iterator<MindMapNode> i = node.childrenUnfolded(); i.hasNext(); ) {
            MindMapNode child = i.next();
            gatherLeavesAndSetStyle(child);
        }
    }

    private void gatherLeavesAndSetParentsStyle(MindMapNode node) {
        if (node.getChildCount() == 0) {
            // call setStyleRecursive for all parents:
            if (node.getParentNode() != null) {
                setStyleRecursive(node.getParentNode());
            }
            return;
        }
        for (Iterator<MindMapNode> i = node.childrenUnfolded(); i.hasNext(); ) {
            MindMapNode child = i.next();
            gatherLeavesAndSetParentsStyle(child);
        }
    }

    private void setStyleRecursive(MindMapNode node) {
        // log.trace("setStyle " + node);
        setStyle(node);
        // recurse:
        if (node.getParentNode() != null) {
            setStyleRecursive(node.getParentNode());
        }
    }

}
