package freemind.model.services;

import freemind.model.MindMapCloud;
import freemind.model.MindMapNode;
import freemind.model.NodeAdapter;
import freemind.model.NodeIcon;
import lombok.Getter;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Owns the per-node decorations: icons, state icons (transient indicators), and the cloud.
 * Extracted from {@link NodeAdapter} to consolidate visual-marker state.
 */
public class NodeDecorationsService {

    private final NodeAdapter node;

    private List<NodeIcon> icons = null; // lazy

    private TreeMap<String, ImageIcon> stateIcons = null; // lazy

    @Getter
    private MindMapCloud cloud;

    public NodeDecorationsService(NodeAdapter node) {
        this.node = node;
    }

    public List<NodeIcon> getIcons() {
        if (icons == null)
            return Collections.emptyList();
        return icons;
    }

    public void addIcon(NodeIcon icon, int position) {
        if (icons == null) {
            icons = new ArrayList<>();
        }
        if (position == NodeIcon.LAST) {
            icons.add(icon);
        } else {
            icons.add(position, icon);
        }
    }

    public int removeIcon(int position) {
        if (icons == null) {
            icons = new ArrayList<>();
        }
        if (position == NodeIcon.LAST) {
            position = icons.size() - 1;
        }
        icons.remove(position);
        int returnSize = icons.size();
        if (returnSize == 0) {
            icons = null;
        }
        return returnSize;
    }

    public Map<String, ImageIcon> getStateIcons() {
        if (stateIcons == null)
            return Collections.emptyMap();
        return Collections.unmodifiableSortedMap(stateIcons);
    }

    /**
     * This method must be synchronized as the underlying TreeMap isn't.
     */
    public synchronized void setStateIcon(String key, ImageIcon icon) {
        if (stateIcons == null) {
            stateIcons = new TreeMap<>();
        }
        if (icon != null) {
            stateIcons.put(key, icon);
        } else {
            stateIcons.remove(key);
        }
        if (stateIcons.isEmpty()) {
            stateIcons = null;
        }
    }

    public void setCloud(MindMapCloud cloud) {
        // Take care to keep the calculated iterative levels consistent
        if (cloud != null && this.cloud == null) {
            changeChildCloudIterativeLevels(1);
        } else if (cloud == null && this.cloud != null) {
            changeChildCloudIterativeLevels(-1);
        }
        this.cloud = cloud;
    }

    void changeChildCloudIterativeLevels(int deltaLevel) {
        for (ListIterator<MindMapNode> e = node.childrenUnfolded(); e.hasNext(); ) {
            MindMapNode childNode = e.next();
            MindMapCloud childCloud = childNode.getCloud();
            if (childCloud != null) {
                childCloud.changeIterativeLevel(deltaLevel);
            }
            // Recurse via the child's own decorations service.
            ((NodeAdapter) childNode).getDecorationsService().changeChildCloudIterativeLevels(deltaLevel);
        }
    }
}
