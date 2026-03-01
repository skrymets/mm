package freemind.view.mindmapview.services;

import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.NodeView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScrollServiceTest {

    private MapView mapView;
    private ScrollService service;

    @BeforeEach
    void setUp() {
        mapView = mock(MapView.class);
        service = new ScrollService(mapView);
    }

    @Test
    void nodeToBeVisibleDefaultsToNull() {
        assertNull(service.getNodeToBeVisible());
    }

    @Test
    void setNodeToBeVisibleStoresNode() {
        NodeView node = mock(NodeView.class);
        service.setNodeToBeVisible(node);
        assertEquals(node, service.getNodeToBeVisible());
    }

    @Test
    void rootContentLocationDefaultsToOrigin() {
        Point loc = service.getRootContentLocation();
        assertEquals(0, loc.x);
        assertEquals(0, loc.y);
    }

    @Test
    void setViewPositionNoOpWhenParentNotViewport() {
        when(mapView.getParent()).thenReturn(mock(Container.class));
        // Should not throw
        service.setViewPosition(new Point(10, 10));
    }

    @Test
    void scrollNodeToVisibleDefersWhenInvalid() {
        when(mapView.isValid()).thenReturn(false);
        NodeView node = mock(NodeView.class);
        service.scrollNodeToVisible(node, 5);
        assertEquals(node, service.getNodeToBeVisible());
    }
}
