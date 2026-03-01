package freemind.view.mindmapview.services;

import freemind.model.MindMapNode;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.NodeView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NodeGeometryServiceTest {

    private NodeView nodeView;
    private MapView mapView;
    private NodeGeometryService service;

    @BeforeEach
    void setUp() {
        nodeView = mock(NodeView.class);
        mapView = mock(MapView.class);
        when(nodeView.getMap()).thenReturn(mapView);
        service = new NodeGeometryService(nodeView);
    }

    @Test
    void getShiftDelegatesToMapZoomed() {
        MindMapNode model = mock(MindMapNode.class);
        when(nodeView.getModel()).thenReturn(model);
        when(model.calcShiftY()).thenReturn(10);
        when(mapView.getZoomed(10)).thenReturn(20);

        assertEquals(20, service.getShift());
    }

    @Test
    void getVGapDelegatesToMapZoomed() {
        MindMapNode model = mock(MindMapNode.class);
        when(nodeView.getModel()).thenReturn(model);
        when(model.getVGap()).thenReturn(5);
        when(mapView.getZoomed(5)).thenReturn(10);

        assertEquals(10, service.getVGap());
    }

    @Test
    void getHGapDelegatesToMapZoomed() {
        MindMapNode model = mock(MindMapNode.class);
        when(nodeView.getModel()).thenReturn(model);
        when(model.getHGap()).thenReturn(8);
        when(mapView.getZoomed(8)).thenReturn(16);

        assertEquals(16, service.getHGap());
    }

    @Test
    void getAdditionalCloudHeightReturnsZeroWhenNotVisible() {
        when(nodeView.isContentVisible()).thenReturn(false);
        assertEquals(0, service.getAdditionalCloudHeigth());
    }
}
