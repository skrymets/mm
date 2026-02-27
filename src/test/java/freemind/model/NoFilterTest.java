package freemind.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link NoFilter} - the default no-op filter that makes all nodes visible.
 */
class NoFilterTest {

    private final NoFilter filter = NoFilter.INSTANCE;

    @Test
    void instanceIsSingleton() {
        assertSame(NoFilter.INSTANCE, filter);
    }

    @Test
    void isVisibleAlwaysReturnsTrue() {
        MindMapNode mockNode = mock(MindMapNode.class);
        assertTrue(filter.isVisible(mockNode));
    }

    @Test
    void isVisibleReturnsTrueForNull() {
        // NoFilter should not blow up on null
        assertTrue(filter.isVisible(null));
    }

    @Test
    void applyFilterIsNoOp() {
        FilterContext mockContext = mock(FilterContext.class);
        // Should not throw and should not interact with context
        filter.applyFilter(mockContext);
        verifyNoInteractions(mockContext);
    }

    @Test
    void areMatchedShownReturnsTrue() {
        assertTrue(filter.areMatchedShown());
    }

    @Test
    void areHiddenShownReturnsFalse() {
        assertFalse(filter.areHiddenShown());
    }

    @Test
    void areAncestorsShownReturnsTrue() {
        assertTrue(filter.areAncestorsShown());
    }

    @Test
    void areDescendantsShownReturnsTrue() {
        assertTrue(filter.areDescendantsShown());
    }

    @Test
    void areEclipsedShownReturnsFalse() {
        assertFalse(filter.areEclipsedShown());
    }

    @Test
    void getConditionReturnsNull() {
        assertNull(filter.getCondition());
    }

    @Test
    void implementsFilterInterface() {
        assertInstanceOf(Filter.class, filter);
    }
}
