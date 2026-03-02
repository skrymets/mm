package freemind.modes.viewmodes;

import freemind.view.mindmapview.NodeView;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
@Slf4j
public class CommonToggleChildrenFoldedAction extends AbstractAction {

    private final ViewControllerAdapter modeController;

    public CommonToggleChildrenFoldedAction(ViewControllerAdapter controller) {
        super(controller.getText("toggle_children_folded"));
        this.modeController = controller;
    }

    public void actionPerformed(ActionEvent e) {
        NodeView selected = modeController.getSelectedView();
        modeController.toggleFolded.toggleFolded(selected.getModel().childrenUnfolded());
        modeController.getView().getSelectionService().selectAsTheOnlyOneSelected(selected);
        modeController.getController().obtainFocusForSelected();
    }

}
