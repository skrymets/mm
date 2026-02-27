package freemind.events;

import freemind.model.MapAdapter;
import freemind.model.MindMapNode;
import freemind.modes.ControllerAdapter;
import freemind.modes.Mode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for NodeModifiedEvent record and its integration
 * with ControllerAdapter's nodeChanged() method.
 */
class NodeModifiedEventTest {

    private FreeMindEventBus eventBus;
    private ControllerAdapter controllerAdapter;
    private MindMapNode mindMapNode;

    @BeforeEach
    void setUp() {
        eventBus = mock(FreeMindEventBus.class);
        Mode mockMode = mock(Mode.class);
        controllerAdapter = mock(ControllerAdapter.class, withSettings()
                .useConstructor(mockMode)
                .defaultAnswer(CALLS_REAL_METHODS));
        controllerAdapter.setEventBus(eventBus);

        // Stub setSaved to avoid deep call chain (getMap -> getController -> setTitle)
        doNothing().when(controllerAdapter).setSaved(anyBoolean());

        // Set up a mock map so nodeRefresh -> getMap().nodeChangedInternal doesn't NPE
        MapAdapter mockMap = mock(MapAdapter.class);
        controllerAdapter.setModel(mockMap);

        mindMapNode = mock(MindMapNode.class);
    }

    @Test
    void eventRecordContainsCorrectData() {
        MindMapNode node = mock(MindMapNode.class);
        NodeModifiedEvent event = new NodeModifiedEvent(node, "text", "old", "new");

        assertSame(node, event.node());
        assertEquals("text", event.property());
        assertEquals("old", event.oldValue());
        assertEquals("new", event.newValue());
    }

    @Test
    void eventRecordWithNullValues() {
        MindMapNode node = mock(MindMapNode.class);
        NodeModifiedEvent event = new NodeModifiedEvent(node, null, null, null);

        assertSame(node, event.node());
        assertNull(event.property());
        assertNull(event.oldValue());
        assertNull(event.newValue());
    }

    @Test
    void eventRecordEquality() {
        MindMapNode node = mock(MindMapNode.class);
        NodeModifiedEvent event1 = new NodeModifiedEvent(node, "color", "red", "blue");
        NodeModifiedEvent event2 = new NodeModifiedEvent(node, "color", "red", "blue");

        assertEquals(event1, event2);
        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    void eventRecordInequalityOnProperty() {
        MindMapNode node = mock(MindMapNode.class);
        NodeModifiedEvent event1 = new NodeModifiedEvent(node, "color", "red", "blue");
        NodeModifiedEvent event2 = new NodeModifiedEvent(node, "font", "red", "blue");

        assertNotEquals(event1, event2);
    }

    @Test
    void nodeChangedPostsModifiedEvent() {
        controllerAdapter.nodeChanged(mindMapNode);

        ArgumentCaptor<NodeModifiedEvent> captor =
                ArgumentCaptor.forClass(NodeModifiedEvent.class);
        verify(eventBus).post(captor.capture());

        NodeModifiedEvent event = captor.getValue();
        assertSame(mindMapNode, event.node());
        // The generic nodeChanged call posts with null property/values
        assertNull(event.property());
        assertNull(event.oldValue());
        assertNull(event.newValue());
    }

    @Test
    void nodeChangedStillCallsOriginalBehavior() {
        controllerAdapter.nodeChanged(mindMapNode);

        verify(controllerAdapter).setSaved(false);
    }

    @Test
    void noExceptionWhenEventBusIsNull() {
        controllerAdapter.setEventBus(null);

        assertDoesNotThrow(() -> controllerAdapter.nodeChanged(mindMapNode));
    }

    @Test
    void eventRecordToString() {
        MindMapNode node = mock(MindMapNode.class);
        NodeModifiedEvent event = new NodeModifiedEvent(node, "text", "hello", "world");
        String str = event.toString();

        assertTrue(str.contains("NodeModifiedEvent"));
        assertTrue(str.contains("text"));
    }

    @Test
    void eventRecordWithNullNode() {
        NodeModifiedEvent event = new NodeModifiedEvent(null, "prop", "a", "b");
        assertNull(event.node());
        assertEquals("prop", event.property());
    }

    @Test
    void multipleNodeChangedCallsPostMultipleEvents() {
        MindMapNode node1 = mock(MindMapNode.class);
        MindMapNode node2 = mock(MindMapNode.class);

        controllerAdapter.nodeChanged(node1);
        controllerAdapter.nodeChanged(node2);

        verify(eventBus, times(2)).post(any(NodeModifiedEvent.class));
    }
}
