package freemind.view.mindmapview.services;

import freemind.main.HtmlTools;
import freemind.main.Resources;
import freemind.main.Tools;
import freemind.modes.MindIcon;
import freemind.view.mindmapview.*;

import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class NodeUpdateService {

    private final NodeView nodeView;
    private final NodeStyleService styleService;

    private boolean isLong = false;
    private int maxToolTipWidth;
    private static Boolean showAttributeIcon;
    private static ImageIcon sAttributeIcon;

    public NodeUpdateService(NodeView nodeView, NodeStyleService styleService) {
        this.nodeView = nodeView;
        this.styleService = styleService;
    }

    public boolean getIsLong() {
        return isLong;
    }

    public void update() {
        updateStyle();
        if (!nodeView.isContentVisible()) {
            nodeView.removeFoldingListener();
            nodeView.getMainView().setVisible(false);
            return;
        }
        nodeView.getMainView().setVisible(true);
        updateTextColor();
        updateFont();
        updateIcons();
        if (nodeView.getModel().hasVisibleChilds()) {
            nodeView.addFoldingListener();
        } else {
            nodeView.removeFoldingListener();
        }
        updateText();
        updateToolTip();
        nodeView.revalidate();
    }

    public void repaintSelected() {
        updateTextColor();
        nodeView.repaint();
    }

    public void updateAll() {
        update();
        nodeView.invalidate();
        for (NodeView child : nodeView.getChildrenViews()) {
            child.updateAll();
        }
    }

    public void updateToolTip() {
        Map<String, String> tooltips = new TreeMap<>(nodeView.getModel().getToolTip());
        if (tooltips.isEmpty()) {
            nodeView.getMainView().setToolTipText(null);
        } else {
            StringBuilder text = new StringBuilder("<html><table width=\""
                    + getMaxToolTipWidth() + "\">");
            for (String key : tooltips.keySet()) {
                String value = tooltips.get(key);
                value = value.replaceAll("</html>", "");
                text.append("<tr><td>");
                text.append(value);
                text.append("</td></tr>");
            }
            text.append("</table></html>");
            nodeView.getMainView().setToolTipText(text.toString());
        }
    }

    public int getMaxToolTipWidth() {
        if (maxToolTipWidth == 0) {
            try {
                maxToolTipWidth = Integer.parseInt(nodeView.getViewFeedback().getProperty(
                        "max_tooltip_width"));
            } catch (NumberFormatException e) {
                maxToolTipWidth = 600;
            }
        }
        return maxToolTipWidth;
    }

    void updateStyle() {
        MainView mainView = nodeView.getMainView();
        if (mainView != null
                && (mainView.getStyle().equals(nodeView.getModel().getStyle()) || nodeView.getModel()
                .isRoot())) {
            return;
        }
        final MainView newMainView = NodeViewFactory.getInstance().newMainView(
                nodeView.getModel());
        nodeView.setMainView(newMainView);
    }

    private void updateText() {
        MapView mapView = nodeView.getMap();
        String nodeText = nodeView.getModel().toString();
        final boolean isHtml = nodeText.startsWith("<html>");
        boolean widthMustBeRestricted = false;
        if (!isHtml) {
            String[] lines = nodeText.split("\n");
            for (String s : lines) {
                nodeView.setText(s);
                widthMustBeRestricted = nodeView.getMainView().getPreferredSize().width > mapView
                        .getZoomed(mapView.getMaxNodeWidth())
                        + nodeView.getMainView().getIconWidth();
                if (widthMustBeRestricted) {
                    break;
                }
            }
            isLong = widthMustBeRestricted || lines.length > 1;
        }

        if (isHtml) {
            if (nodeText.indexOf("<img") >= 0 && nodeText.indexOf("<base ") < 0) {
                try {
                    nodeText = "<html><base href=\"" + mapView.getModel().getURL()
                            + "\">" + nodeText.substring(6);
                } catch (MalformedURLException ignored) {
                }
            }
            String htmlLongNodeHead = nodeView.getViewFeedback()
                    .getProperty("html_long_node_head");
            if (htmlLongNodeHead != null && !htmlLongNodeHead.isEmpty()) {
                if (nodeText.matches("(?ims).*<head>.*")) {
                    nodeText = nodeText.replaceFirst("(?ims).*<head>.*",
                            "<head>" + htmlLongNodeHead);
                } else {
                    nodeText = nodeText.replaceFirst("(?ims)<html>",
                            "<html><head>" + htmlLongNodeHead + "</head>");
                }
            }
            if (nodeText.length() < 30000) {
                nodeView.setText(nodeText);
                widthMustBeRestricted = nodeView.getMainView().getPreferredSize().width > mapView
                        .getZoomed(mapView.getMaxNodeWidth())
                        + nodeView.getMainView().getIconWidth();
            } else {
                widthMustBeRestricted = true;
            }
            if (widthMustBeRestricted) {
                nodeText = nodeText.replaceFirst("(?i)<body>", "<body width=\""
                        + mapView.getMaxNodeWidth() + "\">");
            }
            nodeView.setText(nodeText);
        } else if (nodeText.startsWith("<table>")) {
            String[] lines = nodeText.split("\n");
            lines[0] = lines[0].substring(7);
            int startingLine = lines[0].matches("\\s*") ? 1 : 0;
            StringBuilder text = new StringBuilder("<html><table border=1 style=\"border-color: white\">");
            for (int line = startingLine; line < lines.length; line++) {
                text.append("<tr><td style=\"border-color: white;\">").append(HtmlTools.toXMLEscapedText(lines[line]).replaceAll(
                        "\t", "<td style=\"border-color: white\">"));
            }
            nodeView.setText(text.toString());
        } else if (isLong) {
            String text = HtmlTools.plainToHTML(nodeText);
            if (widthMustBeRestricted) {
                text = text.replaceFirst("(?i)<p>",
                        "<p width=\"" + mapView.getMaxNodeWidth() + "\">");
            }
            nodeView.setText(text);
        } else {
            nodeView.setText(nodeText);
        }
    }

    private void updateFont() {
        Font font = nodeView.getModel().getFont();
        font = font == null ? nodeView.getViewFeedback().getDefaultFont() : font;
        if (font != null) {
            nodeView.getMainView().setFont(font);
        } else {
            System.err.println("NodeView.update(): default font is null.");
        }
    }

    private void updateIcons() {
        updateIconPosition();
        MultipleImage iconImages = new MultipleImage(1.0f);
        boolean iconPresent = false;

        Map<String, ImageIcon> stateIcons = nodeView.getModel().getStateIcons();
        for (String key : stateIcons.keySet()) {
            iconPresent = true;
            ImageIcon myIcon = stateIcons.get(key);
            iconImages.addImage(myIcon);
        }

        Resources resources = nodeView.getViewFeedback().getResources();
        if (showAttributeIcon == null) {
            showAttributeIcon = resources.getBoolProperty("el__show_icon_for_attributes");
        }
        if (showAttributeIcon && (nodeView.getModel().getAttributeTableLength() > 0)) {
            if (sAttributeIcon == null) {
                sAttributeIcon = freemind.view.ImageFactory.getInstance().createUnscaledIcon(resources.getResource(
                        "images/showAttributes.gif"));
            }
            iconImages.addImage(sAttributeIcon);
            iconPresent = true;
        }

        List<MindIcon> icons = nodeView.getModel().getIcons();
        for (MindIcon myIcon : icons) {
            iconPresent = true;
            iconImages.addImage(myIcon.getUnscaledIcon());
        }
        String link = nodeView.getModel().getLink();
        if (link != null) {
            iconPresent = true;
            String iconPath = "images/Link.png";
            if (link.startsWith("#")) {
                iconPath = "images/LinkLocal.png";
            } else if (link.startsWith("mailto:")) {
                iconPath = "images/Mail.png";
            } else if (Tools.executableByExtension(link)) {
                iconPath = "images/Executable.png";
            }
            ImageIcon icon = freemind.view.ImageFactory.getInstance().createUnscaledIcon(resources.getResource(iconPath));
            iconImages.addImage(icon);
        }
        nodeView.setIcon(iconPresent ? iconImages : null);
    }

    private void updateIconPosition() {
        nodeView.getMainView().setHorizontalTextPosition(
                nodeView.isLeft() ? SwingConstants.LEADING : SwingConstants.TRAILING);
    }

    private void updateTextColor() {
        Color color = nodeView.getModel().getColor();
        if (color == null) {
            color = MapView.standardNodeTextColor;
        }
        nodeView.getMainView().setForeground(color);
    }
}
