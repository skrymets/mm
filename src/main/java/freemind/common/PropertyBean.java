package freemind.common;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public abstract class PropertyBean {

    private final List<PropertyChangeListener> mPropertyChangeListeners = new ArrayList<>();

    /**
     * The key of the property.
     */
    public abstract String getLabel();

    public abstract void setValue(String value);

    public abstract String getValue();

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        mPropertyChangeListeners.add(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        mPropertyChangeListeners.remove(listener);
    }

    protected void firePropertyChangeEvent() {
        PropertyChangeEvent evt = new PropertyChangeEvent(this, getLabel(), null, getValue());
        for (PropertyChangeListener listener : mPropertyChangeListeners) {
            listener.propertyChange(evt);
        }
    }
}
