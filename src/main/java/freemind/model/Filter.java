package freemind.model;

/**
 * Filter applied to a mind map to control node visibility.
 */
public interface Filter {

    void applyFilter(FilterContext context);

    boolean isVisible(MindMapNode node);

    boolean areMatchedShown();

    boolean areHiddenShown();

    boolean areAncestorsShown();

    boolean areDescendantsShown();

    boolean areEclipsedShown();

    Object getCondition();
}
