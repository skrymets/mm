package freemind.modes.mindmapmode;

import freemind.controller.Controller;
import freemind.diagram.mindmap.MindMapDiagram;
import freemind.diagram.plugin.DiagramPlugin;
import freemind.modes.Mode;
import freemind.modes.ModeController;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
public class MindMapMode extends Mode {

    public static final String MODENAME = "MindMap";

    @Getter
    private Controller controller;
    private MindMapController mindMapController;
    private boolean isRunning = false;

    /**
     * Held for identity (typeId, future contributions). Plan 2a does not
     * invoke {@code plugin.controllerFactory()}; that's Plan 2b's seam.
     */
    private final DiagramPlugin<MindMapDiagram> plugin;

    public MindMapMode(DiagramPlugin<MindMapDiagram> plugin) {
        this.plugin = plugin;
    }

    public void init(Controller controller) {
        this.controller = controller;
        mindMapController = (MindMapController) createModeController();
    }

    public ModeController createModeController() {
        return new MindMapController(this);
    }

    public String toString() {
        return MODENAME;
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
