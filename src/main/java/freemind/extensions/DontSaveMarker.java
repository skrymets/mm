package freemind.extensions;

/**
 * save method is not called and it is not stored.
 * <p>
 * This may be useful, when the activity a plugin produces shouldn't be
 * restored the next time, the map is loaded (eg. the collaboration
 * feature).
 */
public interface DontSaveMarker {

}
