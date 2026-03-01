package freemind.events;

import freemind.controller.Controller;
import freemind.controller.MapModuleManager;
import freemind.model.MindMap;
import freemind.modes.Mode;
import freemind.modes.ModeController;
import freemind.view.MapModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests that MapModuleManager correctly posts MapLoadedEvent and MapClosedEvent
 * to the FreeMindEventBus at the right lifecycle points.
 *
 * Since MapModuleManager.newMapModule() creates Swing components (MapView) which
 * are difficult to mock in isolation, we test integration by injecting a MapModule
 * directly into the internal list and then exercising close(). For the loaded event,
 * we verify event record construction and wiring.
 */
class MapLifecycleEventTest {

    private MapModuleManager mapModuleManager;
    private FreeMindEventBus eventBus;
    private MindMap mockMap;
    private Mode mockMode;

    @BeforeEach
    void setUp() {
        Controller mockController = mock(Controller.class);
        eventBus = mock(FreeMindEventBus.class);

        mapModuleManager = new MapModuleManager(mockController);
        mapModuleManager.setEventBus(eventBus);

        mockMap = mock(MindMap.class);
        mockMode = mock(Mode.class);
    }

    /**
     * Inject a MapModule into the manager's internal list and set it as current,
     * bypassing Swing-dependent newMapModule().
     */
    private MapModule injectMapModule(String displayName) throws Exception {
        ModeController mockModeController = mock(ModeController.class);
        when(mockModeController.getMode()).thenReturn(mockMode);
        when(mockModeController.close(anyBoolean(), any())).thenReturn(true);

        MapModule module = mock(MapModule.class);
        when(module.getModel()).thenReturn(mockMap);
        when(module.getMode()).thenReturn(mockMode);
        when(module.getModeController()).thenReturn(mockModeController);
        when(module.getDisplayName()).thenReturn(displayName);
        when(module.toString()).thenReturn(displayName);

        // Access the private mapModules list via reflection
        Field mapModulesField = MapModuleManager.class.getDeclaredField("mapModules");
        mapModulesField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<MapModule> mapModules = (List<MapModule>) mapModulesField.get(mapModuleManager);
        mapModules.add(module);

        // Set the current module via reflection
        Field mapModuleField = MapModuleManager.class.getDeclaredField("mapModule");
        mapModuleField.setAccessible(true);
        mapModuleField.set(mapModuleManager, module);

        return module;
    }

    @Test
    void closePostsMapClosedEvent() throws Exception {
        when(mockMap.getRestorable()).thenReturn("test.mm");
        injectMapModule("test.mm");

        StringBuilder restorable = new StringBuilder();
        boolean closed = mapModuleManager.close(true, restorable);

        assertTrue(closed);

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        verify(eventBus, atLeastOnce()).post(captor.capture());

        MapClosedEvent closedEvent = captor.getAllValues().stream()
                .filter(e -> e instanceof MapClosedEvent)
                .map(e -> (MapClosedEvent) e)
                .findFirst()
                .orElse(null);

        assertNotNull(closedEvent, "MapClosedEvent should have been posted");
        assertSame(mockMap, closedEvent.map());
    }

    @Test
    void closeDoesNotPostEventWhenCancelled() throws Exception {
        ModeController mockModeController = mock(ModeController.class);
        when(mockModeController.getMode()).thenReturn(mockMode);
        when(mockModeController.close(anyBoolean(), any())).thenReturn(false);

        MapModule module = mock(MapModule.class);
        when(module.getModel()).thenReturn(mockMap);
        when(module.getMode()).thenReturn(mockMode);
        when(module.getModeController()).thenReturn(mockModeController);
        when(module.getDisplayName()).thenReturn("cancel.mm");

        Field mapModulesField = MapModuleManager.class.getDeclaredField("mapModules");
        mapModulesField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<MapModule> mapModules = (List<MapModule>) mapModulesField.get(mapModuleManager);
        mapModules.add(module);

        Field mapModuleField = MapModuleManager.class.getDeclaredField("mapModule");
        mapModuleField.setAccessible(true);
        mapModuleField.set(mapModuleManager, module);

        boolean closed = mapModuleManager.close(false, null);

        assertFalse(closed);
        verify(eventBus, never()).post(any(MapClosedEvent.class));
    }

    @Test
    void closePostsEventWithCorrectMap() throws Exception {
        MindMap specificMap = mock(MindMap.class);
        when(specificMap.getRestorable()).thenReturn("specific.mm");

        ModeController mockModeController = mock(ModeController.class);
        when(mockModeController.getMode()).thenReturn(mockMode);
        when(mockModeController.close(anyBoolean(), any())).thenReturn(true);

        MapModule module = mock(MapModule.class);
        when(module.getModel()).thenReturn(specificMap);
        when(module.getMode()).thenReturn(mockMode);
        when(module.getModeController()).thenReturn(mockModeController);
        when(module.getDisplayName()).thenReturn("specific.mm");

        Field mapModulesField = MapModuleManager.class.getDeclaredField("mapModules");
        mapModulesField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<MapModule> mapModules = (List<MapModule>) mapModulesField.get(mapModuleManager);
        mapModules.add(module);

        Field mapModuleField = MapModuleManager.class.getDeclaredField("mapModule");
        mapModuleField.setAccessible(true);
        mapModuleField.set(mapModuleManager, module);

        mapModuleManager.close(true, null);

        ArgumentCaptor<MapClosedEvent> captor = ArgumentCaptor.forClass(MapClosedEvent.class);
        verify(eventBus).post(captor.capture());

        assertSame(specificMap, captor.getValue().map());
    }

    @Test
    void mapLoadedEventContainsCorrectData() {
        MindMap map = mock(MindMap.class);
        MapLoadedEvent event = new MapLoadedEvent(map, "myfile.mm");

        assertSame(map, event.map());
        assertEquals("myfile.mm", event.fileName());
    }

    @Test
    void mapClosedEventContainsCorrectData() {
        MindMap map = mock(MindMap.class);
        MapClosedEvent event = new MapClosedEvent(map);

        assertSame(map, event.map());
    }

    @Test
    void mapLoadedEventRecordEquality() {
        MindMap map = mock(MindMap.class);
        MapLoadedEvent event1 = new MapLoadedEvent(map, "file.mm");
        MapLoadedEvent event2 = new MapLoadedEvent(map, "file.mm");

        assertEquals(event1, event2);
        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    void mapClosedEventRecordEquality() {
        MindMap map = mock(MindMap.class);
        MapClosedEvent event1 = new MapClosedEvent(map);
        MapClosedEvent event2 = new MapClosedEvent(map);

        assertEquals(event1, event2);
        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    void noExceptionWhenEventBusIsNull() throws Exception {
        mapModuleManager.setEventBus(null);
        when(mockMap.getRestorable()).thenReturn("test.mm");
        injectMapModule("test.mm");

        // Should not throw NPE when event bus is null
        assertDoesNotThrow(() -> mapModuleManager.close(true, null));
    }

    @Test
    void mapLoadedEventWithNullFileName() {
        MindMap map = mock(MindMap.class);
        MapLoadedEvent event = new MapLoadedEvent(map, null);

        assertSame(map, event.map());
        assertNull(event.fileName());
    }
}
