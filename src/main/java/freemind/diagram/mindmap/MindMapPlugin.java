package freemind.diagram.mindmap;

import com.google.inject.Inject;
import freemind.diagram.DiagramTypeId;
import freemind.diagram.persistence.DiagramPayloadCodec;
import freemind.diagram.persistence.external.ExternalDiagramFormat;
import freemind.diagram.plugin.DiagramControllerFactory;
import freemind.diagram.plugin.DiagramLifecycleHooks;
import freemind.diagram.plugin.DiagramModelFactory;
import freemind.diagram.mindmap.legacy.FreemindMmImportFormat;
import freemind.diagram.plugin.DiagramPlugin;
import freemind.diagram.ui.DiagramUiContributions;

import java.util.List;

public final class MindMapPlugin implements DiagramPlugin<MindMapDiagram> {

    private final DiagramModelFactory<MindMapDiagram> modelFactory;
    private final DiagramControllerFactory<MindMapDiagram> controllerFactory;
    private final DiagramUiContributions uiContributions = new MindMapUiContributions();
    private final DiagramLifecycleHooks<MindMapDiagram> hooks = new MindMapLifecycleHooks();
    private final DiagramPayloadCodec<MindMapDiagram> nativePayloadCodec = new MindMapPayloadCodec();
    private final List<ExternalDiagramFormat<MindMapDiagram>> externalFormats;

    @Inject
    public MindMapPlugin(MindMapModelFactory modelFactory,
                         MindMapControllerFactory controllerFactory) {
        this.modelFactory = modelFactory;
        this.controllerFactory = controllerFactory;
        this.externalFormats = List.of(new FreemindMmImportFormat());
    }

    @Override public DiagramTypeId typeId()                                          { return MindMapDiagramImpl.TYPE_ID; }
    @Override public DiagramModelFactory<MindMapDiagram> modelFactory()              { return modelFactory; }
    @Override public DiagramControllerFactory<MindMapDiagram> controllerFactory()    { return controllerFactory; }
    @Override public DiagramUiContributions uiContributions()                        { return uiContributions; }
    @Override public DiagramLifecycleHooks<MindMapDiagram> hooks()                   { return hooks; }
    @Override public DiagramPayloadCodec<MindMapDiagram> nativePayloadCodec()        { return nativePayloadCodec; }
    @Override public List<ExternalDiagramFormat<MindMapDiagram>> externalFormats()   { return externalFormats; }
}
