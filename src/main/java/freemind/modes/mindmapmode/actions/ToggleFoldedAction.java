package freemind.modes.mindmapmode.actions;

import freemind.modes.mindmapmode.MindMapController;

import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class ToggleFoldedAction extends MindmapAction {
    private final MindMapController modeController;

    public ToggleFoldedAction(MindMapController controller) {
        super("toggle_folded", controller);
        this.modeController = controller;
    }

    public void actionPerformed(ActionEvent e) {
        modeController.toggleFolded();
    }

}
