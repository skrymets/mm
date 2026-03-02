package freemind.model;

/**
 * Filter flag constants shared between the model and controller filter layers.
 */
public final class FilterConstants {
    public static final int FILTER_INITIAL_VALUE = 1;
    public static final int FILTER_SHOW_MATCHED = 2;
    public static final int FILTER_SHOW_ANCESTOR = 4;
    public static final int FILTER_SHOW_DESCENDANT = 8;
    public static final int FILTER_SHOW_ECLIPSED = 16;
    public static final int FILTER_SHOW_HIDDEN = 32;

    private FilterConstants() {
        // utility class
    }
}
