package freemind.view.mindmapview.services;

import freemind.model.MindMapNode;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.NodeView;
import freemind.view.mindmapview.NodeViewVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ViewerRegistryServiceTest {

    private MapView mapView;
    private ViewerRegistryService service;

    @BeforeEach
    void setUp() {
        mapView = mock(MapView.class);
        service = new ViewerRegistryService(mapView);
    }

    @Test
    void getViewersReturnsEmptyCollectionForNewNode() {
        MindMapNode node = mock(MindMapNode.class);
        Collection<NodeView> viewers = service.getViewers(node);
        assertNotNull(viewers);
        assertTrue(viewers.isEmpty());
    }

    @Test
    void addViewerRegistersViewerAndAddsTreeModelListener() {
        MindMapNode node = mock(MindMapNode.class);
        NodeView viewer = mock(NodeView.class);

        service.addViewer(node, viewer);

        assertTrue(service.getViewers(node).contains(viewer));
        verify(node).addTreeModelListener(viewer);
    }

    @Test
    void removeViewerUnregistersViewerAndRemovesTreeModelListener() {
        MindMapNode node = mock(MindMapNode.class);
        NodeView viewer = mock(NodeView.class);

        service.addViewer(node, viewer);
        service.removeViewer(node, viewer);

        assertFalse(service.getViewers(node).contains(viewer));
        verify(node).removeTreeModelListener(viewer);
    }

    @Test
    void getNodeViewReturnsNullForNullNode() {
        assertNull(service.getNodeView(null));
    }

    @Test
    void getNodeViewReturnsMatchingViewer() {
        MindMapNode node = mock(MindMapNode.class);
        NodeView viewer = mock(NodeView.class);
        when(viewer.getMap()).thenReturn(mapView);

        service.addViewer(node, viewer);

        assertEquals(viewer, service.getNodeView(node));
    }

    @Test
    void acceptViewVisitorVisitsAllViewers() {
        MindMapNode node = mock(MindMapNode.class);
        NodeView viewer1 = mock(NodeView.class);
        NodeView viewer2 = mock(NodeView.class);

        service.addViewer(node, viewer1);
        service.addViewer(node, viewer2);

        AtomicInteger count = new AtomicInteger(0);
        NodeViewVisitor visitor = v -> count.incrementAndGet();
        service.acceptViewVisitor(node, visitor);

        assertEquals(2, count.get());
    }
}
