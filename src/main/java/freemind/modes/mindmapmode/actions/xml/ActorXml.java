package freemind.modes.mindmapmode.actions.xml;

import freemind.controller.actions.xml.operations.XmlAction;

public interface ActorXml {

    void act(XmlAction action);

    Class<?> getDoActionClass();

}
