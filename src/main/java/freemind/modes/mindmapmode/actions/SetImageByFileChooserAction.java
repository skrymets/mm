package freemind.modes.mindmapmode.actions;

import freemind.modes.mindmapmode.MindMapController;

import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class SetImageByFileChooserAction extends MindmapAction {

    public SetImageByFileChooserAction(MindMapController controller) {
        super("set_image_by_filechooser", controller);
    }

    public void actionPerformed(ActionEvent e) {
        getMindMapController().setImageByFileChooser();
        getMindMapController().getController().obtainFocusForSelected();
    }
}
