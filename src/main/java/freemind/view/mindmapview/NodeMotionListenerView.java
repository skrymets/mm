package freemind.view.mindmapview;

import freemind.main.SwingUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;

/**
 * The oval appearing to move nodes to other positions.
 */
@Getter
@Slf4j
public class NodeMotionListenerView extends JComponent {

    public NodeMotionListenerView(NodeView view) {
        super();
        this.movedView = view;

        MapView map = view.getMap();
        addMouseListener(map.getNodeMotionListener());
        addMouseMotionListener(map.getNodeMotionListener());

        // fc, 16.6.2005: to emphasis the possible movement.
        this.setCursor(new Cursor(Cursor.MOVE_CURSOR));
        final String helpMsg = view.getMap().getViewFeedback().getResources().getResourceString("node_location_help");
        this.setToolTipText(helpMsg);
    }

    private final NodeView movedView;
    private boolean isMouseEntered;

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isMouseEntered()) {
            Graphics2D g2 = (Graphics2D) g;
            // set antialiasing.
            Object renderingHint = movedView.getMap().getRenderingService().setEdgesRenderingHint(g2);
            Color color = g2.getColor();
            Stroke oldStroke = g2.getStroke();
            g2.setStroke(new BasicStroke());
            if (movedView.getModel().getHGap() <= 0) {
                g2.setColor(Color.RED);
                g.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
            } else {
                g2.setColor(Color.BLACK);
                g.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
            }
            g2.setStroke(oldStroke);
            g2.setColor(color);
            SwingUtils.restoreAntialiasing(g2, renderingHint);
        }
    }

    public void setMouseEntered() {
        this.isMouseEntered = true;
        // fc, 13.3.2008: variable is not used:
        // final FreeMindMain frame =
        // movedView.getMap().getModel().getModeController().getFrame();
        repaint();
    }

    public void setMouseExited() {
        this.isMouseEntered = false;
        repaint();
    }
}
