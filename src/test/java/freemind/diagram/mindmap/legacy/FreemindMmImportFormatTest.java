package freemind.diagram.mindmap.legacy;

import freemind.diagram.ContentFormat;
import freemind.diagram.DiagramTypeId;
import freemind.diagram.mindmap.MindMapDiagram;
import freemind.diagram.mindmap.MindMapNode;
import freemind.diagram.persistence.external.ExportContext;
import freemind.diagram.persistence.external.ImportContext;
import freemind.diagram.persistence.external.ProbeContext;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

class FreemindMmImportFormatTest {

    private final FreemindMmImportFormat fmt = new FreemindMmImportFormat();

    @Test
    void canReadAcceptsMmExtension() {
        assertTrue(fmt.canRead(Path.of("foo.mm"), new ProbeContext()));
        assertTrue(fmt.canRead(Path.of("FOO.MM"), new ProbeContext()));
        assertFalse(fmt.canRead(Path.of("foo.txt"), new ProbeContext()));
    }

    @Test
    void importsRepresentativeFixture() throws Exception {
        var fixture = Path.of(getClass().getResource(
            "/freemind/diagram/mindmap/legacy/sample-tiny.mm").toURI());
        var diagram = fmt.importDiagram(fixture, new ImportContext());
        assertEquals(new DiagramTypeId("mindmap"), diagram.typeId());

        // Build node-id -> node map for assertions
        var byId = new HashMap<String, MindMapNode>();
        diagram.allNodes().forEach(n -> byId.put(n.nodeId().value(), n));

        assertTrue(byId.size() >= 4, "Expected at least 4 nodes, got " + byId.size());

        // Verify root text
        var root = diagram.rootNode();
        assertEquals("Root", root.content().text(),
            "Root text should be preserved");

        // Verify Node A's attribute survived
        var nodeA = byId.get("ID_a");
        assertNotNull(nodeA, "Node ID_a should be present");
        assertEquals("Node A", nodeA.content().text());
        assertEquals("high", nodeA.attributes().get("priority").orElse(null),
            "Node A's priority=high attribute should be preserved");

        // Verify Node C is HTML
        var nodeC = byId.get("ID_c");
        assertNotNull(nodeC, "Node ID_c should be present");
        assertEquals(ContentFormat.HTML, nodeC.content().format(),
            "Node C should be imported as HTML content");

        // Verify arrowlink connects the right pair (A -> B)
        var auxLinks = StreamSupport.stream(
            diagram.auxiliaryLinks().spliterator(), false).toList();
        assertEquals(1, auxLinks.size(), "Expected exactly one auxiliary link");
        var link = auxLinks.get(0);
        assertEquals("ID_a", link.source().nodeId().value(), "Link source should be Node A");
        assertEquals("ID_b", link.target().nodeId().value(), "Link target should be Node B");
    }

    @Test
    void exportThrows() {
        assertFalse(fmt.canExport(null));
        assertThrows(UnsupportedOperationException.class,
            () -> fmt.exportDiagram(null, Path.of("irrelevant.mm"), new ExportContext()));
    }
}
