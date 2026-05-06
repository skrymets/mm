package freemind.diagram.persistence;

import freemind.diagram.Diagram;
import freemind.diagram.DiagramListener;
import freemind.diagram.DiagramMetadata;
import freemind.diagram.DiagramTypeId;
import freemind.diagram.DocumentId;
import freemind.diagram.ResourceTable;
import freemind.diagram.StyleId;
import freemind.diagram.StylePalette;
import freemind.diagram.plugin.DiagramController;
import freemind.diagram.plugin.DiagramControllerFactory;
import freemind.diagram.plugin.DiagramLifecycleHooks;
import freemind.diagram.plugin.DiagramModelFactory;
import freemind.diagram.plugin.DiagramPlugin;
import freemind.diagram.plugin.InMemoryDiagramPluginRegistry;
import freemind.diagram.persistence.external.ExternalDiagramFormat;
import freemind.diagram.style.ColorEntry;
import freemind.diagram.ui.DiagramUiContributions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class NativeDiagramDocumentFormatTest {

    @Test
    void writeThenReadProducesEqualMetadataAndPalette() {
        var registry = new InMemoryDiagramPluginRegistry();
        var plugin = new StubPlugin();
        registry.register(plugin);
        var format = new NativeDiagramDocumentFormat(registry, "test-app/0.0.1");

        var docId = DocumentId.newRandom();
        var palette = StylePalette.empty()
            .withColor(new StyleId("primary"), new ColorEntry("#2D5BAA"));
        var diagram = new StubDiagram(docId,
            DiagramMetadata.empty(Instant.parse("2026-05-01T00:00:00Z")), palette);

        var bytes = new ByteArrayOutputStream();
        format.write(diagram, bytes);

        var read = (StubDiagram) format.read(new ByteArrayInputStream(bytes.toByteArray()));

        assertEquals(docId, read.documentId());
        assertEquals(palette.colors(), read.stylePalette().colors());
    }

    @Test
    void readRejectsUnknownDiagramType() {
        var registry = new InMemoryDiagramPluginRegistry();
        registry.register(new StubPlugin());
        var format = new NativeDiagramDocumentFormat(registry, "test-app/0.0.1");

        // Write with the registered plugin, then mutate type id in raw XML before re-reading.
        var bytes = new ByteArrayOutputStream();
        var diagram = new StubDiagram(DocumentId.newRandom(),
            DiagramMetadata.empty(Instant.parse("2026-01-01T00:00:00Z")),
            StylePalette.empty());
        format.write(diagram, bytes);
        var xml = bytes.toString().replace("type=\"stub\"", "type=\"unknown\"");
        assertThrows(UnsupportedDiagramTypeException.class,
            () -> format.read(new ByteArrayInputStream(xml.getBytes())));
    }

    @Test
    void readRejectsUnsupportedFormatVersion() {
        var registry = new InMemoryDiagramPluginRegistry();
        registry.register(new StubPlugin());
        var format = new NativeDiagramDocumentFormat(registry, "test-app/0.0.1");

        var bytes = new ByteArrayOutputStream();
        var diagram = new StubDiagram(DocumentId.newRandom(),
            DiagramMetadata.empty(Instant.parse("2026-01-01T00:00:00Z")),
            StylePalette.empty());
        format.write(diagram, bytes);
        var xml = bytes.toString().replace("formatVersion=\"1\"", "formatVersion=\"99\"");
        assertThrows(UnsupportedFormatVersionException.class,
            () -> format.read(new ByteArrayInputStream(xml.getBytes())));
    }

    private record StubPlugin() implements DiagramPlugin<StubDiagram> {
        @Override public DiagramTypeId typeId()                           { return new DiagramTypeId("stub"); }
        @Override public DiagramModelFactory<StubDiagram> modelFactory()  { return null; }
        @Override public DiagramControllerFactory<StubDiagram> controllerFactory() {
            return d -> new DiagramController<>() {
                @Override public StubDiagram diagram() { return d; }
                @Override public void dispose() { }
            };
        }
        @Override public DiagramUiContributions uiContributions()          { return DiagramUiContributions.empty(); }
        @Override public DiagramLifecycleHooks<StubDiagram> hooks()        { return DiagramLifecycleHooks.noop(); }
        @Override public DiagramPayloadCodec<StubDiagram> nativePayloadCodec() {
            return new DiagramPayloadCodec<>() {
                @Override public int currentPayloadVersion()                            { return 1; }
                @Override public Set<Integer> supportedPayloadVersions()                { return Set.of(1); }
                @Override public StubDiagram readPayload(int v, PayloadReadContext c)   {
                    return new StubDiagram(c.documentId(),
                        DiagramMetadata.empty(Instant.EPOCH), c.stylePalette());
                }
                @Override public void writePayload(StubDiagram d, PayloadWriteContext c) {
                    c.setPayloadRoot(c.createElement("stub-payload"));
                }
            };
        }
        @Override public List<ExternalDiagramFormat<StubDiagram>> externalFormats() { return List.of(); }
    }

    private record StubDiagram(DocumentId docId, DiagramMetadata metadata, StylePalette palette)
        implements Diagram {
        @Override public DocumentId documentId()                  { return docId; }
        @Override public DiagramTypeId typeId()                   { return new DiagramTypeId("stub"); }
        @Override public StylePalette stylePalette()              { return palette; }
        @Override public ResourceTable resources()                { return ResourceTable.empty(); }
        @Override public void addListener(DiagramListener l)      { /* no-op */ }
        @Override public void removeListener(DiagramListener l)   { /* no-op */ }
    }
}
