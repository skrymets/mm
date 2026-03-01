package freemind.view.mindmapview.services;

import freemind.model.MindMap;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.NodeView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LinkRenderingServiceTest {

    private MapView mapView;
    private LinkRenderingService service;

    @BeforeEach
    void setUp() {
        mapView = mock(MapView.class);
        service = new LinkRenderingService(mapView);
    }

    @Test
    void arrowLinkViewsStartsEmpty() {
        assertTrue(service.getArrowLinkViews().isEmpty());
    }

    @Test
    void resetArrowLinkViewsClearsCollection() {
        service.resetArrowLinkViews();
        assertTrue(service.getArrowLinkViews().isEmpty());
    }

    @Test
    void detectCollisionReturnsNullWhenNoLinks() {
        assertNull(service.detectCollision(new Point(10, 10)));
    }

    @Test
    void collectLabelsHandlesNullRegistry() {
        MindMap model = mock(MindMap.class);
        when(mapView.getModel()).thenReturn(model);
        when(model.getLinkRegistry()).thenReturn(null);

        NodeView source = mock(NodeView.class);
        HashMap<String, NodeView> labels = new HashMap<>();
        service.collectLabels(source, labels);
        assertTrue(labels.isEmpty());
    }

    @Test
    void paintLinksHandlesNullRegistry() {
        MindMap model = mock(MindMap.class);
        when(mapView.getModel()).thenReturn(model);
        when(model.getLinkRegistry()).thenReturn(null);

        NodeView source = mock(NodeView.class);
        // Should not throw
        service.paintLinks(source, mock(java.awt.Graphics2D.class), new HashMap<>(), null);
    }
}
