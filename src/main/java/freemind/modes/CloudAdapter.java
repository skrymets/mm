package freemind.modes;

import freemind.main.FreeMind;
import freemind.main.ColorUtils;
import freemind.model.LineAdapter;
import freemind.model.MindMapNode;
import freemind.preferences.FreemindPropertyListener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.*;

public abstract class CloudAdapter extends LineAdapter implements MindMapCloud {

    public static final String RESOURCES_STANDARDCLOUDSTYLE = "standardcloudstyle";
    private static Color standardColor = null;
    private static String standardStyle = null;
    private static CloudAdapterListener listener = null;
    static final Stroke DEF_STROKE = new BasicStroke(3);

    //
    // Constructors
    //
    public CloudAdapter(MindMapNode target, MapFeedback pMapFeedback) {
        super(target, pMapFeedback);
        NORMAL_WIDTH = 3;
        iterativeLevel = -1;
        if (listener == null) {
            listener = new CloudAdapterListener();
            pMapFeedback.addPropertyChangeListener(listener);
        }

    }

    /**
     * calculates the cloud iterative level which is importent for the cloud
     * size
     */

    private void calcIterativeLevel(MindMapNode target) {
        iterativeLevel = 0;
        if (target != null) {
            for (MindMapNode parentNode = target.getParentNode(); parentNode != null; parentNode = parentNode
                    .getParentNode()) {
                MindMapCloud cloud = parentNode.getCloud();
                if (cloud != null) {
                    iterativeLevel = cloud.getIterativeLevel() + 1;
                    break;
                }
            }
        }
    }

    public void setTarget(MindMapNode target) {
        super.setTarget(target);
    }

    public Color getExteriorColor() {
        return getColor().darker();
    }

    /**
     * gets iterative level which is required for painting and layout.
     */
    public int getIterativeLevel() {
        if (iterativeLevel == -1) {
            calcIterativeLevel(target);
        }
        return iterativeLevel;
    }

    /**
     * changes the iterative level.
     */
    public void changeIterativeLevel(int deltaLevel) {
        if (iterativeLevel != -1) {
            iterativeLevel = iterativeLevel + deltaLevel;
        }
    }

    private int iterativeLevel;

    public Element save(Document doc) {
        Element cloud = doc.createElement("cloud");

        if (style != null) {
            cloud.setAttribute("STYLE", style);
        }
        if (color != null) {
            cloud.setAttribute("COLOR", ColorUtils.colorToXml(color));
        }
        if (width != DEFAULT_WIDTH) {
            cloud.setAttribute("WIDTH", Integer.toString(width));
        }
        return cloud;
    }

    protected Color getStandardColor() {
        return standardColor;
    }

    protected void setStandardColor(Color standardColor) {
        CloudAdapter.standardColor = standardColor;
    }

    protected String getStandardStyle() {
        return standardStyle;
    }

    protected void setStandardStyle(String standardStyle) {
        CloudAdapter.standardStyle = standardStyle;
    }

    protected String getStandardColorPropertyString() {
        return FreeMind.RESOURCES_CLOUD_COLOR;
    }

    protected String getStandardStylePropertyString() {
        return RESOURCES_STANDARDCLOUDSTYLE;
    }

    protected static class CloudAdapterListener implements
            FreemindPropertyListener {
        public void propertyChanged(String propertyName, String newValue,
                                    String oldValue) {
            if (propertyName.equals(FreeMind.RESOURCES_CLOUD_COLOR)) {
                CloudAdapter.standardColor = ColorUtils.xmlToColor(newValue);
            }
            if (propertyName.equals(RESOURCES_STANDARDCLOUDSTYLE)) {
                CloudAdapter.standardStyle = newValue;
            }
        }
    }

}
