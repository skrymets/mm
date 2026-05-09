package freemind.diagram.mindmap;

import com.google.inject.Provider;
import freemind.controller.Controller;
import freemind.diagram.DiagramTypeId;
import freemind.diagram.plugin.InMemoryDiagramPluginRegistry;
import freemind.main.Resources;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class MindMapPluginRegistrationTest {

    private static MindMapControllerFactory stubFactory() {
        @SuppressWarnings("unchecked")
        Provider<Controller> cp = Mockito.mock(Provider.class);
        Resources res = Mockito.mock(Resources.class);
        return new MindMapControllerFactory(cp, res);
    }

    @Test
    void pluginRegistersWithExpectedTypeId() {
        var registry = new InMemoryDiagramPluginRegistry();
        var plugin = new MindMapPlugin(new MindMapModelFactory(), stubFactory());
        registry.register(plugin);
        assertSame(plugin, registry.findByTypeId(new DiagramTypeId("mindmap")).orElseThrow());
    }

    @Test
    void modelFactoryCreatesEmptyDiagram() {
        var plugin = new MindMapPlugin(new MindMapModelFactory(), stubFactory());
        var d = plugin.modelFactory().createNew();
        assertEquals(new DiagramTypeId("mindmap"), d.typeId());
        assertNotNull(d.rootNode());
        var impl = (MindMapDiagramImpl) d;
        assertTrue(impl.getChildren(d.rootNode()).isEmpty());
    }

    @Test
    void nativePayloadCodecReportsVersion1() {
        var plugin = new MindMapPlugin(new MindMapModelFactory(), stubFactory());
        assertEquals(1, plugin.nativePayloadCodec().currentPayloadVersion());
    }

    @Test
    void externalFormatsContainsFreemindMmFormat() {
        var plugin = new MindMapPlugin(new MindMapModelFactory(), stubFactory());
        // Task 21 wired FreemindMmImportFormat into MindMapPlugin.
        assertEquals(1, plugin.externalFormats().size());
        var fmt = plugin.externalFormats().get(0);
        assertInstanceOf(freemind.diagram.mindmap.legacy.FreemindMmImportFormat.class, fmt);
    }
}
