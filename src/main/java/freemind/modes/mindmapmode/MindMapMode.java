package freemind.modes.mindmapmode;

import freemind.controller.Controller;
import freemind.modes.Mode;
import freemind.modes.ModeController;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
public class MindMapMode extends Mode {

    @Getter
    private Controller controller;
    private MindMapController mindMapController;
    private boolean isRunning = false;

    public MindMapMode() {
    }

    public void init(Controller controller) {
        this.controller = controller;
        mindMapController = (MindMapController) createModeController();
    }

    public ModeController createModeController() {
        return new MindMapController(this);
    }

    public String toString() {
        return "MindMap";
    }

    /**
     * Called whenever this mode is chosen in the program. (updates Actions etc.)
     */
    public void activate() {
        if (isRunning) {
            controller.getMapModuleManager().changeToMapOfMode(this);
        } else {
            isRunning = true;
        }
    }

    public void restore(String restorable) throws IOException {
        getDefaultModeController().load(new File(restorable));
    }

    public ModeController getDefaultModeController() {
        return mindMapController;
    }

}
