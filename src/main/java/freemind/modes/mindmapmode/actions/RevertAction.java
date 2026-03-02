package freemind.modes.mindmapmode.actions;

import freemind.model.MindMap;
import freemind.modes.mindmapmode.MindMapController;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

/**
 * Reverts the map to the saved version. In Xml, the old map is stored as xml
 * and as an undo action, the new map is stored, too.
 * <p>
 * Moreover, the filename of the doAction is set to the appropriate map file's
 * name. The undo action has no file name associated.
 * <p>
 * The action goes like this: close the actual map and open the given Xml/File.
 * If only a Xml string is given, a temporary file name is created, the xml
 * stored into and this map is opened instead of the actual.
 */
@SuppressWarnings("serial")
@Slf4j
public class RevertAction extends MindmapAction {

    private final MindMapController mindMapController;

    public RevertAction(MindMapController modeController) {
        super("RevertAction", (String) null, modeController);
        mindMapController = modeController;

    }

    public void actionPerformed(ActionEvent arg0) {
        try {
            MindMap map = mindMapController.getMap();
            File file = map.getFile();
            if (file == null) {
                JOptionPane.showMessageDialog(mindMapController.getView(),
                        mindMapController.getText("map_not_saved"), "FreeMind",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            // FIXME: Make action from MindMapActions out of it.
            mindMapController.getActorFactory().getRevertActor().revertMap(map, file);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
        }

    }

}
