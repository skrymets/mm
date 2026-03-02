package freemind.model;

import freemind.main.ColorUtils;
import freemind.modes.MapFeedback;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

public abstract class LineAdapter implements MindMapLine {

    protected final MapFeedback mMapFeedback;
    /**
     * -- GETTER --
     *  I see no reason to hide the node, the line belongs to, to the public,
     *  but... fc.
     */
    @Setter
    @Getter
    protected MindMapNode target;

    public static final int DEFAULT_WIDTH = -1;
    protected int NORMAL_WIDTH = 1;

    // recursive attributes. may be accessed directly by the save() method.
    @Setter
    protected Color color;
    @Setter
    protected String style;
    @Setter
    protected int width;

    //
    // Constructors
    //
    public LineAdapter(MindMapNode target, MapFeedback pMapFeedback) {
        this.mMapFeedback = pMapFeedback;
        this.target = target;
        width = DEFAULT_WIDTH;
        updateStandards();

    }

    //
    // Attributes
    //

    protected void updateStandards() {
        if (getStandardColor() == null) {
            String stdColor = getMapFeedback().getProperty(
                    getStandardColorPropertyString());
            if (stdColor != null && stdColor.length() == 7) {
                setStandardColor(ColorUtils.xmlToColor(stdColor));
            } else {
                setStandardColor(Color.RED);
            }
        }
        if (getStandardStyle() == null) {
            String stdStyle = getMapFeedback().getProperty(
                    getStandardStylePropertyString());
            if (stdStyle != null) {
                setStandardStyle(stdStyle);
            } else {
                // setStandardStyle(Style.RED);
            }
        }
    }

    public MapFeedback getMapFeedback() {
        return mMapFeedback;
    }

    public Color getColor() {
        if (color == null) {
            return getStandardColor();
        }
        return color;
    }

    public int getWidth() {
        if (width == DEFAULT_WIDTH)
            return NORMAL_WIDTH;
        return width;
    }

    /**
     * Get the width in pixels rather than in width constant (like -1)
     */
    public int getRealWidth() {
        return getWidth();
    }

    public String getStyle() {
        if (style == null) {
            return getStandardStyle();
        }
        return style;
    }

    public String toString() {
        return "";
    }

    // /////////
    // Private Methods
    // ///////

    public Object clone() {
        try {
            LineAdapter link = (LineAdapter) super.clone();
            // color, ...
            link.color = (color == null) ? null : new Color(color.getRGB());
            return link;
        } catch (java.lang.CloneNotSupportedException e) {
            return null;
        }
    }

    /**
     * As this color is static but used in at least three different objects
     * (edges, clouds and links), the abstract mechanism was chosen. The derived
     * classes set and get the static instance variable.
     */
    protected abstract void setStandardColor(Color standardColor);

    protected abstract Color getStandardColor();

    protected abstract void setStandardStyle(String standardStyle);

    protected abstract String getStandardStyle();

    protected abstract String getStandardStylePropertyString();

    protected abstract String getStandardColorPropertyString();

}
