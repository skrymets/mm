/**
 * Diagram model abstraction. Topology-agnostic core types ({@link Diagram},
 * {@link DiagramNode}) plus identity and metadata value types.
 *
 * <p>Topology contracts ({@link freemind.diagram.topology.TreeDiagram},
 * {@link freemind.diagram.topology.GraphDiagram}) and capability mixins
 * (e.g. {@link freemind.diagram.capabilities.HasAuxiliaryLinks}) live in
 * adjacent subpackages.
 *
 * <p>This package and its subpackages must not depend on
 * {@code javax.swing.*} or {@code java.awt.*} (enforced by ArchUnit).
 * The single permitted exception is {@code freemind.diagram.swing}, which
 * houses the Swing bridge ({@link freemind.diagram.swing.SwingTreeAdapter}).
 */
package freemind.diagram;
