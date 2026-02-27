package freemind.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface MindMapEdge extends MindMapLine {

    Element save(Document doc);

    // returns false if and only if the style is inherited from parent
    boolean hasStyle();

    int getStyleAsInt();
}
