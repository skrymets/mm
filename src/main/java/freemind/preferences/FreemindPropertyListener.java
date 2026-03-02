package freemind.preferences;

/**
 * Is issued by the OptionPanel when the user accepted a change of its
 * preferences.
 */
public interface FreemindPropertyListener {
    void propertyChanged(String propertyName, String newValue, String oldValue);
}