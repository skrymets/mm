package freemind.model;

import java.time.Instant;

/**
 * Here, the creation and modification times of objects (by now, only for nodes)
 * are stored.
 * <p>
 * The storage as longs is preferred as they are normally inlined by the Java
 * compiler.
 */
public class HistoryInformation {
    long createdAt = 0L;
    long lastModifiedAt = 0L;

    /**
     * Initializes to today.
     */
    public HistoryInformation() {
        long now = Instant.now().toEpochMilli();
        createdAt = now;
        lastModifiedAt = now;
    }

    public HistoryInformation(Instant createdAt, Instant lastModifiedAt) {
        this.createdAt = createdAt.toEpochMilli();
        this.lastModifiedAt = lastModifiedAt.toEpochMilli();
    }

    public Instant getCreatedAt() {
        return Instant.ofEpochMilli(createdAt);
    }

    public Instant getLastModifiedAt() {
        return Instant.ofEpochMilli(lastModifiedAt);
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt.toEpochMilli();
    }

    public void setLastModifiedAt(Instant lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt.toEpochMilli();
    }
}
