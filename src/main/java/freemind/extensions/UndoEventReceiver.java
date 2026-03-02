package freemind.extensions;

/**
 * This is a marker interface for hooks. If a PermanentNodeHook implements this
 * interface (which is easy), it receives onUpdateNodeHook,
 * onUpdateChildrenHook, even when the action issuing this update is caused by an
 * undo action.
 * <p>
 * Normally, on undo, no event are generated.
 * <p>
 * The onAddChild, onAddChildren events are not implemented yet. They are
 * generated in any case.
 */
public interface UndoEventReceiver {

}
