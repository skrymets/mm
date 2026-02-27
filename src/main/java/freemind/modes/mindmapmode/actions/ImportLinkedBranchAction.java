package freemind.modes.mindmapmode.actions;

import freemind.main.Tools;
import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.MindMapNodeModel;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.net.MalformedURLException;
import java.net.URL;

@SuppressWarnings("serial")
@Slf4j
public class ImportLinkedBranchAction extends MindmapAction {

    public ImportLinkedBranchAction(MindMapController controller) {
        super("import_linked_branch", controller);
    }

    public void actionPerformed(ActionEvent e) {
        MindMapController controller = getMindMapController();
        MindMapNodeModel selected = (MindMapNodeModel) controller.getSelected();
        if (selected == null || selected.getLink() == null) {
            JOptionPane.showMessageDialog(controller.getView(),
                    controller.getText("import_linked_branch_no_link"));
            return;
        }
        URL absolute;
        try {
            String relative = selected.getLink();
            absolute = new URL(Tools.fileToUrl(controller.getMap().getFile()), relative);
        } catch (MalformedURLException ex) {
            JOptionPane.showMessageDialog(controller.getView(),
                    "Couldn't create valid URL for:" + controller.getMap().getFile());
            log.error(ex.getLocalizedMessage(), ex);
            return;
        }
        try {
            MindMapNode node = controller.loadTree(Tools.urlToFile(absolute));
            controller.paste(node, selected);
            controller.invokeHooksRecursively(node, controller.getMindMapMapModel());
        } catch (Exception ex) {
            controller.handleLoadingException(ex);
        }
    }
}
