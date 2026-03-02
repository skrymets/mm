package freemind.modes;

import freemind.model.MindMapLine;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.*;

public interface MindMapCloud extends MindMapLine {

    // public Color getColor();
    // public String getStyle();
    // public Stroke getStroke();
    // public int getWidth();
    // public String toString();

    /**
     * Describes the color of the exterior of the cloud. Normally, this color is
     * derived from the interior color.
     */
    Color getExteriorColor();

    /**
     * gets iterative level which is required for painting and layout.
     * <p>
     * Cloud iterative level is kept in CloudAdapter object. It is automatically
     * calculated during the first call of this Method (delayed initialisation).
     */
    int getIterativeLevel();

    /**
     * changes the iterative level.
     * <p>
     * When some parent node gets or loses its cloud, it should call this
     * Method, with deltaLevel equal to 1 or -1.
     */
    void changeIterativeLevel(int deltaLevel);

    Element save(Document doc);
}
