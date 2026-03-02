package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.model.MindMapNode;
import freemind.model.NodeAdapter;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.MindMapLinkRegistry;
import freemind.modes.mindmapmode.actions.xml.ActionPair;
import freemind.modes.mindmapmode.actions.xml.ActorXml;
import freemind.view.mindmapview.ViewFeedback;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class XmlActorAdapter implements ActorXml {

    protected final ExtendedMapFeedback mMapFeedback;

    public XmlActorAdapter(ExtendedMapFeedback pMapFeedback) {
        mMapFeedback = pMapFeedback;
        addActor(this);
    }

    public ExtendedMapFeedback getExMapFeedback() {
        return mMapFeedback;
    }

    public ViewFeedback getViewFeedback() {
        return getExMapFeedback().getViewFeedback();
    }

    protected void execute(ActionPair pActionPair) {
        getExMapFeedback().doTransaction(getDoActionClass().getName(), pActionPair);

    }

    protected NodeAdapter getNodeFromID(String pNodeId) {
        return getExMapFeedback().getNodeFromID(pNodeId);
    }

    protected MindMapNode getSelected() {
        return getExMapFeedback().getSelected();
    }

    protected String getNodeID(MindMapNode pNode) {
        return getExMapFeedback().getNodeID(pNode);
    }

    protected void addActor(ActorXml actor) {
        if (actor != null) {
            // registration:
            getExMapFeedback().getActionRegistry().registerActor(actor, actor.getDoActionClass());
        }
    }

    protected XmlActorFactory getXmlActorFactory() {
        return getExMapFeedback().getActorFactory();
    }

    protected MindMapLinkRegistry getLinkRegistry() {
        return getExMapFeedback().getMap().getLinkRegistry();
    }

}
