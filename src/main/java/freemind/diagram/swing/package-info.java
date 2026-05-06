/**
 * Swing bridge for the diagram model. {@link SwingTreeAdapter} adapts any
 * {@link freemind.diagram.topology.TreeDiagram} to the
 * {@link javax.swing.tree.TreeModel} contract so existing Swing UI code
 * (JTree, NodeView, layout strategies) can consume diagrams without the
 * model layer itself depending on Swing.
 *
 * <p>This is the only package under {@code freemind.diagram.*} permitted
 * to depend on {@code javax.swing.*}.
 */
package freemind.diagram.swing;
