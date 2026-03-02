package freemind.model;

import freemind.main.FreeMind;
import freemind.main.ColorUtils;
import freemind.modes.MapFeedback;
import freemind.preferences.FreemindPropertyListener;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Setter
@Getter
public abstract class LinkAdapter extends LineAdapter implements MindMapLink {
    public static final String RESOURCES_STANDARDLINKSTYLE = "standardlinkstyle";
    private static Color standardColor = null;
    private static String standardStyle = null;
    private static LinkAdapterListener listener = null;
    String destinationLabel;
    String referenceText;
    protected MindMapNode source;
    /**
     * -- GETTER --
     * <p>
     *
     * -- SETTER --
     *
     */
    private String uniqueId;

    public LinkAdapter(MindMapNode source, MindMapNode target, MapFeedback pMapFeedback) {
        super(target, pMapFeedback);
        this.source = source;
        destinationLabel = null;
        referenceText = null;
        if (listener == null) {
            listener = new LinkAdapterListener();
            pMapFeedback.addPropertyChangeListener(listener);
        }
    }

    // public Object clone() {
    // try {
    // return super.clone();
    // } catch(java.lang.CloneNotSupportedException e) {
    // return null;
    // }
    // }

    protected Color getStandardColor() {
        return standardColor;
    }

    protected void setStandardColor(Color standardColor) {
        LinkAdapter.standardColor = standardColor;
    }

    protected String getStandardStyle() {
        return standardStyle;
    }

    protected void setStandardStyle(String standardStyle) {
        LinkAdapter.standardStyle = standardStyle;
    }

    protected String getStandardColorPropertyString() {
        return FreeMind.RESOURCES_LINK_COLOR;
    }

    protected String getStandardStylePropertyString() {
        return RESOURCES_STANDARDLINKSTYLE;
    }

    protected static class LinkAdapterListener implements
            FreemindPropertyListener {
        public void propertyChanged(String propertyName, String newValue,
                                    String oldValue) {
            if (propertyName.equals(FreeMind.RESOURCES_LINK_COLOR)) {
                LinkAdapter.standardColor = ColorUtils.xmlToColor(newValue);
            }
            if (propertyName.equals(RESOURCES_STANDARDLINKSTYLE)) {
                LinkAdapter.standardStyle = newValue;
            }
        }
    }
}
