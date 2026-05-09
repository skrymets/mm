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
import java.util.Objects;

@Slf4j
public class MindMapMode extends Mode {

    public static final String MODENAME = "MindMap";

    @Getter
    private Controller controller;
    private MindMapController mindMapController;
    private boolean isRunning = false;

    /**
     * Held for identity (typeId, future contributions) and controller
     * creation — {@code createModeController()} now routes through
     * {@code plugin.controllerFactory()} (Plan 2b seam activated).
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
        var diagram = plugin.modelFactory().createNew();
        var rawCtrl = plugin.controllerFactory().createFor(diagram);
        Objects.requireNonNull(rawCtrl,
            "MindMapPlugin.controllerFactory().createFor returned null");
        var ctrl = (MindMapController) rawCtrl;
        ctrl.bindMode(this);
        return ctrl;
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
