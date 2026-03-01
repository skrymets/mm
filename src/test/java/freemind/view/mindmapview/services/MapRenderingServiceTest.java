package freemind.view.mindmapview.services;

import freemind.view.mindmapview.MapView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MapRenderingServiceTest {

    private MapView mapView;
    private LinkRenderingService linkRenderingService;
    private MapRenderingService service;

    @BeforeEach
    void setUp() {
        mapView = mock(MapView.class);
        linkRenderingService = mock(LinkRenderingService.class);
        service = new MapRenderingService(mapView, linkRenderingService);
    }

    @Test
    void antialiasDefaultsToFalse() {
        assertFalse(MapRenderingService.getAntialiasEdges());
        assertFalse(MapRenderingService.getAntialiasAll());
    }

    @Test
    void setAntialiasEdgesUpdatesState() {
        MapRenderingService.setAntialiasEdges(true);
        assertTrue(MapRenderingService.getAntialiasEdges());
        MapRenderingService.setAntialiasEdges(false);
    }

    @Test
    void setEdgesRenderingHintReturnsOldHint() {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        Object oldHint = service.setEdgesRenderingHint(g);
        assertNotNull(oldHint);
        g.dispose();
    }

    @Test
    void recordPaintTimeAccumulates() {
        service.recordPaintTime(100);
        service.recordPaintTime(200);
        assertEquals(300, service.getPaintingTime());
        assertEquals(2, service.getPaintingAmount());
    }
}
