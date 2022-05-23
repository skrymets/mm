package freemind.view.mindmapview;

import freemind.main.Tools;
import freemind.model.EdgeAdapter;
import freemind.model.MindMapEdge;

import java.awt.*;

/**
 * This class represents a single Edge of a MindMap.
 */
public abstract class EdgeView {

    protected static final BasicStroke DEF_STROKE = new BasicStroke();

    private NodeView target;
    protected NodeView source;

    protected Point start, end;

    static Stroke ECLIPSED_STROKE = null;

    /**
     * This should be a task of MindMapLayout start,end must be initialized...
     */
    public void paint(NodeView target, Graphics2D g) {
        this.source = target.getVisibleParentView();
        this.target = target;
        createEnd();
        createStart();
        paint(g);

        this.source = null;
        this.target = null;
    }

    protected void createEnd() {
        end = getTarget().getMainViewInPoint();
        Tools.convertPointToAncestor(this.target.getMainView(), end, source);
    }

    protected void createStart() {
        start = source.getMainViewOutPoint(getTarget(), end);
        Tools.convertPointToAncestor(source.getMainView(), start, source);
    }

    abstract protected void paint(Graphics2D g);

    protected void reset() {
        this.source = null;
        this.target = null;
    }

    public abstract Color getColor();

    public Stroke getStroke() {
        int width = getWidth();
        if (width == EdgeAdapter.WIDTH_THIN) {
            return DEF_STROKE;
        }
        return new BasicStroke(width * getMap().getZoom(), BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
    }

    public int getWidth() {
        return getModel().getWidth();
    }

    protected MindMapEdge getModel() {
        return getTarget().getModel().getEdge();
    }

    protected MapView getMap() {
        return getTarget().getMap();
    }

    protected static Stroke getEclipsedStroke() {
        if (ECLIPSED_STROKE == null) {
            float[] dash = {3.0f, 9.0f};
            ECLIPSED_STROKE = new BasicStroke(3.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 12.0f, dash, 0.0f);
        }
        return ECLIPSED_STROKE;
    }

    protected boolean isTargetEclipsed() {
        return getTarget().isParentHidden();
    }

    /**
     * @return Returns the source.
     */
    protected NodeView getSource() {
        return source;
    }

    /**
     * @return Returns the target.
     */
    protected NodeView getTarget() {
        return target;
    }
}
