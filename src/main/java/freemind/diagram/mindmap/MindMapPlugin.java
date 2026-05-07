package freemind.diagram.mindmap;

import freemind.diagram.DiagramTypeId;
import freemind.diagram.persistence.DiagramPayloadCodec;
import freemind.diagram.persistence.external.ExternalDiagramFormat;
import freemind.diagram.plugin.DiagramControllerFactory;
import freemind.diagram.plugin.DiagramLifecycleHooks;
import freemind.diagram.plugin.DiagramModelFactory;
import freemind.diagram.plugin.DiagramPlugin;
import freemind.diagram.ui.DiagramUiContributions;

import java.util.List;

public final class MindMapPlugin implements DiagramPlugin<MindMapDiagram> {

    private final DiagramModelFactory<MindMapDiagram> modelFactory = new MindMapModelFactory();
    private final DiagramControllerFactory<MindMapDiagram> controllerFactory = new MindMapControllerFactory();
    private final DiagramUiContributions uiContributions = new MindMapUiContributions();
    private final DiagramLifecycleHooks<MindMapDiagram> hooks = new MindMapLifecycleHooks();
    private final DiagramPayloadCodec<MindMapDiagram> nativePayloadCodec = new MindMapPayloadCodec();
    private final List<ExternalDiagramFormat<MindMapDiagram>> externalFormats;

    public MindMapPlugin() {
        // populated by Task 21 once FreemindMmImportFormat exists
        this.externalFormats = List.of();
    }

    @Override public DiagramTypeId typeId()                                          { return MindMapDiagramImpl.TYPE_ID; }
    @Override public DiagramModelFactory<MindMapDiagram> modelFactory()              { return modelFactory; }
    @Override public DiagramControllerFactory<MindMapDiagram> controllerFactory()    { return controllerFactory; }
    @Override public DiagramUiContributions uiContributions()                        { return uiContributions; }
    @Override public DiagramLifecycleHooks<MindMapDiagram> hooks()                   { return hooks; }
    @Override public DiagramPayloadCodec<MindMapDiagram> nativePayloadCodec()        { return nativePayloadCodec; }
    @Override public List<ExternalDiagramFormat<MindMapDiagram>> externalFormats()   { return externalFormats; }
}
