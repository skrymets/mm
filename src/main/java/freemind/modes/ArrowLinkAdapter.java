package freemind.modes;

import freemind.main.ColorUtils;
import freemind.main.PointUtils;
import freemind.model.LinkAdapter;
import freemind.model.MindMapNode;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.*;

@Slf4j
public abstract class ArrowLinkAdapter extends LinkAdapter implements MindMapArrowLink {

    private static final String ARROW_DEFAULT_UP = "DEFAULT";
    private static final String ARROW_NONE_UC = "NONE";

    /* the zero is the start point of the line */
    @Setter
    protected Point startInclination;
    /* the zero is the end point of the line */
    @Setter
    protected Point endInclination;
    @Getter
    protected String startArrow;
    @Getter
    protected String endArrow;
    protected boolean showControlPointsFlag;

    public ArrowLinkAdapter(MindMapNode source, MindMapNode target, MapFeedback pMapFeedback) {
        super(source, target, pMapFeedback);
        startArrow = ARROW_NONE;
        endArrow = ARROW_DEFAULT;
    }

    public Point getStartInclination() {
        if (startInclination == null)
            return null;
        return new Point(startInclination);
    }

    public Point getEndInclination() {
        if (endInclination == null)
            return null;
        return new Point(endInclination);
    }

    public void setStartArrow(String startArrow) {
        if (startArrow == null || startArrow.equalsIgnoreCase(ARROW_NONE_UC)) {
            this.startArrow = ARROW_NONE;
            return;
        } else if (startArrow.equalsIgnoreCase(ARROW_DEFAULT_UP)) {
            this.startArrow = ARROW_DEFAULT;
            return;
        }
        // dont change:
        log.error("Cannot set the start arrow type to {}", startArrow);
    }

    public void setEndArrow(String endArrow) {
        if (endArrow == null || endArrow.equalsIgnoreCase(ARROW_NONE_UC)) {
            this.endArrow = ARROW_NONE;
            return;
        } else if (endArrow.equalsIgnoreCase(ARROW_DEFAULT_UP)) {
            this.endArrow = ARROW_DEFAULT;
            return;
        }
        // dont change:
        log.error("Cannot set the end arrow type to {}", endArrow);
    }

    public Object clone() {
        ArrowLinkAdapter arrowLink = (ArrowLinkAdapter) super.clone();
        // now replace the points:
        arrowLink.startInclination = (startInclination == null) ? null : new Point(startInclination.x, startInclination.y);
        arrowLink.endInclination = (endInclination == null) ? null : new Point(endInclination.x, endInclination.y);
        arrowLink.startArrow = (startArrow == null) ? null : startArrow;
        arrowLink.endArrow = (endArrow == null) ? null : endArrow;
        return arrowLink;
    }

    public void showControlPoints(boolean bShowControlPointsFlag) {
        showControlPointsFlag = bShowControlPointsFlag;
    }

    public boolean getShowControlPointsFlag() {
        return showControlPointsFlag;
    }

    public Element save(Document doc) {
        Element arrowLink = doc.createElement("arrowlink");

        if (style != null) {
            arrowLink.setAttribute("STYLE", style);
        }
        if (getUniqueId() != null) {
            arrowLink.setAttribute("ID", getUniqueId());
        }
        if (color != null) {
            arrowLink.setAttribute("COLOR", ColorUtils.colorToXml(color));
        }
        if (getDestinationLabel() != null) {
            arrowLink.setAttribute("DESTINATION", getDestinationLabel());
        }
        if (getReferenceText() != null) {
            arrowLink.setAttribute("REFERENCETEXT", getReferenceText());
        }
        if (getStartInclination() != null) {
            arrowLink.setAttribute("STARTINCLINATION", PointUtils.PointToXml(getStartInclination()));
        }
        if (getEndInclination() != null) {
            arrowLink.setAttribute("ENDINCLINATION", PointUtils.PointToXml(getEndInclination()));
        }
        if (getStartArrow() != null)
            arrowLink.setAttribute("STARTARROW", (getStartArrow()));
        if (getEndArrow() != null)
            arrowLink.setAttribute("ENDARROW", (getEndArrow()));
        return arrowLink;
    }

    public ArrowLinkTarget createArrowLinkTarget(MindMapLinkRegistry pRegistry) {
        ArrowLinkTarget linkTarget = new ArrowLinkTarget(source, target, mMapFeedback);
        linkTarget.setSourceLabel(pRegistry.getLabel(source));
        copy(linkTarget);
        return linkTarget;
    }

    protected void copy(ArrowLinkAdapter linkTarget) {
        linkTarget.setUniqueId(getUniqueId());
        linkTarget.setColor(getColor());
        linkTarget.setDestinationLabel(getDestinationLabel());
        linkTarget.setEndArrow(getEndArrow());
        linkTarget.setEndInclination(getEndInclination());
        linkTarget.setReferenceText(getReferenceText());
        linkTarget.setStartArrow(getStartArrow());
        linkTarget.setStartInclination(getStartInclination());
        linkTarget.setStyle(getStyle());
        linkTarget.setTarget(getTarget());
        linkTarget.setWidth(getWidth());
        linkTarget.setSource(getSource());
    }

}
