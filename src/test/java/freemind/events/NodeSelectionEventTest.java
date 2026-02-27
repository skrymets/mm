package freemind.events;

import freemind.extensions.PermanentNodeHook;
import freemind.model.MindMapNode;
import freemind.modes.ControllerAdapter;
import freemind.modes.Mode;
import freemind.view.mindmapview.NodeView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for NodeSelectionChangedEvent record and its integration
 * with ControllerAdapter's onFocusNode/onLostFocusNode methods.
 */
class NodeSelectionEventTest {

    private FreeMindEventBus eventBus;
    private ControllerAdapter controllerAdapter;
    private NodeView nodeView;
    private MindMapNode mindMapNode;

    @BeforeEach
    void setUp() {
        eventBus = mock(FreeMindEventBus.class);
        Mode mockMode = mock(Mode.class);
        controllerAdapter = mock(ControllerAdapter.class, withSettings()
                .useConstructor(mockMode)
                .defaultAnswer(CALLS_REAL_METHODS));
        controllerAdapter.setEventBus(eventBus);

        mindMapNode = mock(MindMapNode.class);
        when(mindMapNode.getActivatedHooks()).thenReturn(Collections.<PermanentNodeHook>emptyList());

        nodeView = mock(NodeView.class);
        when(nodeView.getModel()).thenReturn(mindMapNode);
    }

    @Test
    void eventRecordContainsCorrectData() {
        MindMapNode node = mock(MindMapNode.class);
        NodeSelectionChangedEvent event = new NodeSelectionChangedEvent(node, true);

        assertSame(node, event.node());
        assertTrue(event.selected());
    }

    @Test
    void eventRecordDeselected() {
        MindMapNode node = mock(MindMapNode.class);
        NodeSelectionChangedEvent event = new NodeSelectionChangedEvent(node, false);

        assertSame(node, event.node());
        assertFalse(event.selected());
    }

    @Test
    void eventRecordEquality() {
        MindMapNode node = mock(MindMapNode.class);
        NodeSelectionChangedEvent event1 = new NodeSelectionChangedEvent(node, true);
        NodeSelectionChangedEvent event2 = new NodeSelectionChangedEvent(node, true);

        assertEquals(event1, event2);
        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    void eventRecordInequalityOnSelectedFlag() {
        MindMapNode node = mock(MindMapNode.class);
        NodeSelectionChangedEvent selected = new NodeSelectionChangedEvent(node, true);
        NodeSelectionChangedEvent deselected = new NodeSelectionChangedEvent(node, false);

        assertNotEquals(selected, deselected);
    }

    @Test
    void onFocusNodePostsSelectionEvent() {
        controllerAdapter.onFocusNode(nodeView);

        ArgumentCaptor<NodeSelectionChangedEvent> captor =
                ArgumentCaptor.forClass(NodeSelectionChangedEvent.class);
        verify(eventBus).post(captor.capture());

        NodeSelectionChangedEvent event = captor.getValue();
        assertSame(mindMapNode, event.node());
        assertTrue(event.selected());
    }

    @Test
    void onLostFocusNodePostsDeselectionEvent() {
        controllerAdapter.onLostFocusNode(nodeView);

        ArgumentCaptor<NodeSelectionChangedEvent> captor =
                ArgumentCaptor.forClass(NodeSelectionChangedEvent.class);
        verify(eventBus).post(captor.capture());

        NodeSelectionChangedEvent event = captor.getValue();
        assertSame(mindMapNode, event.node());
        assertFalse(event.selected());
    }

    @Test
    void noExceptionWhenEventBusIsNull() {
        controllerAdapter.setEventBus(null);

        // Should not throw NPE
        assertDoesNotThrow(() -> controllerAdapter.onFocusNode(nodeView));
        assertDoesNotThrow(() -> controllerAdapter.onLostFocusNode(nodeView));
    }

    @Test
    void eventRecordWithNullNode() {
        NodeSelectionChangedEvent event = new NodeSelectionChangedEvent(null, true);
        assertNull(event.node());
        assertTrue(event.selected());
    }

    @Test
    void eventRecordToString() {
        MindMapNode node = mock(MindMapNode.class);
        NodeSelectionChangedEvent event = new NodeSelectionChangedEvent(node, true);
        String str = event.toString();

        assertTrue(str.contains("NodeSelectionChangedEvent"));
        assertTrue(str.contains("true"));
    }
}
