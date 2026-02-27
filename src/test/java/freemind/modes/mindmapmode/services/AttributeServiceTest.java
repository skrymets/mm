package freemind.modes.mindmapmode.services;

import freemind.model.MindMapNode;
import freemind.modes.attributes.Attribute;
import freemind.modes.mindmapmode.actions.xml.actors.AddAttributeActor;
import freemind.modes.mindmapmode.actions.xml.actors.InsertAttributeActor;
import freemind.modes.mindmapmode.actions.xml.actors.RemoveAttributeActor;
import freemind.modes.mindmapmode.actions.xml.actors.SetAttributeActor;
import freemind.modes.mindmapmode.actions.xml.actors.XmlActorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AttributeServiceTest {

    private XmlActorFactory actorFactory;
    private SetAttributeActor setAttributeActor;
    private InsertAttributeActor insertAttributeActor;
    private AddAttributeActor addAttributeActor;
    private RemoveAttributeActor removeAttributeActor;
    private AttributeService service;

    @BeforeEach
    void setUp() {
        actorFactory = mock(XmlActorFactory.class);
        setAttributeActor = mock(SetAttributeActor.class);
        insertAttributeActor = mock(InsertAttributeActor.class);
        addAttributeActor = mock(AddAttributeActor.class);
        removeAttributeActor = mock(RemoveAttributeActor.class);

        when(actorFactory.getSetAttributeActor()).thenReturn(setAttributeActor);
        when(actorFactory.getInsertAttributeActor()).thenReturn(insertAttributeActor);
        when(actorFactory.getAddAttributeActor()).thenReturn(addAttributeActor);
        when(actorFactory.getRemoveAttributeActor()).thenReturn(removeAttributeActor);

        service = new AttributeService(actorFactory);
    }

    @Test
    void constructorCreatesServiceWithoutError() {
        assertNotNull(service);
    }

    // --- setAttribute tests ---

    @Test
    void setAttributeDelegatesToSetAttributeActor() {
        MindMapNode node = mock(MindMapNode.class);
        Attribute attr = new Attribute("key", "value");

        service.setAttribute(node, 0, attr);

        verify(setAttributeActor).setAttribute(node, 0, attr);
    }

    @Test
    void setAttributeAtDifferentPositions() {
        MindMapNode node = mock(MindMapNode.class);
        Attribute attr1 = new Attribute("a", "1");
        Attribute attr2 = new Attribute("b", "2");

        service.setAttribute(node, 0, attr1);
        service.setAttribute(node, 5, attr2);

        verify(setAttributeActor).setAttribute(node, 0, attr1);
        verify(setAttributeActor).setAttribute(node, 5, attr2);
    }

    @Test
    void setAttributeWithEmptyValue() {
        MindMapNode node = mock(MindMapNode.class);
        Attribute attr = new Attribute("key", "");

        service.setAttribute(node, 0, attr);

        verify(setAttributeActor).setAttribute(node, 0, attr);
    }

    @Test
    void setAttributeUsesCorrectActorFromFactory() {
        MindMapNode node = mock(MindMapNode.class);
        Attribute attr = new Attribute("k", "v");

        service.setAttribute(node, 0, attr);

        verify(actorFactory).getSetAttributeActor();
        verifyNoInteractions(insertAttributeActor, addAttributeActor, removeAttributeActor);
    }

    // --- insertAttribute tests ---

    @Test
    void insertAttributeDelegatesToInsertAttributeActor() {
        MindMapNode node = mock(MindMapNode.class);
        Attribute attr = new Attribute("key", "value");

        service.insertAttribute(node, 0, attr);

        verify(insertAttributeActor).insertAttribute(node, 0, attr);
    }

    @Test
    void insertAttributeAtPositionZero() {
        MindMapNode node = mock(MindMapNode.class);
        Attribute attr = new Attribute("first", "item");

        service.insertAttribute(node, 0, attr);

        verify(insertAttributeActor).insertAttribute(node, 0, attr);
    }

    @Test
    void insertAttributeAtHighPosition() {
        MindMapNode node = mock(MindMapNode.class);
        Attribute attr = new Attribute("last", "item");

        service.insertAttribute(node, 100, attr);

        verify(insertAttributeActor).insertAttribute(node, 100, attr);
    }

    @Test
    void insertAttributeUsesCorrectActorFromFactory() {
        MindMapNode node = mock(MindMapNode.class);
        Attribute attr = new Attribute("k", "v");

        service.insertAttribute(node, 0, attr);

        verify(actorFactory).getInsertAttributeActor();
        verifyNoInteractions(setAttributeActor, addAttributeActor, removeAttributeActor);
    }

    // --- addAttribute tests ---

    @Test
    void addAttributeDelegatesToAddAttributeActor() {
        MindMapNode node = mock(MindMapNode.class);
        Attribute attr = new Attribute("key", "value");
        when(addAttributeActor.addAttribute(node, attr)).thenReturn(3);

        int index = service.addAttribute(node, attr);

        assertEquals(3, index);
        verify(addAttributeActor).addAttribute(node, attr);
    }

    @Test
    void addAttributeReturnsZeroIndex() {
        MindMapNode node = mock(MindMapNode.class);
        Attribute attr = new Attribute("first", "attr");
        when(addAttributeActor.addAttribute(node, attr)).thenReturn(0);

        int index = service.addAttribute(node, attr);

        assertEquals(0, index);
    }

    @Test
    void addAttributeReturnsCorrectIndex() {
        MindMapNode node = mock(MindMapNode.class);
        Attribute attr = new Attribute("key", "val");
        when(addAttributeActor.addAttribute(node, attr)).thenReturn(7);

        int index = service.addAttribute(node, attr);

        assertEquals(7, index);
    }

    @Test
    void addAttributeUsesCorrectActorFromFactory() {
        MindMapNode node = mock(MindMapNode.class);
        Attribute attr = new Attribute("k", "v");
        when(addAttributeActor.addAttribute(node, attr)).thenReturn(0);

        service.addAttribute(node, attr);

        verify(actorFactory).getAddAttributeActor();
        verifyNoInteractions(setAttributeActor, insertAttributeActor, removeAttributeActor);
    }

    // --- removeAttribute tests ---

    @Test
    void removeAttributeDelegatesToRemoveAttributeActor() {
        MindMapNode node = mock(MindMapNode.class);

        service.removeAttribute(node, 2);

        verify(removeAttributeActor).removeAttribute(node, 2);
    }

    @Test
    void removeAttributeAtPositionZero() {
        MindMapNode node = mock(MindMapNode.class);

        service.removeAttribute(node, 0);

        verify(removeAttributeActor).removeAttribute(node, 0);
    }

    @Test
    void removeAttributeAtHighPosition() {
        MindMapNode node = mock(MindMapNode.class);

        service.removeAttribute(node, 99);

        verify(removeAttributeActor).removeAttribute(node, 99);
    }

    @Test
    void removeAttributeUsesCorrectActorFromFactory() {
        MindMapNode node = mock(MindMapNode.class);

        service.removeAttribute(node, 0);

        verify(actorFactory).getRemoveAttributeActor();
        verifyNoInteractions(setAttributeActor, insertAttributeActor, addAttributeActor);
    }

    // --- cross-operation tests ---

    @Test
    void allFourOperationsOnSameNode() {
        MindMapNode node = mock(MindMapNode.class);
        Attribute attr = new Attribute("key", "value");
        when(addAttributeActor.addAttribute(node, attr)).thenReturn(0);

        service.addAttribute(node, attr);
        service.setAttribute(node, 0, attr);
        service.insertAttribute(node, 1, attr);
        service.removeAttribute(node, 0);

        verify(addAttributeActor).addAttribute(node, attr);
        verify(setAttributeActor).setAttribute(node, 0, attr);
        verify(insertAttributeActor).insertAttribute(node, 1, attr);
        verify(removeAttributeActor).removeAttribute(node, 0);
    }

    @Test
    void operationsOnDifferentNodes() {
        MindMapNode node1 = mock(MindMapNode.class);
        MindMapNode node2 = mock(MindMapNode.class);
        Attribute attr = new Attribute("shared", "attr");

        service.setAttribute(node1, 0, attr);
        service.setAttribute(node2, 0, attr);

        verify(setAttributeActor).setAttribute(node1, 0, attr);
        verify(setAttributeActor).setAttribute(node2, 0, attr);
    }
}
