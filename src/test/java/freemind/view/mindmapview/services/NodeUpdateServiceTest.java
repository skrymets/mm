package freemind.view.mindmapview.services;

import freemind.model.MindMapNode;
import freemind.view.mindmapview.MainView;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.NodeView;
import freemind.view.mindmapview.ViewFeedback;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NodeUpdateServiceTest {

    private NodeView nodeView;
    private NodeStyleService styleService;
    private NodeUpdateService service;

    @BeforeEach
    void setUp() {
        nodeView = mock(NodeView.class);
        styleService = mock(NodeStyleService.class);
        service = new NodeUpdateService(nodeView, styleService);
    }

    @Test
    void serviceCreatedSuccessfully() {
        assertNotNull(service);
    }

    @Test
    void getIsLongReturnsFalseByDefault() {
        assertFalse(service.getIsLong());
    }

    @Test
    void getMaxToolTipWidthDefaultsTo600() {
        MapView mapView = mock(MapView.class);
        ViewFeedback feedback = mock(ViewFeedback.class);
        when(nodeView.getMap()).thenReturn(mapView);
        when(mapView.getViewFeedback()).thenReturn(feedback);
        when(feedback.getProperty("max_tooltip_width")).thenReturn("invalid");
        assertEquals(600, service.getMaxToolTipWidth());
    }

    @Test
    void getMaxToolTipWidthParsesProperty() {
        MapView mapView = mock(MapView.class);
        ViewFeedback feedback = mock(ViewFeedback.class);
        when(nodeView.getMap()).thenReturn(mapView);
        when(mapView.getViewFeedback()).thenReturn(feedback);
        when(feedback.getProperty("max_tooltip_width")).thenReturn("800");
        assertEquals(800, service.getMaxToolTipWidth());
    }

    @Test
    void repaintSelectedCallsRepaint() {
        MindMapNode model = mock(MindMapNode.class);
        MainView mainViewMock = mock(MainView.class);
        when(nodeView.getModel()).thenReturn(model);
        when(model.getColor()).thenReturn(Color.RED);
        when(nodeView.getMainView()).thenReturn(mainViewMock);
        service.repaintSelected();
        verify(mainViewMock).setForeground(Color.RED);
        verify(nodeView).repaint();
    }
}
