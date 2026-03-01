package freemind.view.mindmapview.services;

import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.NodeView;

import java.awt.*;

public class NodeStyleService {

    private final NodeView nodeView;

    public NodeStyleService(NodeView nodeView) {
        this.nodeView = nodeView;
    }

    public Color getSelectedColor() {
        return MapView.standardSelectColor;
    }

    public static Color getAntiColor1(Color c) {
        float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        hsb[0] += 0.40;
        if (hsb[0] > 1)
            hsb[0]--;
        hsb[1] = 1;
        hsb[2] = 0.7f;
        return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
    }

    public static Color getAntiColor2(Color c) {
        float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
        hsb[0] -= 0.40;
        if (hsb[0] < 0)
            hsb[0]++;
        hsb[1] = 1;
        hsb[2] = (float) 0.8;
        return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
    }

    public Color getTextColor() {
        Color color = nodeView.getModel().getColor();
        if (color == null) {
            color = MapView.standardNodeTextColor;
        }
        return color;
    }

    public Font getTextFont() {
        return nodeView.getMainView().getFont();
    }

    public Color getTextBackground() {
        final Color modelBackgroundColor = nodeView.getModel().getBackgroundColor();
        if (modelBackgroundColor != null) {
            return modelBackgroundColor;
        }
        return getBackgroundColor();
    }

    private Color getBackgroundColor() {
        final freemind.modes.MindMapCloud cloud = nodeView.getModel().getCloud();
        if (cloud != null) {
            return cloud.getColor();
        }
        if (nodeView.isRoot()) {
            return nodeView.getMap().getBackground();
        }
        return new NodeStyleService(nodeView.getParentView()).getBackgroundColor();
    }

    public boolean useSelectionColors() {
        return nodeView.isSelected() && !MapView.standardDrawRectangleForSelection
                && !nodeView.getMap().isCurrentlyPrinting();
    }
}
