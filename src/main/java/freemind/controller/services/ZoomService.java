package freemind.controller.services;

import freemind.controller.ZoomListener;

import java.util.HashSet;

/**
 * Manages zoom level values and notifies registered listeners when the zoom changes.
 */
public class ZoomService {

    private static final float[] ZOOM_VALUES = {0.25f, 0.5f, 0.75f, 1.0f, 1.5f, 2.0f, 3.0f, 4.0f};

    private final HashSet<ZoomListener> zoomListenerSet = new HashSet<>();

    public void registerZoomListener(ZoomListener listener) {
        zoomListenerSet.add(listener);
    }

    public void unregisterZoomListener(ZoomListener listener) {
        zoomListenerSet.remove(listener);
    }

    /**
     * Notifies all registered zoom listeners of a zoom value change.
     *
     * @param zoomValue the new zoom value (e.g. 1.0 for 100%)
     */
    public void changeZoomValueProperty(float zoomValue) {
        for (ZoomListener listener : zoomListenerSet) {
            listener.setZoom(zoomValue);
        }
    }

    /**
     * Returns the available zoom levels as display strings (e.g. "100%").
     */
    public String[] getZooms() {
        String[] zooms = new String[ZOOM_VALUES.length];
        for (int i = 0; i < ZOOM_VALUES.length; i++) {
            zooms[i] = (int) (ZOOM_VALUES[i] * 100f) + "%";
        }
        return zooms;
    }

    /**
     * Returns the static array of zoom float values.
     */
    public static float[] getZoomValues() {
        return ZOOM_VALUES;
    }
}
