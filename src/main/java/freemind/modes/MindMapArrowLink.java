package freemind.modes;

import freemind.model.MindMapLink;
import freemind.view.mindmapview.MapView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.*;

public interface MindMapArrowLink extends MindMapLink {

    /**
     * Means: yes, it has the default arrow
     */
    String ARROW_DEFAULT = "Default";
    /**
     * Means: no, it hasn't an arrow at this side.
     */
    String ARROW_NONE = "None";

    /* for arrows: */
    Point getStartInclination(); // the zero is the start point of the
    // line;

    Point getEndInclination(); // the zero is the end point of the line;

    void setStartInclination(Point startInclination);

    void setEndInclination(Point endInclination);

    /**
     * the type of the start arrow: currently "None" and "Default".
     */
    String getStartArrow();

    /**
     * the type of the end arrow: currently "None" and "Default".
     */
    String getEndArrow();

    void changeInclination(MapView map, int originX, int originY,
                           int deltaX, int deltaY);

    void showControlPoints(boolean bShowControlPointsFlag);

    boolean getShowControlPointsFlag();

    Element save(Document doc);

}
