package freemind.view;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StatusBarPanelTest {

    @Test
    void displaysStatusMessage() {
        StatusBarPanel panel = new StatusBarPanel();
        panel.setStatusText("Hello");
        assertEquals("Hello", panel.getStatusText());
    }

    @Test
    void displaysZoomPercentage() {
        StatusBarPanel panel = new StatusBarPanel();
        panel.setZoom(1.5f);
        assertEquals("150%", panel.getZoomText());
    }

    @Test
    void displaysZoomRounded() {
        StatusBarPanel panel = new StatusBarPanel();
        panel.setZoom(0.75f);
        assertEquals("75%", panel.getZoomText());
    }

    @Test
    void displaysNodeCount() {
        StatusBarPanel panel = new StatusBarPanel();
        panel.setNodeCount(42);
        assertEquals("Nodes: 42", panel.getNodeCountText());
    }

    @Test
    void displaysMapName() {
        StatusBarPanel panel = new StatusBarPanel();
        panel.setMapName("My Map.mm");
        assertEquals("My Map.mm", panel.getMapNameText());
    }
}
