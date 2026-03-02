package freemind.model;

import freemind.main.FreeMind;
import freemind.main.ColorUtils;
import freemind.modes.MapFeedback;
import freemind.preferences.FreemindPropertyListener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.*;
import java.util.Objects;

public abstract class EdgeAdapter extends LineAdapter implements MindMapEdge {

    public static final String EDGE_WIDTH_THIN_STRING = "thin";
    private static Color standardColor = null;
    private static String standardStyle = null;
    private static EdgeAdapterListener listener = null;

    public static final int WIDTH_PARENT = -1;

    public static final int WIDTH_THIN = 0;

    public final static String EDGESTYLE_LINEAR = "linear";
    public final static String EDGESTYLE_BEZIER = "bezier";
    public final static String EDGESTYLE_SHARP_LINEAR = "sharp_linear";
    public final static String EDGESTYLE_SHARP_BEZIER = "sharp_bezier";
    public final static String[] EDGESTYLES = new String[]{
            EDGESTYLE_LINEAR,
            EDGESTYLE_BEZIER,
            EDGESTYLE_SHARP_LINEAR,
            EDGESTYLE_SHARP_BEZIER
    };
    public final static int INT_EDGESTYLE_LINEAR = 0;
    public final static int INT_EDGESTYLE_BEZIER = 1;
    public final static int INT_EDGESTYLE_SHARP_LINEAR = 2;
    public final static int INT_EDGESTYLE_SHARP_BEZIER = 3;

    // private static Color standardEdgeColor = new Color(0);

    public EdgeAdapter(MindMapNode target, MapFeedback pMapFeedback) {
        super(target, pMapFeedback);
        NORMAL_WIDTH = WIDTH_PARENT;
        if (listener == null) {
            listener = new EdgeAdapterListener();
            pMapFeedback.addPropertyChangeListener(listener);
        }
    }

    public Color getColor() {
        if (color == null) {
            if (getTarget().isRoot()) {
                return getStandardColor();
            }
            return getSource().getEdge().getColor();
        }
        return color;
    }

    public Color getRealColor() {
        return color;
    }

    public int getWidth() {
        if (width == WIDTH_PARENT) {
            if (getTarget().isRoot()) {
                return WIDTH_THIN;
            }
            return getSource().getEdge().getWidth();
        }
        return width;
    }

    public int getRealWidth() {
        return width;
    }

    public String getStyle() {
        if (style == null) {
            if (getTarget().isRoot()) {
                return getMapFeedback().getProperty(getStandardStylePropertyString());
            }
            return getSource().getEdge().getStyle();
        }
        return style;
    }

    public boolean hasStyle() {
        return style != null;
    }

    // /////////
    // Private Methods
    // ///////

    private MindMapNode getSource() {
        return target.getParentNode();
    }

    public Element save(Document doc) {
        if (style != null || color != null || width != WIDTH_PARENT) {
            Element edge = doc.createElement("edge");

            if (style != null) {
                edge.setAttribute("STYLE", style);
            }
            if (color != null) {
                edge.setAttribute("COLOR", ColorUtils.colorToXml(color));
            }
            if (width != WIDTH_PARENT) {
                if (width == WIDTH_THIN)
                    edge.setAttribute("WIDTH", EDGE_WIDTH_THIN_STRING);
                else
                    edge.setAttribute("WIDTH", Integer.toString(width));
            }
            return edge;
        }
        return null;
    }

    protected Color getStandardColor() {
        return standardColor;
    }

    protected void setStandardColor(Color standardColor) {
        EdgeAdapter.standardColor = standardColor;
    }

    protected String getStandardStyle() {
        return standardStyle;
    }

    protected void setStandardStyle(String standardStyle) {
        EdgeAdapter.standardStyle = standardStyle;
    }

    protected String getStandardColorPropertyString() {
        return FreeMind.RESOURCES_EDGE_COLOR;
    }

    protected String getStandardStylePropertyString() {
        return FreeMind.RESOURCES_EDGE_STYLE;
    }

    protected static class EdgeAdapterListener implements
            FreemindPropertyListener {
        public void propertyChanged(String propertyName, String newValue,
                                    String oldValue) {
            if (propertyName.equals(FreeMind.RESOURCES_EDGE_COLOR)) {
                EdgeAdapter.standardColor = ColorUtils.xmlToColor(newValue);
            }
            if (propertyName.equals(FreeMind.RESOURCES_EDGE_STYLE)) {
                EdgeAdapter.standardStyle = newValue;
            }
        }
    }

    public int getStyleAsInt() {
        final String edgeStyle = getStyle();
        if (Objects.equals(edgeStyle, EDGESTYLE_LINEAR)) {
            return INT_EDGESTYLE_LINEAR;
        } else if (Objects.equals(edgeStyle, EDGESTYLE_BEZIER)) {
            return INT_EDGESTYLE_BEZIER;
        } else if (Objects.equals(edgeStyle, EDGESTYLE_SHARP_LINEAR)) {
            return INT_EDGESTYLE_SHARP_LINEAR;
        } else if (Objects.equals(edgeStyle, EDGESTYLE_SHARP_BEZIER)) {
            return INT_EDGESTYLE_SHARP_BEZIER;
        } else {
            throw new IllegalArgumentException("Unknown Edge Style " + edgeStyle);
        }
    }

}
