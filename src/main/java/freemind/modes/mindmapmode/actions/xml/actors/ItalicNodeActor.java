package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.ItalicNodeAction;
import freemind.controller.actions.XmlAction;
import freemind.model.MindMap;
import freemind.model.MindMapNode;
import freemind.model.NodeAdapter;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

public class ItalicNodeActor extends NodeXmlActorAdapter {

    public ItalicNodeActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public void act(XmlAction action) {
        ItalicNodeAction italicact = (ItalicNodeAction) action;
        NodeAdapter node = getNodeFromID(italicact.getNode());
        if (node.isItalic() != italicact.isItalic()) {
            node.setItalic(italicact.isItalic());
            mMapFeedback.nodeChanged(node);
        }
    }

    public Class<ItalicNodeAction> getDoActionClass() {
        return ItalicNodeAction.class;
    }

    public ActionPair apply(MindMap model, MindMapNode selected) {
        // every node is set to the inverse of the focussed node.
        boolean italic = getSelected().isItalic();
        return getActionPair(selected, !italic);
    }

    private ActionPair getActionPair(MindMapNode selected, boolean italic) {
        ItalicNodeAction italicAction = toggleItalic(selected, italic);
        ItalicNodeAction undoItalicAction = toggleItalic(selected,
                selected.isItalic());
        return new ActionPair(italicAction, undoItalicAction);
    }

    private ItalicNodeAction toggleItalic(MindMapNode selected, boolean italic) {
        ItalicNodeAction italicAction = new ItalicNodeAction();
        italicAction.setNode(getNodeID(selected));
        italicAction.setItalic(italic);
        return italicAction;
    }

    public void setItalic(MindMapNode node, boolean italic) {
        execute(getActionPair(node, italic));
    }

}
