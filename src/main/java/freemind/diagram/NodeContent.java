package freemind.diagram;

import java.util.Objects;

/** Text content of a node, in a declared format. */
public record NodeContent(String text, ContentFormat format) {

    public NodeContent {
        Objects.requireNonNull(text, "text");
        Objects.requireNonNull(format, "format");
    }

    public static NodeContent plain(String text) {
        return new NodeContent(text, ContentFormat.PLAIN);
    }

    public static NodeContent html(String text) {
        return new NodeContent(text, ContentFormat.HTML);
    }

    public static NodeContent markdown(String text) {
        return new NodeContent(text, ContentFormat.MARKDOWN);
    }
}
