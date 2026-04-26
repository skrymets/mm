package freemind.model;

/**
 * Marker interface implemented by node-attachable icons. Lives in the model
 * layer so that node APIs (icon list type, addIcon position parameter) do not
 * leak modes-layer types.
 */
public interface NodeIcon {
    /**
     * Sentinel position passed to addIcon to mean "append".
     * The value is -1.
     */
    int LAST = -1;

    /** Returns the icon name used for persistence and lookup. */
    String getName();
}
