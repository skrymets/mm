package freemind.model;

import freemind.main.XMLElement;

public interface MindMapEdge extends MindMapLine {

    XMLElement save();

    // returns false if and only if the style is inherited from parent
    boolean hasStyle();

    int getStyleAsInt();
}
