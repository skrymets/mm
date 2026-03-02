package freemind.modes.mindmapmode.actions.xml;

import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.actions.RedoAction;
import freemind.modes.mindmapmode.actions.UndoAction;

public class UndoActionHandler {
    private final MindMapController controller;
    private final UndoAction undo;
    private final RedoAction redo;

    public UndoActionHandler(MindMapController adapter, UndoAction undo,
                             RedoAction redo) {
        this.controller = adapter;
        this.undo = undo;
        this.redo = redo;
    }

    public void executeAction(ActionPair pair) {
        if (!controller.isUndoAction()) {
            redo.clear();
            undo.add(pair);
            // undo.print();
            undo.setEnabled(true);
            redo.setEnabled(false);
        }
    }

    public void startTransaction(String name) {
    }

    public void endTransaction(String name) {
    }
}
