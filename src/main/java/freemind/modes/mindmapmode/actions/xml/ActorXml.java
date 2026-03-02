package freemind.modes.mindmapmode.actions.xml;

import freemind.controller.actions.XmlAction;

public interface ActorXml {

    void act(XmlAction action);

    Class<?> getDoActionClass();

}
