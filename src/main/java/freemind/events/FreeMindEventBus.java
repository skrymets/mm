package freemind.events;

import com.google.common.eventbus.EventBus;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class FreeMindEventBus {
    private final EventBus eventBus;

    public FreeMindEventBus() {
        this.eventBus = new EventBus((exception, context) -> {
            log.error("Event bus error in subscriber {}: {}",
                    context.getSubscriberMethod(), exception.getMessage(), exception);
        });
    }

    public void register(Object subscriber) {
        eventBus.register(subscriber);
    }

    public void unregister(Object subscriber) {
        eventBus.unregister(subscriber);
    }

    public void post(Object event) {
        log.trace("Posting event: {}", event.getClass().getSimpleName());
        eventBus.post(event);
    }
}
