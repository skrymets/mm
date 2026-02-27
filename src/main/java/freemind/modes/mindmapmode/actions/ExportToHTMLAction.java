package freemind.modes.mindmapmode.actions;

import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.MindMapNodeModel;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("serial")
@Slf4j
public class ExportToHTMLAction extends MindmapAction {

    public ExportToHTMLAction(MindMapController controller) {
        super("export_to_html", controller);
    }

    public void actionPerformed(ActionEvent e) {
        MindMapController controller = getMindMapController();
        if (controller.getMap().getFile() == null) {
            JOptionPane.showMessageDialog(controller.getFrame().getContentPane(),
                    controller.getText("map_not_saved"), "FreeMind", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            File file = new File(controller.getMindMapMapModel().getFile() + ".html");
            MindMapController.saveHTML((MindMapNodeModel) controller.getMindMapMapModel().getRoot(), file);
            controller.loadURL(file.toString());
        } catch (IOException ex) {
            log.error(ex.getLocalizedMessage(), ex);
        }
    }
}
