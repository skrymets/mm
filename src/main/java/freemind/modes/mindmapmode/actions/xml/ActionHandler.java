package freemind.modes.mindmapmode.actions.xml;

import freemind.controller.actions.XmlAction;

public interface ActionHandler {

    void executeAction(XmlAction action);

    void startTransaction(String name);

    void endTransaction(String name);

}
