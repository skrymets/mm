package freemind.view.mindmapview.services;

import freemind.model.MindMapNode;
import freemind.view.mindmapview.NodeView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NodeNavigationServiceTest {

    private NodeView nodeView;
    private NodeNavigationService service;

    @BeforeEach
    void setUp() {
        nodeView = mock(NodeView.class);
        service = new NodeNavigationService(nodeView);
    }

    @Test
    void serviceCreatedSuccessfully() {
        assertNotNull(service);
    }

    @Test
    void preferredChildIsNullByDefault() {
        assertNull(service.getPreferredChild());
    }

    @Test
    void setPreferredChildStoresValue() {
        NodeView child = mock(NodeView.class);
        when(nodeView.getParent()).thenReturn(null);
        service.setPreferredChild(child);
        assertEquals(child, service.getPreferredChild());
    }

    @Test
    void getNextPageReturnsThisWhenRoot() {
        MindMapNode model = mock(MindMapNode.class);
        when(nodeView.getModel()).thenReturn(model);
        when(model.isRoot()).thenReturn(true);
        assertEquals(nodeView, service.getNextPage());
    }

    @Test
    void getPreviousPageReturnsThisWhenRoot() {
        MindMapNode model = mock(MindMapNode.class);
        when(nodeView.getModel()).thenReturn(model);
        when(model.isRoot()).thenReturn(true);
        assertEquals(nodeView, service.getPreviousPage());
    }
}
