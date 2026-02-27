package freemind.modes.mindmapmode.actions;

import freemind.common.OptionalDontShowMeAgainDialog;
import freemind.main.FreeMind;
import freemind.main.Tools;
import freemind.modes.mindmapmode.MindMapController;

import javax.swing.*;

public class MapSourceChangeDialog implements Runnable {

    private final MindMapController controller;
    private boolean mReturnValue = true;

    public MapSourceChangeDialog(MindMapController controller) {
        this.controller = controller;
    }

    public void run() {
        int showResult = new OptionalDontShowMeAgainDialog(
                controller.getFrame().getJFrame(),
                controller.getSelectedView(),
                "file_changed_on_disk_reload",
                "confirmation",
                controller,
                new OptionalDontShowMeAgainDialog.StandardPropertyHandler(
                        controller.getController(),
                        FreeMind.RESOURCES_RELOAD_FILES_WITHOUT_QUESTION),
                OptionalDontShowMeAgainDialog.BOTH_OK_AND_CANCEL_OPTIONS_ARE_STORED
        ).show().getResult();
        if (showResult != JOptionPane.OK_OPTION) {
            controller.getFrame().setStatusText(
                    Tools.expandPlaceholders(controller.getText("file_not_reloaded"),
                            controller.getMap().getFile().toString()));
            mReturnValue = false;
            return;
        }
        controller.revertAction.actionPerformed(null);
    }

    public boolean getReturnValue() {
        return mReturnValue;
    }
}
