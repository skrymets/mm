package freemind.view.mindmapview.services;

import freemind.model.MindMapNode;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.NodeView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NodeStyleServiceTest {

    private NodeView nodeView;
    private NodeStyleService service;

    @BeforeEach
    void setUp() {
        nodeView = mock(NodeView.class);
        service = new NodeStyleService(nodeView);
    }

    @Test
    void getAntiColor1ReturnsDifferentColor() {
        Color c = Color.RED;
        Color anti = NodeStyleService.getAntiColor1(c);
        assertNotEquals(c, anti);
    }

    @Test
    void getAntiColor2ReturnsDifferentColor() {
        Color c = Color.BLUE;
        Color anti = NodeStyleService.getAntiColor2(c);
        assertNotEquals(c, anti);
    }

    @Test
    void getTextColorReturnsModelColor() {
        MindMapNode model = mock(MindMapNode.class);
        when(nodeView.getModel()).thenReturn(model);
        when(model.getColor()).thenReturn(Color.GREEN);
        assertEquals(Color.GREEN, service.getTextColor());
    }

    @Test
    void getTextColorFallsBackToStandardColor() {
        MindMapNode model = mock(MindMapNode.class);
        when(nodeView.getModel()).thenReturn(model);
        when(model.getColor()).thenReturn(null);
        MapView.standardNodeTextColor = Color.BLACK;
        assertEquals(Color.BLACK, service.getTextColor());
    }
}
