package freemind.diagram.mindmap;

import freemind.diagram.DiagramTypeId;
import freemind.diagram.plugin.InMemoryDiagramPluginRegistry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MindMapPluginRegistrationTest {

    @Test
    void pluginRegistersWithExpectedTypeId() {
        var registry = new InMemoryDiagramPluginRegistry();
        var plugin = new MindMapPlugin(new MindMapModelFactory(), new MindMapControllerFactory());
        registry.register(plugin);
        assertSame(plugin, registry.findByTypeId(new DiagramTypeId("mindmap")).orElseThrow());
    }

    @Test
    void modelFactoryCreatesEmptyDiagram() {
        var plugin = new MindMapPlugin(new MindMapModelFactory(), new MindMapControllerFactory());
        var d = plugin.modelFactory().createNew();
        assertEquals(new DiagramTypeId("mindmap"), d.typeId());
        assertNotNull(d.rootNode());
        var impl = (MindMapDiagramImpl) d;
        assertTrue(impl.getChildren(d.rootNode()).isEmpty());
    }

    @Test
    void nativePayloadCodecReportsVersion1() {
        var plugin = new MindMapPlugin(new MindMapModelFactory(), new MindMapControllerFactory());
        assertEquals(1, plugin.nativePayloadCodec().currentPayloadVersion());
    }

    @Test
    void externalFormatsContainsFreemindMmFormat() {
        var plugin = new MindMapPlugin(new MindMapModelFactory(), new MindMapControllerFactory());
        // Task 21 wired FreemindMmImportFormat into MindMapPlugin.
        assertEquals(1, plugin.externalFormats().size());
        var fmt = plugin.externalFormats().get(0);
        assertInstanceOf(freemind.diagram.mindmap.legacy.FreemindMmImportFormat.class, fmt);
    }
}
