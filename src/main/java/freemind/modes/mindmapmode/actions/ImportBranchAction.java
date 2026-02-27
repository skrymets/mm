package freemind.modes.mindmapmode.actions;

import freemind.model.MindMapNode;
import freemind.modes.FreeMindFileDialog;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.MindMapNodeModel;

import javax.swing.*;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class ImportBranchAction extends MindmapAction {

    public ImportBranchAction(MindMapController controller) {
        super("import_branch", controller);
    }

    public void actionPerformed(ActionEvent e) {
        MindMapController controller = getMindMapController();
        MindMapNodeModel parent = (MindMapNodeModel) controller.getSelected();
        if (parent == null) {
            return;
        }
        FreeMindFileDialog chooser = controller.getFileChooser();
        int returnVal = chooser.showOpenDialog(controller.getFrame().getContentPane());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                MindMapNode node = controller.loadTree(chooser.getSelectedFile());
                controller.paste(node, parent);
                controller.invokeHooksRecursively(node, controller.getMindMapMapModel());
            } catch (Exception ex) {
                controller.handleLoadingException(ex);
            }
        }
    }
}
