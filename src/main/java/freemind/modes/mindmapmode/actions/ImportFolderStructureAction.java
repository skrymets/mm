package freemind.modes.mindmapmode.actions;

import freemind.main.FreeMindMain;
import freemind.main.Tools;
import freemind.model.MindMapNode;
import freemind.modes.FreeMindFileDialog;
import freemind.modes.mindmapmode.MindMapController;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.MalformedURLException;

@SuppressWarnings("serial")
@Slf4j
public class ImportFolderStructureAction extends MindmapAction {

    private final MindMapController controller;

    public ImportFolderStructureAction(MindMapController controller) {
        super("import_folder_structure", controller);
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        FreeMindFileDialog chooser = controller.getFileChooser(null);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle(controller.getText("select_folder_for_importing"));
        FreeMindMain frame = getFrame();
        int returnVal = chooser.showOpenDialog(frame.getContentPane());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File folder = chooser.getSelectedFile();
            frame.setStatusText("Importing folder structure ...");
            // getFrame().repaint(); // Refresh the frame, namely hide dialog
            // and show status
            // getView().updateUI();
            // Problem: the frame should be refreshed here, but I don't know how
            // to do it
            try {
                frame.setWaitingCursor(true);
                importFolderStructure(folder, controller.getSelected(), true);
            } catch (Exception ex) {
                log.error(ex.getLocalizedMessage(), ex);
            }
            frame.setWaitingCursor(false);
            frame.setStatusText("Folder structure imported.");
        }
    }

    private FreeMindMain getFrame() {
        return controller.getFrame();
    }

    public void importFolderStructure(File folder, MindMapNode target, boolean redisplay) throws MalformedURLException {
        log.trace("Entering folder: {}", folder);

        if (folder.isDirectory()) {
            getFrame().setStatusText(folder.getName());
            File[] list = folder.listFiles();
            if (list != null) {
                // Go recursively to subfolders
                for (File value : list) {
                    if (value.isDirectory()) {
                        // Insert a new node
                        MindMapNode node = addNode(target, value.getName(),
                                Tools.fileToUrl(value).toString());
                        importFolderStructure(value, node, false);
                    }
                }
                // For each file: add it
                for (File file : list) {
                    if (!file.isDirectory()) {
                        addNode(target, file.getName(),
                                Tools.fileToUrl(file).toString());
                    }
                }
            }
        }
        controller.setFolded(target, true);

    }

    private MindMapNode addNode(MindMapNode target, String nodeContent, String link) {

        MindMapNode node = controller.addNewNode(target, target.getChildCount(), target.isNewChildLeft());
        controller.setNodeText(node, nodeContent);
        controller.setLink(node, link);
        return node;
    }

}
