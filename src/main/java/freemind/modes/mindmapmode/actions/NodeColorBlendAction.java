package freemind.modes.mindmapmode.actions;

import freemind.modes.mindmapmode.MindMapController;

@SuppressWarnings("serial")
public class NodeColorBlendAction extends NodeGeneralAction {
    public NodeColorBlendAction(final MindMapController modeController) {
        super(modeController, "blend_color", null, (map, node) -> modeController.blendNodeColor(node));
    }

}
