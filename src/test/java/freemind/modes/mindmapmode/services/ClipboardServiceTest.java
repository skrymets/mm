package freemind.modes.mindmapmode.services;

import freemind.model.MindMap;
import freemind.modes.MindMapLinkRegistry;
import freemind.model.MindMapNode;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.mindmapmode.actions.xml.actors.CutActor;
import freemind.modes.mindmapmode.actions.xml.actors.PasteActor;
import freemind.modes.mindmapmode.actions.xml.actors.XmlActorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.datatransfer.Transferable;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClipboardServiceTest {

    private XmlActorFactory actorFactory;
    private ExtendedMapFeedback mapFeedback;
    private CutActor cutActor;
    private PasteActor pasteActor;
    private ClipboardService service;

    @BeforeEach
    void setUp() {
        actorFactory = mock(XmlActorFactory.class);
        mapFeedback = mock(ExtendedMapFeedback.class);
        cutActor = mock(CutActor.class);
        pasteActor = mock(PasteActor.class);

        when(actorFactory.getCutActor()).thenReturn(cutActor);
        when(actorFactory.getPasteActor()).thenReturn(pasteActor);

        MindMap mindMap = mock(MindMap.class);
        MindMapLinkRegistry linkRegistry = mock(MindMapLinkRegistry.class);
        when(mapFeedback.getMap()).thenReturn(mindMap);
        when(mindMap.getLinkRegistry()).thenReturn(linkRegistry);

        service = new ClipboardService(actorFactory, mapFeedback);
    }

    @Test
    void constructorCreatesServiceWithoutError() {
        assertNotNull(service);
    }

    @Test
    void cutDelegatesToCutActor() {
        MindMapNode node = mock(MindMapNode.class);
        List<MindMapNode> nodes = List.of(node);
        Transferable expected = mock(Transferable.class);
        when(cutActor.cut(nodes)).thenReturn(expected);

        Transferable result = service.cut(nodes);

        assertSame(expected, result);
        verify(cutActor).cut(nodes);
    }

    @Test
    void cutWithEmptyList() {
        List<MindMapNode> empty = Collections.emptyList();
        Transferable expected = mock(Transferable.class);
        when(cutActor.cut(empty)).thenReturn(expected);

        Transferable result = service.cut(empty);

        assertSame(expected, result);
        verify(cutActor).cut(empty);
    }

    @Test
    void cutWithMultipleNodes() {
        MindMapNode node1 = mock(MindMapNode.class);
        MindMapNode node2 = mock(MindMapNode.class);
        List<MindMapNode> nodes = List.of(node1, node2);
        Transferable expected = mock(Transferable.class);
        when(cutActor.cut(nodes)).thenReturn(expected);

        Transferable result = service.cut(nodes);

        assertSame(expected, result);
        verify(cutActor).cut(nodes);
    }

    @Test
    void pasteNodeDelegatesToPasteActor() {
        MindMapNode node = mock(MindMapNode.class);
        MindMapNode parent = mock(MindMapNode.class);

        service.paste(node, parent);

        verify(pasteActor).paste(node, parent);
    }

    @Test
    void pasteTransferableAsChildDelegatesToPasteActor() {
        Transferable t = mock(Transferable.class);
        MindMapNode parent = mock(MindMapNode.class);
        when(parent.isNewChildLeft()).thenReturn(false);
        when(parent.isFolded()).thenReturn(false);
        when(pasteActor.paste(t, parent, false, false)).thenReturn(true);

        service.paste(t, parent);

        verify(pasteActor).paste(t, parent, false, false);
    }

    @Test
    void pasteAsChildUsesNodeIsNewChildLeft() {
        Transferable t = mock(Transferable.class);
        MindMapNode parent = mock(MindMapNode.class);
        when(parent.isNewChildLeft()).thenReturn(true);
        when(parent.isFolded()).thenReturn(false);
        when(pasteActor.paste(t, parent, false, true)).thenReturn(true);

        service.paste(t, parent);

        verify(pasteActor).paste(t, parent, false, true);
    }

    @Test
    void pasteWithExplicitParametersDelegatesToPasteActor() {
        Transferable t = mock(Transferable.class);
        MindMapNode target = mock(MindMapNode.class);
        when(target.isFolded()).thenReturn(false);
        when(pasteActor.paste(t, target, true, true)).thenReturn(true);

        boolean result = service.paste(t, target, true, true);

        assertTrue(result);
        verify(pasteActor).paste(t, target, true, true);
    }

    @Test
    void pasteReturnsFalseWhenActorReturnsFalse() {
        Transferable t = mock(Transferable.class);
        MindMapNode target = mock(MindMapNode.class);
        when(target.isFolded()).thenReturn(false);
        when(pasteActor.paste(t, target, false, false)).thenReturn(false);

        boolean result = service.paste(t, target, false, false);

        assertFalse(result);
    }

    @Test
    void pasteAsSiblingDoesNotCheckFolded() {
        Transferable t = mock(Transferable.class);
        MindMapNode target = mock(MindMapNode.class);
        when(target.isFolded()).thenReturn(true);
        when(pasteActor.paste(t, target, true, false)).thenReturn(true);

        service.paste(t, target, true, false);

        // setFolded should NOT be called when asSibling is true, even if folded
        verify(mapFeedback, never()).setFolded(any(), anyBoolean());
    }

    @Test
    void copyReturnsTransferableForNode() {
        MindMapNode node = mock(MindMapNode.class);
        when(mapFeedback.getNodeID(node)).thenReturn("ID_123");

        Transferable result = service.copy(node, false);

        assertNotNull(result);
    }

    @Test
    void copyReturnsTransferableWithSaveInvisible() {
        MindMapNode node = mock(MindMapNode.class);
        when(mapFeedback.getNodeID(node)).thenReturn("ID_456");

        Transferable result = service.copy(node, true);

        assertNotNull(result);
    }

    @Test
    void copyCallsSaveOnNode() throws Exception {
        MindMapNode node = mock(MindMapNode.class);
        when(mapFeedback.getNodeID(node)).thenReturn("ID_789");

        service.copy(node, false);

        verify(node).save(any(), any(), any(), eq(false), eq(true));
    }

    @Test
    void copyWithSaveInvisibleTruePassesFlag() throws Exception {
        MindMapNode node = mock(MindMapNode.class);
        when(mapFeedback.getNodeID(node)).thenReturn("ID_789");

        service.copy(node, true);

        verify(node).save(any(), any(), any(), eq(true), eq(true));
    }

    @Test
    void copyUsesMapLinkRegistry() throws Exception {
        MindMapNode node = mock(MindMapNode.class);
        MindMap map = mapFeedback.getMap();
        MindMapLinkRegistry registry = map.getLinkRegistry();
        when(mapFeedback.getNodeID(node)).thenReturn("ID_100");

        service.copy(node, false);

        verify(node).save(any(), any(), eq(registry), anyBoolean(), anyBoolean());
    }

    @Test
    void pasteNodeWithDifferentParents() {
        MindMapNode node = mock(MindMapNode.class);
        MindMapNode parent1 = mock(MindMapNode.class);
        MindMapNode parent2 = mock(MindMapNode.class);

        service.paste(node, parent1);
        service.paste(node, parent2);

        verify(pasteActor).paste(node, parent1);
        verify(pasteActor).paste(node, parent2);
    }

    @Test
    void cutDelegatesToCorrectActorFromFactory() {
        MindMapNode node = mock(MindMapNode.class);
        List<MindMapNode> nodes = List.of(node);

        service.cut(nodes);

        verify(actorFactory).getCutActor();
        verifyNoInteractions(pasteActor);
    }

    @Test
    void pasteNodeDelegatesToCorrectActorFromFactory() {
        MindMapNode node = mock(MindMapNode.class);
        MindMapNode parent = mock(MindMapNode.class);

        service.paste(node, parent);

        verify(actorFactory).getPasteActor();
        verifyNoInteractions(cutActor);
    }
}
