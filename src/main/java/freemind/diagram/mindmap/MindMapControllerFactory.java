package freemind.diagram.mindmap;

import freemind.diagram.plugin.DiagramController;
import freemind.diagram.plugin.DiagramControllerFactory;

public final class MindMapControllerFactory implements DiagramControllerFactory<MindMapDiagram> {
    @Override
    public DiagramController<MindMapDiagram> createFor(MindMapDiagram diagram) {
        return new DiagramController<>() {
            @Override public MindMapDiagram diagram() { return diagram; }
            @Override public void dispose()           { /* Plan 2: dispose controller services */ }
        };
    }
}
