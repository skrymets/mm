package freemind.modes.mindmapmode.services;

import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.services.SearchService.SearchResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchServiceTest {

    private SearchService searchService;

    @BeforeEach
    void setUp() {
        searchService = new SearchService();
    }

    private MindMapNode createMockNode(String text, String noteText) {
        MindMapNode node = mock(MindMapNode.class);
        when(node.getPlainTextContent()).thenReturn(text);
        when(node.getNoteText()).thenReturn(noteText);
        when(node.getAttributeKeyList()).thenReturn(Collections.emptyList());
        when(node.getChildren()).thenReturn(Collections.emptyList());
        return node;
    }

    @Test
    void searchFindsNodeByText() {
        MindMapNode node = createMockNode("Hello World", null);

        List<SearchResult> results = searchService.searchNodes(List.of(node), "Hello", false);

        assertEquals(1, results.size());
        assertEquals("text", results.get(0).matchLocation());
        assertEquals("Hello World", results.get(0).matchedText());
        assertSame(node, results.get(0).node());
    }

    @Test
    void searchIsCaseInsensitive() {
        MindMapNode node = createMockNode("Hello World", null);

        List<SearchResult> results = searchService.searchNodes(List.of(node), "hello world", false);

        assertEquals(1, results.size());
        assertEquals("text", results.get(0).matchLocation());
    }

    @Test
    void searchFindsInNoteText() {
        MindMapNode node = createMockNode("Title", "This is a detailed note");

        List<SearchResult> results = searchService.searchNodes(List.of(node), "detailed", false);

        assertEquals(1, results.size());
        assertEquals("note", results.get(0).matchLocation());
        assertEquals("This is a detailed note", results.get(0).matchedText());
    }

    @Test
    void searchReturnsEmptyForNoMatch() {
        MindMapNode node = createMockNode("Hello World", "Some note");

        List<SearchResult> results = searchService.searchNodes(List.of(node), "nonexistent", false);

        assertTrue(results.isEmpty());
    }

    @Test
    void searchSupportsRegex() {
        MindMapNode node = createMockNode("Error code: 404", null);

        List<SearchResult> results = searchService.searchNodes(List.of(node), "code:\\s*\\d+", true);

        assertEquals(1, results.size());
        assertEquals("text", results.get(0).matchLocation());
    }

    @Test
    void searchTraversesDeepTree() {
        MindMapNode grandchild = createMockNode("deep target", null);
        MindMapNode child = createMockNode("middle", null);
        MindMapNode root = createMockNode("top", null);

        when(child.getChildren()).thenReturn(List.of(grandchild));
        when(root.getChildren()).thenReturn(List.of(child));

        List<SearchResult> results = searchService.searchNodes(List.of(root), "target", false);

        assertEquals(1, results.size());
        assertSame(grandchild, results.get(0).node());
        assertEquals("deep target", results.get(0).matchedText());
    }

    @Test
    void searchFindsInAttributes() {
        MindMapNode node = mock(MindMapNode.class);
        when(node.getPlainTextContent()).thenReturn("Title");
        when(node.getNoteText()).thenReturn(null);
        when(node.getAttributeKeyList()).thenReturn(List.of("priority"));
        when(node.getAttribute("priority")).thenReturn("high urgency");
        when(node.getChildren()).thenReturn(Collections.emptyList());

        List<SearchResult> results = searchService.searchNodes(List.of(node), "urgency", false);

        assertEquals(1, results.size());
        assertEquals("attribute:priority", results.get(0).matchLocation());
        assertEquals("high urgency", results.get(0).matchedText());
    }

    @Test
    void searchFindsMultipleMatchesInSameNode() {
        MindMapNode node = createMockNode("Find me here", "Find me in notes too");

        List<SearchResult> results = searchService.searchNodes(List.of(node), "find me", false);

        assertEquals(2, results.size());
        assertEquals("text", results.get(0).matchLocation());
        assertEquals("note", results.get(1).matchLocation());
    }
}
