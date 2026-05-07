package freemind.diagram.mindmap;

import freemind.diagram.AttributeBag;
import freemind.diagram.DiagramNodeChangeEvent;
import freemind.diagram.DiagramNodeChangeEvent.ChangeKind;
import freemind.diagram.NodeContent;
import freemind.diagram.NodeId;
import freemind.diagram.StyleReferences;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MindMapNodeImplTest {

    @Test
    void setContentNotifiesListeners() {
        var node = new MindMapNodeImpl(new NodeId("n1"), NodeContent.plain("a"));
        var events = new ArrayList<DiagramNodeChangeEvent>();
        node.addListener(events::add);
        node.setContent(NodeContent.plain("b"));
        assertEquals(1, events.size());
        assertEquals(ChangeKind.CONTENT, events.get(0).kind());
        assertSame(node, events.get(0).node());
        assertEquals("b", node.content().text());
    }

    @Test
    void setStyleReferencesNotifiesListeners() {
        var node = new MindMapNodeImpl(new NodeId("n1"), NodeContent.plain("a"));
        var events = new ArrayList<DiagramNodeChangeEvent>();
        node.addListener(events::add);
        node.setStyleReferences(StyleReferences.none());
        assertEquals(1, events.size());
        assertEquals(ChangeKind.STYLE, events.get(0).kind());
    }

    @Test
    void setAttributesNotifiesListeners() {
        var node = new MindMapNodeImpl(new NodeId("n1"), NodeContent.plain("a"));
        var events = new ArrayList<DiagramNodeChangeEvent>();
        node.addListener(events::add);
        node.setAttributes(AttributeBag.empty().with("priority", "high"));
        assertEquals(1, events.size());
        assertEquals(ChangeKind.ATTRIBUTES, events.get(0).kind());
        assertEquals("high", node.attributes().get("priority").orElseThrow());
    }

    @Test
    void removeListenerStopsNotifications() {
        var node = new MindMapNodeImpl(new NodeId("n1"), NodeContent.plain("a"));
        var events = new ArrayList<DiagramNodeChangeEvent>();
        freemind.diagram.DiagramNodeListener listener = events::add;
        node.addListener(listener);
        node.removeListener(listener);
        node.setContent(NodeContent.plain("b"));
        assertEquals(0, events.size());
    }

    @Test
    void rejectsNullSetters() {
        var node = new MindMapNodeImpl(new NodeId("n1"), NodeContent.plain("a"));
        assertThrows(NullPointerException.class, () -> node.setContent(null));
        assertThrows(NullPointerException.class, () -> node.setStyleReferences(null));
        assertThrows(NullPointerException.class, () -> node.setAttributes(null));
    }
}
