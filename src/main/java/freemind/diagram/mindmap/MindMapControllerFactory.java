package freemind.diagram.mindmap;

import com.google.inject.Inject;
import com.google.inject.Provider;
import freemind.controller.Controller;
import freemind.diagram.plugin.DiagramController;
import freemind.diagram.plugin.DiagramControllerFactory;
import freemind.main.Resources;
import freemind.modes.mindmapmode.MindMapController;

public final class MindMapControllerFactory implements DiagramControllerFactory<MindMapDiagram> {

    private final Provider<Controller> controllerProvider;
    private final Resources resources;

    @Inject
    public MindMapControllerFactory(Provider<Controller> controllerProvider, Resources resources) {
        this.controllerProvider = controllerProvider;
        this.resources = resources;
    }

    @Override
    public DiagramController<MindMapDiagram> createFor(MindMapDiagram diagram) {
        return new MindMapController(diagram, controllerProvider.get(), resources);
    }
}
