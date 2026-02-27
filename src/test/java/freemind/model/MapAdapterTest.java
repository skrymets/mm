package freemind.model;

import freemind.modes.ArrowLinkAdapter;
import freemind.modes.ArrowLinkTarget;
import freemind.modes.CloudAdapter;
import freemind.modes.MapFeedback;
import freemind.modes.mindmapmode.MindMapNodeModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link MapAdapter} using a concrete test subclass.
 */
class MapAdapterTest {

    private MapFeedback mockFeedback;
    private TestMapAdapter map;

    /**
     * Minimal concrete subclass of MapAdapter for testing purposes.
     */
    static class TestMapAdapter extends MapAdapter {

        public TestMapAdapter(MapFeedback mapFeedback) {
            super(mapFeedback);
        }

        public void stopTimer() {
            cancelFileChangeObservationTimer();
        }

        @Override
        public NodeAdapter createNodeAdapter(MindMap pMap, String nodeClass) {
            return new MindMapNodeModel(pMap);
        }

        @Override
        public EdgeAdapter createEdgeAdapter(NodeAdapter node) {
            return null;
        }

        @Override
        public CloudAdapter createCloudAdapter(NodeAdapter node) {
            return null;
        }

        @Override
        public ArrowLinkAdapter createArrowLinkAdapter(NodeAdapter source, NodeAdapter target) {
            return null;
        }

        @Override
        public ArrowLinkTarget createArrowLinkTarget(NodeAdapter source, NodeAdapter target) {
            return null;
        }

        @Override
        public NodeAdapter createEncryptedNode(String additionalInfo) {
            return null;
        }

        @Override
        public boolean save(java.io.File file) {
            return false;
        }

        @Override
        public void getXml(java.io.Writer fileout) {
            // no-op for testing
        }

        @Override
        public void getFilteredXml(java.io.Writer fileout) {
            // no-op for testing
        }
    }

    @BeforeEach
    void setUp() {
        mockFeedback = mock(MapFeedback.class);
        when(mockFeedback.getProperty(anyString())).thenReturn("#000000");
        when(mockFeedback.getResourceString(anyString())).thenReturn("test");
        map = new TestMapAdapter(mockFeedback);
    }

    @AfterEach
    void tearDown() {
        map.stopTimer();
    }

    @Nested
    class RootNodeManagement {

        @Test
        void rootNode_initiallyNull_thenSetAndGet() {
            assertNull(map.getRootNode());

            MindMapNodeModel root = new MindMapNodeModel("Root", map);
            map.setRoot(root);
            assertSame(root, map.getRootNode());
        }

        @Test
        void setRoot_replacesExisting() {
            MindMapNodeModel root1 = new MindMapNodeModel("Root1", map);
            MindMapNodeModel root2 = new MindMapNodeModel("Root2", map);
            map.setRoot(root1);
            map.setRoot(root2);
            assertSame(root2, map.getRootNode());
        }
    }

    @Nested
    class SavedState {

        @Test
        void newMap_isSaved_and_toggleBehavior() {
            assertTrue(map.isSaved());

            // First unsave returns true (title change needed)
            boolean setTitle = map.setSaved(false);
            assertTrue(setTitle);
            assertFalse(map.isSaved());

            // Second unsave returns false (title already changed)
            setTitle = map.setSaved(false);
            assertFalse(setTitle);

            // Save again
            setTitle = map.setSaved(true);
            assertTrue(setTitle);
            assertTrue(map.isSaved());
        }
    }

    @Nested
    class FileManagement {

        @Test
        void file_initiallyNull_setAndGetURL() throws Exception {
            assertNull(map.getFile());
            assertNull(map.getURL());

            File f = new File("/tmp/test.mm");
            map.setFile(f);
            assertEquals(f, map.getFile());
            assertNotNull(map.getURL());
        }
    }

    @Nested
    class ReadOnlyAndFilter {

        @Test
        void initiallyReadOnly_canBeChanged() {
            assertTrue(map.isReadOnly());
            map.setReadOnly(false);
            assertFalse(map.isReadOnly());
        }

        @Test
        void filter_defaultIsNoFilter_canBeReplaced() {
            assertNotNull(map.getFilter());
            assertSame(NoFilter.INSTANCE, map.getFilter());

            Filter customFilter = mock(Filter.class);
            map.setFilter(customFilter);
            assertSame(customFilter, map.getFilter());
        }
    }

    @Nested
    class MapFeedbackAccess {

        @Test
        void getMapFeedback_returnsInjectedFeedback() {
            assertSame(mockFeedback, map.getMapFeedback());
        }
    }

    @Nested
    class NodeInsertion {

        @Test
        void insertNodeInto_addsChildAndFiresEvent() {
            MindMapNodeModel root = new MindMapNodeModel("Root", map);
            map.setRoot(root);
            MindMapNodeModel child = new MindMapNodeModel("Child", map);

            map.insertNodeInto(child, root, 0);

            assertEquals(1, root.getChildCount());
            assertSame(child, root.getChildAt(0));
            verify(mockFeedback).fireRecursiveNodeCreateEvent(child);
        }
    }

    @Nested
    class DefaultMethods {

        @Test
        void textExportMethods_returnDefaults() {
            assertEquals("", map.getAsPlainText(java.util.Collections.emptyList()));
            assertEquals("", map.getAsRTF(java.util.Collections.emptyList()));
            assertNull(map.getAsHTML(java.util.Collections.emptyList()));
            assertNull(map.getRestorable());
            assertNull(map.getLinkRegistry());
        }
    }

    @Nested
    class MapSourceObserver {

        @Test
        void registerAndDeregister_observer() {
            MindMap.MapSourceChangedObserver observer =
                    mock(MindMap.MapSourceChangedObserver.class);
            map.registerMapSourceChangedObserver(observer, 0);
            long fileTime = map.deregisterMapSourceChangedObserver(observer);
            assertEquals(0, fileTime);
        }
    }

    @Nested
    class Locking {

        @Test
        void tryToLock_returnsNull() throws Exception {
            assertNull(map.tryToLock(new File("/tmp/test.mm")));
        }
    }
}
