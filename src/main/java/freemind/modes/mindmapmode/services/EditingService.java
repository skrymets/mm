package freemind.modes.mindmapmode.services;

import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.actions.xml.actors.XmlActorFactory;

/**
 * Service for text editing operations on mind map nodes.
 * Extracted from MindMapController to reduce class coupling.
 */
public class EditingService {

    private final XmlActorFactory actorFactory;

    public EditingService(XmlActorFactory actorFactory) {
        this.actorFactory = actorFactory;
    }

    /**
     * Sets the text content of the specified node.
     */
    public void setNodeText(MindMapNode selected, String newText) {
        actorFactory.getEditActor().setNodeText(selected, newText);
    }

    /**
     * Sets the note text of the specified node.
     */
    public void setNoteText(MindMapNode selected, String newText) {
        actorFactory.getChangeNoteTextActor().setNoteText(selected, newText);
    }
}
