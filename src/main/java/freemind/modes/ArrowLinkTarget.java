package freemind.modes;

import freemind.model.MindMapNode;
import freemind.modes.mindmapmode.MindMapArrowLinkModel;
import freemind.view.mindmapview.MapView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Stores targets of arrow links. It is used to enable cut/copy+paste for every
 * parts of links (ie. source, destination or both).
 */
public class ArrowLinkTarget extends ArrowLinkAdapter {
    private String mSourceLabel;

    public ArrowLinkTarget(MindMapNode pSource, MindMapNode pTarget,
                           MapFeedback pMapFeedback) {
        super(pSource, pTarget, pMapFeedback);
    }

    public String getSourceLabel() {
        return mSourceLabel;
    }

    public void setSourceLabel(String sourceLabel) {
        mSourceLabel = sourceLabel;
    }

    public Element save(Document doc) {
        Element arrowLink = super.save(doc);
        // Re-create as "linktarget" since super creates "arrowlink"
        Element linkTarget = doc.createElement("linktarget");
        // Copy all attributes from arrowLink
        var attrs = arrowLink.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            var attr = attrs.item(i);
            linkTarget.setAttribute(attr.getNodeName(), attr.getNodeValue());
        }
        if (getSourceLabel() != null) {
            linkTarget.setAttribute("SOURCE", getSourceLabel());
        }
        return linkTarget;
    }

    public void changeInclination(MapView pMap, int pOriginX, int pOriginY,
                                  int pDeltaX, int pDeltaY) {
    }

    public ArrowLinkAdapter createArrowLinkAdapter(MindMapLinkRegistry pRegistry) {
        ArrowLinkAdapter linkAdapter = new MindMapArrowLinkModel(source, target, mMapFeedback);
        copy(linkAdapter);
        return linkAdapter;
    }

}
