package freemind.modes.mindmapmode.actions;

import freemind.modes.FreemindAction;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.actions.xml.ActorXml;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;

/**
 * Common class for mindmap actions.
 */
@SuppressWarnings("serial")
@Slf4j
public abstract class MindmapAction extends FreemindAction {

    private final MindMapController pMindMapController;

    /**
     * @param title is a fixed title (no translation is done via resources)
     */
    public MindmapAction(String title, Icon icon,
                         MindMapController mindMapController) {
        super(title, icon, mindMapController);
        this.pMindMapController = mindMapController;

    }

    /**
     * @param title Title is a resource.
     */
    public MindmapAction(String title, MindMapController mindMapController) {
        this(title, (String) null, mindMapController);
    }

    /**
     * @param title    Title is a resource.
     * @param iconPath is a path to an icon.
     */
    public MindmapAction(String title, String iconPath, final MindMapController mindMapController) {
        this(mindMapController.getText(title),
                (iconPath == null)
                        ? null
                        : freemind.view.ImageFactory.getInstance().createIconWithSvgFallback(mindMapController.getResource(iconPath)),
                mindMapController);
    }

    public void addActor(ActorXml actor) {
        // registration:
        pMindMapController.getActionRegistry().registerActor(actor, actor.getDoActionClass());
    }

    public MindMapController getMindMapController() {
        return pMindMapController;
    }

}
