package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.EditNoteToNodeAction;
import freemind.controller.actions.XmlAction;
import freemind.main.HtmlTools;
import freemind.main.Tools;
import freemind.model.MindMapNode;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.mindmapmode.actions.xml.ActionPair;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class ChangeNoteTextActor extends XmlActorAdapter {

    public ChangeNoteTextActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public void act(XmlAction action) {
        if (action instanceof EditNoteToNodeAction noteTextAction) {
            MindMapNode node = getNodeFromID(noteTextAction.getNode());
            String newText = noteTextAction.getText();
            String oldText = node.getNoteText();
            if (!Objects.equals(newText, oldText)) {
                node.setNoteText(newText);
                getExMapFeedback().nodeChanged(node);
            }
        }
    }

    public Class<EditNoteToNodeAction> getDoActionClass() {
        return EditNoteToNodeAction.class;
    }

    public EditNoteToNodeAction createEditNoteToNodeAction(MindMapNode node, String text) {
        EditNoteToNodeAction nodeAction = new EditNoteToNodeAction();
        nodeAction.setNode(getNodeID(node));
        if (text != null && (!HtmlTools.htmlToPlain(text).isEmpty() || text.contains("<img"))) {
            nodeAction.setText(text);
        } else {
            nodeAction.setText(null);
        }
        return nodeAction;
    }

    public void setNoteText(MindMapNode node, String text) {
        String oldNoteText = node.getNoteText();
        if (Objects.equals(text, oldNoteText)) {
            // they are equal.
            return;
        }
        log.trace("Old Note Text:'{}, new:'{}'.", oldNoteText, text);
        log.trace(Tools.compareText(oldNoteText, text));
        EditNoteToNodeAction doAction = createEditNoteToNodeAction(node, text);
        EditNoteToNodeAction undoAction = createEditNoteToNodeAction(node,
                oldNoteText);
        execute(new ActionPair(doAction, undoAction));
    }

}
