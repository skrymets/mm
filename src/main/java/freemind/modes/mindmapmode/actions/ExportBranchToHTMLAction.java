package freemind.modes.mindmapmode.actions;

import freemind.main.FreeMindCommon;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.MindMapNodeModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("serial")
public class ExportBranchToHTMLAction extends MindmapAction {

    public ExportBranchToHTMLAction(MindMapController controller) {
        super("export_branch_to_html", controller);
    }

    public void actionPerformed(ActionEvent e) {
        MindMapController controller = getMindMapController();
        File mindmapFile = controller.getMap().getFile();
        if (mindmapFile == null) {
            JOptionPane.showMessageDialog(controller.getFrame().getContentPane(),
                    controller.getText("map_not_saved"), "FreeMind", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            File file = File.createTempFile(
                    mindmapFile.getName().replace(FreeMindCommon.FREEMIND_FILE_EXTENSION, "_"),
                    ".html", mindmapFile.getParentFile());
            MindMapController.saveHTML((MindMapNodeModel) controller.getSelected(), file);
            controller.loadURL(file.toString());
        } catch (IOException ignored) {
        }
    }
}
