package freemind.diagram.plugin;

import freemind.diagram.Diagram;
import freemind.diagram.DiagramTypeId;
import freemind.diagram.DocumentId;
import freemind.diagram.DiagramListener;
import freemind.diagram.DiagramMetadata;
import freemind.diagram.ResourceTable;
import freemind.diagram.StylePalette;
import freemind.diagram.persistence.DiagramPayloadCodec;
import freemind.diagram.persistence.PayloadReadContext;
import freemind.diagram.persistence.PayloadWriteContext;
import freemind.diagram.persistence.external.ExternalDiagramFormat;
import freemind.diagram.ui.DiagramUiContributions;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryDiagramPluginRegistryTest {

    @Test
    void emptyRegistryFindsNothing() {
        var r = new InMemoryDiagramPluginRegistry();
        assertTrue(r.findByTypeId(new DiagramTypeId("missing")).isEmpty());
        assertTrue(r.all().isEmpty());
    }

    @Test
    void registerThenFindReturnsPlugin() {
        var r = new InMemoryDiagramPluginRegistry();
        var plugin = new TestPlugin(new DiagramTypeId("t1"));
        r.register(plugin);
        assertSame(plugin, r.findByTypeId(new DiagramTypeId("t1")).orElseThrow());
        assertEquals(1, r.all().size());
    }

    @Test
    void duplicateRegistrationFails() {
        var r = new InMemoryDiagramPluginRegistry();
        r.register(new TestPlugin(new DiagramTypeId("t1")));
        assertThrows(IllegalStateException.class,
            () -> r.register(new TestPlugin(new DiagramTypeId("t1"))));
    }

    // Minimal stub plugin for registry exercise.
    private record TestPlugin(DiagramTypeId typeId) implements DiagramPlugin<TestDiagram> {
        @Override public DiagramModelFactory<TestDiagram> modelFactory() {
            return () -> new TestDiagram(typeId);
        }
        @Override public DiagramControllerFactory<TestDiagram> controllerFactory() {
            return d -> new DiagramController<>() {
                @Override public TestDiagram diagram() { return d; }
                @Override public void dispose() { }
            };
        }
        @Override public DiagramUiContributions uiContributions() {
            return DiagramUiContributions.empty();
        }
        @Override public DiagramLifecycleHooks<TestDiagram> hooks() {
            return DiagramLifecycleHooks.noop();
        }
        @Override public DiagramPayloadCodec<TestDiagram> nativePayloadCodec() {
            return new DiagramPayloadCodec<>() {
                @Override public int currentPayloadVersion()                          { return 1; }
                @Override public Set<Integer> supportedPayloadVersions()              { return Set.of(1); }
                @Override public TestDiagram readPayload(int v, PayloadReadContext c) { return new TestDiagram(typeId); }
                @Override public void writePayload(TestDiagram d, PayloadWriteContext c) { /* no-op */ }
            };
        }
        @Override public List<ExternalDiagramFormat<TestDiagram>> externalFormats() {
            return List.of();
        }
    }

    private record TestDiagram(DiagramTypeId typeId) implements Diagram {
        @Override public DocumentId documentId()                 { return DocumentId.newRandom(); }
        @Override public DiagramMetadata metadata()              { return DiagramMetadata.empty(Instant.EPOCH); }
        @Override public StylePalette stylePalette()             { return StylePalette.empty(); }
        @Override public ResourceTable resources()               { return ResourceTable.empty(); }
        @Override public void addListener(DiagramListener l)     { /* no-op */ }
        @Override public void removeListener(DiagramListener l)  { /* no-op */ }
    }
}
