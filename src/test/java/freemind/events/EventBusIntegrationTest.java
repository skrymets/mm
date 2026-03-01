package freemind.events;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;
import freemind.controller.Controller;
import freemind.controller.MapModuleManager;
import freemind.main.Resources;
import freemind.model.MindMap;
import freemind.model.MindMapNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Integration tests for the FreeMindEventBus verifying end-to-end dispatch
 * of domain events, multi-subscriber delivery, type hierarchy, thread safety,
 * dead event handling, and MapModuleManager wiring.
 */
class EventBusIntegrationTest {

    private FreeMindEventBus eventBus;

    @BeforeEach
    void setUp() {
        eventBus = new FreeMindEventBus();
    }

    // ---- Event dispatch for each event type ----

    @Test
    void nodeSelectionChangedEventDispatchedToSubscriber() {
        NodeSelectionSubscriber subscriber = new NodeSelectionSubscriber();
        eventBus.register(subscriber);

        MindMapNode node = mock(MindMapNode.class);
        eventBus.post(new NodeSelectionChangedEvent(node, true));

        assertEquals(1, subscriber.received.size());
        assertSame(node, subscriber.received.get(0).node());
        assertTrue(subscriber.received.get(0).selected());
    }

    @Test
    void nodeModifiedEventDispatchedToSubscriber() {
        NodeModifiedSubscriber subscriber = new NodeModifiedSubscriber();
        eventBus.register(subscriber);

        MindMapNode node = mock(MindMapNode.class);
        eventBus.post(new NodeModifiedEvent(node, "text", "old", "new"));

        assertEquals(1, subscriber.received.size());
        NodeModifiedEvent event = subscriber.received.get(0);
        assertSame(node, event.node());
        assertEquals("text", event.property());
        assertEquals("old", event.oldValue());
        assertEquals("new", event.newValue());
    }

    @Test
    void mapLoadedEventDispatchedToSubscriber() {
        MapLoadedSubscriber subscriber = new MapLoadedSubscriber();
        eventBus.register(subscriber);

        MindMap map = mock(MindMap.class);
        eventBus.post(new MapLoadedEvent(map, "test.mm"));

        assertEquals(1, subscriber.received.size());
        assertSame(map, subscriber.received.get(0).map());
        assertEquals("test.mm", subscriber.received.get(0).fileName());
    }

    @Test
    void mapClosedEventDispatchedToSubscriber() {
        MapClosedSubscriber subscriber = new MapClosedSubscriber();
        eventBus.register(subscriber);

        MindMap map = mock(MindMap.class);
        eventBus.post(new MapClosedEvent(map));

        assertEquals(1, subscriber.received.size());
        assertSame(map, subscriber.received.get(0).map());
    }

    // ---- Event record correctness ----

    @Test
    void allEventTypesContainCorrectData() {
        MindMapNode node = mock(MindMapNode.class);
        MindMap map = mock(MindMap.class);

        NodeSelectionChangedEvent selEvent = new NodeSelectionChangedEvent(node, false);
        assertSame(node, selEvent.node());
        assertFalse(selEvent.selected());

        NodeModifiedEvent modEvent = new NodeModifiedEvent(node, "color", "red", "blue");
        assertSame(node, modEvent.node());
        assertEquals("color", modEvent.property());
        assertEquals("red", modEvent.oldValue());
        assertEquals("blue", modEvent.newValue());

        MapLoadedEvent loadEvent = new MapLoadedEvent(map, "file.mm");
        assertSame(map, loadEvent.map());
        assertEquals("file.mm", loadEvent.fileName());

        MapClosedEvent closeEvent = new MapClosedEvent(map);
        assertSame(map, closeEvent.map());
    }

    @Test
    void eventRecordsWithNullFieldsAreValid() {
        NodeModifiedEvent event = new NodeModifiedEvent(null, null, null, null);
        assertNull(event.node());
        assertNull(event.property());

        MapLoadedEvent loadEvent = new MapLoadedEvent(null, null);
        assertNull(loadEvent.map());
        assertNull(loadEvent.fileName());
    }

