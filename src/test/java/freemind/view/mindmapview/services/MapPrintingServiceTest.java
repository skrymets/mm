package freemind.view.mindmapview.services;

import freemind.view.mindmapview.MapView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MapPrintingServiceTest {

    private MapView mapView;
    private MapPrintingService service;

    @BeforeEach
    void setUp() {
        mapView = mock(MapView.class);
        service = new MapPrintingService(mapView);
    }

    @Test
    void isNotPrintingByDefault() {
        assertFalse(service.isCurrentlyPrinting());
    }

    @Test
    void printOnWhiteBackgroundDefaultFalse() {
        MapPrintingService.setPrintOnWhiteBackground(false);
        assertFalse(MapPrintingService.isPrintOnWhiteBackground());
    }

    @Test
    void printOnWhiteBackgroundCanBeSet() {
        MapPrintingService.setPrintOnWhiteBackground(true);
        assertTrue(MapPrintingService.isPrintOnWhiteBackground());
        MapPrintingService.setPrintOnWhiteBackground(false);
    }

    @Test
    void endPrintingWhenNotPrintingLogsWarning() {
        // Should not throw
        service.endPrinting();
        assertFalse(service.isCurrentlyPrinting());
    }
}
