package freemind.diagram.mindmap;

import freemind.diagram.AttributeBag;
import freemind.diagram.ContentFormat;
import freemind.diagram.DiagramMetadata;
import freemind.diagram.NodeContent;
import freemind.diagram.NodeId;
import freemind.diagram.StyleId;
import freemind.diagram.StylePalette;
import freemind.diagram.StyleRef;
import freemind.diagram.StyleReferences;
import freemind.diagram.capabilities.AuxiliaryLink;
import freemind.diagram.mindmap.legacy.FreemindMmImportFormat;
import freemind.diagram.persistence.NativeDiagramDocumentFormat;
import freemind.diagram.persistence.external.ImportContext;
import freemind.diagram.plugin.InMemoryDiagramPluginRegistry;
import freemind.diagram.style.ColorEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.file.Path;
import java.time.Instant;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

class MindMapPluginIntegrationTest {

    private InMemoryDiagramPluginRegistry registry;
    private NativeDiagramDocumentFormat format;
    private MindMapPlugin plugin;

    @BeforeEach
    void setUp() {
        registry = new InMemoryDiagramPluginRegistry();
        plugin = new MindMapPlugin(new MindMapModelFactory(), new MindMapControllerFactory());
        registry.register(plugin);
        format = new NativeDiagramDocumentFormat(registry, "test-app/1.0.0");
    }

    @Test
    void pathA_newDiagramRoundTripsThroughNativeEnvelope() {
        // 1. Build a non-trivial diagram from scratch via factory + mutators
        var diagram = (MindMapDiagramImpl) plugin.modelFactory().createNew();
        diagram.setMetadata(DiagramMetadata.empty(Instant.parse("2026-05-01T00:00:00Z"))
            .withTitle("Round-trip Test"));
        diagram.setStylePalette(StylePalette.empty()
            .withColor(new StyleId("primary"), new ColorEntry("#2D5BAA")));

        var root = (MindMapNodeImpl) diagram.rootNode();
        root.setContent(NodeContent.plain("Root"));
        var a = new MindMapNodeImpl(new NodeId("a"), NodeContent.html("<b>Alpha</b>"));
        a.setStyleReferences(new StyleReferences(
            Optional.of(new StyleRef(new StyleId("primary"))),
            Optional.empty(), Optional.empty()));
        a.setAttributes(AttributeBag.empty().with("kind", "important"));
        var b = new MindMapNodeImpl(new NodeId("b"), NodeContent.plain("Beta"));
        diagram.addChild(root, a);
        diagram.addChild(root, b);
        diagram.addAuxiliaryLink(new AuxiliaryLink<>(a, b,
            Optional.of("relates-to"), StyleReferences.none()));

        // 2. Write to bytes
        var bytes = new ByteArrayOutputStream();
        format.write(diagram, bytes);

        // 3. Read back
        var reloaded = (MindMapDiagram) format.read(new ByteArrayInputStream(bytes.toByteArray()));

        // 4. Assertions
        assertEquals(diagram.documentId(), reloaded.documentId(), "documentId preserved");
        assertEquals(Optional.of("Round-trip Test"), reloaded.metadata().title(),
            "title preserved");
        assertEquals(diagram.stylePalette().colors(), reloaded.stylePalette().colors(),
            "style palette preserved");

        // Tree shape
        var nodeIds = StreamSupport.stream(reloaded.allNodes().spliterator(), false)
            .map(n -> n.nodeId().value()).collect(Collectors.toSet());
        assertEquals(Set.of("root", "a", "b"), nodeIds, "tree node ids preserved");

        // Content
        var byId = new HashMap<String, MindMapNode>();
        reloaded.allNodes().forEach(n -> byId.put(n.nodeId().value(), n));
        assertEquals(ContentFormat.HTML, byId.get("a").content().format(),
            "node a content format preserved");
        assertEquals(ContentFormat.PLAIN, byId.get("b").content().format(),
            "node b content format preserved");
        assertEquals("Beta", byId.get("b").content().text(),
            "node b content text preserved");

        // Style ref
        var aRefs = byId.get("a").styleReferences();
        assertEquals(Optional.of(new StyleRef(new StyleId("primary"))), aRefs.color(),
            "style color reference preserved");

        // Attribute
        assertEquals("important", byId.get("a").attributes().get("kind").orElse(null),
            "node attribute preserved");

        // Aux link
        var auxLinks = StreamSupport.stream(reloaded.auxiliaryLinks().spliterator(), false).toList();
        assertEquals(1, auxLinks.size(), "one auxiliary link");
        var link = auxLinks.get(0);
        assertEquals("a", link.source().nodeId().value(), "aux link source preserved");
        assertEquals("b", link.target().nodeId().value(), "aux link target preserved");
        assertEquals(Optional.of("relates-to"), link.label(), "aux link label preserved");
    }

