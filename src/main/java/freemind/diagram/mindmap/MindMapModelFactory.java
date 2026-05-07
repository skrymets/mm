package freemind.diagram.mindmap;

import freemind.diagram.NodeContent;
import freemind.diagram.NodeId;
import freemind.diagram.plugin.DiagramModelFactory;

public final class MindMapModelFactory implements DiagramModelFactory<MindMapDiagram> {
    @Override
    public MindMapDiagram createNew() {
        var root = new MindMapNodeImpl(new NodeId("root"), NodeContent.plain(""));
        return MindMapDiagramImpl.createEmpty(root);
    }
}
