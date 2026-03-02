package freemind.modes;

import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.actions.MindmapAction;

import java.awt.event.ActionEvent;
import java.util.List;

@SuppressWarnings("serial")
public class NodeDownAction extends MindmapAction {
    private final MindMapController modeController;

    public NodeDownAction(MindMapController adapter) {
        super("node_down", adapter);
        this.modeController = adapter;
    }

    public void actionPerformed(ActionEvent e) {
        MindMapNode selected = modeController.getSelected();
        List<MindMapNode> selecteds = modeController.getSelecteds();
        modeController.moveNodes(selected, selecteds, 1);
        modeController.select(selected, selecteds);

    }
}