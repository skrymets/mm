package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.UndoPasteNodeAction;
import freemind.controller.actions.XmlAction;
import freemind.model.MindMapNode;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.mindmapmode.actions.xml.actors.PasteActor.NodeCoordinate;

public class UndoPasteActor extends XmlActorAdapter {

    public UndoPasteActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public void act(XmlAction pAction) {
        if (pAction instanceof UndoPasteNodeAction) {
            UndoPasteNodeAction undoAction = (UndoPasteNodeAction) pAction;
            MindMapNode selectedNode = getNodeFromID(undoAction.getNode());
            int amount = undoAction.getNodeAmount();
            while (amount > 0) {
                NodeCoordinate coordinate = new NodeCoordinate(selectedNode,
                        undoAction.isAsSibling(), undoAction.isIsLeft());
                MindMapNode targetNode = coordinate.getNode();
                getXmlActorFactory().getDeleteChildActor().deleteWithoutUndo(targetNode);
                amount--;
            }
        }
    }

    public Class<UndoPasteNodeAction> getDoActionClass() {
        return UndoPasteNodeAction.class;
    }

}
