package freemind.events;

import com.google.common.eventbus.Subscribe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FreeMindEventBusTest {

    private FreeMindEventBus eventBus;

    @BeforeEach
    void setUp() {
        eventBus = new FreeMindEventBus();
    }

    @Test
    void subscriberReceivesPostedEvent() {
        StringSubscriber subscriber = new StringSubscriber();
        eventBus.register(subscriber);

        eventBus.post("hello");

        assertEquals(1, subscriber.received.size());
        assertEquals("hello", subscriber.received.get(0));
    }

    @Test
    void unregisteredSubscriberDoesNotReceive() {
        StringSubscriber subscriber = new StringSubscriber();
        eventBus.register(subscriber);
        eventBus.unregister(subscriber);

        eventBus.post("hello");

        assertTrue(subscriber.received.isEmpty());
    }

    @Test
    void multipleSubscribersReceiveEvent() {
        StringSubscriber sub1 = new StringSubscriber();
        StringSubscriber sub2 = new StringSubscriber();
        eventBus.register(sub1);
        eventBus.register(sub2);

        eventBus.post("event");

        assertEquals(1, sub1.received.size());
        assertEquals(1, sub2.received.size());
    }

    @Test
    void subscriberOnlyReceivesMatchingTypes() {
        StringSubscriber stringSubscriber = new StringSubscriber();
        IntegerSubscriber intSubscriber = new IntegerSubscriber();
        eventBus.register(stringSubscriber);
        eventBus.register(intSubscriber);

        eventBus.post("text");
        eventBus.post(42);

        assertEquals(1, stringSubscriber.received.size());
        assertEquals("text", stringSubscriber.received.get(0));
        assertEquals(1, intSubscriber.received.size());
        assertEquals(42, intSubscriber.received.get(0));
    }

    @Test
    void errorInSubscriberDoesNotAffectOthers() {
        ThrowingSubscriber thrower = new ThrowingSubscriber();
        StringSubscriber normal = new StringSubscriber();
        eventBus.register(thrower);
        eventBus.register(normal);

        eventBus.post("test");

        // The normal subscriber should still receive the event despite the other throwing
        assertEquals(1, normal.received.size());
        assertEquals("test", normal.received.get(0));
    }

    // --- Test subscriber classes ---

    static class StringSubscriber {
        final List<String> received = new ArrayList<>();

        @Subscribe
        public void onString(String event) {
            received.add(event);
        }
    }

    static class IntegerSubscriber {
        final List<Integer> received = new ArrayList<>();

        @Subscribe
        public void onInteger(Integer event) {
            received.add(event);
        }
    }

    static class ThrowingSubscriber {
        @Subscribe
        public void onString(String event) {
            throw new RuntimeException("Intentional test error");
        }
    }
}
