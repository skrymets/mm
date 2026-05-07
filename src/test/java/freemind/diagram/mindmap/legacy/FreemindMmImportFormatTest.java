package freemind.diagram.mindmap.legacy;

import freemind.diagram.DiagramTypeId;
import freemind.diagram.mindmap.MindMapDiagram;
import freemind.diagram.persistence.external.ExportContext;
import freemind.diagram.persistence.external.ImportContext;
import freemind.diagram.persistence.external.ProbeContext;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;
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

        // Tree has at least 4 nodes (root, a, b, c)
        var allIds = new ArrayList<String>();
        diagram.allNodes().forEach(n -> allIds.add(n.nodeId().value()));
        assertTrue(allIds.size() >= 4, "Expected at least 4 nodes, got: " + allIds);

        // At least one auxiliary link (arrowlink a -> b)
        var auxLinks = StreamSupport.stream(diagram.auxiliaryLinks().spliterator(), false).toList();
        assertEquals(1, auxLinks.size(), "Expected exactly one auxiliary link");
    }

    @Test
    void exportThrows() {
        assertFalse(fmt.canExport(null));
        assertThrows(UnsupportedOperationException.class,
            () -> fmt.exportDiagram(null, Path.of("irrelevant.mm"), new ExportContext()));
    }
}
