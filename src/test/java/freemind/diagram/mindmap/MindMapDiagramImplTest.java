package freemind.diagram.mindmap;

import freemind.diagram.DiagramChangeEvent;
import freemind.diagram.DiagramMetadata;
import freemind.diagram.NodeContent;
import freemind.diagram.NodeId;
import freemind.diagram.capabilities.AuxiliaryLink;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.*;

class MindMapDiagramImplTest {

    private static MindMapNodeImpl node(String id) {
        return new MindMapNodeImpl(new NodeId(id), NodeContent.plain(id));
    }

    @Test
    void newDiagramHasOnlyRoot() {
        var root = node("root");
        var d = MindMapDiagramImpl.createEmpty(root);
        assertSame(root, d.rootNode());
        assertTrue(d.getParent(root).isEmpty());
        assertTrue(d.getChildren(root).isEmpty());
        assertEquals(1, allNodeIds(d).size());
    }

    @Test
    void addChildAttachesNode() {
        var root = node("root");
        var d = MindMapDiagramImpl.createEmpty(root);
        var a = node("a");
        d.addChild(root, a);

        assertEquals(List.of(a), d.getChildren(root));
        assertSame(root, d.getParent(a).orElseThrow());
        assertEquals(0, d.depthOf(root));
        assertEquals(1, d.depthOf(a));
    }

    @Test
    void addChildRejectsAlreadyAttachedNode() {
        var root = node("root");
        var d = MindMapDiagramImpl.createEmpty(root);
        var a = node("a");
        d.addChild(root, a);
        assertThrows(IllegalArgumentException.class, () -> d.addChild(root, a));
    }

    @Test
    void removeNodeDetachesSubtreeAndPurgesLinks() {
        var root = node("root");
        var a = node("a");
        var a1 = node("a1");
        var b = node("b");
        var d = MindMapDiagramImpl.createEmpty(root);
        d.addChild(root, a);
        d.addChild(a, a1);
        d.addChild(root, b);
        d.addAuxiliaryLink(AuxiliaryLink.of(a, b));

        d.removeNode(a);

        assertEquals(List.of(b), d.getChildren(root));
        assertTrue(d.getParent(a1).isEmpty());
        assertEquals(0, d.outgoingAuxiliaryLinks(a).size());
        assertEquals(0, d.incomingAuxiliaryLinks(b).size());
        assertEquals(Set.of("root", "b"), allNodeIds(d));
    }

    @Test
    void cannotRemoveRoot() {
        var root = node("root");
        var d = MindMapDiagramImpl.createEmpty(root);
        assertThrows(IllegalArgumentException.class, () -> d.removeNode(root));
    }

    @Test
    void removeNodeRejectsUnattachedNode() {
        var root = node("root");
        var d = MindMapDiagramImpl.createEmpty(root);
        var a = node("a");
        // a was never attached
        assertThrows(IllegalArgumentException.class, () -> d.removeNode(a));
    }

    @Test
    void auxiliaryLinkPersists() {
        var root = node("root");
        var a = node("a");
        var b = node("b");
        var d = MindMapDiagramImpl.createEmpty(root);
        d.addChild(root, a);
        d.addChild(root, b);
        d.addAuxiliaryLink(AuxiliaryLink.of(a, b));

        assertEquals(1, d.outgoingAuxiliaryLinks(a).size());
        assertEquals(1, d.incomingAuxiliaryLinks(b).size());
        assertEquals(0, d.outgoingAuxiliaryLinks(b).size());
    }

    @Test
    void metadataChangeFiresListener() {
        var root = node("root");
        var d = MindMapDiagramImpl.createEmpty(root);
        var events = new ArrayList<DiagramChangeEvent>();
        d.addListener(events::add);
        d.setMetadata(DiagramMetadata.empty(Instant.parse("2026-01-01T00:00:00Z")).withTitle("X"));
        assertEquals(1, events.size());
        assertEquals(DiagramChangeEvent.ChangeKind.METADATA, events.get(0).kind());
    }

    private static Set<String> allNodeIds(MindMapDiagramImpl d) {
        return StreamSupport.stream(d.allNodes().spliterator(), false)
            .map(n -> n.nodeId().value()).collect(java.util.stream.Collectors.toSet());
    }
}
