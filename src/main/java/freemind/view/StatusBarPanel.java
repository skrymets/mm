package freemind.view;

import javax.swing.*;
import java.awt.*;

public class StatusBarPanel extends JPanel {

    private final JLabel statusLabel;
    private final JLabel zoomLabel;
    private final JLabel nodeCountLabel;
    private final JLabel mapNameLabel;

    public StatusBarPanel() {
        super(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));

        statusLabel = new JLabel(" ");
        mapNameLabel = new JLabel(" ");
        zoomLabel = new JLabel("100%");
        nodeCountLabel = new JLabel(" ");

        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        rightPanel.setOpaque(false);
        rightPanel.add(mapNameLabel);
        rightPanel.add(createSeparator());
        rightPanel.add(nodeCountLabel);
        rightPanel.add(createSeparator());
        rightPanel.add(zoomLabel);

        add(statusLabel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }

    private JSeparator createSeparator() {
        JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setPreferredSize(new Dimension(1, 16));
        return sep;
    }

    public void setStatusText(String text) {
        statusLabel.setText(text != null ? text : " ");
    }

    public String getStatusText() {
        return statusLabel.getText();
    }

    public void setZoom(float zoom) {
        zoomLabel.setText(Math.round(zoom * 100) + "%");
    }

    public String getZoomText() {
        return zoomLabel.getText();
    }

    public void setNodeCount(int count) {
        nodeCountLabel.setText("Nodes: " + count);
    }

    public String getNodeCountText() {
        return nodeCountLabel.getText();
    }

    public void setMapName(String name) {
        mapNameLabel.setText(name != null ? name : " ");
    }

    public String getMapNameText() {
        return mapNameLabel.getText();
    }
}