    // ---- Multiple subscribers ----

    @Test
    void multipleSubscribersReceiveSameNodeSelectionEvent() {
        NodeSelectionSubscriber sub1 = new NodeSelectionSubscriber();
        NodeSelectionSubscriber sub2 = new NodeSelectionSubscriber();
        NodeSelectionSubscriber sub3 = new NodeSelectionSubscriber();
        eventBus.register(sub1);
        eventBus.register(sub2);
        eventBus.register(sub3);

        MindMapNode node = mock(MindMapNode.class);
        NodeSelectionChangedEvent event = new NodeSelectionChangedEvent(node, true);
        eventBus.post(event);

        assertEquals(1, sub1.received.size());
        assertEquals(1, sub2.received.size());
        assertEquals(1, sub3.received.size());
        // All subscribers receive the exact same event instance
        assertSame(sub1.received.get(0), sub2.received.get(0));
        assertSame(sub2.received.get(0), sub3.received.get(0));
    }

    @Test
    void multipleSubscribersReceiveSameMapLoadedEvent() {
        MapLoadedSubscriber sub1 = new MapLoadedSubscriber();
        MapLoadedSubscriber sub2 = new MapLoadedSubscriber();
        eventBus.register(sub1);
        eventBus.register(sub2);

        MindMap map = mock(MindMap.class);
        eventBus.post(new MapLoadedEvent(map, "shared.mm"));

        assertEquals(1, sub1.received.size());
        assertEquals(1, sub2.received.size());
        assertSame(sub1.received.get(0), sub2.received.get(0));
    }

    // ---- Event type hierarchy (Record -> Object) ----

    @Test
    void objectSubscriberReceivesAllEventTypes() {
        ObjectSubscriber subscriber = new ObjectSubscriber();
        eventBus.register(subscriber);

        MindMapNode node = mock(MindMapNode.class);
        MindMap map = mock(MindMap.class);

        eventBus.post(new NodeSelectionChangedEvent(node, true));
        eventBus.post(new NodeModifiedEvent(node, "p", "a", "b"));
        eventBus.post(new MapLoadedEvent(map, "f.mm"));
        eventBus.post(new MapClosedEvent(map));

        assertEquals(4, subscriber.received.size());
        assertInstanceOf(NodeSelectionChangedEvent.class, subscriber.received.get(0));
        assertInstanceOf(NodeModifiedEvent.class, subscriber.received.get(1));
        assertInstanceOf(MapLoadedEvent.class, subscriber.received.get(2));
        assertInstanceOf(MapClosedEvent.class, subscriber.received.get(3));
    }

    @Test
    void recordSubscriberReceivesAllRecordEvents() {
        RecordSubscriber subscriber = new RecordSubscriber();
        eventBus.register(subscriber);

        MindMapNode node = mock(MindMapNode.class);
        MindMap map = mock(MindMap.class);

        eventBus.post(new NodeSelectionChangedEvent(node, true));
        eventBus.post(new MapClosedEvent(map));

        // Guava EventBus dispatches to Record subscriber since all event types extend Record
        assertEquals(2, subscriber.received.size());
    }

    @Test
    void specificSubscriberDoesNotReceiveOtherEventTypes() {
        NodeSelectionSubscriber selSub = new NodeSelectionSubscriber();
        MapClosedSubscriber closeSub = new MapClosedSubscriber();
        eventBus.register(selSub);
        eventBus.register(closeSub);

        MindMapNode node = mock(MindMapNode.class);
        MindMap map = mock(MindMap.class);

        eventBus.post(new NodeSelectionChangedEvent(node, true));
        eventBus.post(new MapClosedEvent(map));

        assertEquals(1, selSub.received.size());
        assertEquals(1, closeSub.received.size());
    }

    // ---- Thread safety ----

