/*
 * Created on 15.05.2005
 *
 */
package freemind.model;

public class FilterInfo {
    private int info = FilterConstants.FILTER_INITIAL_VALUE;

    public FilterInfo() {
        super();
    }

    public void reset() {
        info = FilterConstants.FILTER_INITIAL_VALUE;
    }

    public void setAncestor() {
        add(FilterConstants.FILTER_SHOW_ANCESTOR);
    }

    public void setDescendant() {
        add(FilterConstants.FILTER_SHOW_DESCENDANT);
    }

    public void setMatched() {
        add(FilterConstants.FILTER_SHOW_MATCHED);
    }

    public void add(int flag) {
        if ((flag & (FilterConstants.FILTER_SHOW_MATCHED | FilterConstants.FILTER_SHOW_HIDDEN)) != 0) {
            info &= ~FilterConstants.FILTER_INITIAL_VALUE;
        }
        info |= flag;
    }

    public int get() {
        return info;
    }

    public boolean isAncestor() {
        return (info & FilterConstants.FILTER_SHOW_ANCESTOR) != 0;
    }

    public boolean isMatched() {
        return (info & FilterConstants.FILTER_SHOW_MATCHED) != 0;
    }
}
