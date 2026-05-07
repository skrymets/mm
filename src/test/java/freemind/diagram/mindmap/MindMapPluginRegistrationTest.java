package freemind.diagram.mindmap;

import freemind.diagram.DiagramTypeId;
import freemind.diagram.plugin.InMemoryDiagramPluginRegistry;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MindMapPluginRegistrationTest {

    @Test
    void pluginRegistersWithExpectedTypeId() {
        var registry = new InMemoryDiagramPluginRegistry();
        var plugin = new MindMapPlugin();
        registry.register(plugin);
        assertSame(plugin, registry.findByTypeId(new DiagramTypeId("mindmap")).orElseThrow());
    }

    @Test
    void modelFactoryCreatesEmptyDiagram() {
        var plugin = new MindMapPlugin();
        var d = plugin.modelFactory().createNew();
        assertEquals(new DiagramTypeId("mindmap"), d.typeId());
        assertNotNull(d.rootNode());
        var impl = (MindMapDiagramImpl) d;
        assertTrue(impl.getChildren(d.rootNode()).isEmpty());
    }

    @Test
    void nativePayloadCodecReportsVersion1() {
        var plugin = new MindMapPlugin();
        assertEquals(1, plugin.nativePayloadCodec().currentPayloadVersion());
    }

    @Test
    void externalFormatsListIsEmptyInPlan1() {
        var plugin = new MindMapPlugin();
        // Task 21 populates this. For Plan 1 step Task 19, list is empty.
        assertTrue(plugin.externalFormats().isEmpty());
    }
}
