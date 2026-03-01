package freemind.view.mindmapview.services;

import freemind.view.mindmapview.NodeView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.event.TreeModelEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NodeTreeSyncServiceTest {

    private NodeView nodeView;
    private NodeTreeSyncService service;

    @BeforeEach
    void setUp() {
        nodeView = mock(NodeView.class);
        service = new NodeTreeSyncService(nodeView);
    }

    @Test
    void serviceCreatedSuccessfully() {
        assertNotNull(service);
    }

    @Test
    void treeNodesChangedCallsUpdate() {
        TreeModelEvent event = mock(TreeModelEvent.class);
        service.treeNodesChanged(event);
        verify(nodeView).update();
    }
}
