package freemind.modes.mindmapmode.actions.xml.actors;

import freemind.controller.actions.CompoundAction;
import freemind.controller.actions.XmlAction;
import freemind.frok.patches.JIBXGeneratedUtil;
import freemind.modes.ExtendedMapFeedback;
import freemind.modes.mindmapmode.actions.xml.ActorXml;

import java.util.List;

public class CompoundActor extends XmlActorAdapter {

    public CompoundActor(ExtendedMapFeedback pMapFeedback) {
        super(pMapFeedback);
    }

    public void act(XmlAction action) {
        CompoundAction compound = (CompoundAction) action;
        List<XmlAction> xmlActions = JIBXGeneratedUtil.listXmlActions(compound);

        for (XmlAction xmlAction : xmlActions) {
            ActorXml actor = getExMapFeedback().getActionRegistry().getActor(xmlAction);
            actor.act(xmlAction);
        }
    }

    public Class<CompoundAction> getDoActionClass() {
        return CompoundAction.class;
    }

}
