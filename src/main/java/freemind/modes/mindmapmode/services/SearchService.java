package freemind.modes.mindmapmode.services;

import freemind.model.MindMapNode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SearchService {

    public record SearchResult(MindMapNode node, String matchLocation, String matchedText) {}

    public List<SearchResult> searchNodes(List<MindMapNode> rootNodes, String query, boolean useRegex) {
        List<SearchResult> results = new ArrayList<>();
        Pattern pattern = useRegex
                ? Pattern.compile(query, Pattern.CASE_INSENSITIVE)
                : Pattern.compile(Pattern.quote(query), Pattern.CASE_INSENSITIVE);

        for (MindMapNode root : rootNodes) {
            searchRecursive(root, pattern, results);
        }
        return results;
    }

    private void searchRecursive(MindMapNode node, Pattern pattern, List<SearchResult> results) {
        String text = node.getPlainTextContent();
        if (text != null && pattern.matcher(text).find()) {
            results.add(new SearchResult(node, "text", text));
        }

        String noteText = node.getNoteText();
        if (noteText != null && pattern.matcher(noteText).find()) {
            results.add(new SearchResult(node, "note", noteText));
        }

        for (String key : node.getAttributeKeyList()) {
            String value = node.getAttribute(key);
            if (value != null && pattern.matcher(value).find()) {
                results.add(new SearchResult(node, "attribute:" + key, value));
            }
        }

        for (Object child : node.getChildren()) {
            if (child instanceof MindMapNode childNode) {
                searchRecursive(childNode, pattern, results);
            }
        }
    }
}