    @Test
    void concurrentPostsDoNotLoseEvents() throws InterruptedException {
        int threadCount = 10;
        int eventsPerThread = 100;
        int totalExpected = threadCount * eventsPerThread;

        ThreadSafeSubscriber subscriber = new ThreadSafeSubscriber();
        eventBus.register(subscriber);

        MindMapNode node = mock(MindMapNode.class);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        for (int t = 0; t < threadCount; t++) {
            executor.submit(() -> {
                try {
                    startLatch.await();
                    for (int i = 0; i < eventsPerThread; i++) {
                        eventBus.post(new NodeSelectionChangedEvent(node, true));
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        assertTrue(doneLatch.await(10, TimeUnit.SECONDS), "Threads did not finish in time");
        executor.shutdown();

        assertEquals(totalExpected, subscriber.received.size(),
                "Expected all events to be delivered without loss");
    }

    @Test
    void concurrentPostsOfDifferentEventTypes() throws InterruptedException {
        ThreadSafeObjectSubscriber subscriber = new ThreadSafeObjectSubscriber();
        eventBus.register(subscriber);

        MindMapNode node = mock(MindMapNode.class);
        MindMap map = mock(MindMap.class);

        int eventsPerType = 50;
        CountDownLatch latch = new CountDownLatch(4);
        ExecutorService executor = Executors.newFixedThreadPool(4);

        executor.submit(() -> {
            for (int i = 0; i < eventsPerType; i++)
                eventBus.post(new NodeSelectionChangedEvent(node, true));
            latch.countDown();
        });
        executor.submit(() -> {
            for (int i = 0; i < eventsPerType; i++)
                eventBus.post(new NodeModifiedEvent(node, "p", "a", "b"));
            latch.countDown();
        });
        executor.submit(() -> {
            for (int i = 0; i < eventsPerType; i++)
                eventBus.post(new MapLoadedEvent(map, "f.mm"));
            latch.countDown();
        });
        executor.submit(() -> {
            for (int i = 0; i < eventsPerType; i++)
                eventBus.post(new MapClosedEvent(map));
            latch.countDown();
        });

        assertTrue(latch.await(10, TimeUnit.SECONDS));
        executor.shutdown();

        assertEquals(4 * eventsPerType, subscriber.received.size());
    }

    // ---- Dead events ----

    @Test
    void eventWithNoSubscriberDoesNotCrash() {
        // Post an event with no subscribers registered at all
        MindMapNode node = mock(MindMapNode.class);
        assertDoesNotThrow(() -> eventBus.post(new NodeSelectionChangedEvent(node, true)));
    }

    @Test
    void deadEventReceivedWhenNoMatchingSubscriber() {
        DeadEventSubscriber deadSub = new DeadEventSubscriber();
        eventBus.register(deadSub);

        MindMapNode node = mock(MindMapNode.class);
        eventBus.post(new NodeSelectionChangedEvent(node, true));

        // Guava wraps unhandled events in DeadEvent and dispatches them
        assertEquals(1, deadSub.received.size());
        assertInstanceOf(NodeSelectionChangedEvent.class, deadSub.received.get(0).getEvent());
    }

    @Test
    void noDeadEventWhenSubscriberExists() {
        DeadEventSubscriber deadSub = new DeadEventSubscriber();
        NodeSelectionSubscriber normalSub = new NodeSelectionSubscriber();
        eventBus.register(deadSub);
        eventBus.register(normalSub);

        MindMapNode node = mock(MindMapNode.class);
        eventBus.post(new NodeSelectionChangedEvent(node, true));

        assertEquals(1, normalSub.received.size());
        assertTrue(deadSub.received.isEmpty(), "Dead event subscriber should not fire when a matching subscriber exists");
    }

    // ---- MapModuleManager integration points ----

    @Test
    void mapModuleManagerAcceptsEventBusInjection() {
        Controller mockController = mock(Controller.class);
        MapModuleManager manager = new MapModuleManager(mockController, mock(Resources.class));
        FreeMindEventBus bus = new FreeMindEventBus();

        assertDoesNotThrow(() -> manager.setEventBus(bus));
    }

    @Test
    void mapModuleManagerWorksWithNullEventBus() {
        Controller mockController = mock(Controller.class);
        MapModuleManager manager = new MapModuleManager(mockController, mock(Resources.class));

        // Setting null event bus should not cause issues
        assertDoesNotThrow(() -> manager.setEventBus(null));
    }

    // ---- Unregister and re-register ----

    @Test
    void unregisteredSubscriberStopsReceivingEvents() {
        NodeSelectionSubscriber subscriber = new NodeSelectionSubscriber();
        eventBus.register(subscriber);

        MindMapNode node = mock(MindMapNode.class);
        eventBus.post(new NodeSelectionChangedEvent(node, true));
        assertEquals(1, subscriber.received.size());

        eventBus.unregister(subscriber);
        eventBus.post(new NodeSelectionChangedEvent(node, false));
        assertEquals(1, subscriber.received.size(), "Should not receive events after unregister");
    }

    @Test
    void reregisteredSubscriberResumesReceivingEvents() {
        NodeSelectionSubscriber subscriber = new NodeSelectionSubscriber();
        eventBus.register(subscriber);

        MindMapNode node = mock(MindMapNode.class);
        eventBus.post(new NodeSelectionChangedEvent(node, true));
        assertEquals(1, subscriber.received.size());

        eventBus.unregister(subscriber);
        eventBus.post(new NodeSelectionChangedEvent(node, false));
        assertEquals(1, subscriber.received.size());

        eventBus.register(subscriber);
        eventBus.post(new NodeSelectionChangedEvent(node, true));
        assertEquals(2, subscriber.received.size());
    }

    // ---- Subscriber error isolation ----

    @Test
    void errorInOneSubscriberDoesNotPreventDeliveryToOthers() {
        ThrowingNodeSubscriber thrower = new ThrowingNodeSubscriber();
        NodeSelectionSubscriber normal = new NodeSelectionSubscriber();
        eventBus.register(thrower);
        eventBus.register(normal);

        MindMapNode node = mock(MindMapNode.class);
        eventBus.post(new NodeSelectionChangedEvent(node, true));

        assertEquals(1, normal.received.size(),
                "Normal subscriber should receive event despite error in another subscriber");
    }

    // ---- Inner subscriber classes ----

    static class NodeSelectionSubscriber {
        final List<NodeSelectionChangedEvent> received = new ArrayList<>();

        @Subscribe
        public void onEvent(NodeSelectionChangedEvent event) {
            received.add(event);
        }
    }

    static class NodeModifiedSubscriber {
        final List<NodeModifiedEvent> received = new ArrayList<>();

        @Subscribe
        public void onEvent(NodeModifiedEvent event) {
            received.add(event);
        }
    }

    static class MapLoadedSubscriber {
        final List<MapLoadedEvent> received = new ArrayList<>();

        @Subscribe
        public void onEvent(MapLoadedEvent event) {
            received.add(event);
        }
    }

    static class MapClosedSubscriber {
        final List<MapClosedEvent> received = new ArrayList<>();

        @Subscribe
        public void onEvent(MapClosedEvent event) {
            received.add(event);
        }
    }

    static class ObjectSubscriber {
        final List<Object> received = new ArrayList<>();

        @Subscribe
        public void onEvent(Object event) {
            received.add(event);
        }
    }

    static class RecordSubscriber {
        final List<Record> received = new ArrayList<>();

        @Subscribe
        public void onEvent(Record event) {
            received.add(event);
        }
    }

    static class DeadEventSubscriber {
        final List<DeadEvent> received = new ArrayList<>();

        @Subscribe
        public void onDeadEvent(DeadEvent event) {
            received.add(event);
        }
    }

    static class ThreadSafeSubscriber {
        final List<NodeSelectionChangedEvent> received = new CopyOnWriteArrayList<>();

        @Subscribe
        public void onEvent(NodeSelectionChangedEvent event) {
            received.add(event);
        }
    }

    static class ThreadSafeObjectSubscriber {
        final List<Object> received = new CopyOnWriteArrayList<>();

        @Subscribe
        public void onEvent(Object event) {
            received.add(event);
        }
    }

    static class ThrowingNodeSubscriber {
        @Subscribe
        public void onEvent(NodeSelectionChangedEvent event) {
            throw new RuntimeException("Intentional test error");
        }
    }
}
