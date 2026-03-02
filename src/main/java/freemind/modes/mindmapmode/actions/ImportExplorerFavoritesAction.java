package freemind.modes.mindmapmode.actions;

import freemind.main.Tools;
import org.apache.commons.io.FilenameUtils;
import freemind.model.MindMapNode;
import freemind.modes.FreeMindFileDialog;
import freemind.modes.mindmapmode.MindMapController;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

@SuppressWarnings("serial")
@Slf4j
public class ImportExplorerFavoritesAction extends MindmapAction {
    private final MindMapController controller;

    public ImportExplorerFavoritesAction(MindMapController controller) {
        super("import_explorer_favorites", controller);
        this.controller = controller;
    }

    public void actionPerformed(ActionEvent e) {
        FreeMindFileDialog chooser = controller.getFileChooser(null);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle(controller.getText("select_favorites_folder"));
        int returnVal = chooser.showOpenDialog(controller.getFrame()
                .getContentPane());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File folder = chooser.getSelectedFile();
            controller.getFrame().setStatusText("Importing Favorites ...");
            // getFrame().repaint(); // Refresh the frame, namely hide dialog
            // and show status
            // getView().updateUI();
            // Problem: the frame should be refreshed here, but I don't know how
            // to do it
            importExplorerFavorites(folder, controller.getSelected(),/*
                     * redisplay=
                     */
                    true);
            controller.getFrame().setStatusText("Favorites imported.");
        }
    }

    public boolean importExplorerFavorites(File folder, MindMapNode target,
                                           boolean redisplay) {
        // Returns true iff any favorites found
        boolean favoritesFound = false;
        if (folder.isDirectory()) {
            File[] list = folder.listFiles();
            // Go recursively to subfolders
            for (File value : list) {
                if (value.isDirectory()) {
                    // Insert a new node
                    String nodeContent = value.getName();
                    MindMapNode node = addNode(target, nodeContent);
                    //
                    boolean favoritesFoundInSubfolder = importExplorerFavorites(
                            value, node, false);
                    if (favoritesFoundInSubfolder) {
                        favoritesFound = true;
                    } else {
                        controller.deleteNode(node);
                    }
                }
            }

            // For each .url file: add it
            for (File file : list) {
                if (!file.isDirectory()
                        && FilenameUtils.getExtension(file.toString()).equalsIgnoreCase("url")) {
                    favoritesFound = true;
                    try {
                        MindMapNode node = addNode(target,
                                FilenameUtils.removeExtension(file.getName()));
                        // For each line: Is it URL? => Set it as link
                        BufferedReader in = new BufferedReader(new FileReader(
                                file));
                        while (in.ready()) {
                            String line = in.readLine();
                            if (line.startsWith("URL=")) {
                                node.setLink(line.substring(4));
                                break;
                            }
                        }

                    } catch (Exception e) {
                        log.error(e.getLocalizedMessage(), e);
                    }
                }
            }
        }
        if (redisplay) {
            controller.nodeChanged(target);
        }
        return favoritesFound;
    }

    private MindMapNode addNode(MindMapNode target, String nodeContent) {
        MindMapNode node = controller.addNewNode(target,
                target.getChildCount(), target.isNewChildLeft());
        controller.setNodeText(node, nodeContent);
        return node;
    }

}
