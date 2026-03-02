package freemind.modes;

import java.util.Date;

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
        long now = new Date().getTime();
        createdAt = now;
        lastModifiedAt = now;
    }

    public HistoryInformation(Date createdAt, Date lastModifiedAt) {
        this.createdAt = createdAt.getTime();
        this.lastModifiedAt = lastModifiedAt.getTime();
    }

    public Date getCreatedAt() {
        return new Date(createdAt);
    }

    public Date getLastModifiedAt() {
        return new Date(lastModifiedAt);
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt.getTime();
    }

    public void setLastModifiedAt(Date lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt.getTime();
    }
}
