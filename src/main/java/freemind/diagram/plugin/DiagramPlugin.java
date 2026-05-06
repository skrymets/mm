package freemind.diagram.plugin;

import freemind.diagram.Diagram;
import freemind.diagram.DiagramTypeId;
import freemind.diagram.persistence.DiagramPayloadCodec;
import freemind.diagram.persistence.external.ExternalDiagramFormat;
import freemind.diagram.ui.DiagramUiContributions;
import java.util.List;

/**
 * The integration unit for a diagram type. Bundles model factory, controller
 * factory, UI contributions, hooks, native payload codec, and optional
 * external import/export adapters.
 *
 * <p>One {@code DiagramPlugin} instance per diagram type, registered with
 * {@link DiagramPluginRegistry}.
 */
public interface DiagramPlugin<D extends Diagram> {

    DiagramTypeId typeId();

    DiagramModelFactory<D> modelFactory();

    DiagramControllerFactory<D> controllerFactory();

    DiagramUiContributions uiContributions();

    DiagramLifecycleHooks<D> hooks();

    DiagramPayloadCodec<D> nativePayloadCodec();

    List<ExternalDiagramFormat<D>> externalFormats();
}
