package freemind.modes.browsemode;

import freemind.controller.Controller;
import freemind.modes.Mode;
import freemind.modes.ModeController;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class BrowseMode extends Mode {

    private Controller c;
    private BrowseController modecontroller;
    public final static String MODENAME = "Browse";
    private boolean isRunning = false;

    public BrowseMode() {
    }

    public void init(Controller c) {
        this.c = c;
        modecontroller = new BrowseController(this);
    }

    public String toString() {
        return MODENAME;
    }

    /**
     * Called whenever this mode is chosen in the program. (updates Actions
     * etc.)
     */
    public void activate() {
        if (isRunning) {
            c.getMapModuleManager().changeToMapOfMode(this);
        } else {
            isRunning = true;
        }

    }

    public void restore(String restoreable) {
        try {
            getDefaultModeController().load(new File(restoreable));
        } catch (Exception e) {
            c.errorMessage("An error occured on opening the file: "
                    + restoreable + ".");
            log.error(e.getLocalizedMessage(), e);
        }
    }

    public Controller getController() {
        return c;
    }

    public ModeController getDefaultModeController() {
        // no url should be visible for the empty controller.
        modecontroller.getToolBar().setURLField("");
        return modecontroller;
    }

    public ModeController createModeController() {
        return new BrowseController(this);
    }

}
