package freemind.modes.mindmapmode.services;

import freemind.extensions.ModeControllerHook;
import freemind.model.MindMapNode;
import freemind.modes.MapFeedback;
import freemind.modes.mindmapmode.actions.xml.actors.AddHookActor;
import freemind.modes.mindmapmode.actions.xml.actors.XmlActorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HookServiceTest {

    private XmlActorFactory actorFactory;
    private AddHookActor addHookActor;
    private HookService service;

    @BeforeEach
    void setUp() {
        actorFactory = mock(XmlActorFactory.class);
        addHookActor = mock(AddHookActor.class);

        when(actorFactory.getAddHookActor()).thenReturn(addHookActor);

        service = new HookService(actorFactory);
    }

    @Test
    void constructorCreatesServiceWithoutError() {
        assertNotNull(service);
    }

    @Test
    void addHookDelegatesToAddHookActor() {
        MindMapNode focussed = mock(MindMapNode.class);
        List<MindMapNode> selecteds = List.of(focussed);
        Properties props = new Properties();

        service.addHook(focussed, selecteds, "testHook", props);

        verify(addHookActor).addHook(focussed, selecteds, "testHook", props);
    }

    @Test
    void addHookWithNullProperties() {
        MindMapNode focussed = mock(MindMapNode.class);
        List<MindMapNode> selecteds = List.of(focussed);

        service.addHook(focussed, selecteds, "hookName", null);

        verify(addHookActor).addHook(focussed, selecteds, "hookName", null);
    }

    @Test
    void addHookWithEmptySelectedList() {
        MindMapNode focussed = mock(MindMapNode.class);
        List<MindMapNode> empty = Collections.emptyList();

        service.addHook(focussed, empty, "hookName", new Properties());

        verify(addHookActor).addHook(eq(focussed), eq(empty), eq("hookName"), any(Properties.class));
    }

    @Test
    void addHookWithMultipleSelectedNodes() {
        MindMapNode focussed = mock(MindMapNode.class);
        MindMapNode other = mock(MindMapNode.class);
        List<MindMapNode> selecteds = List.of(focussed, other);

        service.addHook(focussed, selecteds, "multiHook", null);

        verify(addHookActor).addHook(focussed, selecteds, "multiHook", null);
    }

    @Test
    void addHookWithProperties() {
        MindMapNode focussed = mock(MindMapNode.class);
        List<MindMapNode> selecteds = List.of(focussed);
        Properties props = new Properties();
        props.setProperty("key1", "value1");
        props.setProperty("key2", "value2");

        service.addHook(focussed, selecteds, "configuredHook", props);

        verify(addHookActor).addHook(focussed, selecteds, "configuredHook", props);
    }

    @Test
    void removeHookDelegatesToAddHookActor() {
        MindMapNode focussed = mock(MindMapNode.class);
        List<MindMapNode> selecteds = List.of(focussed);

        service.removeHook(focussed, selecteds, "hookToRemove");

        verify(addHookActor).removeHook(focussed, selecteds, "hookToRemove");
    }

    @Test
    void removeHookWithEmptySelectedList() {
        MindMapNode focussed = mock(MindMapNode.class);
        List<MindMapNode> empty = Collections.emptyList();

        service.removeHook(focussed, empty, "hookName");

        verify(addHookActor).removeHook(focussed, empty, "hookName");
    }

    @Test
    void removeHookWithMultipleSelectedNodes() {
        MindMapNode focussed = mock(MindMapNode.class);
        MindMapNode node2 = mock(MindMapNode.class);
        List<MindMapNode> selecteds = List.of(focussed, node2);

        service.removeHook(focussed, selecteds, "hookToRemove");

        verify(addHookActor).removeHook(focussed, selecteds, "hookToRemove");
    }

    @Test
    void invokeHookCallsSetControllerStartupAndShutdown() {
        ModeControllerHook hook = mock(ModeControllerHook.class);
        MapFeedback controller = mock(MapFeedback.class);

        service.invokeHook(hook, controller);

        var inOrder = inOrder(hook);
        inOrder.verify(hook).setController(controller);
        inOrder.verify(hook).startupMapHook();
        inOrder.verify(hook).shutdownMapHook();
    }

    @Test
    void invokeHookDoesNotUseActorFactory() {
        ModeControllerHook hook = mock(ModeControllerHook.class);
        MapFeedback controller = mock(MapFeedback.class);

        service.invokeHook(hook, controller);

        verifyNoInteractions(addHookActor);
    }

    @Test
    void invokeHookHandlesExceptionInStartup() {
        ModeControllerHook hook = mock(ModeControllerHook.class);
        MapFeedback controller = mock(MapFeedback.class);
        doThrow(new RuntimeException("startup error")).when(hook).startupMapHook();

        // Should not throw - exception is caught and logged
        assertDoesNotThrow(() -> service.invokeHook(hook, controller));
    }

    @Test
    void invokeHookHandlesExceptionInShutdown() {
        ModeControllerHook hook = mock(ModeControllerHook.class);
        MapFeedback controller = mock(MapFeedback.class);
        doThrow(new RuntimeException("shutdown error")).when(hook).shutdownMapHook();

        assertDoesNotThrow(() -> service.invokeHook(hook, controller));
    }

    @Test
    void invokeHookHandlesExceptionInSetController() {
        ModeControllerHook hook = mock(ModeControllerHook.class);
        MapFeedback controller = mock(MapFeedback.class);
        doThrow(new RuntimeException("controller error")).when(hook).setController(any());

        assertDoesNotThrow(() -> service.invokeHook(hook, controller));
    }

    @Test
    void invokeHookStartupNotCalledWhenSetControllerThrows() {
        ModeControllerHook hook = mock(ModeControllerHook.class);
        MapFeedback controller = mock(MapFeedback.class);
        doThrow(new RuntimeException("controller error")).when(hook).setController(any());

        service.invokeHook(hook, controller);

        verify(hook).setController(controller);
        verify(hook, never()).startupMapHook();
        verify(hook, never()).shutdownMapHook();
    }

    @Test
    void invokeHookShutdownNotCalledWhenStartupThrows() {
        ModeControllerHook hook = mock(ModeControllerHook.class);
        MapFeedback controller = mock(MapFeedback.class);
        doThrow(new RuntimeException("startup error")).when(hook).startupMapHook();

        service.invokeHook(hook, controller);

        verify(hook).setController(controller);
        verify(hook).startupMapHook();
        verify(hook, never()).shutdownMapHook();
    }

    @Test
    void addHookUsesCorrectActorFromFactory() {
        MindMapNode focussed = mock(MindMapNode.class);

        service.addHook(focussed, List.of(focussed), "hook", null);

        verify(actorFactory).getAddHookActor();
    }

    @Test
    void removeHookUsesCorrectActorFromFactory() {
        MindMapNode focussed = mock(MindMapNode.class);

        service.removeHook(focussed, List.of(focussed), "hook");

        verify(actorFactory).getAddHookActor();
    }

    @Test
    void addAndRemoveHookUseSameActorMethod() {
        MindMapNode focussed = mock(MindMapNode.class);
        List<MindMapNode> selecteds = List.of(focussed);

        service.addHook(focussed, selecteds, "hook1", null);
        service.removeHook(focussed, selecteds, "hook1");

        verify(actorFactory, times(2)).getAddHookActor();
        verify(addHookActor).addHook(focussed, selecteds, "hook1", null);
        verify(addHookActor).removeHook(focussed, selecteds, "hook1");
    }
}
