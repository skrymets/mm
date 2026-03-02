package freemind.modes.mindmapmode.actions.xml;

import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.actions.MindmapAction;
import lombok.Getter;

import javax.swing.*;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public abstract class AbstractXmlAction extends MindmapAction {

    @Getter
    private ActorXml actor;

    private final MindMapController controller;

    protected AbstractXmlAction(String name, Icon icon, MindMapController controller) {
        super(name, icon, controller);
        this.controller = controller;
    }

    public final void actionPerformed(ActionEvent arg0) {
        xmlActionPerformed(arg0);
    }

    protected String getShortDescription() {
        return (String) getValue(Action.SHORT_DESCRIPTION);
    }

    protected abstract void xmlActionPerformed(ActionEvent arg0);

    public MindMapController getMindMapController() {
        return controller;
    }

    public void addActor(ActorXml actor) {
        this.actor = actor;
        if (actor != null) {
            // registration:
            getMindMapController().getActionRegistry().registerActor(actor,
                    actor.getDoActionClass());
        }
    }

}
