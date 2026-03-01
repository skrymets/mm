package freemind.view.mindmapview.services;

import freemind.model.MindMapNode;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.NodeView;
import freemind.view.mindmapview.ViewFeedback;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MapSelectionServiceTest {

    private MapView mapView;
    private MapPrintingService printingService;
    private MapSelectionService service;
    private ViewFeedback feedback;

    @BeforeEach
    void setUp() {
        mapView = mock(MapView.class);
        printingService = mock(MapPrintingService.class);
        feedback = mock(ViewFeedback.class);
        when(mapView.getViewFeedback()).thenReturn(feedback);
        service = new MapSelectionService(mapView, printingService);
    }

    @Test
    void getSelectedReturnsNullWhenEmpty() {
        assertNull(service.getSelected());
    }

    @Test
    void getSelectedsReturnsEmptyListWhenEmpty() {
        assertTrue(service.getSelecteds().isEmpty());
    }

    @Test
    void isSelectedReturnsFalseWhenPrinting() {
        when(printingService.isCurrentlyPrinting()).thenReturn(true);
        NodeView node = mock(NodeView.class);
        MindMapNode model = mock(MindMapNode.class);
        when(node.getModel()).thenReturn(model);
        assertFalse(service.isSelected(node));
    }

    @Test
    void selectWithNullDoesNotThrow() {
        service.select(null);
        assertNull(service.getSelected());
    }

    @Test
    void resetShiftSelectionOriginClearsOrigin() {
        service.resetShiftSelectionOrigin();
        assertNull(service.getShiftSelectionOrigin());
    }

    @Test
    void revalidateAndValidateSelecteds() {
        service.revalidateSelecteds();
        service.validateSelecteds();
        // Should not throw, validates empty selection
    }
}
