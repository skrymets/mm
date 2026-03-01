package freemind.view.mindmapview.services;

import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.NodeView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MapGeometryServiceTest {

    private MapView mapView;
    private MapGeometryService service;

    @BeforeEach
    void setUp() {
        mapView = mock(MapView.class);
        service = new MapGeometryService(mapView);
    }

    @Test
    void getViewportSizeReturnsNullWhenParentIsNotViewport() {
        when(mapView.getParent()).thenReturn(mock(Container.class));
        assertNull(service.getViewportSize());
    }

    @Test
    void getViewPositionReturnsOriginWhenParentIsNotViewport() {
        when(mapView.getParent()).thenReturn(mock(Container.class));
        Point pos = service.getViewPosition();
        assertEquals(0, pos.x);
        assertEquals(0, pos.y);
    }

    @Test
    void getInnerBoundsWithEmptyArrowLinks() {
        NodeView root = mock(NodeView.class);
        when(mapView.getRoot()).thenReturn(root);
        when(root.getInnerBounds()).thenReturn(new Rectangle(10, 10, 100, 100));
        when(root.getX()).thenReturn(0);
        when(root.getY()).thenReturn(0);
        when(mapView.getWidth()).thenReturn(500);
        when(mapView.getHeight()).thenReturn(500);

        Rectangle bounds = service.getInnerBounds(new ArrayList<>());
        assertNotNull(bounds);
    }
}
