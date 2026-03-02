package freemind.model;

/**
 * A no-op filter that makes all nodes visible.
 * Used as the default filter when no filtering is active,
 * replacing the previous dependency on controller-layer
 * DefaultFilter and NoFilteringCondition classes.
 */
public class NoFilter implements Filter {
    public static final NoFilter INSTANCE = new NoFilter();

    @Override
    public void applyFilter(FilterContext context) {
        /* no-op */
    }

    @Override
    public boolean isVisible(MindMapNode node) {
        return true;
    }

    @Override
    public boolean areMatchedShown() {
        return true;
    }

    @Override
    public boolean areHiddenShown() {
        return false;
    }

    @Override
    public boolean areAncestorsShown() {
        return true;
    }

    @Override
    public boolean areDescendantsShown() {
        return true;
    }

    @Override
    public boolean areEclipsedShown() {
        return false;
    }

    @Override
    public Object getCondition() {
        return null;
    }
}
