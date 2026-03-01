/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2006 Joerg Mueller, Daniel Polansky, Christian Foltin and others.
 *See COPYING for Details
 *
 *This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package freemind.modes.mindmapmode.services;

import freemind.common.OptionalDontShowMeAgainDialog;
import freemind.main.ExampleFileFilter;
import freemind.main.FreeMind;
import freemind.modes.FreeMindFileDialog;
import freemind.main.Tools;
import freemind.model.MapAdapter;
import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Service for file loading and file-chooser-based link/image operations.
 * Extracted from MindMapController to reduce class size.
 */
@Slf4j
public class FileManagementService {

    private final MindMapController controller;

    public FileManagementService(MindMapController controller) {
        this.controller = controller;
    }

    /**
     * Loads a mind map from the given URL into the given model, handling file
     * existence checks, read-only detection, and file locking.
     */
    public void loadInternally(URL url, MapAdapter model) throws URISyntaxException, IOException {
        log.info("Loading file: {}", url.toString());
        File file = Tools.urlToFile(url);
        if (!file.exists()) {
            throw new FileNotFoundException(Tools.expandPlaceholders(
                    controller.getText("file_not_found"), file.getPath()));
        }
        if (!file.canWrite()) {
            model.setReadOnly(true);
        } else {
            try {
                String lockingUser = model.tryToLock(file);
                if (lockingUser != null) {
                    controller.getFrame().getController().informationMessage(
                            Tools.expandPlaceholders(controller.getText("map_locked_by_open"),
                                    file.getName(), lockingUser));
                    model.setReadOnly(true);
                } else {
                    model.setReadOnly(false);
                }
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
                controller.getFrame().getController().informationMessage(
                        Tools.expandPlaceholders(controller.getText("locking_failed_by_open"),
                                file.getName()));
                model.setReadOnly(true);
            }
        }

        synchronized (model) {
            MindMapNode root = loadTree(file);
            if (root != null) {
                model.setRoot(root);
            }
            model.setFile(file);
            model.setFileTime();
        }
    }

    /**
     * Loads a mind map tree from the given file.
     */
    public MindMapNode loadTree(final File pFile) throws IOException {
        return loadTree(new Tools.FileReaderCreator(pFile));
    }

    /**
     * Loads a mind map tree using the given reader creator, prompting for version
     * conversion if needed.
     */
    public MindMapNode loadTree(Tools.ReaderCreator pReaderCreator) throws IOException {
        return controller.getMap().loadTree(pReaderCreator, () -> {
            int showResult = new OptionalDontShowMeAgainDialog(
                    controller.getFrame().getJFrame(),
                    controller.getSelectedView(),
                    "really_convert_to_current_version2",
                    "confirmation",
                    controller,
                    new OptionalDontShowMeAgainDialog.StandardPropertyHandler(
                            controller.getController(),
                            FreeMind.RESOURCES_CONVERT_TO_CURRENT_VERSION),
                    OptionalDontShowMeAgainDialog.ONLY_OK_SELECTION_IS_STORED)
                    .show().getResult();
            return (showResult == JOptionPane.OK_OPTION);
        });
    }

    /**
     * Opens a file chooser dialog and returns the relative path to the chosen file,
     * or null if cancelled or the map is not yet saved.
     */
    public String getLinkByFileChooser(FileFilter fileFilter) {
        String relative = null;
        File input;
        FreeMindFileDialog chooser = controller.getFileChooser(fileFilter);
        if (controller.getMap().getFile() == null) {
            JOptionPane.showMessageDialog(controller.getFrame().getContentPane(),
                    controller.getText("not_saved_for_link_error"), "FreeMind",
                    JOptionPane.WARNING_MESSAGE);
            return null;
        }

        int returnVal = chooser.showOpenDialog(controller.getFrame().getContentPane());
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            input = chooser.getSelectedFile();
            relative = Tools.fileToRelativeUrlString(input, controller.getMap().getFile());
        }
        return relative;
    }

    /**
     * Opens a file chooser for a link and sets it on the selected node.
     */
    public void setLinkByFileChooser() {
        String relative = getLinkByFileChooser(null);
        if (relative != null) {
            controller.setLink(controller.getSelected(), relative);
        }
    }

    /**
     * Opens a file chooser filtered for images and sets the selected image as
     * inline HTML on the selected node.
     */
    public void setImageByFileChooser() {
        ExampleFileFilter filter = new ExampleFileFilter();
        filter.addExtension("jpg");
        filter.addExtension("jpeg");
        filter.addExtension("png");
        filter.addExtension("gif");
        filter.setDescription("JPG, PNG and GIF Images");

        String relative = getLinkByFileChooser(filter);
        if (relative != null) {
            String strText = "<html><body><img src=\"" + relative + "\"/></body></html>";
            controller.setNodeText(controller.getSelected(), strText);
        }
    }
}
