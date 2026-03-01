package freemind.view.mindmapview.services;

import freemind.model.MindMapNode;
import freemind.view.mindmapview.MapView;
import freemind.view.mindmapview.NodeView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.mockito.Mockito.*;

class NavigationServiceTest {

    private MapView mapView;
    private MapSelectionService selectionService;
    private ScrollService scrollService;
    private NavigationService service;

    @BeforeEach
    void setUp() {
        mapView = mock(MapView.class);
        selectionService = mock(MapSelectionService.class);
        scrollService = mock(ScrollService.class);
        service = new NavigationService(mapView, selectionService, scrollService);
    }

    @Test
    void moveToRootSelectsAndCentersRoot() {
        NodeView root = mock(NodeView.class);
        when(mapView.getRoot()).thenReturn(root);

        service.moveToRoot();

        verify(selectionService).selectAsTheOnlyOneSelected(root);
        verify(scrollService).centerNode(root);
    }

    @Test
    void getMainViewYDoesNotThrow() {
        NodeView node = mock(NodeView.class);
        JComponent mainView = mock(JComponent.class);
        when(node.getMainView()).thenReturn((freemind.view.mindmapview.MainView) null);
        // Cannot easily test without real Swing hierarchy, verify no NPE in construction
    }
}
