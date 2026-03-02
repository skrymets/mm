package freemind.modes.mindmapmode.actions.xml;

import freemind.controller.actions.XmlAction;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ActionPair {
    private XmlAction doAction;
    private XmlAction undoAction;

    public ActionPair(XmlAction doAction, XmlAction undoAction) {
        this.doAction = doAction;
        this.undoAction = undoAction;
    }

    public ActionPair reverse() {
        return new ActionPair(getUndoAction(), getDoAction());
    }

}
