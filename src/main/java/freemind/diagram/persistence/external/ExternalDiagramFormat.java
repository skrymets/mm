package freemind.diagram.persistence.external;

import freemind.diagram.Diagram;
import java.nio.file.Path;
import java.util.Set;

/**
 * Plugin-owned import/export adapter for a non-native format.
 *
 * @param <D> the concrete diagram type produced by import / consumed by export
 */
public interface ExternalDiagramFormat<D extends Diagram> {

    ExternalFormatId id();

    Set<String> fileExtensions();

    boolean canRead(Path file, ProbeContext context);

    D importDiagram(Path file, ImportContext context);

    boolean canExport(D diagram);

    void exportDiagram(D diagram, Path file, ExportContext context);
}
