package freemind.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link FilterInfo} - bitflag-based filter state for mind map nodes.
 */
class FilterInfoTest {

    private FilterInfo filterInfo;

    @BeforeEach
    void setUp() {
        filterInfo = new FilterInfo();
    }

    @Test
    void newInstanceHasInitialValue() {
        assertEquals(FilterConstants.FILTER_INITIAL_VALUE, filterInfo.get());
    }

    @Test
    void resetRestoresInitialValue() {
        filterInfo.setMatched();
        filterInfo.reset();
        assertEquals(FilterConstants.FILTER_INITIAL_VALUE, filterInfo.get());
    }

    @Test
    void setMatchedClearsInitialValueAndSetsMatchedFlag() {
        filterInfo.setMatched();
        assertTrue(filterInfo.isMatched());
        // FILTER_INITIAL_VALUE should be cleared when FILTER_SHOW_MATCHED is added
        assertEquals(0, filterInfo.get() & FilterConstants.FILTER_INITIAL_VALUE);
    }

    @Test
    void setAncestorSetsAncestorFlag() {
        filterInfo.setAncestor();
        assertTrue(filterInfo.isAncestor());
    }

    @Test
    void setDescendantSetsDescendantFlag() {
        filterInfo.setDescendant();
        int expected = FilterConstants.FILTER_INITIAL_VALUE | FilterConstants.FILTER_SHOW_DESCENDANT;
        assertEquals(expected, filterInfo.get());
    }

    @Test
    void isMatchedReturnsFalseByDefault() {
        assertFalse(filterInfo.isMatched());
    }

    @Test
    void isAncestorReturnsFalseByDefault() {
        assertFalse(filterInfo.isAncestor());
    }

    @Test
    void addHiddenClearsInitialValue() {
        filterInfo.add(FilterConstants.FILTER_SHOW_HIDDEN);
        assertEquals(0, filterInfo.get() & FilterConstants.FILTER_INITIAL_VALUE);
        assertNotEquals(0, filterInfo.get() & FilterConstants.FILTER_SHOW_HIDDEN);
    }

    @Test
    void addAncestorPreservesInitialValue() {
        // ANCESTOR flag does not clear INITIAL_VALUE (not in the matched|hidden mask)
        filterInfo.add(FilterConstants.FILTER_SHOW_ANCESTOR);
        assertNotEquals(0, filterInfo.get() & FilterConstants.FILTER_INITIAL_VALUE);
        assertTrue(filterInfo.isAncestor());
    }

    @Test
    void multipleFlagsAccumulate() {
        filterInfo.setMatched();
        filterInfo.setAncestor();
        filterInfo.setDescendant();
        assertTrue(filterInfo.isMatched());
        assertTrue(filterInfo.isAncestor());
        int val = filterInfo.get();
        assertNotEquals(0, val & FilterConstants.FILTER_SHOW_DESCENDANT);
    }

    @Test
    void resetAfterMultipleFlagsClearsAll() {
        filterInfo.setMatched();
        filterInfo.setAncestor();
        filterInfo.setDescendant();
        filterInfo.reset();
        assertEquals(FilterConstants.FILTER_INITIAL_VALUE, filterInfo.get());
        assertFalse(filterInfo.isMatched());
        assertFalse(filterInfo.isAncestor());
    }
}
