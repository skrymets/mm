package freemind.view.mindmapview.services;

import freemind.view.mindmapview.NodeView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NodeRenderingServiceTest {

    private NodeView nodeView;
    private NodeRenderingService service;

    @BeforeEach
    void setUp() {
        nodeView = mock(NodeView.class);
        service = new NodeRenderingService(nodeView);
    }

    @Test
    void serviceCreatedSuccessfully() {
        assertNotNull(service);
    }
}
