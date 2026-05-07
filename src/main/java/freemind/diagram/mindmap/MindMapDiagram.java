package freemind.diagram.mindmap;

import freemind.diagram.capabilities.HasAuxiliaryLinks;
import freemind.diagram.topology.TreeDiagram;

/**
 * The mind-map concrete diagram type: a tree with optional auxiliary links
 * (formerly known as ArrowLinks).
 */
public interface MindMapDiagram
    extends TreeDiagram<MindMapNode>, HasAuxiliaryLinks<MindMapNode> {
}
