package freemind.modes.mindmapmode.actions.xml;

import freemind.controller.actions.XmlAction;

public class DefaultActionHandler implements ActionHandler {

    private final ActionRegistry factory;

    public DefaultActionHandler(ActionRegistry factory) {
        this.factory = factory;
    }

    public void executeAction(XmlAction action) {
        ActorXml actor = factory.getActor(action);
        // exception handling is done by the caller.
        actor.act(action);
    }

    public void startTransaction(String name) {

    }

    public void endTransaction(String name) {

    }

}
