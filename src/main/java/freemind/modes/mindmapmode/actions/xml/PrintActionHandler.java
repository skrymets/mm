package freemind.modes.mindmapmode.actions.xml;

import freemind.controller.actions.XmlAction;
import freemind.modes.mindmapmode.MindMapController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrintActionHandler implements ActionHandler {

    private final MindMapController c;

    public PrintActionHandler(MindMapController c) {
        super();
        this.c = c;

    }

    public void startTransaction(String name) {

    }

    public void endTransaction(String name) {

    }

    public void executeAction(XmlAction action) {
        String s = c.marshall(action);
        log.info(s);
        // Tools.printStackTrace();
    }

}
