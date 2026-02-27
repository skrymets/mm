package freemind.modes.mindmapmode.services;

import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.actions.xml.actors.ChangeNoteTextActor;
import freemind.modes.mindmapmode.actions.xml.actors.EditActor;
import freemind.modes.mindmapmode.actions.xml.actors.XmlActorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EditingServiceTest {

    private XmlActorFactory actorFactory;
    private EditActor editActor;
    private ChangeNoteTextActor changeNoteTextActor;
    private EditingService service;

    @BeforeEach
    void setUp() {
        actorFactory = mock(XmlActorFactory.class);
        editActor = mock(EditActor.class);
        changeNoteTextActor = mock(ChangeNoteTextActor.class);

        when(actorFactory.getEditActor()).thenReturn(editActor);
        when(actorFactory.getChangeNoteTextActor()).thenReturn(changeNoteTextActor);

        service = new EditingService(actorFactory);
    }

    @Test
    void constructorCreatesServiceWithoutError() {
        assertNotNull(service);
    }

    @Test
    void setNodeTextDelegatesToEditActor() {
        MindMapNode node = mock(MindMapNode.class);

        service.setNodeText(node, "Hello World");

        verify(editActor).setNodeText(node, "Hello World");
    }

    @Test
    void setNodeTextWithEmptyString() {
        MindMapNode node = mock(MindMapNode.class);

        service.setNodeText(node, "");

        verify(editActor).setNodeText(node, "");
    }

    @Test
    void setNodeTextWithNullText() {
        MindMapNode node = mock(MindMapNode.class);

        service.setNodeText(node, null);

        verify(editActor).setNodeText(node, null);
    }

    @Test
    void setNodeTextWithHtmlContent() {
        MindMapNode node = mock(MindMapNode.class);
        String htmlText = "<html><body><p>Rich text</p></body></html>";

        service.setNodeText(node, htmlText);

        verify(editActor).setNodeText(node, htmlText);
    }

    @Test
    void setNodeTextWithUnicodeContent() {
        MindMapNode node = mock(MindMapNode.class);
        String unicodeText = "Привет мир 你好世界";

        service.setNodeText(node, unicodeText);

        verify(editActor).setNodeText(node, unicodeText);
    }

    @Test
    void setNodeTextWithLongContent() {
        MindMapNode node = mock(MindMapNode.class);
        String longText = "A".repeat(10000);

        service.setNodeText(node, longText);

        verify(editActor).setNodeText(node, longText);
    }

    @Test
    void setNoteTextDelegatesToChangeNoteTextActor() {
        MindMapNode node = mock(MindMapNode.class);

        service.setNoteText(node, "This is a note");

        verify(changeNoteTextActor).setNoteText(node, "This is a note");
    }

    @Test
    void setNoteTextWithEmptyString() {
        MindMapNode node = mock(MindMapNode.class);

        service.setNoteText(node, "");

        verify(changeNoteTextActor).setNoteText(node, "");
    }

    @Test
    void setNoteTextWithNullText() {
        MindMapNode node = mock(MindMapNode.class);

        service.setNoteText(node, null);

        verify(changeNoteTextActor).setNoteText(node, null);
    }

    @Test
    void setNoteTextWithHtmlContent() {
        MindMapNode node = mock(MindMapNode.class);
        String htmlNote = "<html><body><h1>Note</h1></body></html>";

        service.setNoteText(node, htmlNote);

        verify(changeNoteTextActor).setNoteText(node, htmlNote);
    }

    @Test
    void setNodeTextUsesCorrectActorFromFactory() {
        MindMapNode node = mock(MindMapNode.class);

        service.setNodeText(node, "text");

        verify(actorFactory).getEditActor();
        verifyNoInteractions(changeNoteTextActor);
    }

    @Test
    void setNoteTextUsesCorrectActorFromFactory() {
        MindMapNode node = mock(MindMapNode.class);

        service.setNoteText(node, "note");

        verify(actorFactory).getChangeNoteTextActor();
        verifyNoInteractions(editActor);
    }

    @Test
    void setNodeTextOnDifferentNodes() {
        MindMapNode node1 = mock(MindMapNode.class);
        MindMapNode node2 = mock(MindMapNode.class);

        service.setNodeText(node1, "text1");
        service.setNodeText(node2, "text2");

        verify(editActor).setNodeText(node1, "text1");
        verify(editActor).setNodeText(node2, "text2");
    }

    @Test
    void setNoteTextOnDifferentNodes() {
        MindMapNode node1 = mock(MindMapNode.class);
        MindMapNode node2 = mock(MindMapNode.class);

        service.setNoteText(node1, "note1");
        service.setNoteText(node2, "note2");

        verify(changeNoteTextActor).setNoteText(node1, "note1");
        verify(changeNoteTextActor).setNoteText(node2, "note2");
    }

    @Test
    void setNodeTextAndNoteTextOnSameNode() {
        MindMapNode node = mock(MindMapNode.class);

        service.setNodeText(node, "title");
        service.setNoteText(node, "details");

        verify(editActor).setNodeText(node, "title");
        verify(changeNoteTextActor).setNoteText(node, "details");
    }

    @Test
    void setNodeTextWithMultilineContent() {
        MindMapNode node = mock(MindMapNode.class);
        String multiline = "Line 1\nLine 2\nLine 3";

        service.setNodeText(node, multiline);

        verify(editActor).setNodeText(node, multiline);
    }

    @Test
    void setNoteTextWithMultilineContent() {
        MindMapNode node = mock(MindMapNode.class);
        String multiline = "Paragraph 1\n\nParagraph 2";

        service.setNoteText(node, multiline);

        verify(changeNoteTextActor).setNoteText(node, multiline);
    }
}
