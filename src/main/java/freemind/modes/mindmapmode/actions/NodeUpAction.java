package freemind.modes.mindmapmode.actions;

import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import lombok.extern.slf4j.Slf4j;

import java.awt.event.ActionEvent;
import java.util.List;

@SuppressWarnings("serial")
@Slf4j
public class NodeUpAction extends MindmapAction {
    private final MindMapController modeController;

    public NodeUpAction(MindMapController modeController) {
        super("node_up", modeController);
        this.modeController = modeController;
    }

    public void actionPerformed(ActionEvent e) {
        MindMapNode selected = modeController.getSelected();
        List<MindMapNode> selecteds = modeController.getSelecteds();
        modeController.moveNodes(selected, selecteds, -1);
        modeController.select(selected, selecteds);
    }

}
