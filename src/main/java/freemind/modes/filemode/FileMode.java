package freemind.modes.filemode;

import freemind.controller.Controller;
import freemind.modes.Mode;
import freemind.modes.ModeController;
import lombok.Getter;

import javax.swing.*;

public class FileMode extends Mode {

    private Controller c;
    private FileController modecontroller;
    @Getter
    private JToolBar toolbar;
    private static final String MODENAME = "File";
    private static boolean isRunning = false;

    public FileMode() {
    }

    public void init(Controller c) {
        this.c = c;
        modecontroller = new FileController(this);
        toolbar = new FileToolBar(modecontroller);
    }

    public String toString() {
        return MODENAME;
    }

    /**
     * Called whenever this mode is chosen in the program. (updates Actions
     * etc.)
     */
    public void activate() {
        getDefaultModeController().newMap();
        c.getMapModuleManager().changeToMapOfMode(this);
        if (!isRunning) {
            isRunning = true;
        } else {
        }
    }

    public void restore(String restoreable) {
    }

    public Controller getController() {
        return c;
    }

    public ModeController getDefaultModeController() {
        return modecontroller;
    }

    public ModeController createModeController() {
        return new FileController(this);
    }

}
