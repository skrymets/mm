package accessories.plugins;

import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.hooks.MindMapNodeHookAdapter;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ShowCloneNodes extends MindMapNodeHookAdapter {

    public void invoke(MindMapNode pNode) {
        super.invoke(pNode);
        final List<MindMapNode> newSelecteds = new ArrayList<>();
        final MindMapController mindMapController = getMindMapController();
        List<MindMapNode> selecteds = mindMapController.getSelecteds();
        for (MindMapNode node : selecteds) {
            addClonesToList(newSelecteds, node);
            newSelecteds.remove(node);
        }
        if (!newSelecteds.isEmpty()) {
            EventQueue.invokeLater(() -> mindMapController.select(
                    newSelecteds.get(0), newSelecteds));
        }
    }

    protected void addClonesToList(List<MindMapNode> newSelecteds, MindMapNode node) {
        ClonePlugin hook = ClonePlugin.getHook(node);
        if (hook != null) {
            // original found.
            HashSet<MindMapNode> clones = hook.getCloneNodes();
            newSelecteds.addAll(clones);
        }
    }
}
