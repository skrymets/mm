package freemind.controller;

public interface ZoomListener {
    /**
     * On each zoom change, this method is called, if registered.
     *
     * @param f the effective zoom value (1.25 means 125%)
     */
    void setZoom(float f);
}
