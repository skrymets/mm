package freemind.modes.mindmapmode.actions;

import freemind.main.Tools;
import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.MindMapNodeModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ListIterator;

@SuppressWarnings("serial")
public class ImportLinkedBranchWithoutRootAction extends MindmapAction {

    public ImportLinkedBranchWithoutRootAction(MindMapController controller) {
        super("import_linked_branch_without_root", controller);
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
            JOptionPane.showMessageDialog(controller.getView(), "Couldn't create valid URL.");
            return;
        }
        try {
            MindMapNode node = controller.loadTree(Tools.urlToFile(absolute));
            for (ListIterator<MindMapNode> i = node.childrenUnfolded(); i.hasNext(); ) {
                MindMapNodeModel importNode = (MindMapNodeModel) i.next();
                controller.paste(importNode, selected);
                controller.invokeHooksRecursively(importNode, controller.getMindMapMapModel());
            }
        } catch (Exception ex) {
            controller.handleLoadingException(ex);
        }
    }
}
