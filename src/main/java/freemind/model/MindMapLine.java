package freemind.model;

import java.awt.*;

public interface MindMapLine extends Cloneable {

    Color getColor();

    String getStyle();

    int getWidth();

    int getRealWidth();

    String toString();

    /**
     * The node to which this line is associated.
     */
    void setTarget(MindMapNode node);

    Object clone();
}
