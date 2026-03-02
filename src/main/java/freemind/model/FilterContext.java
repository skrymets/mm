package freemind.model;

/**
 * Provides the model-level context needed by {@link Filter#applyFilter(FilterContext)},
 * so the Filter interface can live in the model package without depending on Controller.
 */
public interface FilterContext {
    MindMapNode getRootNode();
    void setWaitingCursor(boolean waiting);
}