    @Test
    void pathB_legacyMmImportThenNativeSaveAndReload() throws Exception {
        // 1. Find the .mm import format on the plugin
        var mmFormat = plugin.externalFormats().stream()
            .filter(f -> f instanceof FreemindMmImportFormat)
            .findFirst()
            .orElseThrow(() -> new AssertionError("FreemindMmImportFormat not registered"));

        // 2. Import sample-tiny.mm
        var fixture = Path.of(getClass().getResource(
            "/freemind/diagram/mindmap/legacy/sample-tiny.mm").toURI());
        var imported = mmFormat.importDiagram(fixture, new ImportContext());

        var importedNodeIds = StreamSupport.stream(imported.allNodes().spliterator(), false)
            .map(n -> n.nodeId().value()).collect(Collectors.toSet());

        // 3. Save imported diagram via native envelope
        var bytes = new ByteArrayOutputStream();
        format.write(imported, bytes);

        // 4. Reload via native envelope
        var reloaded = (MindMapDiagram) format.read(new ByteArrayInputStream(bytes.toByteArray()));

        // 5. Assert structural & content equality
        var reloadedNodeIds = StreamSupport.stream(reloaded.allNodes().spliterator(), false)
            .map(n -> n.nodeId().value()).collect(Collectors.toSet());
        assertEquals(importedNodeIds, reloadedNodeIds,
            "node id set preserved across .mm -> native -> reload");

        // Specific assertions on known fixture content
        var byId = new HashMap<String, MindMapNode>();
        reloaded.allNodes().forEach(n -> byId.put(n.nodeId().value(), n));
        assertEquals("high", byId.get("ID_a").attributes().get("priority").orElse(null),
            "Node A's priority=high attribute survives full round-trip");
        assertEquals(ContentFormat.HTML, byId.get("ID_c").content().format(),
            "Node C's HTML format survives full round-trip");

        var auxLinks = StreamSupport.stream(reloaded.auxiliaryLinks().spliterator(), false).toList();
        assertEquals(1, auxLinks.size(), "arrowlink survives full round-trip");
        var link = auxLinks.get(0);
        assertEquals("ID_a", link.source().nodeId().value(),
            "arrowlink source preserved");
        assertEquals("ID_b", link.target().nodeId().value(),
            "arrowlink target preserved");
    }

    @Test
    void pathC_envelopeMetadataRoundTripsThroughRealCodec() {
        // 1. Build a diagram with non-empty metadata
        var diagram = (MindMapDiagramImpl) plugin.modelFactory().createNew();
        diagram.setMetadata(DiagramMetadata.empty(Instant.parse("2026-05-01T00:00:00Z"))
            .withTitle("Hello").withAuthor("Alice"));

        // 2. Round-trip
        var bytes = new ByteArrayOutputStream();
        format.write(diagram, bytes);
        var reloaded = (MindMapDiagram) format.read(new ByteArrayInputStream(bytes.toByteArray()));

        // 3. Assert metadata preserved
        assertEquals(Optional.of("Hello"), reloaded.metadata().title(),
            "metadata title preserved through real codec");
        assertEquals(Optional.of("Alice"), reloaded.metadata().author(),
            "metadata author preserved through real codec");
    }
}
